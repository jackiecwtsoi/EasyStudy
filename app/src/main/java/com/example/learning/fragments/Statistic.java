package com.example.learning.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning.R;
import com.example.learning.SpecialCalendar;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Statistic.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Statistic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Statistic extends Fragment implements View.OnClickListener,GridView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PieChart pie;
    List<PieEntry> list;
    private GridView registration_calendar_gv;
    private TextView today;
    private RegistrationAdapter adapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    int mYear=0;//年
    int mMonth=0;//月
    int mDay=0;//日


    private OnFragmentInteractionListener mListener;

    public Statistic() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Statistic.
     */
    // TODO: Rename and change types and number of parameters
    public static Statistic newInstance(String param1, String param2) {
        Statistic fragment = new Statistic();
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
        // 创建图表
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        pie = (PieChart) view.findViewById(R.id.pie);
        list=new ArrayList<>();


        list.add(new PieEntry(30,"Forset"));
        list.add(new PieEntry(30,"Hard"));
        list.add(new PieEntry(40,"Easy"));

        PieDataSet pieDataSet=new PieDataSet(list,"");
        PieData pieData=new PieData(pieDataSet);
        pie.setData(pieData);

        pieDataSet.setColors(Color.rgb(69,124,153), Color.rgb(69,124,153),Color.rgb(69,124,153));//设置各个数据的颜色

        //创建日历
        registration_calendar_gv=(GridView)view.findViewById(R.id.registration_calendar_gv);
        today=(TextView)view.findViewById(R.id.Today);

        Calendar calendar=Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR); // 获取当前年份
        mMonth = calendar.get(Calendar.MONTH) ;// 获取当前月份以（0开头）
        mDay = calendar.get(Calendar.DAY_OF_MONTH) ;// 获取当前天以（0开头）

        SpecialCalendar mCalendar=new SpecialCalendar();
        boolean isLeapYear =mCalendar.isLeapYear(mYear);
        int mDays=mCalendar.getDaysOfMonth(isLeapYear,mMonth+1);
        int week =mCalendar.getWeekdayOfMonth(mYear,mMonth);

        adapter=new RegistrationAdapter(mDays,week,mDay);
        registration_calendar_gv.setAdapter(adapter);
        registration_calendar_gv.setOnItemClickListener(this);
        today.setText(mYear+"/"+mMonth+"/"+mDay);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String today=mYear+"-"+mMonth+"-"+l;
        if (l!=0){
            if (l==mDay){
                TextView today_tv= (TextView) view.findViewById(R.id.day);
                today_tv.setBackgroundResource(R.mipmap.member_ok);
                today_tv.setTextColor(Color.BLACK);
                today_tv.setText(l+"");
                view.setBackgroundColor(Color.parseColor("#ffffff"));
                Toast.makeText(view.getContext(),"签到成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(view.getContext(),"您选择的日期："+today,Toast.LENGTH_SHORT).show();

            }
        }

    }
    public class RegistrationAdapter extends BaseAdapter {

        private final int days;
        private final int week;
        private int[] dayNumber;
        private final int day;

        public RegistrationAdapter(int days, int week,int day) {
            this.days = days;
            this.week = week;
            this.day=day;
            getEveryDay();
        }



        @Override
        public int getCount() {
            return 42;
        }

        @Override
        public String getItem(int i) {

            return null;
        }

        @Override
        public long getItemId(int i) {
            return dayNumber[i];
        }//点击时

        private ViewHolder viewHolder;
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (null==view){
                view= LayoutInflater.from(getContext()).inflate(R.layout.item_registrationadatper,null);
                viewHolder=new ViewHolder(view);
                view.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.day.setText(dayNumber[i]==0? "" : dayNumber[i]+"");
            if (dayNumber[i]!=0&&dayNumber[i]<day&&dayNumber[i]%2==1){
                viewHolder.day.setBackgroundResource(R.mipmap.member_ok);
            }
            if (dayNumber[i]==day){
                viewHolder.day.setText("今");
                view.setBackgroundResource(R.color.colorPrimary);
                viewHolder.day.setTextColor(Color.parseColor("#ffffff"));
            }

            return view;
        }

        private class ViewHolder{
            private TextView day;

            public ViewHolder(View view) {
                this.day = (TextView) view.findViewById(R.id.day);
            }
        }

        /**
         * 得到42格子 每一格子的值
         */
        private void getEveryDay(){
            dayNumber=new int[42];

            for (int i=0;i<42;i++){
                if (i < days+week && i >= week){
                    dayNumber[i]=i-week+1;
                }else{
                    dayNumber[i]=0;
                }
            }
        }
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
