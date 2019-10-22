package com.example.maptest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class Notification extends AppCompatActivity {
    //    String GROUP_ID = "100";
//    String GROUP_NAME = "群组名称";

    public static final String CHANNEL_Lost_ID = "101";
    public static final String CHANNEL_LOST_NAME = "Lost Information";
    public static final String CHANNEL_LOST_DESCRIPTION = "The channel is going to notify the lost information";

    public static final String CHANNEL_MESSAGE_ID = "102";
    public static final String CHANNEL_MESSAGE_NAME = "MESSAGE";
    public static final String CHANNEL_MESSAGE_DESCRIPTION = "The channel is going to notify the message";

    public static final String CHANNEL_BUILDING_ID = "103";
    public static final String CHANNEL_BUILDING_NAME = "BUILDING";
    public static final String CHANNEL_BUILDING_DESCRIPTION = "The channel is going to notify the lost information in some building";

    public static NotificationManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_test);
//        启动服务
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        建群
//        NotificationChannelGroup group = new NotificationChannelGroup(GROUP_ID,GROUP_NAME);
//        manager.createNotificationChannelGroup(group);

//        建立频道
        NotificationChannel channel_lost = new NotificationChannel(CHANNEL_Lost_ID, CHANNEL_LOST_NAME, NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel_lost);

        NotificationChannel channel_message = new NotificationChannel(CHANNEL_MESSAGE_ID, CHANNEL_MESSAGE_NAME, NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel_message);

        NotificationChannel channel_building = new NotificationChannel(CHANNEL_BUILDING_ID, CHANNEL_BUILDING_NAME, NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel_building);

//        发通知
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
//        builder.setContentTitle("Hello");
//        builder.setContentText("通知内容");
//        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
//        manager.notify(1, builder.build());
//        sendNotification("Hello", "Hellotitle", "content", CHANNEL_ID, 1 );
//        sendButtonNotification();
    }

    public NotificationCompat.Builder getSimpleBuilder(String channelID, String title, String content){
        NotificationCompat.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = new NotificationCompat.Builder(this, channelID);
        } else{
            builder = new NotificationCompat.Builder(this);
        }
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_foreground));
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setOngoing(false);
        Intent intent = new Intent(this, MainActivity.class);//点击通知进入哪个画面
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        return builder;
    }

    //    发送通知
    public void sendNotification(String CHANNEL_ID){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setTicker("Hello!");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_foreground));
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setOngoing(false);

        if(CHANNEL_ID.equals("101")){
            builder.setContentTitle("Lost Information");
            builder.setContentText("Your lost item has been found!");
            Intent intent = new Intent(this, MainActivity.class);//点击通知进入哪个画面
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
            manager.notify(1,builder.build());
        }
        else if(CHANNEL_ID.equals("102")){
            builder.setContentTitle("New Message");
            builder.setContentText("You have received a new message!");
            Intent intent = new Intent(this, MainActivity.class);//点击通知进入哪个画面
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
            manager.notify(2,builder.build());
        }
        else if(CHANNEL_ID.equals("103")){
            builder.setContentTitle("Building Information");
            builder.setContentText("There are lost information in nearby building! Type to see...");
            Intent intent = new Intent(this, MainActivity.class);//点击通知进入哪个画面
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 3, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
            manager.notify(3,builder.build());
        }
    }

//    带按钮通知
//    public void sendButtonNotification(String CHANNEL_ID){
//        if(CHANNEL_ID.equals("101")){
//            String content = "Your lost item has been found!";
//            NotificationCompat.Builder builder = getSimpleBuilder(CHANNEL_Lost_ID, "Lost Information", content);
//            Intent intent = new Intent(this, MainActivity.class);//点击通知进入哪个画面
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_launcher_foreground, "按钮文字", intent);
//            builder.addAction(action);
//
//            manager.notify(1, builder.build());
//        }
//
//    }

    public void myClick1(View view)
    {
        TextView name=findViewById(R.id.button);

        name.setText("Click");
        sendNotification("101" );
    }

    public void myClick2(View view)
    {
        TextView name=findViewById(R.id.button2);
        name.setText("Click");
        sendNotification("102" );

    }

    public void myClick3(View view)
    {
        TextView name=findViewById(R.id.button2);
        name.setText("Click");
        sendNotification("103" );

    }
}
