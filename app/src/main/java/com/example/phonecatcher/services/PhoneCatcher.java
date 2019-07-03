package com.example.phonecatcher.services;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.example.phonecatcher.R;
import com.example.phonecatcher.views.PopupView;
import com.example.phonecatcher.vo.BlackPhone;

public class PhoneCatcher extends Service {
    private final String TAG = "PhoneCatcher";
    public final static String EXTRA_CALL_NUMBER = "phone_catcher_call_number";
    public final static String EXTRA_CALL_METHOD = "phone_catcher_call_method";
    public final static String EXTRA_CALL_FINISH = "phone_catcher_call_finish";

    public final static String CALL_INCOMING = "phone_catcher_call_incoming";

    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private PopupView popupView;

    public PhoneCatcher() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String channel_id = getResources().getString(R.string.notification_channel_id);
        String c_title = getResources().getString(R.string.calling_searching_title);
        String c_body = getResources().getString(R.string.calling_searching_msg);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_id,
                    "Phone Number Catcher",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new Notification.Builder(this, channel_id)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setColor(getResources().getColor(R.color.colorPrimary, null))
                    .setContentTitle(c_title)
                    .setContentText(c_body)
                    .build();
            startForeground(1, notification);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            Log.i(TAG, "intent is null...");
        }else{
            boolean finish = intent.getBooleanExtra(EXTRA_CALL_FINISH, false);
            if(finish){
                destroyWindows();
            }else{
                String phone_number = intent.getStringExtra(EXTRA_CALL_NUMBER);
                String phone_method = intent.getStringExtra(EXTRA_CALL_METHOD);
                Log.d(TAG, "Number : " + phone_number);
                Log.d(TAG, "Method : " + phone_method);

                showWindows(phone_number, phone_method);
            }
        }
        return START_NOT_STICKY;
    }

    private void destroyWindows() {
        if(windowManager != null){        //서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
            if(popupView != null) {
                windowManager.removeView(popupView);
                popupView = null;
            }
            windowManager = null;
        }
    }

    private void showWindows(String phone_number, String phone_method) {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        int LAYOUT_FLAG = -1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        DisplayMetrics disp = getApplicationContext().getResources().getDisplayMetrics();
        int deviceWidth = disp.widthPixels;

        int width = (int) (deviceWidth * 0.9); //Display 사이즈의 90%
        params = new WindowManager.LayoutParams(width,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.TRANSLUCENT);
        params.windowAnimations = android.R.style.Animation_Translucent;

        params.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

        popupView = new PopupView(getApplicationContext());
        popupView.setPhone(phone_number);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);  //윈도우 매니저
        windowManager.addView(popupView, params);      //윈도우에 뷰 넣기. permission 필요.
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy...");
        destroyWindows();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
