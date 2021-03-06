package com.example.learning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning.customizeview.HonrizonViewPager;
import com.example.learning.fragments.Add;
import com.example.learning.fragments.Deck;
import com.example.learning.fragments.Folder;
import com.example.learning.fragments.Home;
import com.example.learning.fragments.Profile;
import com.example.learning.fragments.Statistic;
import com.example.learning.fragments.Study;
import com.example.learning.fragments.StudyFront;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager mcContainer;


    ImageView mcHome;
    ImageView mcFolder;
    ImageView mcAdd;
    ImageView mcStudy;
    ImageView mcStats;

    private List<Fragment> fragmentList;

    private List<ImageView> textViewList;

    private int mDefaultColor = Color.BLACK;

    private int mActiveColor = Color.RED;
    private HonrizonViewPager viewPager;
    private TabLayout tabLayout;
    private static final String TAG = "luchixiang";
    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    FragmentTransaction fragmentTransaction;
    ArrayList<Integer> defaultIcons = new ArrayList<>();
    ArrayList<Integer> fillIcons = new ArrayList<>();
    private int uid;
    private String userName;

    private DeckEntity selectedDeck;
    private int selectedFriendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        uid = intent.getIntExtra("user_id", 1);
        userName = intent.getStringExtra("user_name");
        createNotificationChannel();

        // connect to the database
        myDBHelper = new MyDBOpenHelper(MainActivity.this, "elearning.db", null, 1);
        db = myDBHelper.getWritableDatabase();

        sendNotification(db);

        mcHome = findViewById(R.id.home_text);
        mcFolder = findViewById(R.id.folders_text);
        mcAdd = findViewById(R.id.add_text);
        mcStudy = findViewById(R.id.study_text);
        mcStats = findViewById(R.id.stats_text);
        mcContainer = findViewById(R.id.vp_container);

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new Home(db));
        fragmentList.add(new Folder(db));
        fragmentList.add(new Add(db));
        fragmentList.add(new Study(db));
        fragmentList.add(new Statistic(db));
//        fragmentList.add(new Profile(db));

        textViewList = new ArrayList<ImageView>();
        textViewList.add(mcHome);
        textViewList.add(mcFolder);
        textViewList.add(mcAdd);
        textViewList.add(mcStudy);
        textViewList.add(mcStats);
        defaultIcons.add(R.drawable.home);
        defaultIcons.add(R.drawable.folder);
        defaultIcons.add(R.drawable.add);
        defaultIcons.add(R.drawable.study);
        defaultIcons.add(R.drawable.statistic);
        fillIcons.add(R.drawable.home_fill);
        fillIcons.add(R.drawable.folder_fill);
        fillIcons.add(R.drawable.add_fill);
        fillIcons.add(R.drawable.study_fill);
        fillIcons.add(R.drawable.statistic_fill);
        mcHome.setImageResource(R.drawable.home_fill);
        for (final ImageView viewer :
                textViewList) {
            viewer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.home_text:
                            System.out.println("we select the home tab");
                            // fragmentList[0] = new Home();
                            mcContainer.setCurrentItem(0);
                            break;
                        case R.id.folders_text:
                            System.out.println("we select the folder tab");
                            mcContainer.setCurrentItem(1);
                            break;
                        case R.id.add_text:
                            System.out.println("we select the add tab");
                            mcContainer.setCurrentItem(2);
                            break;
                        case R.id.study_text:
                            System.out.println("we select the study tab");
                            mcContainer.setCurrentItem(3);
                            break;
                        case R.id.stats_text:
                            System.out.println("we select the stats tab");
                            mcContainer.setCurrentItem(4);
                            break;
                    }
                }
            });
        }

        mcContainer.setAdapter(new AppFragmentPageAdapter(getSupportFragmentManager(), fragmentList));
        mcContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Do Nothing
            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i < 5; i++){
                    textViewList.get(i).setImageResource(defaultIcons.get(i));
                }
                textViewList.get(position).setImageResource(fillIcons.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Do Nothing
            }
        });
        mcContainer.setOffscreenPageLimit(0);

    }

    public void changeToStudy(DeckEntity selected_deck) {
        this.selectedDeck = selected_deck;
        mcContainer.setCurrentItem(3);
    }

    public void changeToFolder(int selectedFriendId) {
        this.selectedFriendId = selectedFriendId;
        mcContainer.setCurrentItem(1);

    }

    public void changeToDeck() {
        mcContainer.setCurrentItem(1);
    }

    public int getSelectedFriendId() {
        return this.selectedFriendId;
    }

    public void changeToProfile() {
        mcContainer.setCurrentItem(5);
    }

    public DeckEntity getSelectedDeck() {
        return this.selectedDeck;
    }

    public int getLoginUserId() {
        return uid;
    }

    public String getUserName() {
        return this.userName;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "reminderChannel";
            String channelDescription = "Channel for reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyTest", channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(SQLiteDatabase db) {
        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        intent.putExtra("userId", getLoginUserId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }

//    @Override
//    public void onBackPressed() {
//        Log.d("FragmentList", getSupportFragmentManager().getFragments().toString());
//    }

}
