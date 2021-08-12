package com.example.timemanagerfirebase.adapter;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timemanagerfirebase.R;
import com.example.timemanagerfirebase.manager.HomePageActivity;
import com.example.timemanagerfirebase.manager.TaskActivity;
import com.example.timemanagerfirebase.model.TaskModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static com.example.timemanagerfirebase.notifications.APP.CHANNEL_ID;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<TaskModel> tasks = new ArrayList<>();
    Context mContext;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tasks");

    public RecyclerViewAdapter(List<TaskModel> tasks, Context mContext) {
        this.tasks = tasks;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.taskTitleTextView.setText(tasks.get(position).getTitle());
        String date = tasks.get(position).getDay() + "/" + (tasks.get(position).getMonth()) +"/" +tasks.get(position).getYear();
        holder.dueDateTextView.setText(date);
        String time = "";

        if(tasks.get(position).getHour() < 10)
            time += "0" + tasks.get(position).getHour();
        else if(tasks.get(position).getHour() >= 10)
            time += tasks.get(position).getHour();
        time += ":";

        if(tasks.get(position).getMinute() < 10)
            time += "0" + tasks.get(position).getMinute();
        else if(tasks.get(position).getMinute() >= 10)
            time += tasks.get(position).getMinute();

        holder.dueTimeTextView.setText(time);
        holder.radioButton.setChecked(tasks.get(position).isFlag());

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot d: snapshot.getChildren())
                        {
                                String email = d.child("mail").getValue(String.class);
                                String title = d.child("title").getValue(String.class);
                                int year = d.child("year").getValue(Integer.class);
                                int month = d.child("month").getValue(Integer.class);
                                int day = d.child("day").getValue(Integer.class);
                                int hour = d.child("hour").getValue(Integer.class);
                                int minute = d.child("minute").getValue(Integer.class);
                                if(tasks.get(position).getMail().equals(email) && tasks.get(position).getTitle().equals(title)
                                        && (tasks.get(position).getYear() == year) && (tasks.get(position).getMonth() == month)
                                        && (tasks.get(position).getDay() == day) && (tasks.get(position).getHour() == hour)
                                        && (tasks.get(position).getMinute() == minute)){

                                    if (validation(year, month, day, hour, minute)) {
                                        d.getRef().child("flag").setValue(true);
                                    }
                                    else {
                                        holder.radioButton.setChecked(false);
                                        holder.dueDateTextView.setText("Dismissed");

                                        }

                                    }

                            }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.deleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot d: snapshot.getChildren())
                        {
                            String email = d.child("mail").getValue(String.class);
                            String title = d.child("title").getValue(String.class);
                            int year = d.child("year").getValue(Integer.class);
                            int month = d.child("month").getValue(Integer.class);
                            int day = d.child("day").getValue(Integer.class);
                            int hour = d.child("hour").getValue(Integer.class);
                            int minute = d.child("minute").getValue(Integer.class);
                            if(tasks.get(position).getMail().equals(email) && tasks.get(position).getTitle().equals(title)
                                    && (tasks.get(position).getYear() == year) && (tasks.get(position).getMonth() == month)
                                    && (tasks.get(position).getDay() == day) && (tasks.get(position).getHour() == hour)
                                    && (tasks.get(position).getMinute() == minute)) {
                                    d.getRef().removeValue();
                                    mContext.startActivity(new Intent(mContext, HomePageActivity.class));

                            }
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
    public Boolean validation(int y,int m, int d,int h,int mi)
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR);
        int currentMinute = calendar.get(Calendar.MINUTE);
        if (y > currentYear)
            return true;
        else if(y == currentYear && m > currentMonth)
            return true;
        else if(y == currentYear && m == currentMonth && d > currentDay)
            return true;
        else if(y == currentYear && m == currentMonth && d == currentDay && h > currentHour)
            return true;
        else if (y == currentYear && m == currentMonth && d == currentDay && h == currentHour && mi > currentMinute)
            return  true;
        else {
            return false;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitleTextView,dueDateTextView,dueTimeTextView,deleteTextView;
        RadioButton radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleTextView = (itemView).findViewById(R.id.title_text_view);
            dueDateTextView = (itemView).findViewById(R.id.date_text_view);
            dueTimeTextView = (itemView).findViewById(R.id.time_text_view);
            deleteTextView = (itemView).findViewById(R.id.delete_text_view);
            radioButton = (itemView).findViewById(R.id.radio_button);

        }
    }
}
