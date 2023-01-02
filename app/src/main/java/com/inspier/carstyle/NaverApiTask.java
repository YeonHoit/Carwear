package com.inspier.carstyle;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//네이버 로그인 시 필요한 회원 정보를 가지고 오는 class
public class NaverApiTask extends AsyncTask<String, Void, String> {
    String AccessToken; //네이버 접근 토큰
    String result; //회원 정보 JSON 저장

    //값을 넘겨 받아오는 생성자
    NaverApiTask(String AccessToken) {
        this.AccessToken = AccessToken;
    }

    @Override
    protected String doInBackground(String... strings) {
        String header = "Bearer " + AccessToken; //결과를 받아오는 데 필요한 header String

        try {
            String apiURL = "https://openapi.naver.com/v1/nid/me"; //회원 정보를 가져오는 URL

            //URL Open 및 Connect
            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", header);
            int responseCode = conn.getResponseCode();
            BufferedReader br;

            //정상적으로 호출 되었을 때
            if(responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            //에러가 발생 되었을 때
            else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            //결과 값을 읽어오기 위한 객체 선언
            String inputLine;
            StringBuffer response = new StringBuffer();

            //결과 값을 받아오는 while
            while((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            //결과 값을 저장한다.
            result = response.toString();
            br.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }

}
