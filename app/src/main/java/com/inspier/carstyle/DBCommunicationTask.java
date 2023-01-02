package com.inspier.carstyle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//DB와 통신하는 Class
public class DBCommunicationTask extends AsyncTask<String, Void, String> {
    private static String IP_ADDRESS = "pdbx12.cafe24.com";

    private Context context = null;

    private CustomProgressDialog customProgressDialog;

    @Override
    protected void onPreExecute() {
//        customProgressDialog = new CustomProgressDialog(context);
//        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        customProgressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String serverURL = "http://" + IP_ADDRESS + ":80/" + params[0]; //Server에 있는 PHP 파일 URL
        String kind = params[1]; //통신 종류
        String postParameters = "";

        //로그인시
        if (kind.equals("Login")) {
            String Login = params[2]; //로그인 방식
            String post_id = params[3]; //참조 할 id값
            postParameters = "Login=" + Login + "&id=" + post_id + "";
        }
        //게시판 글 가져올때
        else if (kind.equals("GetCommunityPost")) {
            String BoardName = params[2];
            postParameters = "BoardName=" + BoardName;
        }
        //댓글 가져올때 또는 댓글 개수 가지고 올 때
        else if (kind.equals("GetComments") || kind.equals("GetCommentCount") || kind.equals("GetLikeCount") || kind.equals("GetCommentTag")) {
            String CommunityNumber = params[2];
            postParameters = "CommunityNumber=" + CommunityNumber;
        }
        //댓글 작성할때
        else if (kind.equals("InsertComment")) {
            String CommunityNumber = params[2];
            String Comments = params[3];
            String NickName = params[4];
            postParameters = "CommunityNumber=" + CommunityNumber + "&Comments=" + Comments + "&NickName=" + NickName;
        }
        //좋아요 정보 가져올 때
        else if (kind.equals("GetLikeOnOff") || kind.equals("InsertLike") || kind.equals("DeleteLike")) {
            String ID = params[2];
            String CommunityNumber = params[3];
            postParameters = "ID=" + ID + "&CommunityNumber=" + CommunityNumber;
        }
        //글 작성할때
        else if (kind.equals("InsertCommunityPost")) {
            String BoardName = params[2];
            String NickName = params[3];
            String Title = params[4];
            String Contents = params[5];
            String Photo = params[6];
            postParameters = "BoardName=" + BoardName + "&NickName=" + NickName + "&Title=" + Title + "&Contents=" + Contents + "&Photo=" + Photo;
        }
        else if (kind.equals("ViewCountUpdate")) {
            String CommunityNumber = params[2];
            String ViewUpdate = params[3];
            postParameters = "CommunityNumber=" + CommunityNumber + "&ViewUpdate=" + ViewUpdate;
        }
        //커뮤니티 글 수정할때
        else if (kind.equals("UpdateCommunityPost")) {
            String Index_num = params[2];
            String BoardName = params[3];
            String Title = params[4];
            String Contents = params[5];
            String Photo = params[6];
            postParameters = "Index_num=" + Index_num + "&BoardName=" + BoardName + "&Title=" + Title + "&Contents=" + Contents + "&Photo=" + Photo;
        }
        //댓글 삭제할때
        else if (kind.equals("DeleteComment")) {
            String Index_num = params[2];
            postParameters = "Index_num=" + Index_num;
        }
        //커뮤니티 글 삭제할때
        else if (kind.equals("DeleteCommunityPost")) {
            String Index_num = params[2];
            postParameters = "Index_num=" + Index_num;
        }
        //좋아요 또는 댓글 알림을 보낼 때
        else if (kind.equals("Comment_Like_Notification")) {
            String Msg = params[2];
            String AlertType = params[3];
            String FromUser = params[4];
            String ToUser = params[5];
            String Target_num = params[6];
            postParameters = "Msg=" + Msg + "&AlertType=" + AlertType + "&FromUser=" + FromUser + "&ToUser=" + ToUser + "&Target_num=" + Target_num;
        }
        //알림 목록을 가지고 올 때
        else if (kind.equals("GetAlert")) {
            String NickName = params[2];
            postParameters = "NickName=" + NickName;
        }
        //하나의 커뮤니티 글을 가지고 올 때
        else if (kind.equals("GetCommunityPostData")) {
            String Index_num = params[2];
            postParameters = "Index_num=" + Index_num;
        }
        //닉네임 중복 체크 할 때
        else if (kind.equals("NickNameOverlapCheck")) {
            String NickName = params[2];
            postParameters = "NickName=" + NickName;
        }
        //사용자 정보 변경 할 떄
        else if (kind.equals("UpdateUserInfo")) {
            String OldNickName = params[2];
            String NewNickName = params[3];
            String Photo = params[4];
            String Email = params[5];
            String Phone = params[6];
            postParameters = "OldNickName=" + OldNickName + "&NewNickName=" + NewNickName + "&Photo=" + Photo + "&Email=" + Email + "&Phone=" + Phone;
        }
        //알림 설정 변경 할 때
        else if (kind.equals("UpdateAlertSetting")) {
            String ID = params[2];
            String Alert_Kind = params[3];
            String Value = params[4];
            postParameters = "ID=" + ID + "&Alert_Kind=" + Alert_Kind + "&Value=" + Value;
        }
        //유저 정보 삭제 할 때
        else if (kind.equals("DeleteUserInfo")) {
            String ID = params[2];
            String NickName = params[3];
            postParameters = "ID=" + ID + "&NickName=" + NickName;
        }
        //사용자의 자동차 정보를 변경 할 때
        else if (kind.equals("UpdateUserCar")) {
            String ID = params[2];
            String UserCar = params[3];
            postParameters = "ID=" + ID + "&UserCar=" + UserCar;
        }
        //차종 정보를 가지고 올 때
        else if (kind.equals("GetCarType")) {
            String Manufacturer = params[2];
            postParameters = "Manufacturer=" + Manufacturer;
        }
        //연식 정보를 가지고 올 때
        else if (kind.equals("GetYearType")) {
            String CarType = params[2];
            postParameters = "CarType=" + CarType;
        }

        try {
            //HttpConnection Open
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            //HttpConnection Setting
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            //POST 방식으로 보낼 데이터 설정
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            //응답 Code 확인
            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d("Response Code", "" + responseStatusCode);

            //받아올 데이터 설정
            InputStream inputStream;
            //응답 Code가 200(OK)이면 데이터를 받아온다
            if (responseStatusCode == httpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }

            //데이터 가공
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            //httpURLConnection Close
            httpURLConnection.disconnect();

            return sb.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //customProgressDialog.dismiss();
    }

    public void setContext(Context context) {
        this.context = context;
    }
}