package com.candra.alarmmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

    // Kode yang di bawah ini adalah implementasi dari TimePickerFragment dan DatePickerFragment
public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DialogDateListener,TimeFragment.DialogTimeListener {

        private TextView text1;
        private TextView text2;
        private ImageButton image1;
        private ImageButton image2;
        private EditText editText1;
        private TextView tvRepeatingTime;
        private EditText edtRepeatingTime;
        private ImageButton image3;
        private Button btnSetRepeatingTime;
        private Button cancel;

        Button btn1;
        private AlarmReceiver alaramReceive;


        final String DATE_PICKER_TAG = "DatePicker";
        final String TIME_PICKER_ONCE_TAG = "TimePickerOnce";
        final String TIME_PICKER_REPEAT_TAG = "TimePickerRepeat";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            text1 = findViewById(R.id.tv_once_date);
            text2 = findViewById(R.id.tv_once_time);
            editText1 = findViewById(R.id.tv_message);
            image1 = findViewById(R.id.waktu);
            image2 = findViewById(R.id.tanggal);
            btn1 = findViewById(R.id.btn_set_once_alarm);
            tvRepeatingTime = findViewById(R.id.tv_repeating_time);
            image3 = findViewById(R.id.btn_repeating_time);
            edtRepeatingTime = findViewById(R.id.edt_repeating_message);
            btnSetRepeatingTime = findViewById(R.id.btn_set_repeating_alarm);
            cancel = findViewById(R.id.btn_cancel_repeating_alarm);

            btnSetRepeatingTime.setOnClickListener(this);
            image3.setOnClickListener(this);

            image2.setOnClickListener(this);
            image1.setOnClickListener(this);
            btn1.setOnClickListener(this);
            cancel.setOnClickListener(this);

            alaramReceive = new AlarmReceiver();

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // Membuat sebuah dialog tanggal
                case R.id.tanggal:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
                break;

                // Membuat sebuah waktu dialog tanggal
                case R.id.waktu:
                    TimeFragment timePickerFragmentOne = new TimeFragment();
                    timePickerFragmentOne.show(getSupportFragmentManager(), TIME_PICKER_ONCE_TAG);
                    break;

                // Menampilkan jadwal pada notifikasi alaram
                case R.id.btn_set_once_alarm:
                    String onceDate = text1.getText().toString();
                    String onceTime = text2.getText().toString();
                    String onceMessage = editText1.getText().toString();
                    alaramReceive.setOneTimeAlarm(this, AlarmReceiver.TYPE_ONE_TIME,
                            onceDate,
                            onceTime,
                            onceMessage);
                    break;

                // Menampilkan waktu pada dialog fragment
                case R.id.btn_repeating_time:
                    TimeFragment timePickerFragmenttwo = new TimeFragment();
                    timePickerFragmenttwo.show(getSupportFragmentManager(), TIME_PICKER_REPEAT_TAG);
                    break;

                // Menampilkan tanggal dialog fragment
                case R.id.btn_set_repeating_alarm:
                    String repeatTime = tvRepeatingTime.getText().toString();
                    String repeatMessage = edtRepeatingTime.getText().toString();
                    alaramReceive.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING,
                            repeatTime, repeatMessage);
                    break;
                 // untuk membatalkan alarm
                case R.id.btn_cancel_repeating_alarm:
                    alaramReceive.cancelAlarm(this,AlarmReceiver.TYPE_REPEATING);
                    break;
            }
        }

        // Method callback yang dipanggil apabila kita mencamtumkan kelas dari datepicker dan timerfragment
        @Override
        public void onDialogDataSet(String tag, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            // Suatu kelas yang dipanggil untuk membuat atau mendeklerasikan tanggal bulan dan tahun
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            text1.setText(dateFormat.format(calendar.getTime()));
        }

        // Method callback yang dipanggil apabila kita mencantumkan kelas dari timepicker
        @Override
        public void onDialogTimeSet(String tag, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            // Suatu kelas yang dpanggil untuk membuat waktu
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            switch (tag) {
                case TIME_PICKER_ONCE_TAG:
                    text2.setText(dateFormat.format(calendar.getTime()));
                    break;
                case TIME_PICKER_REPEAT_TAG:
                    tvRepeatingTime.setText(dateFormat.format(calendar.getTime()));
                    break;
                default:
                    break;
            }
        }

    }
