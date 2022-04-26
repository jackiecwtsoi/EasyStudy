package com.example.learning.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.example.learning.DbApi;
import com.example.learning.FolderEntity;
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

    protected boolean isCreated = false;
    WheelView wva;
    private String mParam1;
    private String mParam2;
    private TextView select;
    private TextView add;
    private View addView;
    private View rootView;
    private TextView createDeck;
    private SeekBar addSeekBar;
    private TextView seekDaysText;
    private String selectedFolder;
    private EditText deckName;
    private EditText deckDescription;
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
    private DbApi dbApi;
    private int userid;
    String coverPath = "/storage/emulated/0/Android/data/com.example.learning/files/deckCovers/default.png";
    List<Boolean> whetherDaySelect = new ArrayList<Boolean>();
    List<TextView> selectedDays = new ArrayList<TextView>();
    SQLiteDatabase db;
    private int selectedFolderId = -1;
    //    TextView selected;
    private ArrayList<FolderEntity> folderList = new ArrayList<FolderEntity>();

    private static final String TAG = Add.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    public Add(SQLiteDatabase db) {
        this.db = db;
        // Required empty public constructor
    }

    public static Add newInstance(String param1, String param2, SQLiteDatabase db) {
        Add fragment = new Add(db);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
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
        dbApi = new DbApi(db);
        context = this.getContext();
//        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.spring_showers);
//        coverPath = ImageUtils.saveImageToGallery(context, bm, true);
        selectedFolderId = -1;
        getFolderList();
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
        deckName = rootView.findViewById(R.id.add_deck_name);
        deckDescription = rootView.findViewById(R.id.add_deck_description);
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
                    if (reminder == 0) {
                        Toast toast = Toast.makeText(context, "you have selected no reminder", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
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
                if (reminder == 0) {
                    Toast toast = Toast.makeText(context, "you have selected no reminder", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    seekDaysText.setText(Integer.toString(progress) + " days");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (reminder == 0) {
                    Toast toast = Toast.makeText(context, "you have selected no reminder", Toast.LENGTH_LONG);
                    toast.show();
                    seekBar.setProgress(0);
                }
            }
        });
        addSeekBar.setProgress(0);
        //add.setBackgroundColor(Color.BLUE);
//        selected = rootView.findViewById(R.id.selected_folder);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> folderNames = new ArrayList<>();
                for (FolderEntity folder : folderList) {
                    folderNames.add(folder.getFolderName());
                }
                selectedFolder = folderNames.get(0);
                selectedFolderId = 0;
                View outerView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.folder_wheel_view, null);
                WheelView wv = (WheelView) outerView.findViewById(R.id.foler_wheel_view);
                wv.setOffset(2);
                wv.setItems(folderNames);
                wv.setSeletion(0);
                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                        selectedFolder = item;
                        selectedFolderId = folderList.get(selectedIndex - 2).getFolderID();
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
                if (selectedFolderId == -1) {
                    Toast.makeText(context, "Please select a folder to add the deck", Toast.LENGTH_LONG).show();
                    System.out.println(1);
                } else if (deckName.getText().toString().equals("")) {
                    Toast.makeText(context, "Please input the folder name", Toast.LENGTH_LONG).show();
                    System.out.println(1);
                } else {
                    String name = deckName.getText().toString().trim();

                    String description = deckDescription.getText().toString().trim();
                    if (description.equals("")) {
                        description = "This is the description for " + name;
                    }
                    int frequency = -1;
                    String dayOfWeek = "";
                    int interval = 0;
                    if (reminder == 1) {
                        // select the remind me button
                        frequency = daily;
                        ArrayList<String> days = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
                        if (frequency == 0 || frequency == 1) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < 7; i++) {
                                if (whetherDaySelect.get(i)) {
                                    String day = days.get(i) + ";";
                                    sb.append(day);
                                }
                                dayOfWeek = sb.toString();
                            }
                        } else {
                            frequency = 2;
                            interval = addSeekBar.getProgress();
                        }

                    }
                    long id = dbApi.insertDeck(name, description, 0, frequency, dayOfWeek, interval, selectedFolderId, userid, coverPath, whetherPublic);
                    Intent intent = new Intent(rootView.getContext(), AddCardActivity.class);
                    intent.putExtra("deck_id", (int) id);
                    intent.putExtra("user_id", userid);
                    intent.putExtra("folder_id", selectedFolderId);
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    private void getFolderList() {
        MainActivity main = (MainActivity) getActivity();
        userid = main.getLoginUserId();
        folderList.addAll(dbApi.queryFolder(userid));
    }

    private void setDaily() {
        dailyText.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller_selected));
        dailyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reminder == 0) {
                    Toast toast = Toast.makeText(context, "you have selected no reminder", Toast.LENGTH_LONG);
                    toast.show();
                } else {
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
                if (reminder == 0) {
                    Toast toast = Toast.makeText(context, "you have selected no reminder", Toast.LENGTH_LONG);
                    toast.show();
                } else {
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
                if (reminder == 0) {
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

    private void clearSelectDays() {
        for (TextView day : selectedDays) {
            day.setBackground(ContextCompat.getDrawable(context, R.drawable.corner_icon_smaller));
            whetherDaySelect.set(selectedDays.indexOf(day), false);
            seekDaysText.setText(Integer.toString(0) + " days");
            addSeekBar.setProgress(0);
        }
    }

    private void setReminder() {
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

    private void setPublic() {
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

    private void selectImg() {
        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlbum();
            }
        });
    }

    private void openAlbum() {
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
////        intent.setType("image/*");
////        startActivityForResult(intent, 1);//1是resultcode 我们自己定义
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        deckDescription.getText().clear();
        deckName.getText().clear();
        clearSelectDays();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                try {
                    Uri uriImage = data.getData();
                    Bitmap bitmap = BitmapFactory.decodeStream
                            (getActivity().getContentResolver().openInputStream(uriImage));
//                    String path = RealPathFromUriUtils.getRealPathFromUri(context, uriImage);
//                    System.out.println(path);
                    coverPath = ImageUtils.saveImageToGallery(context, bitmap, "");
                    selectedImg.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        void onFragmentInteraction(Uri uri);
    }

}
