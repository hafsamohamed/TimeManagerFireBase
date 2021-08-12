package com.example.timemanagerfirebase.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.timemanagerfirebase.R;
import com.example.timemanagerfirebase.model.TaskModel;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.example.timemanagerfirebase.notifications.APP.CHANNEL_ID;

public class TaskActivity extends AppCompatActivity {
    EditText titleEditText;
    CalendarView calendar;
    TimePicker timePicker;
    Button doneButton;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tasks");
    int Year,Month,Day,Hour,Minute = 0;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        titleEditText = findViewById(R.id.task_title_edit_text);
        calendar = findViewById(R.id.day_calender);
        timePicker = findViewById(R.id.time_Picker);
        doneButton = findViewById(R.id.done_button);
        notificationManager=NotificationManagerCompat.from(this);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite foldingCube = new Wave();
        progressBar.setIndeterminateDrawable(foldingCube);
        progressBar.setVisibility(View.GONE);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Year = year;
                Month = month+1;
                Day = dayOfMonth;
            }
        });

        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Hour = hourOfDay;
                Minute = minute;
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = titleEditText.getText().toString();
                Calendar calendarr = Calendar.getInstance(TimeZone.getDefault());
                int currentYear = calendarr.get(Calendar.YEAR);
                int currentMonth = calendarr.get(Calendar.MONTH) + 1;
                int currentDay = calendarr.get(Calendar.DAY_OF_MONTH);
                int currentHour = calendarr.get(Calendar.HOUR);
                int currentMinute = calendarr.get(Calendar.MINUTE);


                if (Title.equals(""))
                    Toast.makeText(TaskActivity.this, "title required", Toast.LENGTH_SHORT).show();
                else if(Year==0 && Month==0 && Day==0)
                    Toast.makeText(TaskActivity.this, "select date", Toast.LENGTH_SHORT).show();
                else if(Hour==0 && Minute==0)
                    Toast.makeText(TaskActivity.this, "select Time", Toast.LENGTH_SHORT).show();
                else if(Year < currentYear)
                    Toast.makeText(TaskActivity.this, "it is too old date please set date in year " + currentYear , Toast.LENGTH_SHORT).show();
                else if(Year == currentYear && Month < currentMonth)
                    Toast.makeText(TaskActivity.this, "it is old month .. please set new month  " + currentYear , Toast.LENGTH_SHORT).show();

                else if(Year == currentYear && Month == currentMonth && Day < currentDay)
                    Toast.makeText(TaskActivity.this, "it is old day .. please set new day  " + currentYear , Toast.LENGTH_SHORT).show();
                else if(Year == currentYear && Month == currentMonth && Day == currentDay && Hour < currentHour)
                    Toast.makeText(TaskActivity.this, "it is old hour .. please set new hour  " + currentYear , Toast.LENGTH_SHORT).show();

                else if(Year == currentYear && Month == currentMonth && Day == currentDay && Hour == currentHour && Minute < currentMinute)
                    Toast.makeText(TaskActivity.this, "it is old minute .. please set new minute  " + currentYear , Toast.LENGTH_SHORT).show();

                else{
                    progressBar.setVisibility(View.VISIBLE);
                    String email = getIntent().getStringExtra("email");
                    String title = titleEditText.getText().toString();
                    TaskModel taskModel = new TaskModel();
                    taskModel.setMail(email);
                    taskModel.setTitle(title);
                    taskModel.setYear(Year); taskModel.setMonth(Month); taskModel.setDay(Day);
                    taskModel.setHour(Hour); taskModel.setMinute(Minute); taskModel.setFlag(false);
                    ref.push().setValue(taskModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                String time = "";

                                if(Hour< 10)
                                    time += "0" + Hour;
                                else if(Hour >= 10)
                                    time += Hour;
                                time += ":";

                                if(Minute < 10)
                                    time += "0" + Minute;
                                else if(Minute >= 10)
                                    time += Minute;

                                int count = getIntent().getIntExtra("count",0);
                                String due_date = "due date is " + Day + "/" + Month + "/" + Year + "      due time is " + time;
                                Notification notification = new NotificationCompat.Builder(TaskActivity.this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.ic_baseline_edit_24)
                                        .setContentTitle(title)
                                        .setContentText(due_date)
                                        .setPriority(NotificationCompat.PRIORITY_LOW)
                                        .build();

                                SystemClock.sleep(20);
                                notificationManager.notify(count,notification);

                                progressBar.setVisibility(View.GONE);
                                titleEditText.setText("");
                                startActivity(new Intent(TaskActivity.this,HomePageActivity.class));
                            }
                        }
                    });


                }


            }
        });


    }
}