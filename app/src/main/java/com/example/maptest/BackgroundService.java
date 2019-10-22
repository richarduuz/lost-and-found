package com.example.maptest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BackgroundService extends Service {
    public static final String TAG = "BackgroundService";

    public class InnerBinder extends Binder implements ICommunication {
        @Override
        public void callServiceInnerMethod() {
            sayHello();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind...");
        return new InnerBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate...");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy...");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand...");
        return super.onStartCommand(intent, flags, startId);
    }

    private void sayHello(){
        Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();
    }
}
