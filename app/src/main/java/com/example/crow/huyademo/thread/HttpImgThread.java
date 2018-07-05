package com.example.crow.huyademo.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpImgThread extends Thread {
    private Handler handler;
    private ImageView iv;
    private String strurl;
    private String imgname;

    private static final String TAG = "HttpImgThread";

    public HttpImgThread (Handler handler, ImageView iv, String strurl){
        this.handler = handler;
        this.iv = iv;
        this.strurl = strurl;
        String []temp = strurl.split("/");
        this.imgname = temp[temp.length-1];
    }

    @Override
    public void run() {
        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL(strurl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(5000);
            con.setDoInput(true);
            //InputStream in = con.getInputStream();
            File parent = Environment.getExternalStorageDirectory();
            String path=parent.getPath()+"/Pictures";
            File file = new File(path,imgname);
            if(file.exists()) {//文件存在则直接使用
                final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null){
                            iv.setImageBitmap(bitmap);
                        }
                    }
                });
                return;
            }
            //文件不存在则开启下载
            FileOutputStream fos = new FileOutputStream(file);
            InputStream in = con.getInputStream();
            byte ch[] = new byte[2 * 1024];
            int len;
            if (fos != null){
                while ((len = in.read(ch)) != -1){
                    fos.write(ch,0,len);
                }
                in.close();
                fos.close();
            }
            final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (bitmap != null){
                        iv.setImageBitmap(bitmap);
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}