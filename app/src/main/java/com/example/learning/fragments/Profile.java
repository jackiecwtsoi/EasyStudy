package com.example.learning.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.learning.DbApi;
import com.example.learning.MainActivity;
import com.example.learning.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SQLiteDatabase db;
    private DbApi dbApi;
    private EditText user_name ;
    private EditText phone_number;
    private EditText password;
    private Button save_button;
    private Button cancel_button;
    private int userId;
    private String name_information;
    private String phone_information;

    public Profile() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity main = (MainActivity) getActivity();
        userId = main.getLoginUserId();
        dbApi = new DbApi(db);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user_name = view.findViewById(R.id.editTextTextPersonName);
        phone_number = view.findViewById(R.id.editTextTextPhone);
        password = view.findViewById(R.id.editTextTextPassword);
        save_button = view.findViewById(R.id.save_change_btn);
        cancel_button = view.findViewById(R.id.cacel_change_btn);



        return view;
    }

    @Override
    public void onClick(View view) {

    }
    public void showUserInformation(){
        ArrayList<String> arrayList = dbApi.getUserIfo(userId);


    }
}