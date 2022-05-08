package com.example.onlinegyogyszertar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        TextInputEditText email = findViewById(R.id.emailTextInputEditText);
        TextInputEditText password = findViewById(R.id.passwordTextInputEditText);
        Button registerButton = findViewById(R.id.registrationButton);
        DatePicker datePicker = findViewById(R.id.date_picker);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Registration failed",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year =  datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                if(getDateDiff(calendar.getTime(), new Date()) >= 365*18){
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegistrationActivity.this, "Registration failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegistrationActivity.this, "18 éves kor alatt nem vehetsz gyógyszert!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static long getDateDiff(Date oldDate, Date newDate) {
        try {
            return TimeUnit.DAYS.convert(newDate.getTime() - oldDate.getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }



}
