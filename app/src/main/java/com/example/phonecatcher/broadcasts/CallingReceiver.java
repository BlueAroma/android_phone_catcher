package com.example.phonecatcher.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.phonecatcher.services.PhoneCatcher;

public class CallingReceiver extends BroadcastReceiver {
    private final String TAG = CallingReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "intent : " + intent.getAction());

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.d(TAG, "state : " + state);

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.d(TAG, "Number : " + incomingNumber);

            if(TextUtils.isEmpty(incomingNumber))
                return;

            Intent serviceIntent = new Intent(context, PhoneCatcher.class);
            serviceIntent.putExtra(PhoneCatcher.EXTRA_CALL_NUMBER, incomingNumber);
            serviceIntent.putExtra(PhoneCatcher.EXTRA_CALL_METHOD, PhoneCatcher.CALL_INCOMING);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        }else if(TelephonyManager.EXTRA_STATE_IDLE.equals(state)){
            Intent serviceIntent = new Intent(context, PhoneCatcher.class);
            serviceIntent.putExtra(PhoneCatcher.EXTRA_CALL_FINISH, true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        }
    }
}
