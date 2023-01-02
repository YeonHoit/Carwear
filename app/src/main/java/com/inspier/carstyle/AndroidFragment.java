package com.inspier.carstyle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//Android Fragment
public class AndroidFragment extends Fragment {

    //Fragment 객체 생성
    private HomeFragment homeFragment;
    private CommunityFragment communityFragment;
    private TuningShopFragment tuningShopFragment;
    private AlertFragment alertFragment;
    private MenuFragment menuFragment;

    String mode = null;

    public AndroidFragment() {
        //AndroidFragment의 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_android, container, false); //View 제작

        //((MainActivity)getActivity()).setHomeFragment(); //초기 화면을 HomeFragment로 설정
        //접속 방법에 따라 초기 화면을 HomeFragment 또는 AlertFragment로 설정

        mode = ((MainActivity)getActivity()).mode;

        Log.d("AndroidFragment", mode);

        //NavigationView의 OnSelectedListener 연결
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //Home 화면으로 변경 시
                    case R.id.Home_Button:
                        homeFragment = ((MainActivity)getActivity()).fragmentMaintenance.getHomeFragment();
                        ((MainActivity)getActivity()).replaceFragment(homeFragment);
                        break;

                    //Community 화면으로 변경 시
                    case R.id.Community_Button:
                        communityFragment = ((MainActivity)getActivity()).fragmentMaintenance.getCommunityFragment();
                        ((MainActivity)getActivity()).replaceFragment(communityFragment);
                        break;

                    //TuningShop 화면으로 변경 시
                    case R.id.Tuning_Shop_Button:
                        tuningShopFragment = ((MainActivity)getActivity()).fragmentMaintenance.getTuningShopFragment();
                        ((MainActivity)getActivity()).replaceFragment(tuningShopFragment);
                        break;

                    //Alert 화면으로 변경 시
                    case R.id.Alert_Button:
                        alertFragment = ((MainActivity)getActivity()).fragmentMaintenance.getAlertFragment();
                        ((MainActivity)getActivity()).replaceFragment(alertFragment);
                        break;

                    //Menu 화면으로 변경 시
                    case R.id.Menu_Button:
                        menuFragment = ((MainActivity)getActivity()).fragmentMaintenance.getMenuFragment();
                        ((MainActivity)getActivity()).replaceFragment(menuFragment);
                        break;
                }
                return true;
            }
        });

        if(mode.equals("normal")) {
            //((MainActivity)getActivity()).setHomeFragment();
            bottomNavigationView.setSelectedItemId(R.id.Home_Button);
        }
        if(mode.equals("alert")) {
            //((MainActivity)getActivity()).setAlertFragment();
            bottomNavigationView.setSelectedItemId(R.id.Alert_Button);
        }

        return view;
    }
}
