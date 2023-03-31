package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, password, confirmpass;
    private Button SignUp;
    private ImageView backB;
    private FirebaseAuth mAuth;
    private String emailStr, passStr, confirmPassStr, nameStr;
    private Dialog progressDialog;
    private TextView dialogText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        name = findViewById(R.id.username);
        email = findViewById(R.id.emailID);
        password = findViewById(R.id.password);
        confirmpass = findViewById(R.id.confirm_pass);
        SignUp = findViewById(R.id.signupB);
        backB = findViewById(R.id.backB);


        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = findViewById(R.id.dialog_text);
        dialogText.setText("Registering user");

        mAuth = FirebaseAuth.getInstance();

        backB.setOnClickListener(view -> finish());

        SignUp.setOnClickListener(view -> {

            if(validate()){
                signupNewUser();
            }


        });

    }
    private boolean validate(){
        nameStr = name.getText().toString().trim();
        passStr = password.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        confirmPassStr = confirmpass.getText().toString().trim();

        if(nameStr.isEmpty()){
            name.setError("Enter your name");
            return false;
        }
        if(emailStr.isEmpty()){
            email.setError("Enter Email ID");
            return false;
        }
        if(passStr.isEmpty())
        {
            password.setError("Enter password");
            return false;
        }
        if(confirmPassStr.isEmpty()){
            confirmpass.setError("Enter Password");
            return false;
        }
        if(passStr.compareTo(confirmPassStr) !=0){
            Toast.makeText(SignUpActivity.this, "Password and confirm password must be same !",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signupNewUser(){
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        DBQuery.createUserData(emailStr, nameStr, new MyCompleteListener(){

                            @Override
                            public void onSuccess() {
                                DBQuery.loadData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        SignUpActivity.this.finish();

                                    }

                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    }
                                });


                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        });


                    } else {


                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }







}