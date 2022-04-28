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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String userProfileURL;

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
        userProfileURL = "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/8394f798931623.5ee79b6a909ea.jpg";

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
                String url = "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/8394f798931623.5ee79b6a909ea.jpg";

                if(name.length() == 0  ){
                    Toast.makeText(RegisterActivity.this,"Name can not be empty!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.length() > 20  ){
                    Toast.makeText(RegisterActivity.this,"Name must be less than 20 characters",Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断密码
                if(pass.length() == 0 ){
                    Toast.makeText(RegisterActivity.this,"password can not be empty!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.length() > 16  ){
                    Toast.makeText(RegisterActivity.this,"Password must be less than 16 characters!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.length() != 0 && password.length() < 6  ){
                    Toast.makeText(RegisterActivity.this,"Password must be greater than 8 digits!",Toast.LENGTH_SHORT).show();
                    return;
                }

                //判断邮箱
                if(mail.length() == 0 ){
                    Toast.makeText(RegisterActivity.this,"email can not be empty!",Toast.LENGTH_SHORT).show();
                    return;
                }

                String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern p;
                Matcher m;

                p = Pattern.compile(regEx1);
                m = p.matcher(mail);

                if (!m.matches()){
                    Toast.makeText(RegisterActivity.this,"E-mail format is incorrect!",Toast.LENGTH_SHORT).show();
                    return;
                }




                if (!name.equals("") && !mail.equals("") && !pass.equals("")) {
                    if(isRegister == 1){
                        int result = dbApi.queryUser(name, mail, pass);
                        if (result == -2){
                        userID = (int)dbApi.insertUserFull(name, mail, pass, url);
                        Toast.makeText(RegisterActivity.this, "okokokokokokokok", Toast.LENGTH_LONG).show();
                        startMain();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Email has been registered, please login directly", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        int result = searchUser(name, mail, pass);
                        if (result == -2){
                            Toast.makeText(RegisterActivity.this, "Email not exists, automatically register", Toast.LENGTH_LONG).show();
                            userID = (int)dbApi.insertUserFull(name, mail, pass, url);
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
        intent.putExtra("user_name", userName.getText().toString());
        startActivity(intent);
    }
}
