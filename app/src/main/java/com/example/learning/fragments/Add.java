package com.example.learning.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning.AddCardActivity;
import com.example.learning.R;
import com.example.learning.customizeview.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Add.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Add#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    protected boolean isCreated = false;
    WheelView wva;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView select;
    private  TextView add;
    private View addView;
    private View rootView;
    private TextView createDeck;
    private String selectedFolder = "Folder 1";
//    TextView selected;
    private static  String[] PLANETS = new String[]{"Folder1", "Folder2", "Folder3", "Folder4", "Folder5", "Folder6", "Folder7", "Folder8"};
    private ArrayList<String> folderList = new ArrayList<String>();

    private static final String TAG = Add.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    public Add() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Add.
     */
    // TODO: Rename and change types and number of parameters
    public static Add newInstance(String param1, String param2) {
        Add fragment = new Add();
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
        System.out.println("add view created");
        isCreated = true;
        if (rootView != null){
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null){
                parent.removeView(rootView);
            }
        }
        folderList.addAll(Arrays.asList(PLANETS));
        rootView = inflater.inflate(R.layout.fragment_add, container, false);
        addView = inflater.inflate(R.layout.create_folder,container,false);
        addView.setBackgroundColor(Color.WHITE);
        select = rootView.findViewById(R.id.select_folder);
        add = rootView.findViewById(R.id.add_folder);
        createDeck = rootView.findViewById(R.id.add_create_deck);
        add.setTextColor(Color.WHITE);
        //add.setBackgroundColor(Color.BLUE);
//        selected = rootView.findViewById(R.id.selected_folder);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View outerView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.folder_wheel_view, null);
                WheelView wv = (WheelView) outerView.findViewById(R.id.foler_wheel_view);
                wv.setOffset(2);
                wv.setItems(folderList);
                wv.setSeletion(3);
                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                        selectedFolder = item;
                    }
                });

                new AlertDialog.Builder(rootView.getContext())
                        .setTitle("Select your folder")
                        .setView(outerView)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                select.setText(selectedFolder);
                            }
                        })
                        .show();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        final AlertDialog dialog = builder.create();
        // dialog.setTitle("Add folder");
        dialog.setView(addView);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                TextView cancel = addView.findViewById(R.id.add_folder_cancel);
                TextView save = addView.findViewById(R.id.add_folder_save);
                final EditText title = addView.findViewById(R.id.create_folder_input);
                EditText description = addView.findViewById(R.id.create_folder_description);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        folderList.add(title.getText().toString());
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        createDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), AddCardActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("Add on resume");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
