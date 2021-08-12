package com.example.timemanagerfirebase.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timemanagerfirebase.R;
import com.example.timemanagerfirebase.manager.HomePageActivity;
import com.example.timemanagerfirebase.model.UserModel;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    EditText userNameEditText, emailEditText, passwordEditText;
    Button registerButton;
    TextView loginTextView;
    FirebaseAuth firebaseAuth;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userNameEditText = findViewById(R.id.user_name_edit_text);
        emailEditText =findViewById(R.id.E_mail_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginTextView = findViewById(R.id.Login_text_view);
        registerButton = findViewById(R.id.Register_button);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite foldingCube = new Wave();
        progressBar.setIndeterminateDrawable(foldingCube);
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = userNameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                if(userName.equals("") || email.equals("") || password.equals("")){
                    Toast.makeText(RegistrationActivity.this, "Invalid!", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() <= 6){
                    Toast.makeText(RegistrationActivity.this, "Password length should be greater than 6"+email, Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegistrationActivity.this, "Already have an account please login", Toast.LENGTH_SHORT).show();
                                userNameEditText.setText("");
                                emailEditText.setText("");
                                passwordEditText.setText("");
                                progressBar.setVisibility(View.GONE);
                                firebaseAuth.getInstance().signOut();

                            }
                            else{
                                progressBar.setVisibility(View.VISIBLE);
                                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            UserModel userModel = new UserModel();
                                            userModel.setUserName(userName);
                                            userModel.setEmail(email);
                                            userModel.setPassword(password);
                                            ref.push().setValue(userModel);

                                            userNameEditText.setText("");
                                            emailEditText.setText("");
                                            passwordEditText.setText("");
                                            progressBar.setVisibility(View.GONE);

                                            Intent intent = new Intent(RegistrationActivity.this, HomePageActivity.class);
                                            startActivity(intent);


                                        }
                                        else{
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });
        loginTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                return false;
            }
        });


    }
}