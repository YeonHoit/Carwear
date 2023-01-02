package com.inspier.carstyle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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

public class SelectCarTypeFragment extends Fragment {

    private static final String TAG = "SelectCarTypeFragment";

    //제조사 String 객체
    private String manufacturer = null;

    //View 객체
    private FrameLayout SelectCarType_Back_Button;
    private RecyclerView SelectCarType_RecyclerView;

    //Fragment 객체
    private SelectManufacturerFragment selectManufacturerFragment;
    private SelectYearTypeFragment selectYearTypeFragment;

    //Adapter 객체
    private CarTypeRecyclerAdapter adapter;

    public SelectCarTypeFragment() {
        //SelectCarTypeFragment의 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_cartype, container, false);

        //View 객체 초기화
        SelectCarType_Back_Button = (FrameLayout) view.findViewById(R.id.SelectCarType_Back_Button);
        SelectCarType_RecyclerView = (RecyclerView) view.findViewById(R.id.SelectCarType_RecyclerView);

        //RecyclerAdapter 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(((MainActivity)getActivity()));
        SelectCarType_RecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CarTypeRecyclerAdapter();
        SelectCarType_RecyclerView.setAdapter(adapter);

        //Data 세팅
        try {
            DBCommunicationTask task = new DBCommunicationTask();
            task.setContext(((MainActivity)getActivity()));
            String result = task.execute("GetCarType.php", "GetCarType", manufacturer).get();

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("GetCarType");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                adapter.addItem(object.getString("CarType"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.setOnCarTypeClickListener(new CarTypeRecyclerAdapter.OnCarTypeClickListener() {
            @Override
            public void onCarTypeItemClick(View v, int position) {
                selectYearTypeFragment = ((MainActivity)getActivity()).fragmentMaintenance.getSelectYearTypeFragment();
                selectYearTypeFragment.setManufacturer(manufacturer);
                selectYearTypeFragment.setCartype(adapter.CarTypeListData.get(position));
                ((MainActivity)getActivity()).replaceFragment(selectYearTypeFragment);
            }
        });

        adapter.notifyDataSetChanged();

        //뒤로 가기 버튼 OnClickListener
        SelectCarType_Back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectManufacturerFragment = ((MainActivity)getActivity()).fragmentMaintenance.getSelectManufacturerFragment();
                ((MainActivity)getActivity()).replaceFragment(selectManufacturerFragment);
            }
        });

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
