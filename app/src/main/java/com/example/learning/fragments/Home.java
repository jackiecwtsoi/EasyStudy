package com.example.learning.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements View.OnClickListener{
    private Banner mBanner1,mBanner2,mBanner3;
    private TextView mTvTarget;
    private LinearLayout mLlBanner1,mLlBanner2,mLlBanner3;
    private QMUIRoundButton mBtnTotalProgress,mBtnCurrentProgress;
    private ImageView btnFriends;

    private View rootView;
    private OnFragmentInteractionListener mListener;
    SQLiteDatabase db;
    //The parameters of user image in home page.
    private QMUIRadiusImageView user_image;

    public Home(SQLiteDatabase db) {
        this.db = db;
    }

    public static Home newInstance(String param1, String param2, SQLiteDatabase db) {
        Home fragment = new Home(db);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        if (container != null) {
            container.removeView(container.findViewById(R.id.recyclerViewFriends));
            container.removeView(container.findViewById(R.id.btnFriendRequests));
            container.removeView(container.findViewById(R.id.btnAddFriend));
            container.removeView(container.findViewById(R.id.btnFriendsBack));
            container.removeView(container.findViewById(R.id.textContacts));
        }

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mTvTarget = rootView.findViewById(R.id.tvProgress);
        mBtnTotalProgress = rootView.findViewById(R.id.btnTotalProgress);
        mBtnCurrentProgress = rootView.findViewById(R.id.btnCurrentProgress);
        mLlBanner1 = rootView.findViewById(R.id.llBanner1);
        mLlBanner2 = rootView.findViewById(R.id.llBanner2);
        mLlBanner3 = rootView.findViewById(R.id.llBanner3);
        mBanner1 = rootView.findViewById(R.id.banner1);
        mBanner2 = rootView.findViewById(R.id.banner2);
        mBanner3 = rootView.findViewById(R.id.banner3);
        ArrayList list1 = new ArrayList();
        list1.add(R.mipmap.bg_default_banner);
        list1.add(R.mipmap.tu1);
        list1.add(R.mipmap.tu2);
        ArrayList list2 = new ArrayList();
        list2.add(R.mipmap.tu3);
        list2.add(R.mipmap.tu4);
        list2.add(R.mipmap.tu1);
        ArrayList list3 = new ArrayList();
        list3.add(R.mipmap.undraw_predictive_analytics_kf9n);
        list3.add(R.mipmap.undraw_predictive_analytics_kf9n);
        list3.add(R.mipmap.undraw_predictive_analytics_kf9n);
        initBanner(mBanner1,list1);
        initBanner(mBanner2,list2);
        initBanner(mBanner3,list3);
        setProgress(0.7F);
        // Inflate the layout for this fragment

        user_image = rootView.findViewById(R.id.touxiang);
        user_image.setOnClickListener(this);


        // define variables
        //btnFriends = rootView.findViewById(R.id.btnFriends);
        btnFriends = rootView.findViewById(R.id.friend);

        // when the friends button is pressed, switch to a new fragment which contains the friends list
        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Friends friendsFragment = new Friends(db);
                FragmentManager friendsFragmentManager = getFragmentManager();
                friendsFragmentManager.beginTransaction()
                        .replace(R.id.layoutHome, friendsFragment)
                        .addToBackStack("home")
                        .commit();
            }
        });

        return rootView;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.touxiang:
                MainActivity main = (MainActivity) getActivity();
                main.changeToProfile();

        }

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initBanner(Banner banner, ArrayList<Integer> resId) {
        banner.setImages(resId);
        banner.setImageLoader(new CustomLoader());
        banner.isAutoPlay(true);
        banner.setDelayTime(3000);
        banner.start();
    }

    private void setProgress(float per) {
        float total = 280F;
        float current = total*per;
        float px = dpToPx(getContext(),current);
        RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams) mBtnCurrentProgress.getLayoutParams());
//        mBtnCurrentProgress.setWidth(Math.round(px));
        params.width = Math.round(px);

        mBtnCurrentProgress.setLayoutParams(params);
        mTvTarget.setText("Target:" + Math.round((80 * per)) + "/80");
    }

    public class CustomLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

    private float dpToPx(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dp*scale+0.5F;
    }
}
