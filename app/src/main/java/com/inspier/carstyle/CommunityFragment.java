package com.inspier.carstyle;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//Community Fragment
public class CommunityFragment extends Fragment {

    private static final String TAG = "CommunityFragment";

    //커뮤니티 상단 바 객체 생성
    private LinearLayout Car_Type_Board_Button;
    private TextView Car_Type_Board_TextView;
    private LinearLayout Free_Board_Button;
    private TextView Free_Board_TextView;
    private LinearLayout Used_Shop_Button;
    private TextView Used_Shop_TextView;

    private CommunityPostRecyclerAdapter adapter; //커뮤니티 글 목록 Recycler View 어댑터 객체 생성

    //Fragment 객체 생성
    private ContentsFragment contentsFragment;
    private PostWriteFragment postWriteFragment;
    private PostReTouchFragment postReTouchFragment;

    private RecyclerView Community_Post_RecyclerView; //커뮤니티 RecyclerView 객체

    private TextView Community_Post_Write_Button; //글쓰기 TextView 객체

    private SwipeRefreshLayout swipeRefreshLayout; //SwipeRefreshLayout 객체

    //커뮤니티 글 내용 List
    List<String> listBoardName;
    List<String> listTitle;
    List<String> listContents;
    List<Integer> listResId;
    List<String> listNickName;
    List<String> listWriteTime;
    List<String> listViews;

    private ArrayList<CommunityPostData> CommunityPostListData = new ArrayList<>();

    //현재 게시판의 상태를 가지고 있는 변수. 0은 선택 차종 게시판, 1은 자유 게시판, 2는 중고장터
    private int BoardStatus = 0;

    String result = null;

    //수정, 삭제, 신고 listener
    CommunityPostRecyclerAdapter.OnDotsClickListener mDotsListener = new CommunityPostRecyclerAdapter.OnDotsClickListener() {
        @Override
        public void onDotsClick(View v, final int position, final CommunityPostData data) {
            AlertDialog.Builder builder = new AlertDialog.Builder(((MainActivity)getActivity()));

            //글쓴이와 사용자의 닉네임이 같은 경우
            if(((MainActivity)getActivity()).userInfo.getNickname().equals(CommunityPostListData.get(position).getCommunity_Post_NickName())) {
                builder.setItems(R.array.retouch_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] items = getResources().getStringArray(R.array.retouch_delete);

                        //수정을 선택했을 때
                        if(which == 0) {
                            postReTouchFragment = ((MainActivity)getActivity()).fragmentMaintenance.getPostReTouchFragment();
                            postReTouchFragment.setCommunityPostData(CommunityPostListData.get(position));
                            ((MainActivity)getActivity()).replaceFragment(postReTouchFragment);
                        }

                        //삭제를 선택했을 때
                        else if (which == 1) {
                            DBCommunicationTask task = new DBCommunicationTask();
                            task.setContext(((MainActivity)getActivity()));
                            task.execute("DeleteCommunityPost.php", "DeleteCommunityPost", Integer.toString(data.getCommunity_Post_Index_Num()));

                            Data_setting();

                            adapter.setOnItemClickListener(new CommunityPostRecyclerAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    contentsFragment = ((MainActivity)getActivity()).fragmentMaintenance.getContentsFragment();

                                    //넘겨줄 Data 세팅

                                    contentsFragment.setCommunityPostData(CommunityPostListData.get(position), true);

                                    ((MainActivity)getActivity()).replaceFragment(contentsFragment);
                                }
                            });

                            adapter.setOnDotsClickListener(mDotsListener);
                        }
                    }
                });
            }
            //글쓴이와 사용자의 닉네임이 다른 경우
            else {
                builder.setItems(R.array.report, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] items = getResources().getStringArray(R.array.report);
                        Toast.makeText(((MainActivity)getActivity()), items[which], Toast.LENGTH_LONG).show();
                    }
                });
            }

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    };

    public CommunityFragment() {
        //CommunityFragment의 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false); //View 제작

        //커뮤니티 상단 바 객체 연결 및 초기화
        Car_Type_Board_Button = (LinearLayout) view.findViewById(R.id.Car_Type_Board_Button);
        Car_Type_Board_TextView = (TextView) view.findViewById(R.id.Car_Type_Board_TextView);
        Free_Board_Button = (LinearLayout) view.findViewById(R.id.Free_Board_Button);
        Free_Board_TextView = (TextView) view.findViewById(R.id.Free_Board_TextView);
        Used_Shop_Button = (LinearLayout) view.findViewById(R.id.Used_Shop_Button);
        Used_Shop_TextView = (TextView) view.findViewById(R.id.Used_Shop_TextView);

        //swipeRefreshLayout 객체 초기화
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.Swipe_Layout);

        //사용자 차량 게시판 Setting
        Car_Type_Board_TextView.setText(((MainActivity)getActivity()).userInfo.getUserCar());

        if(BoardStatus == 0) {
            NavSetting_SelectCar();
        }
        else if(BoardStatus == 1) {
            NavSetting_Free();
        }
        else if(BoardStatus == 2) {
            NavSetting_UsedMarket();
        }

        //선택 차종 게시판 선택했을 시
        Car_Type_Board_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavSetting_SelectCar();

                //현재 상태 저장
                BoardStatus = 0;

                Data_setting();

                adapter.setOnItemClickListener(new CommunityPostRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {

                        contentsFragment = ((MainActivity)getActivity()).fragmentMaintenance.getContentsFragment();

                        //넘겨줄 Data 세팅

                        contentsFragment.setCommunityPostData(CommunityPostListData.get(position), true);

                        ((MainActivity)getActivity()).replaceFragment(contentsFragment);
                    }
                });

                adapter.setOnDotsClickListener(mDotsListener);
            }
        });

        //자유 게시판 선택했을 시
        Free_Board_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavSetting_Free();

                //현재 상태 저장
                BoardStatus = 1;

                Data_setting();

                adapter.setOnItemClickListener(new CommunityPostRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        contentsFragment = ((MainActivity)getActivity()).fragmentMaintenance.getContentsFragment();

                        //넘겨줄 Data 세팅

                        contentsFragment.setCommunityPostData(CommunityPostListData.get(position), true);

                        ((MainActivity)getActivity()).replaceFragment(contentsFragment);
                    }
                });

                adapter.setOnDotsClickListener(mDotsListener);
            }
        });

        //중고 장터 선택했을 시
        Used_Shop_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavSetting_UsedMarket();

                //현재 상태 저장
                BoardStatus = 2;

                Data_setting();

                adapter.setOnItemClickListener(new CommunityPostRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        contentsFragment = ((MainActivity)getActivity()).fragmentMaintenance.getContentsFragment();

                        //넘겨줄 Data 세팅

                        contentsFragment.setCommunityPostData(CommunityPostListData.get(position), true);

                        ((MainActivity)getActivity()).replaceFragment(contentsFragment);
                    }
                });

                adapter.setOnDotsClickListener(mDotsListener);
            }
        });

        //RecyclerView 연결 및 Adapter 세팅
        Community_Post_RecyclerView = view.findViewById(R.id.Community_Post_RecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(((MainActivity)getActivity()));
        Community_Post_RecyclerView.setLayoutManager(linearLayoutManager);

        //Refresh 처리
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Refresh", "Refresh");

                Data_setting();

                adapter.setOnItemClickListener(new CommunityPostRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        contentsFragment = ((MainActivity)getActivity()).fragmentMaintenance.getContentsFragment();

                        //넘겨줄 Data 세팅

                        contentsFragment.setCommunityPostData(CommunityPostListData.get(position), true);

                        ((MainActivity)getActivity()).replaceFragment(contentsFragment);
                    }
                });

                adapter.setOnDotsClickListener(mDotsListener);

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Data_setting();

        //OnClick 처리
        adapter.setOnItemClickListener(new CommunityPostRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                ((MainActivity)getActivity()).customProgressDialog.show();

                contentsFragment = ((MainActivity)getActivity()).fragmentMaintenance.getContentsFragment();
                Log.d(TAG, "contentsFragment create");

                //넘겨줄 Data 세팅

                contentsFragment.setCommunityPostData(CommunityPostListData.get(position), true);
                Log.d(TAG, "CommunityPostDataSetting");

                ((MainActivity)getActivity()).replaceFragment(contentsFragment);
                Log.d(TAG, "replace");
            }
        });

        //수정, 삭제, 신고 setOnClickListener
        adapter.setOnDotsClickListener(mDotsListener);

        //객체 연결 및 초기화
        Community_Post_Write_Button = (TextView) view.findViewById(R.id.Community_Post_Write_Button);

        Community_Post_Write_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postWriteFragment = ((MainActivity)getActivity()).fragmentMaintenance.getPostWriteFragment();

                ((MainActivity)getActivity()).replaceFragment(postWriteFragment);
            }
        });

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }

    //커뮤니티 글 목록 데이터 세팅
    private void Data_setting() {

        CommunityPostListData.clear();

        adapter = new CommunityPostRecyclerAdapter();
        Community_Post_RecyclerView.setAdapter(adapter);

        try {
            if(BoardStatus == 0) { //차종 선택 게시판일때
                DBCommunicationTask task = new DBCommunicationTask();
                task.setContext(((MainActivity)getActivity()));
                Log.d("CommunityFragment", ((MainActivity)getActivity()).userInfo.getUserCar());
                result = task.execute("GetCommunityPost.php", "GetCommunityPost", ((MainActivity)getActivity()).userInfo.getUserCar()).get();
            }
            else if(BoardStatus == 1) { //자유 게시판일때
                DBCommunicationTask task = new DBCommunicationTask();
                task.setContext(((MainActivity)getActivity()));
                result = task.execute("GetCommunityPost.php", "GetCommunityPost", "FreeBoard").get();
            }
            else if(BoardStatus == 2) { //중고 장터일때
                DBCommunicationTask task = new DBCommunicationTask();
                task.setContext(((MainActivity)getActivity()));
                result = task.execute("GetCommunityPost.php", "GetCommunityPost", "UsedMarket").get();
            }

            //JSON 파싱
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("Community");

            for(int i = jsonArray.length() - 1; i >= 0; i--) {
                CommunityPostData data = new CommunityPostData();
                JSONObject object = jsonArray.getJSONObject(i);

                //데이터 파싱
                data.setCommunity_Post_Index_Num(Integer.parseInt(object.getString("Index_num")));
                data.setCommunity_Post_Board_Name(object.getString("BoardName"));
                data.setCommunity_Post_NickName(object.getString("NickName"));
                data.setCommunity_Post_Title(object.getString("Title"));
                data.setCommunity_Post_Contents(object.getString("Contents"));
                data.setCommunity_Post_WriteTime(object.getString("WriteDate"));
                data.setCommunity_Post_Views(object.getString("Views"));
                data.setCommunity_Post_Photo(object.getString("Photo"));

                //List에 추가
                CommunityPostListData.add(data);
                adapter.addItem(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //adapter의 값이 변경되었다는 것을 알려줌
        adapter.notifyDataSetChanged();
    }

    private void NavSetting_SelectCar() {
        //선택 차종 게시판 활성화
        Car_Type_Board_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_selected));
        Car_Type_Board_TextView.setTextColor(Color.parseColor("#262C3A"));

        //자유 게시판 비활성화
        Free_Board_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Free_Board_TextView.setTextColor(Color.parseColor("#666666"));

        //중고 장터 비활성화
        Used_Shop_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Used_Shop_TextView.setTextColor(Color.parseColor("#666666"));
    }

    private void NavSetting_Free() {
        //선택 차종 게시판 비활성화
        Car_Type_Board_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Car_Type_Board_TextView.setTextColor(Color.parseColor("#666666"));

        //자유 게시판 활성화
        Free_Board_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_selected));
        Free_Board_TextView.setTextColor(Color.parseColor("#262C3A"));

        //중고 장터 비활성화
        Used_Shop_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Used_Shop_TextView.setTextColor(Color.parseColor("#666666"));
    }

    private void NavSetting_UsedMarket() {
        //선택 차종 게시판 비활성화
        Car_Type_Board_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Car_Type_Board_TextView.setTextColor(Color.parseColor("#666666"));

        //자유 게시판 비활성화
        Free_Board_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Free_Board_TextView.setTextColor(Color.parseColor("#666666"));

        //중고 장터 활성화
        Used_Shop_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_selected));
        Used_Shop_TextView.setTextColor(Color.parseColor("#262C3A"));
    }
}
