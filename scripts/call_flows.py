#!/usr/bin/env python3
"""Run Koler's main call flows on an emulator and report PASS or FAIL for each."""

import argparse
import re
import subprocess
import sys
import time
import xml.etree.ElementTree as ElementTree
from collections.abc import Callable
from pathlib import Path

PACKAGE = "com.chooloo.www.koler"
CALL_ACTIVITY = f"{PACKAGE}/.ui.call.CallActivity"
TEST_SERVICE = f"{PACKAGE}/{PACKAGE}.testcall.TestConnectionService"
TEST_ACCOUNT_ID = "koler_test"
TEST_ACTION = f"{PACKAGE}.test"
TEST_RECEIVER = f"{PACKAGE}/.testcall.TestCallReceiver"
CALL_NOTIFICATION_ID = 420
CHANNEL_INCOMING = "call_incoming"
DEFAULT_APK = (
    Path(__file__).resolve().parent.parent / "koler/build/outputs/apk/debug/koler-debug.apk"
)
WAIT_SECONDS = 10.0


class FlowFailed(Exception):
    pass


class Device:
    def __init__(self, serial: str) -> None:
        self.serial = serial

    def adb(self, *args: str) -> str:
        result = subprocess.run(
            ["adb", "-s", self.serial, *args],
            capture_output=True,
            text=True,
            check=False,
        )
        if result.returncode != 0:
            raise RuntimeError(f"adb {' '.join(args)} failed: {result.stderr.strip()}")
        return result.stdout

    def shell(self, *args: str) -> str:
        return self.adb("shell", *args)

    def test_call(self, action: str, number: str | None = None) -> None:
        extras = ["--es", "number", number] if number else []
        output = self.shell(
            "am",
            "broadcast",
            "--include-stopped-packages",
            "-a",
            f"{TEST_ACTION}.{action}",
            "-n",
            TEST_RECEIVER,
            *extras,
        )
        if "result=-1" not in output:
            raise RuntimeError(f"Test call broadcast {action} failed: {output.strip()}")

    def call_states(self) -> list[str]:
        dump = self.shell("dumpsys", "telecom")
        match = re.search(r"mCalls:(.*?)mCallAudioManager:", dump, re.DOTALL)
        if match is None:
            raise RuntimeError("dumpsys telecom has no mCalls section")
        calls_section = match.group(1)
        return re.findall(r"\[Call id=TC@\d+, state=([A-Z_]+)", calls_section)

    def resumed_activity(self) -> str:
        dump = self.shell("dumpsys", "activity", "activities")
        match = re.search(r"topResumedActivity=ActivityRecord\{\S+ \S+ (\S+)", dump)
        return match.group(1) if match else ""

    def call_notification(self) -> str | None:
        dump = self.shell("dumpsys", "notification", "--noredact")
        pattern = (
            rf"NotificationRecord\(\S+: pkg={re.escape(PACKAGE)} user=\S+ "
            rf"id={CALL_NOTIFICATION_ID} .*?(?=NotificationRecord\(|\Z)"
        )
        match = re.search(pattern, dump, re.DOTALL)
        return match.group(0) if match else None

    def find_center(self, description: str) -> tuple[int, int] | None:
        dump = self.adb("exec-out", "uiautomator", "dump", "/dev/tty")
        if "<?xml" not in dump:
            return None
        root = ElementTree.fromstring(dump[: dump.rindex(">") + 1])
        for node in root.iter("node"):
            if node.get("content-desc") == description:
                left, top, right, bottom = map(int, re.findall(r"\d+", node.get("bounds", "")))
                return (left + right) // 2, (top + bottom) // 2
        return None

    def tap_description(self, description: str) -> None:
        x, y = wait_for(f"button '{description}'", lambda: self.find_center(description))
        self.shell("input", "tap", str(x), str(y))

    def wake_and_unlock(self) -> None:
        self.shell("input", "keyevent", "KEYCODE_WAKEUP")
        self.shell("wm", "dismiss-keyguard")
        wait_until(
            "unlocked screen",
            lambda: "isKeyguardShowing=false" in self.shell("dumpsys", "window"),
        )

    def sleep_screen(self) -> None:
        self.shell("input", "keyevent", "KEYCODE_SLEEP")
        wait_until("screen off", lambda: "mWakefulness=Asleep" in self.shell("dumpsys", "power"))


def wait_for[T](what: str, check: Callable[[], T | None]) -> T:
    deadline = time.monotonic() + WAIT_SECONDS
    while time.monotonic() < deadline:
        value = check()
        if value is not None:
            return value
        time.sleep(0.5)
    raise FlowFailed(f"timed out waiting for {what}")


def wait_until(what: str, check: Callable[[], bool]) -> None:
    wait_for(what, lambda: True if check() else None)


def wait_states(device: Device, expected: list[str]) -> None:
    wait_until(
        f"Telecom calls {expected}", lambda: sorted(device.call_states()) == sorted(expected)
    )


def setup(device: Device, apk: Path, install: bool) -> None:
    if install:
        device.adb("install", "-r", "-g", str(apk))
    device.shell("cmd", "role", "add-role-holder", "android.app.role.DIALER", PACKAGE)
    if PACKAGE not in device.shell("cmd", "role", "get-role-holders", "android.app.role.DIALER"):
        raise RuntimeError("Koler is not the default dialer")
    device.shell(
        "telecom", "register-phone-account", TEST_SERVICE, TEST_ACCOUNT_ID, "0", "Koler test"
    )
    device.shell("telecom", "set-phone-account-enabled", TEST_SERVICE, TEST_ACCOUNT_ID, "0")


def end_all_calls(device: Device) -> None:
    device.test_call("HANGUP")
    if any(state != "DISCONNECTED" for state in device.call_states()):
        device.shell("input", "keyevent", "KEYCODE_ENDCALL")
    wait_states(device, [])


def flow_incoming_screen_off(device: Device) -> None:
    device.sleep_screen()
    device.test_call("INCOMING", "5550001")
    wait_states(device, ["RINGING"])
    wait_until("call screen", lambda: device.resumed_activity() == CALL_ACTIVITY)


def flow_incoming_in_use(device: Device) -> None:
    device.wake_and_unlock()
    device.test_call("INCOMING", "5550001")
    wait_states(device, ["RINGING"])
    notification = wait_for("call notification", device.call_notification)
    if f"channel={CHANNEL_INCOMING} " not in notification:
        raise FlowFailed(f"call notification is not on the {CHANNEL_INCOMING} channel")
    if device.resumed_activity() == CALL_ACTIVITY:
        raise FlowFailed("call screen opened over the app in use")


def flow_update_while_ringing(device: Device) -> None:
    device.wake_and_unlock()
    device.test_call("INCOMING", "5550001")
    wait_for("call notification", device.call_notification)
    device.test_call("UPDATE", "5550002")
    wait_until(
        "notification with the new number",
        lambda: "5550002" in (device.call_notification() or ""),
    )


def flow_answer(device: Device) -> None:
    flow_incoming_screen_off(device)
    device.tap_description("Accept Call")
    wait_states(device, ["ACTIVE"])


def flow_second_call(device: Device) -> None:
    flow_answer(device)
    device.test_call("INCOMING", "5550003")
    wait_states(device, ["ACTIVE", "RINGING"])
    device.tap_description("Accept Call")
    wait_states(device, ["ACTIVE", "ON_HOLD"])


def flow_outgoing(device: Device) -> None:
    device.wake_and_unlock()
    device.shell("am", "start", "-a", "android.intent.action.CALL", "-d", "tel:5550100")
    # The emulator modem answers outgoing calls by itself.
    wait_states(device, ["ACTIVE"])
    wait_until("call screen", lambda: device.resumed_activity() == CALL_ACTIVITY)
    device.tap_description("Hangup Call")
    wait_states(device, [])


FLOWS: dict[str, Callable[[Device], None]] = {
    "incoming_screen_off": flow_incoming_screen_off,
    "incoming_in_use": flow_incoming_in_use,
    "update_while_ringing": flow_update_while_ringing,
    "answer": flow_answer,
    "second_call": flow_second_call,
    "outgoing": flow_outgoing,
}


def run_flow(device: Device, name: str) -> bool:
    failure: FlowFailed | None = None
    try:
        FLOWS[name](device)
    except FlowFailed as flow_failure:
        failure = flow_failure
    try:
        end_all_calls(device)
    except FlowFailed as cleanup_failure:
        failure = failure or FlowFailed(f"cleanup: {cleanup_failure}")
    print(f"FAIL {name}: {failure}" if failure else f"PASS {name}")
    return failure is None


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--serial", required=True, help="emulator serial, e.g. emulator-5554")
    parser.add_argument("--apk", type=Path, default=DEFAULT_APK, help="debug APK to install")
    parser.add_argument("--no-install", action="store_true", help="use the installed build")
    parser.add_argument("flows", nargs="*", help=f"flows to run (default: all): {', '.join(FLOWS)}")
    args = parser.parse_args()
    unknown = [name for name in args.flows if name not in FLOWS]
    if unknown:
        parser.error(f"unknown flows: {', '.join(unknown)}")

    device = Device(args.serial)
    setup(device, args.apk, install=not args.no_install)
    end_all_calls(device)
    results = [run_flow(device, name) for name in args.flows or FLOWS]
    print(f"{sum(results)}/{len(results)} passed")
    return 0 if all(results) else 1


if __name__ == "__main__":
    sys.exit(main())
