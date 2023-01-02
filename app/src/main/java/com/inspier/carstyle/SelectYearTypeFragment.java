package com.inspier.carstyle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SelectYearTypeFragment extends Fragment {

    private static final String TAG = "SelectYearTypeFragment";

    //제조사, 차종 객체
    private String manufacturer = null;
    private String cartype = null;

    //View 객체
    private FrameLayout SelectYearType_Back_Button;
    private RecyclerView SelectYearType_RecyclerView;

    //Fragment 객체
    private SelectCarTypeFragment selectCarTypeFragment;
    private HomeFragment homeFragment;

    //Adapter 객체
    private YearTypeRecyclerAdapter adapter;

    public SelectYearTypeFragment() {
        //SelectYearTypeFragment의 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_yeartype, container, false);

        //View 객체 초기화
        SelectYearType_Back_Button = (FrameLayout) view.findViewById(R.id.SelectYearType_Back_Button);
        SelectYearType_RecyclerView = (RecyclerView) view.findViewById(R.id.SelectYearType_RecyclerView);

        //RecyclerAdapter 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(((MainActivity)getActivity()));
        SelectYearType_RecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new YearTypeRecyclerAdapter();
        SelectYearType_RecyclerView.setAdapter(adapter);

        //Data 세팅
        try {
            DBCommunicationTask task = new DBCommunicationTask();
            task.setContext(((MainActivity)getActivity()));
            String result = task.execute("GetYearType.php", "GetYearType", cartype).get();

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("GetYearType");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                adapter.addItem(object.getString("YearType"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.setOnYearTypeClickListener(new YearTypeRecyclerAdapter.OnYearTypeClickListener() {
            @Override
            public void onYearTypeItemClick(View v, int position) {
                DBCommunicationTask task = new DBCommunicationTask();
                task.setContext(((MainActivity)getActivity()));
                task.execute("UpdateUserCar.php", "UpdateUserCar", Long.toString(((MainActivity)getActivity()).userInfo.getID()), adapter.YearTypeListData.get(position));

                ((MainActivity)getActivity()).userInfo.setUserCar(adapter.YearTypeListData.get(position));

                Toast.makeText(((MainActivity)getActivity()), "대표 차종이 변경되었습니다.", Toast.LENGTH_SHORT).show();

                homeFragment = ((MainActivity)getActivity()).fragmentMaintenance.getHomeFragment();
                ((MainActivity)getActivity()).replaceFragment(homeFragment);
            }
        });

        adapter.notifyDataSetChanged();

        //뒤로 가기 버튼 OnClickListener
        SelectYearType_Back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCarTypeFragment = ((MainActivity)getActivity()).fragmentMaintenance.getSelectCarTypeFragment();
                selectCarTypeFragment.setManufacturer(manufacturer);
                ((MainActivity)getActivity()).replaceFragment(selectCarTypeFragment);
            }
        });

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }
}
