package com.example.timemanagerfirebase.manager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timemanagerfirebase.R;
import com.example.timemanagerfirebase.adapter.RecyclerViewAdapter;
import com.example.timemanagerfirebase.login.LoginActivity;
import com.example.timemanagerfirebase.model.TaskModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button addTaskButton;
    TextView userNameTextView;
    public static int count = 0;
    View header;
    ArrayList<TaskModel> list=new ArrayList<>();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_bar);
        addTaskButton = findViewById(R.id.button_add);
        header = navigationView.getHeaderView(0);
        userNameTextView =(header).findViewById(R.id.user_name_text_view_nav);
        navigationBar();

        final DatabaseReference ref = reference.child("Tasks");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userNameTextView.setText(firebaseUser.getEmail());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d: snapshot.getChildren())
                {
                        String email = d.child("mail").getValue(String.class);
                        if(email.equals(firebaseUser.getEmail())) {
                            String title = d.child("title").getValue(String.class);
                            int year = d.child("year").getValue(Integer.class);
                            int month = d.child("month").getValue(Integer.class);
                            int day = d.child("day").getValue(Integer.class);
                            int hour = d.child("hour").getValue(Integer.class);
                            int minute = d.child("minute").getValue(Integer.class);
                            Boolean flag = d.child("flag").getValue(Boolean.class);

                            TaskModel taskModel = new TaskModel();
                            taskModel.setMail(email);
                            taskModel.setTitle(title);
                            taskModel.setYear(year);
                            taskModel.setMonth(month);
                            taskModel.setDay(day);
                            taskModel.setHour(hour);
                            taskModel.setMinute(minute);
                            taskModel.setFlag(flag);
                            list.add(taskModel);
                        }
                    initialRecyclerView();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                Intent intent = new Intent(HomePageActivity.this, TaskActivity.class);
                intent.putExtra("email",firebaseUser.getEmail().toString());
                intent.putExtra("count",count);
                startActivity(intent);
            }
        });


    }

    public void navigationBar()
    {
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.calender_item:
                        startActivity(new Intent(HomePageActivity.this, CalenderActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.time_item:
                        startActivity(new Intent(HomePageActivity.this, TimeActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout_item:
                        firebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
    public void initialRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.notepad_recycleview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}