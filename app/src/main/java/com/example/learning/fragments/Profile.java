package com.example.learning.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning.DbApi;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment implements View.OnClickListener{
    SQLiteDatabase db;
    private DbApi dbApi;
    private TextView email;
    private EditText user_name ;
    private EditText phone_number;
    private EditText password;
    private Button save_button;
    private Button cancel_button;
    private ImageView btnProfileBack;
    private int userId;
    private String name_information;
    private String phone_information;
    private Profile mContext;
    private ImageView imgUserPhoto;

    public Profile(SQLiteDatabase db) {
        // Required empty public constructor
        this.db = db;
    }

    public static Profile newInstance(SQLiteDatabase db) {
        Profile fragment = new Profile(db);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity main = (MainActivity) getActivity();
        userId = main.getLoginUserId();
        mContext = Profile.this;
        dbApi = new DbApi(db);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        email = view.findViewById(R.id.TextTextmail);
        user_name = view.findViewById(R.id.editTextTextPersonName);
        phone_number = view.findViewById(R.id.editTextTextPhone);
        password = view.findViewById(R.id.editTextTextPassword);
        save_button = view.findViewById(R.id.save_change_btn);
        cancel_button = view.findViewById(R.id.cacel_change_btn);
        imgUserPhoto = view.findViewById(R.id.user_profile);
        String userProfileURL = dbApi.queryUserProfileURL(userId);
        if (!userProfileURL.isEmpty()) {
            Picasso.get()
                    .load(userProfileURL)
                    .into(imgUserPhoto);
        }

        showUserInformation();
        save_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);

        btnProfileBack = view.findViewById(R.id.btnProfileBack);

        // when the back button is clicked, change to home fragment
        btnProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("change to home");
                Home homeFragment = new Home(db);
                FragmentManager homeFragmentManager = getFragmentManager();
                homeFragmentManager.beginTransaction()
                        .replace(R.id.layoutProfile, homeFragment)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_change_btn:
                updateInfomation(view);
                break;
            case R.id.cacel_change_btn:
                showUserInformation();
        }


    }
    public void showUserInformation(){
        ArrayList<String> arrayList = dbApi.getUserInfo(userId);
        String email_info =arrayList.get(0);
        String name_info = arrayList.get(1);
        String phone_number_info = arrayList.get(2);
        String password_num = arrayList.get(3);
        email.setText(email_info);
        user_name.setText(name_info);
        phone_number.setText(phone_number_info);
        password.setText(password_num);


    }
    public void updateInfomation(View view){
        register(view);

    }
    public void register(View v){
        String namestring = user_name.getText().toString();
        String passwordstring = password.getText().toString();
        String emailstring = email.getText().toString();
        String phonestring = phone_number.getText().toString();
        //判断账号
        if(namestring.length() == 0  ){
            Toast.makeText(v.getContext(),"Name can not be empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(namestring.length() > 20  ){
            Toast.makeText(v.getContext(),"Name must be less than 20 characters",Toast.LENGTH_SHORT).show();
            return;
        }
//        if(namestring.length() != 0 && namestring.length() < 8  ){
//            Toast.makeText(getApplicationContext(),"账号必须大于8位",Toast.LENGTH_SHORT).show();
//            return;
//        }
        //判断密码
        if(passwordstring.length() == 0 ){
            Toast.makeText(v.getContext(),"password can not be empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwordstring.length() > 16  ){
            Toast.makeText(v.getContext(),"Password must be less than 16 characters!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwordstring.length() != 0 && passwordstring.length() < 6  ){
            Toast.makeText(v.getContext(),"Password must be greater than 8 digits!",Toast.LENGTH_SHORT).show();
            return;
        }

        //判断邮箱
        if(emailstring.length() == 0 ){
            Toast.makeText(v.getContext(),"email can not be empty!",Toast.LENGTH_SHORT).show();
            return;
        }

        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;

        p = Pattern.compile(regEx1);
        m = p.matcher(emailstring);

        if (!m.matches()){
            Toast.makeText(v.getContext(),"E-mail format is incorrect!",Toast.LENGTH_SHORT).show();
            return;
        }

        //判断电话
        if(phonestring.length() == 0 ){
            Toast.makeText(v.getContext(),"Phone cannot be empty!",Toast.LENGTH_SHORT).show();
            return;
        }

        Pattern p1 = Pattern.compile("^1[3,5,7,8,9][0-9]{9}$");
        Matcher m1 = p1.matcher(phonestring);

        if(!m1.matches()){
            Toast.makeText(v.getContext(),"Incorrect phone format!",Toast.LENGTH_SHORT).show();
            return;
        }
        dbApi.UpdateUserIfo(userId,namestring,phonestring,passwordstring);

        Toast.makeText(v.getContext(),"Congraduation,It is ok!",Toast.LENGTH_SHORT).show();

    }




}