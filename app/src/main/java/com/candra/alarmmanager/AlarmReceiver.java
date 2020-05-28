package com.candra.alarmmanager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {



    public static final String TYPE_ONE_TIME = "OneTimeAlarm"; // Menentukan tipe alaram
    public static final String TYPE_REPEATING = "RepeatingAlarm"; // Menentukan tipe alarm
    public static final String EXTRA_MESSAGE = "message"; // Intent key
    public static final String EXTRA_TYPE = "type"; // Intent key
    // Siapkan 2 id untuk 2 macam alarm, onetime dna repeating
    private final int ID_ONETIME = 100; // Notif id sebagai id untuk menampilkan notifikasi kepada pengguna
    private final int ID_REPEATING = 101; // Notif id sebagai id untuk menampilkan notifikasi kepada pengguna

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String title = type.equalsIgnoreCase(TYPE_ONE_TIME) ? TYPE_ONE_TIME : TYPE_REPEATING;
        int notifId = type.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONETIME : ID_REPEATING;
        showToast(context, title, message);
        showAlarmNotification(context, title, message, notifId); // Kode untuk membunyikan alarm
    }

    private void showToast(Context context, String title, String message) {
        Toast.makeText(context, title + " : " + message, Toast.LENGTH_LONG).show();
    }

    //Pada kode di atas kita membuat sebuah obyek dari kelas AlarmManager.
    // Kita menyiapkan sebuah Intent yang akan menjalankan AlarmReceiver dan membawa data alarm dan pesan.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setOneTimeAlarm(Context context, String type, String date, String time, String message) {
        String DATE_FORMAT = "yyyy-MM-dd";
        String TIME_FORMAT = "HH:mm";
        if (isDateInvalid(date, DATE_FORMAT) || isDateInvalid(time, TIME_FORMAT)) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        Log.e("ONE TIME", date + " " + time);
        // Untuk memecah date dan time untuk mengambil data tanggal dan waktu
        String dateArray[] = date.split("-");
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1); // Karena indeks dimulai dari 0 jadi dikurang 1
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        // Intent yang dibuat akan dieksekusi ketika waktu alarm sama dengan waktu pada sistem Android.
        // Di sini komponen PendingIntent  akan diberikan kepada BroadcastReceiver.
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_ONETIME, intent, 0);
        if (alarmManager != null) {
            // maksd dari baris ini adalah kita memasang alarm yang dibuat dengan membangunkan peranti(jika
            // dalam posisi sleep ) untuk menjalankan objek Pending intent
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        Toast.makeText(context, "One time alarm set up", Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    // Membuat notfikasi alaram
    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_time_black)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    private String DATE_FORMAT = "yyyy-MM-dd";
    private String TIME_FORMAT = "HH:mm";


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setRepeatingAlarm(Context context, String type, String time, String message) {
        if (isDateInvalid(time, TIME_FORMAT)) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        String[] timeArray = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        if (alarmManager != null) {
            // Kode untuk menjalankan alarm dan alarm ini akan berjalan bersamaan dengan alarm peranti laen
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show();
    }

    // Metode yang digunakan untuk membatalkan alarm pada pengguna
    public void cancelAlarm(Context context ,String type){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context,AlarmReceiver.class);
            int RequestCode = type.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONETIME : ID_REPEATING;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,RequestCode,intent,0);
            pendingIntent.cancel();

            if (alarmManager != null){
                alarmManager.cancel(pendingIntent);
            }
            Toast.makeText(context,"Repeating alaram dibatalkan ",Toast.LENGTH_SHORT).show();
    }

}






