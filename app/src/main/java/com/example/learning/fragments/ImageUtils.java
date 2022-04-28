package com.example.learning.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtils {
    public static String saveImageToGallery(Context context, Bitmap bmp, String defaultImg) {
        //生成路径
        File root = context.getExternalFilesDir(null);
        String dirName = "deckCovers";
        String fileName = "default.png";
        File appDir = new File(root, dirName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        if (defaultImg != "") {
            fileName = defaultImg;
        } else {
            //文件名为时间
            long timeStamp = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sd = sdf.format(new Date(timeStamp));
            fileName = sd + ".jpg";
        }

        //获取文件
        File file = new File(appDir, fileName);
        System.out.println(file.getAbsolutePath());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
