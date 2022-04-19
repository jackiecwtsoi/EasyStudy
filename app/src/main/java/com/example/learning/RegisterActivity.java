package com.example.learning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText userName;
    private EditText email;
    private EditText password;
    private TextView register;
    private TextView head;
    private int isRegister;
    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private DbApi dbApi;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login2);
        myDBHelper = new MyDBOpenHelper(RegisterActivity.this, "elearning.db", null, 1);
        db = myDBHelper.getWritableDatabase();
        dbApi = new DbApi(db);
        head = findViewById(R.id.login_head);
        register = findViewById(R.id.login_button);
        userName = findViewById(R.id.login_user_name);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        Intent intent = getIntent();
        isRegister = intent.getIntExtra("register", 0);
        if (isRegister == 0) {
            head.setText("Log in");
            register.setText("Log in");
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userName.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                if (!name.equals("") && !mail.equals("") && !pass.equals("")) {
                    if(isRegister == 1){
                        int result = dbApi.queryUser(name, mail, pass);
                        if (result == -2){
                        userID = (int)dbApi.insertUserFull(name, mail, pass);
                        startMain();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Email has been registered, please login directly", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        int result = searchUser(name, mail, pass);
                        if (result == -2){
                            Toast.makeText(RegisterActivity.this, "Emial not exists, automatically register", Toast.LENGTH_LONG).show();
                            userID = (int)dbApi.insertUserFull(name, mail, pass);
                            startMain();
                        }
                        else if(result == -1){
                            Toast.makeText(RegisterActivity.this, "userName and password are wrong", Toast.LENGTH_LONG).show();
                        }
                        else{
                            userID = result;
                            startMain();
                        }
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "email and userName and password should not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

//    private int registerFun(String name, String mail, String pass) {
//        int result = dbApi.queryUser(name, mail, pass);
//        if (result ==-2){
//        return (int) dbApi.insertUserFull(name, mail, pass);}
//        else{
//
//        }
//    }
    private int searchUser(String name, String mail, String pass){
        return dbApi.queryUser(name, mail, pass);
    }
    private void startMain(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.putExtra("user_id", userID);
        startActivity(intent);
    }
}
