package com.inspier.carstyle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

//Alert Fragment
public class AlertFragment extends Fragment {

    //Alert RecyclerView 객체
    private RecyclerView Alert_RecyclerView;

    private AlertRecyclerAdapter adapter; //알림 Recycler View 어댑터 객체 생성

    //Fragment 객체
    private ContentsFragment contentsFragment;

    public AlertFragment() {
        //AlertFragment의 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false); //View 제작

        //객체 연결
        Alert_RecyclerView = (RecyclerView) view.findViewById(R.id.Alert_RecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(((MainActivity)getActivity()));
        Alert_RecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AlertRecyclerAdapter();
        Alert_RecyclerView.setAdapter(adapter);

        try {
            DBCommunicationTask task = new DBCommunicationTask();
            task.setContext(((MainActivity)getActivity()));
            String result = task.execute("GetAlert.php", "GetAlert", ((MainActivity)getActivity()).userInfo.getNickname()).get();

            //JSON 파싱
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("Alert");

            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                JSONObject object = jsonArray.getJSONObject(i);
                AlertData data = new AlertData();

                data.setAlert_Index_Num(object.getInt("Index_num"));
                data.setAlert_Title(object.getString("Title"));
                data.setAlert_Contents(object.getString("Contents"));
                data.setAlert_Date(object.getString("AlertDate"));
                data.setAlert_Type(object.getString("AlertType"));
                data.setAlert_FromUser(object.getString("FromUser"));
                data.setAlert_ToUser(object.getString("ToUser"));
                data.setAlert_Target_Num(object.getInt("Target_num"));

                adapter.addItem(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //setOnAlertItemClickListener 정의
        adapter.setOnAlertItemClickListener(new AlertRecyclerAdapter.OnAlertItemClickListener() {
            @Override
            public void onAlertItemClick(View v, int position, int target_num) {

                //좋아요 또는 댓글 알림인 경우 실행
                if (target_num != -1) {
                    try {
                        DBCommunicationTask task = new DBCommunicationTask();
                        task.setContext(((MainActivity)getActivity()));
                        String result = task.execute("GetCommunityPostData.php", "GetCommunityPostData", Integer.toString(target_num)).get();

                        //JSON 파싱
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("CommunityPostData");

                        JSONObject object = jsonArray.getJSONObject(0);
                        CommunityPostData data = new CommunityPostData();

                        data.setCommunity_Post_Index_Num(Integer.parseInt(object.getString("Index_num")));
                        data.setCommunity_Post_Board_Name(object.getString("BoardName"));
                        data.setCommunity_Post_NickName(object.getString("NickName"));
                        data.setCommunity_Post_Title(object.getString("Title"));
                        data.setCommunity_Post_Contents(object.getString("Contents"));
                        data.setCommunity_Post_WriteTime(object.getString("WriteDate"));
                        data.setCommunity_Post_Views(object.getString("Views"));
                        data.setCommunity_Post_Photo(object.getString("Photo"));

                        contentsFragment = ((MainActivity)getActivity()).fragmentMaintenance.getContentsFragment();
                        contentsFragment.setCommunityPostData(data, true);

                        ((MainActivity)getActivity()).replaceFragment(contentsFragment);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        adapter.notifyDataSetChanged();

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }
}
