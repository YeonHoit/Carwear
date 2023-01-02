package com.inspier.carstyle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//Contents(커뮤니티 글) Fragment
public class ContentsFragment extends Fragment {

    private CommunityPostData communityPostData; //넘겨 받을 Data 객체

    //Community 글 View 객체 생성
    private TextView Community_Contents_Title;
    private TextView Community_Contents_Contents;
    private TextView Community_Contents_NickName;
    private TextView Community_Contents_WriteTime;
    private TextView Community_Contents_Views;
    private ImageView Community_Contents_ImageView1;
    private ImageView Community_Contents_ImageView2;
    private ImageView Community_Contents_ImageView3;
    private ImageView Community_Contents_ImageView4;
    private FrameLayout Community_Contents_Back;
    private TextView Community_Contents_ViewLike;
    private RelativeLayout Community_Contents_ViewLike_Button;
    private ImageView Community_Contents_ViewLike_ImageView;

    private CommunityFragment communityFragment; //Fragment 객체 생성
    private CommentsFragment commentsFragment;

    private RecyclerView Community_Contents_Comments_RecyclerView; //RecyclerView 객체 생성
    private CommentsRecyclerAdapter adapter; //RecyclerView Adapter 객체 생성

    //Community 댓글 View 객체 생성
    private RelativeLayout Community_Comments_Comments_Button;
    private LinearLayout Community_Contents_ViewLike_Comments;
    private TextView Community_Contents_Comments_Count1;
    private TextView Community_Contents_Comments_Count2;

    private Bitmap like_empty_bitmap;
    private Bitmap like_fill_bitmap;

    private String CommentCount;

    private boolean viewupdate;

    private CustomProgressDialog customProgressDialog = null;

    public ContentsFragment() {
        //ContentsFragment의 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contents, container, false); //View 제작

        Drawable like_empty = getResources().getDrawable(R.drawable.like_empty);
        Drawable like_fill = getResources().getDrawable(R.drawable.like_fill);

        like_empty_bitmap = ((BitmapDrawable)like_empty).getBitmap();
        like_fill_bitmap = ((BitmapDrawable)like_fill).getBitmap();

        //View 객체 연동
        Community_Contents_Title = (TextView) view.findViewById(R.id.Community_Contents_Title);
        Community_Contents_Contents = (TextView) view.findViewById(R.id.Community_Contents_Contents);
        Community_Contents_NickName = (TextView) view.findViewById(R.id.Community_Contents_NickName);
        Community_Contents_WriteTime = (TextView) view.findViewById(R.id.Community_Contents_WriteTime);
        Community_Contents_Views = (TextView) view.findViewById(R.id.Community_Contents_Views);
        Community_Contents_ImageView1 = (ImageView) view.findViewById(R.id.Community_Contents_ImageView1);
        Community_Contents_ImageView2 = (ImageView) view.findViewById(R.id.Community_Contents_ImageView2);
        Community_Contents_ImageView3 = (ImageView) view.findViewById(R.id.Community_Contents_ImageView3);
        Community_Contents_ImageView4 = (ImageView) view.findViewById(R.id.Community_Contents_ImageView4);
        Community_Contents_Back = (FrameLayout) view.findViewById(R.id.Community_Contents_Back);
        Community_Contents_ViewLike = (TextView) view.findViewById(R.id.Community_Contents_ViewLike);
        Community_Contents_ViewLike_Button = (RelativeLayout) view.findViewById(R.id.Community_Contents_ViewLike_Button);
        Community_Contents_ViewLike_ImageView = (ImageView) view.findViewById(R.id.Community_Contents_ViewLike_ImageView);

        Community_Comments_Comments_Button = (RelativeLayout) view.findViewById(R.id.Community_Comments_Comments_Button);
        Community_Contents_ViewLike_Comments = (LinearLayout) view.findViewById(R.id.Community_Contents_ViewLike_Comments);
        Community_Contents_Comments_Count1 = (TextView) view.findViewById(R.id.Community_Contents_Comments_Count1);
        Community_Contents_Comments_Count2 = (TextView) view.findViewById(R.id.Community_Contents_Comments_Count2);

        //넘겨받은 Data를 View에 세팅
        Community_Contents_Title.setText(communityPostData.getCommunity_Post_Title());
        Community_Contents_Contents.setText(communityPostData.getCommunity_Post_Contents());
        Community_Contents_NickName.setText(communityPostData.getCommunity_Post_NickName());
        Community_Contents_WriteTime.setText(new DateCalculate().cal(communityPostData.getCommunity_Post_WriteTime()));
        //Community_Contents_Views.setText("조회수: " + communityPostData.getCommunity_Post_Views());

        //사진 존재에 따른 View Visibility 조정
        if(communityPostData.getCommunity_Post_Photo().equals("")) {
            Community_Contents_ImageView1.setVisibility(View.GONE);
            Community_Contents_ImageView2.setVisibility(View.GONE);
            Community_Contents_ImageView3.setVisibility(View.GONE);
            Community_Contents_ImageView4.setVisibility(View.GONE);
        }
        else {
            String [] array = communityPostData.getCommunity_Post_Photo().split("#");
            if(array.length == 1) {
                Community_Contents_ImageView1.setVisibility(View.VISIBLE);
                try {
                    PhotoGetTask task1 = new PhotoGetTask();
                    Community_Contents_ImageView1.setImageBitmap(task1.execute(array[0]).get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Community_Contents_ImageView2.setVisibility(View.GONE);
                Community_Contents_ImageView3.setVisibility(View.GONE);
                Community_Contents_ImageView4.setVisibility(View.GONE);
            }
            else if(array.length == 2) {
                Community_Contents_ImageView1.setVisibility(View.VISIBLE);
                Community_Contents_ImageView2.setVisibility(View.VISIBLE);
                try {
                    PhotoGetTask task1 = new PhotoGetTask();
                    Community_Contents_ImageView1.setImageBitmap(task1.execute(array[0]).get());
                    PhotoGetTask task2 = new PhotoGetTask();
                    Community_Contents_ImageView2.setImageBitmap(task2.execute(array[1]).get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Community_Contents_ImageView3.setVisibility(View.GONE);
                Community_Contents_ImageView4.setVisibility(View.GONE);
            }
            else if(array.length == 3) {
                Community_Contents_ImageView1.setVisibility(View.VISIBLE);
                Community_Contents_ImageView2.setVisibility(View.VISIBLE);
                Community_Contents_ImageView3.setVisibility(View.VISIBLE);
                try {
                    PhotoGetTask task1 = new PhotoGetTask();
                    Community_Contents_ImageView1.setImageBitmap(task1.execute(array[0]).get());
                    PhotoGetTask task2 = new PhotoGetTask();
                    Community_Contents_ImageView2.setImageBitmap(task2.execute(array[1]).get());
                    PhotoGetTask task3 = new PhotoGetTask();
                    Community_Contents_ImageView3.setImageBitmap(task3.execute(array[2]).get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Community_Contents_ImageView4.setVisibility(View.GONE);
            }
            else if(array.length == 4) {
                Community_Contents_ImageView1.setVisibility(View.VISIBLE);
                Community_Contents_ImageView2.setVisibility(View.VISIBLE);
                Community_Contents_ImageView3.setVisibility(View.VISIBLE);
                Community_Contents_ImageView4.setVisibility(View.VISIBLE);
                try {
                    PhotoGetTask task1 = new PhotoGetTask();
                    Community_Contents_ImageView1.setImageBitmap(task1.execute(array[0]).get());
                    PhotoGetTask task2 = new PhotoGetTask();
                    Community_Contents_ImageView2.setImageBitmap(task2.execute(array[1]).get());
                    PhotoGetTask task3 = new PhotoGetTask();
                    Community_Contents_ImageView3.setImageBitmap(task3.execute(array[2]).get());
                    PhotoGetTask task4 = new PhotoGetTask();
                    Community_Contents_ImageView4.setImageBitmap(task4.execute(array[3]).get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //RecyclerView 세팅
        Community_Contents_Comments_RecyclerView = (RecyclerView) view.findViewById(R.id.Community_Contents_Comments_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(((MainActivity)getActivity()));
        Community_Contents_Comments_RecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CommentsRecyclerAdapter();
        Community_Contents_Comments_RecyclerView.setAdapter(adapter);

        Comment_Count_Setting();
        ViewCountUpdate();
        Like_Count_Setting();
        Like_OnOff_Setting();
        Data_setting();

        //뒤로 가기 버튼 OnClickListener
        Community_Contents_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communityFragment = ((MainActivity)getActivity()).fragmentMaintenance.getCommunityFragment();
                ((MainActivity)getActivity()).replaceFragment(communityFragment);
            }
        });

        //좋아요 버튼 OnClickListener
        Community_Contents_ViewLike_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap imgview = ((BitmapDrawable)Community_Contents_ViewLike_ImageView.getDrawable()).getBitmap();
                if(imgview.equals(like_empty_bitmap)) {
                    DBCommunicationTask task = new DBCommunicationTask();
                    task.setContext(((MainActivity)getActivity()));
                    task.execute("InsertLike.php", "InsertLike", "" + ((MainActivity)getActivity()).userInfo.getID(), "" + communityPostData.getCommunity_Post_Index_Num());

                    Like_Count_Setting();
                    Like_OnOff_Setting();

                    //다른 사람의 게시글에 좋아요를 눌렀을 경우 알림을 보낸다.
                    if (!((MainActivity)getActivity()).userInfo.getNickname().equals(communityPostData.getCommunity_Post_NickName())) {
                        Log.d("ContentsFragment", "Alert!");
                        DBCommunicationTask task_like = new DBCommunicationTask();
                        task_like.setContext(((MainActivity)getActivity()));
                        task_like.execute("Comment_Like_Notification.php",
                                "Comment_Like_Notification",
                                "님이 회원님의 게시글을 좋아합니다",
                                "Like",
                                ((MainActivity) getActivity()).userInfo.getNickname(),
                                communityPostData.getCommunity_Post_NickName(),
                                Integer.toString(communityPostData.getCommunity_Post_Index_Num()));
                    }
                }
                else if(imgview.equals(like_fill_bitmap)) {
                    DBCommunicationTask task = new DBCommunicationTask();
                    task.setContext(((MainActivity)getActivity()));
                    task.execute("DeleteLike.php", "DeleteLike", "" + ((MainActivity)getActivity()).userInfo.getID(), "" + communityPostData.getCommunity_Post_Index_Num());

                    Like_Count_Setting();
                    Like_OnOff_Setting();
                }
            }
        });

        //댓글 버튼을 눌렀을 때 댓글 Fragment로 이동
        Community_Comments_Comments_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsFragment = ((MainActivity)getActivity()).fragmentMaintenance.getCommentsFragment();

                commentsFragment.setCommunityPostData(communityPostData);
                ((MainActivity)getActivity()).replaceFragment(commentsFragment);
            }
        });

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }

    //넘겨받은 Data 세팅하는 함수
    public void setCommunityPostData(CommunityPostData communityPostData, boolean viewupdate) {
        this.communityPostData = communityPostData;
        this.viewupdate = viewupdate;
    }

    //댓글 데이터 세팅 함수
    private void Data_setting() {

        try {
            DBCommunicationTask task = new DBCommunicationTask();
            task.setContext(((MainActivity)getActivity()));
            Log.d("ContentsFragment", "" + communityPostData.getCommunity_Post_Index_Num());
            String result = task.execute("GetComments.php", "GetComments", "" + communityPostData.getCommunity_Post_Index_Num()).get();

            //JSON 파싱
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("Comments");

            //댓글 데이터가 있을 때
            if(jsonArray.length() != 0) {
                for(int i = 0; i < jsonArray.length(); i++) {
                    CommentsData data = new CommentsData();
                    JSONObject object = jsonArray.getJSONObject(i);

                    data.setCommunity_Comments_Index_Num(object.getInt("Index_num"));
                    data.setCommunity_Comments_NickName(object.getString("NickName"));
                    data.setCommunity_Comments_DateTime(object.getString("CommentsDateTime"));
                    data.setCommunity_Comments_Comment(object.getString("Comments"));

                    adapter.addItem(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

    //댓글 개수 세팅 함수
    private void Comment_Count_Setting() {

        try {
            DBCommunicationTask task = new DBCommunicationTask();
            task.setContext(((MainActivity)getActivity()));
            String result = task.execute("GetCommentCount.php", "GetCommentCount", "" + communityPostData.getCommunity_Post_Index_Num()).get();

            //JSON 파싱
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("Comments_Count");

            JSONObject object = jsonArray.getJSONObject(0);
            CommentCount = "" + object.getInt("Count(*)");

            Community_Contents_Comments_Count1.setText("댓글 " + CommentCount);
            Community_Contents_Comments_Count2.setText("" + CommentCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //좋아요 개수 세팅 함수
    private void Like_Count_Setting() {

        try {
            DBCommunicationTask task = new DBCommunicationTask();
            task.setContext(((MainActivity)getActivity()));
            String result = task.execute("GetLikeCount.php", "GetLikeCount", "" + communityPostData.getCommunity_Post_Index_Num()).get();

            //JSON 파싱
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("Like_Count");

            JSONObject object = jsonArray.getJSONObject(0);

            Community_Contents_ViewLike.setText("" + object.getInt("Count(*)"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //사용자가 좋아요를 눌렀는가에 대한 세팅 함수
    private void Like_OnOff_Setting() {

        try {
            DBCommunicationTask task = new DBCommunicationTask();
            task.setContext(((MainActivity)getActivity()));
            String result = task.execute("GetLikeOnOff.php", "GetLikeOnOff", "" + ((MainActivity)getActivity()).userInfo.getID(), "" + communityPostData.getCommunity_Post_Index_Num()).get();

            //JSON 파싱
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("Like_OnOff");

            if(jsonArray.length() == 0) {
                Community_Contents_ViewLike_ImageView.setImageBitmap(like_empty_bitmap);
            }
            else if (jsonArray.length() != 0) {
                Community_Contents_ViewLike_ImageView.setImageBitmap(like_fill_bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //조회수 업데이트 하는 함수
    private void ViewCountUpdate() {

        try {
            DBCommunicationTask task = new DBCommunicationTask();
            task.setContext(((MainActivity)getActivity()));
            String result = "";
            if(viewupdate) {
                result = task.execute("ViewCountUpdate.php", "ViewCountUpdate", "" + communityPostData.getCommunity_Post_Index_Num(), "true").get();
            }
            else {
                result = task.execute("ViewCountUpdate.php", "ViewCountUpdate", "" + communityPostData.getCommunity_Post_Index_Num(), "false").get();
            }

            //JSON 파싱
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("ViewCount");

            JSONObject object = jsonArray.getJSONObject(0);

            Community_Contents_Views.setText("조회수: " + object.getInt("Views"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCustomProgressDialog(CustomProgressDialog customProgressDialog) {
        this.customProgressDialog = customProgressDialog;
    }
}
