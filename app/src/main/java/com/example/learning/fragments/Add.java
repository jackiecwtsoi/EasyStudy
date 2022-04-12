package com.example.learning.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning.AddCardActivity;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.example.learning.customizeview.WheelView;

import java.io.FileNotFoundException;
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
    private TextView add;
    private View addView;
    private View rootView;
    private TextView createDeck;
    private SeekBar addSeekBar;
    private TextView seekDaysText;
    private String selectedFolder = "Folder 1";
    private TextView monday;
    private TextView tuesday;
    private TextView wednesday;
    private TextView thursday;
    private TextView friday;
    private TextView saturaday;
    private TextView sunday;
    private TextView dailyText;
    private TextView weeklyText;
    private Context context;
    private TextView monthly;
    private TextView remindMe;
    private TextView notRemindMe;
    private int reminder = 0;
    private TextView publicText;
    private TextView privateText;
    private int whetherPublic = 0;
    private int daily = 0;
    private TextView browser;
    private ImageView selectedImg;
    private TextView lastSelectedDays;
    List<Boolean> whetherDaySelect = new ArrayList<Boolean>();
    List<TextView> selectedDays = new ArrayList<TextView>();
    //    TextView selected;
    private static String[] PLANETS = new String[]{"Folder1", "Folder2", "Folder3", "Folder4", "Folder5", "Folder6", "Folder7", "Folder8"};
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
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        context = this.getContext();
        folderList.addAll(Arrays.asList(PLANETS));
        rootView = inflater.inflate(R.layout.fragment_add, container, false);
        select = rootView.findViewById(R.id.select_folder);
        createDeck = rootView.findViewById(R.id.add_create_deck);
        addSeekBar = rootView.findViewById(R.id.add_progress);
        seekDaysText = rootView.findViewById(R.id.add_progress_text);
        monday = rootView.findViewById(R.id.add_mon);
        tuesday = rootView.findViewById(R.id.add_tue);
        wednesday = rootView.findViewById(R.id.add_wen);
        thursday = rootView.findViewById(R.id.add_thu);
        friday = rootView.findViewById(R.id.add_fri);
        saturaday = rootView.findViewById(R.id.add_sat);
        sunday = rootView.findViewById(R.id.add_sun);
        dailyText = rootView.findViewById(R.id.add_daily);
        weeklyText = rootView.findViewById(R.id.add_weekly);
        monthly = rootView.findViewById(R.id.add_monthly);
        remindMe = rootView.findViewById(R.id.add_reminder_true);
        notRemindMe = rootView.findViewById(R.id.add_reminder_false);
        publicText = rootView.findViewById(R.id.add_public_true);
        privateText = rootView.findViewById(R.id.add_public_false);
        browser = rootView.findViewById(R.id.add_browse_button);
        selectedImg = rootView.findViewById(R.id.add_selected_img);
        setDaily();
        setReminder();
        setPublic();
        selectImg();
        selectedDays.add(monday);
        whetherDaySelect.add(false);
        selectedDays.add(tuesday);
        whetherDaySelect.add(false);
        selectedDays.add(wednesday);
        whetherDaySelect.add(false);
        selectedDays.add(thursday);
        whetherDaySelect.add(false);
        selectedDays.add(friday);
        whetherDaySelect.add(false);
        selectedDays.add(saturaday);
        whetherDaySelect.add(false);
        selectedDays.add(sunday);
        whetherDaySelect.add(false);

        for (final TextView day : selectedDays) {
            day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (reminder == 0){
                        Toast toast = Toast.makeText(context, "you have selected no reminder", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else {
                        if (daily == 0) {
                            if (!whetherDaySelect.get(selectedDays.indexOf(day))) {
                                day.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
                                whetherDaySelect.set(selectedDays.indexOf(day), true);

                            } else {
                                day.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller));
                                whetherDaySelect.set(selectedDays.indexOf(day), false);
                            }
                        } else if (daily == 1) {
                            day.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
                            whetherDaySelect.set(selectedDays.indexOf(day), true);
                            if (lastSelectedDays == null) {
                                lastSelectedDays = day;
                            } else if (lastSelectedDays.getText() != day.getText()) {
                                lastSelectedDays.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller));
                                whetherDaySelect.set(selectedDays.indexOf(lastSelectedDays), false);
                                lastSelectedDays = day;
                            }
                        } else {
                            Toast toast = Toast.makeText(context, "You should select days directly as you choose to remind you monthly", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }
            });
        }
        addSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (reminder == 0){
                    Toast toast = Toast.makeText(context, "you have selected no reminder", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                seekDaysText.setText(Integer.toString(progress) + " days");}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (reminder == 0){
                    Toast toast = Toast.makeText(context, "you have selected no reminder", Toast.LENGTH_LONG);
                    toast.show();
                    seekBar.setProgress(0);
                }
            }
        });

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
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                select.setText(selectedFolder);
                            }
                        })
                        .show();
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

    private void setDaily() {
        dailyText.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
        dailyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reminder == 0){
                    Toast toast = Toast.makeText(context, "you have selected no reminder", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    dailyText.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
                    weeklyText.setBackgroundResource(0);
                    monthly.setBackgroundResource(0);
                    daily = 0;
                }
            }

        });
        weeklyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reminder == 0){
                    Toast toast = Toast.makeText(context, "you have selected no reminder", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    dailyText.setBackgroundResource(0);
                    weeklyText.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
                    monthly.setBackgroundResource(0);
                    daily = 1;
                    clearSelectDays();
                }
            }
        });
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reminder == 0){
                    Toast toast = Toast.makeText(context, "you have selected no reminder", Toast.LENGTH_LONG);
                    toast.show();
                }
                {
                    dailyText.setBackgroundResource(0);
                    weeklyText.setBackgroundResource(0);
                    monthly.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
                    daily = 2;
                    clearSelectDays();
                }
            }
        });
    }
    private void clearSelectDays(){
        for (TextView day: selectedDays){
            day.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller));
            whetherDaySelect.set(selectedDays.indexOf(day), false);
            seekDaysText.setText(Integer.toString(0) + " days");
            addSeekBar.setProgress(0);
        }
    }
    private void setReminder(){
        reminder = 1;
        remindMe.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
        remindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remindMe.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
                notRemindMe.setBackgroundResource(0);
                reminder = 1;
            }
        });
        notRemindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notRemindMe.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
                remindMe.setBackgroundResource(0);
                reminder = 0;
                dailyText.setBackgroundResource(0);
                weeklyText.setBackgroundResource(0);
                monthly.setBackgroundResource(0);
                clearSelectDays();
            }
        });
    }
    private void setPublic(){
        whetherPublic = 1;
        publicText.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
        publicText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicText.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
                privateText.setBackgroundResource(0);
                whetherPublic = 1;
            }
        });
        privateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privateText.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
                publicText.setBackgroundResource(0);
                whetherPublic = 0;
            }
        });

    }
    private void selectImg(){
        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlbum();
            }
        });
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 1);//1是resultcode 我们自己定义

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if (data!=null)
            {
                try {
                    Bitmap bitmap= BitmapFactory.decodeStream
                            (getActivity().getContentResolver().openInputStream(data.getData()));
                    selectedImg.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
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
