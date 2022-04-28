package com.example.learning.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.learning.DbApi;
import com.example.learning.HashUtil;
import com.example.learning.MainActivity;
import com.example.learning.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri; import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View; import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;





import java.io.File;
import java.util.Date;

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


    private static final int CODE_PHOTO_REQUEST = 1;
    private static final int CODE_CAMERA_REQUEST = 2;
    private static final int CODE_PHOTO_CLIP = 3;
    String coverPath = "/storage/emulated/0/Android/data/com.example.learning/files/deckCovers/default.png";
    private ImageView selectedImg;




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
        imgUserPhoto.setOnClickListener(this);
//        String userProfileURL = dbApi.queryUserProfileURL(userId);
        String userProfileURL = "https://zongwei-design-courses.oss-cn-shenzhen.aliyuncs.com/Images/%E5%9B%BE%E7%89%871.png";
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
                break;
            case R.id.user_profile:
                getPicFromLocal();


        }


    }
    private void getPicFromLocal() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
        }

    private void photoClip(Uri uri) { // 调用系统中自带的图片剪裁
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*"); // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        /*outputX outputY 是裁剪图片宽高 *这里仅仅是头像展示，不建议将值设置过高 * 否则超过binder机制的缓存大小的1M限制 * 报TransactionTooLargeException */
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_PHOTO_CLIP);
    }
    private void setImageToHeadView(Intent intent) { Bundle extras = intent.getExtras();
        if (extras != null) { Bitmap photo = extras.getParcelable("data");
        imgUserPhoto.setImageBitmap(photo);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_CANCELED) { Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_LONG).show();
//            return;
//        }
        switch (requestCode) {
            case CODE_PHOTO_REQUEST:
                if (data != null) {
                    try {
                        Uri uriImage = data.getData();
                        Bitmap bitmap = BitmapFactory.decodeStream
                                (getActivity().getContentResolver().openInputStream(uriImage));
//                    String path = RealPathFromUriUtils.getRealPathFromUri(context, uriImage);
//                    System.out.println(path);
                        coverPath = UserImageUtils.saveImageToGallery(this.getContext(), bitmap, "");
                        imgUserPhoto.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

            }
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



    //与个人的存储区域有关
    private static final String ENDPOINT = "oss-cn-shenzhen.aliyuncs.com";
    //上传仓库名
    private static final String BUCKET_NAME = "zongwei-design-courses";

    private OSS getOSSClient() {
        OSSCredentialProvider credentialProvider =
                new OSSPlainTextAKSKCredentialProvider("LTAI5tH9Rt14dxZpnjbLcFwH" ,
                        "58o4ius32UdePzF5zwOsTatQXIyIC0");
        return new OSSClient(mContext.getContext(), ENDPOINT, credentialProvider);
    }

    /**
     * 上传方法
     *
     * @param objectKey 标识
     * @param path      需上传文件的路径
     * @return 外网访问的路径
     */
    private String upload(String objectKey, String path) {
        // 构造上传请求
        PutObjectRequest request =
                new PutObjectRequest(BUCKET_NAME,
                        objectKey, path);
        try {
            //得到client
            OSS client = getOSSClient();
            //上传获取结果
            PutObjectResult result = client.putObject(request);
            //获取可访问的url
            String url = client.presignPublicObjectURL(BUCKET_NAME, objectKey);
            //格式打印输出

            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 上传普通图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public String uploadImage(String path) {
        String key = getObjectImageKey(path);
        return upload(key, path);
    }

    /**
     * 上传头像
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public  String uploadPortrait(String path) {
        String key = getObjectPortraitKey(path);
        return upload(key, path);
    }

    /**
     * 上传audio
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public String uploadAudio(String path) {
        String key = getObjectAudioKey(path);
        return upload(key, path);
    }


    /**
     * 获取时间
     *
     * @return 时间戳 例如:201805
     */
    private String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    /**
     * 返回key
     *
     * @param path 本地路径
     * @return key
     */
    //格式: image/201805/sfdsgfsdvsdfdsfs.jpg
    private String getObjectImageKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    //格式: portrait/201805/sfdsgfsdvsdfdsfs.jpg
    private String getObjectPortraitKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    //格式: audio/201805/sfdsgfsdvsdfdsfs.mp3
    private String getObjectAudioKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("audio/%s/%s.mp3", dateString, fileMd5);
    }






}