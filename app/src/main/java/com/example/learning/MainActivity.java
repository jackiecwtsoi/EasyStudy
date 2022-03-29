package com.example.learning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.learning.customizeview.HonrizonViewPager;
import com.example.learning.fragments.Add;
import com.example.learning.fragments.Folder;
import com.example.learning.fragments.Home;
import com.example.learning.fragments.Statistic;
import com.example.learning.fragments.Study;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager mcContainer;


    TextView mcHome;
    TextView mcFolder;
    TextView mcAdd;
    TextView mcStudy;
    TextView mcStats;

    private List<Fragment> fragmentList;

    private List<TextView> textViewList;

    private int mDefaultColor = Color.BLACK;

    private int mActiveColor = Color.RED;
    private HonrizonViewPager viewPager;
    private TabLayout tabLayout;
    private static final String TAG = "luchixiang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcHome = findViewById(R.id.home_text);
        mcFolder = findViewById(R.id.folders_text);
        mcAdd = findViewById(R.id.add_text);
        mcStudy = findViewById(R.id.study_text);
        mcStats = findViewById(R.id.stats_text);
        mcContainer = findViewById(R.id.vp_container);
        fragmentList=new ArrayList<Fragment>();
        fragmentList.add(new Home());
        fragmentList.add(new Folder());
        fragmentList.add(new Add());
        fragmentList.add(new Study());
        fragmentList.add(new Statistic());

        textViewList=new ArrayList<TextView>();
        textViewList.add(mcHome);
        textViewList.add(mcFolder);
        textViewList.add(mcAdd);
        textViewList.add(mcStudy);
        textViewList.add(mcStats);
        for (final TextView viewer :
                textViewList) {
            viewer.setTextColor(mDefaultColor);
            viewer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcContainer.setCurrentItem(textViewList.indexOf(viewer));
                }
            });
        }
        textViewList.get(0).setTextColor(mActiveColor);

        mcContainer.setAdapter(new AppFragmentPageAdapter(getSupportFragmentManager(),fragmentList));
        mcContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Do Nothing
            }

            @Override
            public void onPageSelected(int position) {
                for (TextView viewer :
                        textViewList) {
                    viewer.setTextColor(mDefaultColor);
                }
                textViewList.get(position).setTextColor(mActiveColor);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Do Nothing
            }
        });
    }

}
