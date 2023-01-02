package com.inspier.carstyle;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateCalculate {
    //날짜 계산하는 함수
    public String cal(String date) {
        //변수 생성
        long duration = 0;
        long second = 0;
        long min = 0;
        long hour = 0;

        //객체 생성
        Date write = null;
        Date nowdate = null;

        //Date 타입으로 변환하기 위한 DateFormat
        SimpleDateFormat string_to_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        //현재 시간 가져오기
        long now = System.currentTimeMillis();

        try {
            //Date 형식으로 변환
            write = string_to_date.parse(date);
            nowdate = new Date(now);

            //시간을 계산한다.
            duration = nowdate.getTime() - write.getTime();
            second = duration / 1000;
            min = second / 60;
            hour = min / 60;

        } catch (Exception e) {
            e.printStackTrace();
        }

        //시간 차이가 1분 미만일 경우
        if(second < 60) {
            return "방금 전";
        }
        //시간 차이가 1시간 미만일 경우
        else if(min < 60) {
            return min + "분 전";
        }
        //시간 차이가 1일 미만일 경우
        else if(hour < 24) {
            return hour + "시간 전";
        }
        //1일 이상일 경우 날짜로 표시
        else {
            SimpleDateFormat date_to_string = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
            return date_to_string.format(write);
        }
    }
}
