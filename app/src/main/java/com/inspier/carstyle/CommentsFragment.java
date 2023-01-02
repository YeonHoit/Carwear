package com.inspier.carstyle;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentsFragment extends Fragment {

    //View 객체 선언
    private FrameLayout Comments_Back;
    private RecyclerView Comments_RecyclerView;
    private MultiAutoCompleteTextView Comments_EditText;
    private FrameLayout Comments_Send_Button;
    private SlidingUpPanelLayout Comments_Main_Panel;
    private TextView Comments_EditText_OverView;

    //Fragment 객체 선언
    private ContentsFragment contentsFragment;

    private CommunityPostData communityPostData; //현재 댓글을 남기려는 커뮤니티 글 데이터 객체

    private CommentsRecyclerAdapter adapter; //RecyclerView Adapter 객체 생성

    //검색 데이터 객체 생성
    private ArrayList<String> items = null;
    private ArrayList<String> items_empty = new ArrayList<>();
    private CharSequence preText;
    private int emptyCount;
    private char lastchar;
    private int preTextLength;

    //OnClickListener 구현
    CommentsRecyclerAdapter.OnCommentsDotsClickListener mOnCommentsDotsClickListener = new CommentsRecyclerAdapter.OnCommentsDotsClickListener() {
        @Override
        public void onCommentsDotsClick(View v, int position, final CommentsData data) {
            AlertDialog.Builder builder = new AlertDialog.Builder(((MainActivity)getActivity()));

            //댓글 글쓴이가 사용자의 닉네임과 같은 경우
            if (((MainActivity)getActivity()).userInfo.getNickname().equals(data.getCommunity_Comments_NickName())) {
                builder.setItems(R.array.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //삭제를 눌렀을 때
                        if (which == 0) {
                            DBCommunicationTask task = new DBCommunicationTask();
                            task.setContext(((MainActivity)getActivity()));
                            task.execute("DeleteComment.php", "DeleteComment", "" + data.getCommunity_Comments_Index_Num());

                            adapter = new CommentsRecyclerAdapter();
                            Comments_RecyclerView.setAdapter(adapter);

                            Data_setting();

                            adapter.setOnCommentsDotsClickListener(mOnCommentsDotsClickListener);
                        }
                    }
                });
            }
            //댓글 글쓴이가 사용자의 닉네임과 다른 경우
            else {
                builder.setItems(R.array.report, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //신고를 눌렀을 때
                    }
                });
            }

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    };

    //CommentsFragment 생성자
    public CommentsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false); //View 제작

        //View 객체 연결
        Comments_Back = (FrameLayout) view.findViewById(R.id.Comments_Back);
        Comments_RecyclerView = (RecyclerView) view.findViewById(R.id.Comments_RecyclerView);
        Comments_EditText = (MultiAutoCompleteTextView) view.findViewById(R.id.Comments_EditText);
        Comments_Send_Button = (FrameLayout) view.findViewById(R.id.Comments_Send_Button);
        Comments_Main_Panel = (SlidingUpPanelLayout) view.findViewById(R.id.Comments_Main_Panel);
        Comments_EditText_OverView = (TextView) view.findViewById(R.id.Comments_EditText_OverView);

        //RecyclerView Adapter 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(((MainActivity)getActivity()));
        Comments_RecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CommentsRecyclerAdapter();
        Comments_RecyclerView.setAdapter(adapter);

        //기타 세팅
        Data_setting();
        adapter.setOnCommentsDotsClickListener(mOnCommentsDotsClickListener);
        Comments_EditText_OverView.setTextColor(Comments_EditText.getCurrentHintTextColor());
        Comments_Main_Panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        Comments_EditText.setText("");
        emptyCount = 0;

        //태그 가능 닉네임 세팅
        items = new ArrayList<>();
        try {
            DBCommunicationTask task = new DBCommunicationTask();
            task.setContext(((MainActivity)getActivity()));
            String result = task.execute("GetCommentTag.php", "GetCommentTag", "" + communityPostData.getCommunity_Post_Index_Num()).get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("GetCommentTag");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                boolean check = true;
                //현재 사용자 닉네임일 때 또는 이미 리스트에 존재하는 닉네임일 때
                if (!object.getString("NickName").equals(((MainActivity)getActivity()).userInfo.getNickname())) {
                    for (int j = 0; j < items.size(); j++) {
                        if (object.getString("NickName").equals(items.get(j))) {
                            check = false;
                            break;
                        }
                    }
                    if (check) {
                        items.add(object.getString("NickName"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Comments_EditText.setTokenizer(new AlphaTokenizer());

        //뒤로 가기 버튼 OnClickListener
        Comments_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentsFragment = ((MainActivity)getActivity()).fragmentMaintenance.getContentsFragment();
                contentsFragment.setCommunityPostData(communityPostData, false);
                ((MainActivity)getActivity()).replaceFragment(contentsFragment);
            }
        });

        //댓글 입력 모형 TextView OnClickListener
        Comments_EditText_OverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CommentsFragment", "Comments_EditText_OverView");
                Comments_EditText_OverView.setVisibility(View.GONE);
                Comments_EditText.performClick();
            }
        });

        //댓글 입력 EditText OnClickListener
        Comments_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CommentsFragment", "Comments_EditText");
                Comments_Main_Panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        //댓글 입력 TextWatcher
        Comments_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                preText = s;
                if (s.length() > 0) {
                    lastchar = s.charAt(s.length() - 1);
                }
                preTextLength = preText.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s.length();
                //아무것도 없을 때
                if (len == 0) {
                    Comments_EditText.setAdapter(new ArrayAdapter<String>(((MainActivity)getActivity()), android.R.layout.simple_dropdown_item_1line, items_empty));
                }
                //글이 짧아 졌을 때
                else if (len < preTextLength) {
                    if (lastchar == ' ') {
                        emptyCount--;
                    }
                    if (emptyCount == 0) {
                        Comments_EditText.setAdapter(new ArrayAdapter<String>(((MainActivity)getActivity()), android.R.layout.simple_dropdown_item_1line, items_empty));
                    }
                }
                //골뱅이가 입력 되었을 때
                else if (s.charAt(len - 1) == '@') {
                    Comments_EditText.setAdapter(new ArrayAdapter<String>(((MainActivity)getActivity()), android.R.layout.simple_dropdown_item_1line, items));
                }
                //공백이 입력 되었을 때
                else if (s.charAt(len - 1) == ' ') {
                    Comments_EditText.setAdapter(new ArrayAdapter<String>(((MainActivity)getActivity()), android.R.layout.simple_dropdown_item_1line, items));
                    emptyCount++;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //댓글 등록 OnClickListener
        Comments_Send_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Comments_EditText.getText().toString().length() != 0) {
                    String CommunityNumber = "" + communityPostData.getCommunity_Post_Index_Num();
                    String Comments = Comments_EditText.getText().toString();
                    String NickName = ((MainActivity)getActivity()).userInfo.getNickname();

                    DBCommunicationTask task = new DBCommunicationTask();
                    task.setContext(((MainActivity)getActivity()));
                    task.execute("InsertComment.php", "InsertComment", CommunityNumber, Comments, NickName);

                    Comments_EditText.getText().clear();

                    InputMethodManager manager = (InputMethodManager) ((MainActivity)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(((MainActivity)getActivity()).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    Comments_Main_Panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

                    //본인 커뮤니티 글에 댓글을 남긴 것이 아닐 때 글쓴이에게 알림이 간다.
                    if (!((MainActivity)getActivity()).userInfo.getNickname().equals(communityPostData.getCommunity_Post_NickName())) {
                        Log.d("CommentsFragment", "Alert!");
                        DBCommunicationTask task_comment = new DBCommunicationTask();
                        task_comment.setContext(((MainActivity)getActivity()));
                        task_comment.execute("Comment_Like_Notification.php",
                                "Comment_Like_Notification",
                                "님이 회원님의 게시글에 댓글을 남겼습니다",
                                "Comment",
                                ((MainActivity)getActivity()).userInfo.getNickname(),
                                communityPostData.getCommunity_Post_NickName(),
                                Integer.toString(communityPostData.getCommunity_Post_Index_Num()));
                    }

                    //공백으로 댓글 나누기
                    String[] split_comment = Comments.split("\\s+");

                    for (int i = 0; i < split_comment.length; i++) {
                        //@가 있으면 태그 알림 진행
                        if (split_comment[i].contains("@")) {
                            String tag_nickname = split_comment[i].substring(split_comment[i].indexOf("@") + 1);
                            //만약 태그 대상이 커뮤티티 글 작성자일 때는 태그 x
                            if (!tag_nickname.equals(communityPostData.getCommunity_Post_NickName())) {
                                DBCommunicationTask task_tag = new DBCommunicationTask();
                                task_tag.setContext(((MainActivity)getActivity()));
                                task_tag.execute("Comment_Like_Notification.php",
                                        "Comment_Like_Notification",
                                        "님이 당신을 태그 헀습니다",
                                        "Tag",
                                        ((MainActivity)getActivity()).userInfo.getNickname(),
                                        tag_nickname,
                                        Integer.toString(communityPostData.getCommunity_Post_Index_Num()));
                            }
                        }
                    }

                    contentsFragment = ((MainActivity)getActivity()).fragmentMaintenance.getContentsFragment();
                    contentsFragment.setCommunityPostData(communityPostData, false);
                    ((MainActivity)getActivity()).replaceFragment(contentsFragment);
                }
            }
        });

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }

    //넘겨받은 Data 세팅하는 함수
    public void setCommunityPostData(CommunityPostData communityPostData) {
        this.communityPostData = communityPostData;
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
}
