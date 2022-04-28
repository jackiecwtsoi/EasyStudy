package com.example.learning.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning.DbApi;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.example.learning.SpecialCalendar;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Statistic extends Fragment implements View.OnClickListener,GridView.OnItemClickListener{
    private PieChart pie;
    List<PieEntry> list;
    private GridView registration_calendar_gv;
    private TextView today;
    private RegistrationAdapter adapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    int mYear=0;//年
    int mMonth=0;//月
    int mDay=0;//日
    SQLiteDatabase db;
    private DbApi dbApi;
    private int userid;
    private Statistic mContext;
    private boolean check_sign;
    private int present_num;
    private int done_num;
    private int ongoing_num;
    private int deck_num;
    private TextView attendance_content;
    private TextView Total_w_p_number;
    private TextView Total_done_number;
    private TextView Total_ongoing_number;
    private TextView Total_w_d_number;
    private TextView attendance_label;
    private Button update_btn;


    private OnFragmentInteractionListener mListener;

    public Statistic(SQLiteDatabase db) {
        // Required empty public constructor
        this.db = db;
    }

    public static Statistic newInstance(String param1, String param2, SQLiteDatabase db) {
        Statistic fragment = new Statistic(db);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        dbApi = new DbApi(db);
        MainActivity main = (MainActivity) getActivity();
        userid = main.getLoginUserId();
        String date = getSignDate();
        //Visual of page number
        check_sign = dbApi.checkSign(userid,date);
        present_num = dbApi.getPresentdays(userid);
        String present_days = Integer.toString(present_num);
        done_num = dbApi.getDonenumber(userid);
        ongoing_num = dbApi.getOngoingnumber(userid);
        String done_number = Integer.toString(done_num);
        String ongoing_number = Integer.toString(ongoing_num);
        deck_num = dbApi.getDecknumber(userid);
        String deck_number = Integer.toString(deck_num);



        attendance_content=(TextView)view.findViewById(R.id.attendece_content);
        Total_w_p_number = (TextView)view.findViewById(R.id.Total_w_p_number);
        Total_done_number = (TextView)view.findViewById(R.id.Total_done_number);
        Total_ongoing_number = (TextView)view.findViewById(R.id.Total_ongoing_number);
        Total_w_d_number = (TextView)view.findViewById(R.id.Total_w_d_number);
        attendance_label = view.findViewById(R.id.attendece_label);
        update_btn = view.findViewById(R.id.pie_data_update);
        pie = (PieChart) view.findViewById(R.id.pie);




        if (check_sign==false){
            attendance_label.setBackground(getResources().getDrawable(R.drawable.status_circle_red));
            attendance_content.setText("absent");

        }
        Total_w_p_number.setText(present_days);
        Total_done_number.setText(done_number);
        Total_ongoing_number.setText(ongoing_number);
        Total_w_d_number.setText(deck_number);
        update_btn.setOnClickListener(this);

        //Update piechart
        ShowPiechart(view);





        //创建日历
        registration_calendar_gv=(GridView)view.findViewById(R.id.registration_calendar_gv);
        today=(TextView)view.findViewById(R.id.Today);


        Calendar calendar=Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR); // 获取当前年份
        mMonth = calendar.get(Calendar.MONTH)+1 ;// 获取当前月份以（0开头）
        mDay = calendar.get(Calendar.DAY_OF_MONTH) ;// 获取当前天以（0开头）

        SpecialCalendar mCalendar=new SpecialCalendar();
        boolean isLeapYear =mCalendar.isLeapYear(mYear);
        int mDays=mCalendar.getDaysOfMonth(isLeapYear,mMonth+1);
        int week =mCalendar.getWeekdayOfMonth(mYear,mMonth);

        adapter=new RegistrationAdapter(mDays,week,mDay,mMonth,mYear);
        registration_calendar_gv.setAdapter(adapter);
        registration_calendar_gv.setOnItemClickListener(this);
        today.setText(mYear+"/"+mMonth+"/"+mDay);


        return view;
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
            case R.id.pie_data_update:
                ShowPiechart(view);
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String today=mYear+"-"+mMonth+"-"+l;
        if (l!=0){
            if (l==mDay){




                if (check_sign==false){
                    long SignID = dbApi.insertSign(userid,"True");
                    TextView today_tv= (TextView) view.findViewById(R.id.day);
                    today_tv.setBackgroundResource(R.mipmap.member_ok);
                    today_tv.setTextColor(Color.BLACK);
                    today_tv.setText(l+"");
                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                    UPdateAttendence();
                    UpdatePresentDay();
                    UpdateDonenumber();
                    UpdateOngoingnumber();

                    Toast.makeText(view.getContext(),"签到成功",Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(view.getContext(),"您已经签到，亲~~waku waku",Toast.LENGTH_SHORT).show();
                }

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
        private int month;
        private int year;
        MainActivity main = (MainActivity) getActivity();
        int user_id = main.getLoginUserId();
        private String cal_date;


        public RegistrationAdapter(int days, int week,int day,int month,int year) {
            this.days = days;
            this.week = week;
            this.day=day;
            this.month = month;
            this.year = year;
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
            if (i<10){
                if (month<10) {
                    cal_date = year + "-" +0+ month + "-" + 0 + dayNumber[i];
                }
                else {
                    cal_date = year + "-" + month + "-" + 0 + dayNumber[i];
                }

                }
            else {
                if (month<10) {
                    cal_date = year + "-" +0+ month + "-" + dayNumber[i];
                }
                else {
                    cal_date = year + "-" + month + "-" + dayNumber[i];
                }
            }

            if (null==view){
                view= LayoutInflater.from(getContext()).inflate(R.layout.item_registrationadatper,null);
                viewHolder=new ViewHolder(view);
                view.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.day.setText(dayNumber[i]==0? "" : dayNumber[i]+"");
            boolean check = dbApi.checkSign(user_id,cal_date);
//            if (dayNumber[i]!=0&&dayNumber[i]<day&&dayNumber[i]%2==1){
            if (dayNumber[i]!=0&&dayNumber[i]<day&&check==true){
                viewHolder.day.setBackgroundResource(R.mipmap.member_ok);
            }
            if (dayNumber[i]==day&&check==false){
                viewHolder.day.setText("今");
                view.setBackgroundResource(R.color.colorPrimary);
                viewHolder.day.setTextColor(Color.parseColor("#ffffff"));
            }
            if (dayNumber[i]==day&&check==true){
                viewHolder.day.setBackgroundResource(R.mipmap.member_ok);
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
    public void ShowPiechart(View view){

        // 创建图表
        int easy;
        int hard;
        int forgot;
        ArrayList<Integer> arrary =dbApi.getUserCardLevel(userid);
        easy = arrary.get(0);
        hard = arrary.get(1);
        forgot = arrary.get(2);
        if (easy==0&&hard==0&&forgot==0){
            PieEntry pieEntry1 = new PieEntry(100,"No Data");
        }
        else {


            list = new ArrayList<>();
            if (easy!=0) {
                PieEntry pieEntry1 = new PieEntry(easy, "Forgot");
                list.add(pieEntry1);
            }
            if (hard!=0) {
                PieEntry pieEntry2 = new PieEntry(hard, "Hard");
                list.add(pieEntry2);

            }
            if(forgot!=0){
                PieEntry pieEntry3 = new PieEntry(forgot, "Easy");
                list.add(pieEntry3);
            }


            PieDataSet pieDataSet = new PieDataSet(list, "");
            PieData pieData = new PieData(pieDataSet);
            pieData.setValueTextSize(16);

            pieData.setValueFormatter(new PercentFormatter());
            pie.setData(pieData);
            pie.setExtraOffsets(26, 26, 26, 26);
            pie.setDrawEntryLabels(false);
            pie.setUsePercentValues(true);
            Legend legend=pie.getLegend();
            legend.setTextSize(18);
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setValueLinePart1Length(0.6f);

            pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);//设置各个数据的颜色
        }
    }
    public void UpdatePiechart(View view){

    }
    public String getSignDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
//        System.out.println(formatter.format(date));
        return formatter.format(date);

    }
    public void UPdateAttendence(){
        attendance_label.setBackground(getResources().getDrawable(R.drawable.status_circle));
        attendance_content.setText("Present");

    }
    public void UpdatePresentDay(){
        present_num = dbApi.getPresentdays(userid);
        String present_days = Integer.toString(present_num);
        Total_w_p_number.setText(present_days);

    }
    public void UpdateDonenumber(){
        done_num = dbApi.getDonenumber(userid);

        String done_number = Integer.toString(done_num);
        Total_done_number.setText(done_number);


    }
    public void UpdateOngoingnumber(){
        ongoing_num = dbApi.getOngoingnumber(userid);
        String ongoing_number = Integer.toString(ongoing_num);
        Total_ongoing_number.setText(ongoing_number);
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
