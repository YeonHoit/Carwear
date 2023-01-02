package com.inspier.carstyle;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//Home Fragment
public class HomeFragment extends Fragment {

    //Fragment 객체 생성
    private SelectManufacturerFragment selectManufacturerFragment;
    private UnityFragment unityFragment;

    //View 객체 생성
    private RelativeLayout Select_Car_Button;
    private RelativeLayout Unity_Button;
    private ViewPager2 Home_SlideViewPager;
    private LinearLayout Home_ViewPager_Indicator;

    //사진 String 객체
    private ArrayList<String> images;

    public HomeFragment() {
        //HomeFragment의 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false); //View 제작

        //버튼 객체 연결
        Select_Car_Button = (RelativeLayout) view.findViewById(R.id.Select_Car_Button);
        Unity_Button = (RelativeLayout) view.findViewById(R.id.Unity_Button);
        Home_SlideViewPager = (ViewPager2) view.findViewById(R.id.Home_SlideViewPager);
        Home_ViewPager_Indicator = (LinearLayout) view.findViewById(R.id.Home_ViewPager_Indicator);

        //자동차 선택 setOnClickListener
        Select_Car_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectManufacturerFragment = ((MainActivity)getActivity()).fragmentMaintenance.getSelectManufacturerFragment();
                ((MainActivity)getActivity()).replaceFragment(selectManufacturerFragment);
            }
        });

        //가상 튜닝 하기 setOnClickListener
        Unity_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unityFragment = ((MainActivity)getActivity()).fragmentMaintenance.getUnityFragment();
                ((MainActivity)getActivity()).switch_Unity(unityFragment);
            }
        });

        //이미지 배열 초기화
        images = new ArrayList<>();

        //이미지 세팅
        try {
            DBCommunicationTask task = new DBCommunicationTask();
            task.setContext(((MainActivity)getActivity()));
            String result = task.execute("GetAdviceInfo.php", "GetAdviceInfo").get();

            //JSON 파싱
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("GetAdviceInfo");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                images.add(object.getString("Photo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Slide ImageView 세팅
        Home_SlideViewPager.setOffscreenPageLimit(1);
        Home_SlideViewPager.setAdapter(new ImageSliderAdapter(images));

        Home_SlideViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position % images.size());
            }
        });

        setupIndicators(images.size());

        ((RecyclerView)Home_SlideViewPager.getChildAt(0)).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(((MainActivity)getActivity()));
            indicators[i].setImageDrawable(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            Home_ViewPager_Indicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = Home_ViewPager_Indicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) Home_ViewPager_Indicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.bg_indicator_active));
            }
            else {
                imageView.setImageDrawable(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.bg_indicator_inactive));
            }
        }
    }
}
