# Call flow script

`call_flows.py` runs Koler's main call flows on an emulator and prints PASS or FAIL for each.
It exits non-zero if any flow fails.

| Flow | Checks |
|---|---|
| `incoming_screen_off` | Screen off, a call rings: the call screen opens. |
| `incoming_in_use` | Phone unlocked, a call rings: the notification is on the `call_incoming` channel and the call screen stays closed. |
| `update_while_ringing` | The caller's number changes while ringing: the notification shows the new number. |
| `answer` | Tapping Accept on the call screen makes the call active. |
| `second_call` | A second call rings during an active call: the call screen shows Accept, and accepting holds the first call. |
| `outgoing` | Dialing opens the call screen, the call connects, and Hangup ends it. |

Incoming calls come from a fake call service that exists only in debug builds
(`koler/src/debug`). It goes through Android's real call system, so Koler handles these calls
exactly like SIM calls. The emulator's own modem can ring only one call at a time, so it is used
for the outgoing flow only.

## Run

Needs an emulator on API 29 or newer, in English (buttons are found by their labels), with no
screen lock, and Python 3.12 or newer.

```bash
./gradlew :koler:assembleDebug
python3 scripts/call_flows.py --serial emulator-5554
```

The script installs the debug build, makes Koler the default phone app and turns on the fake
call account. Run single flows by name, for example `python3 scripts/call_flows.py --serial
emulator-5554 second_call`. Add `--no-install` to test the build already on the emulator.

## Fake calls by hand

```bash
adb shell am broadcast --include-stopped-packages -n com.chooloo.www.koler/.testcall.TestCallReceiver -a com.chooloo.www.koler.test.INCOMING --es number 5550001
adb shell am broadcast --include-stopped-packages -n com.chooloo.www.koler/.testcall.TestCallReceiver -a com.chooloo.www.koler.test.UPDATE --es number 5550002
adb shell am broadcast --include-stopped-packages -n com.chooloo.www.koler/.testcall.TestCallReceiver -a com.chooloo.www.koler.test.HANGUP
```

`HANGUP` ends every fake call as if the other side hung up. Run the script once first so the
fake call account is turned on.
