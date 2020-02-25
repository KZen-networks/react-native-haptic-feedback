
package com.mkuczera;

import android.app.Activity;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.content.Context;
import android.provider.Settings;
import android.view.HapticFeedbackConstants;
import android.view.View;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

public class RNReactNativeHapticFeedbackModule extends ReactContextBaseJavaModule {

    ReactApplicationContext reactContext;

    public RNReactNativeHapticFeedbackModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNReactNativeHapticFeedback";
    }

    @ReactMethod
    public void trigger(String type, ReadableMap options) {
        // Check system settings, if disabled and we're not explicitly ignoring then return immediately
        boolean ignoreAndroidSystemSettings = options.getBoolean("ignoreAndroidSystemSettings");
        int hapticEnabledAndroidSystemSettings = Settings.System.getInt(this.reactContext.getContentResolver(), Settings.System.HAPTIC_FEEDBACK_ENABLED, 0);
        if (!ignoreAndroidSystemSettings && hapticEnabledAndroidSystemSettings == 0) return;

        int hapticConstant = HapticFeedbackConstants.KEYBOARD_TAP;

        switch (type) {
            case "impactLight":
                triggerVibration(new long[]{0, 20});
                break;
            case "impactMedium":
                triggerVibration(new long[]{0, 40});
                break;
            case "impactHeavy":
                triggerVibration(new long[]{0, 60});
                break;
            case "notificationSuccess":
                triggerVibration(new long[]{0, 40, 60, 20});
                break;
            case "notificationWarning":
                triggerVibration(new long[]{0, 20, 60, 40});
                break;
            case "notificationError":
                triggerVibration(new long[]{0, 20, 40, 30, 40, 40});
                break;
            case "clockTick":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    hapticConstant = HapticFeedbackConstants.CLOCK_TICK;
                }
                triggerHaptic(hapticConstant);
                break;
            case "contextClick":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hapticConstant = HapticFeedbackConstants.CONTEXT_CLICK;
                }
                triggerHaptic(hapticConstant);
                break;
            case "keyboardPress":
                triggerHaptic(hapticConstant);
                break;
            case "keyboardRelease":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    hapticConstant = HapticFeedbackConstants.KEYBOARD_RELEASE;
                }
                triggerHaptic(hapticConstant);
                break;
            case "keyboardTap":
                hapticConstant = HapticFeedbackConstants.KEYBOARD_TAP;
                triggerHaptic(hapticConstant);
                break;
            case "longPress":
                hapticConstant = HapticFeedbackConstants.LONG_PRESS;
                triggerHaptic(hapticConstant);
                break;
            case "textHandleMove":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    hapticConstant = HapticFeedbackConstants.TEXT_HANDLE_MOVE;
                }
                triggerHaptic(hapticConstant);
                break;
            case "virtualKey":
                hapticConstant = HapticFeedbackConstants.VIRTUAL_KEY;
                triggerHaptic(hapticConstant);
                break;
            case "virtualKeyRelease":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    hapticConstant = HapticFeedbackConstants.VIRTUAL_KEY_RELEASE;
                }
                triggerHaptic(hapticConstant);
                break;
        }
    }

    private void triggerVibration(long[] timings) {
        Vibrator v = (Vibrator) reactContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (v == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createWaveform(timings, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(timings, -1);
        }
    }

    private void triggerHaptic(int hapticConstant) {
        Activity curActivity = reactContext.getCurrentActivity();
        if (curActivity == null) return;
        View contentView = curActivity.findViewById(android.R.id.content);
        if (contentView == null) return;
        contentView.performHapticFeedback(hapticConstant, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
    }
}
