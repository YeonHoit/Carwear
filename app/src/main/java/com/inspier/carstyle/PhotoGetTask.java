package com.inspier.carstyle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.net.URL;

public class PhotoGetTask extends AsyncTask<String, Void, Bitmap> {

    private static String IP_ADDRESS = "pdbx12.cafe24.com";

    Bitmap bitmap = null; //사진 Bitmap 객체

    @Override
    protected Bitmap doInBackground(String... params) {
        String serverURL = "http://" + IP_ADDRESS + ":80/img/" + params[0]; //Server에 있는 Photo 파일 URL

        try {
            URL url = new URL(serverURL);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
