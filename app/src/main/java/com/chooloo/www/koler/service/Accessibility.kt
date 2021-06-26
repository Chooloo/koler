package com.chooloo.www.koler.service

import android.accessibilityservice.AccessibilityService
import android.app.Service
import android.view.accessibility.AccessibilityEvent

class Accessibility : AccessibilityService() {
    private var _service: Service? = null

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
    }

    override fun onInterrupt() {
    }

    override fun onServiceConnected() {
    }

    override fun onCreate() {
        _service = this
    }
}