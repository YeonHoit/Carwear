package com.inspier.carstyle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AlertSettingFragment extends Fragment {

    private static final String TAG = "AlertSettingFragment";

    //View 객체 생성
    private FrameLayout AlertSetting_Back_Button;
    private TextView AlertSetting_Comment_TextView;
    private Switch AlertSetting_Comment_Switch;
    private TextView AlertSetting_Like_TextView;
    private Switch AlertSetting_Like_Switch;
    private TextView AlertSetting_Tag_TextView;
    private Switch AlertSetting_Tag_Switch;

    //Fragment 객체 생성
    private MenuFragment menuFragment;

    public AlertSettingFragment() {
        //AlertSettingFragment의 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert_setting, container, false);

        //View 객체 초기화
        AlertSetting_Back_Button = (FrameLayout) view.findViewById(R.id.AlertSetting_Back_Button);
        AlertSetting_Comment_TextView = (TextView) view.findViewById(R.id.AlertSetting_Comment_TextView);
        AlertSetting_Comment_Switch = (Switch) view.findViewById(R.id.AlertSetting_Comment_Switch);
        AlertSetting_Like_TextView = (TextView) view.findViewById(R.id.AlertSetting_Like_TextView);
        AlertSetting_Like_Switch = (Switch) view.findViewById(R.id.AlertSetting_Like_Switch);
        AlertSetting_Tag_TextView = (TextView) view.findViewById(R.id.AlertSetting_Tag_TextView);
        AlertSetting_Tag_Switch = (Switch) view.findViewById(R.id.AlertSetting_Tag_Switch);

        //뒤로 가기 버튼 setOnClickListener
        AlertSetting_Back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFragment = ((MainActivity)getActivity()).fragmentMaintenance.getMenuFragment();
                ((MainActivity)getActivity()).replaceFragment(menuFragment);
            }
        });

        //초기값 Setting
        AlertSetting_Comment_Switch.setChecked(((MainActivity)getActivity()).userInfo.isCommentAlert());
        if (((MainActivity)getActivity()).userInfo.isCommentAlert()) {
            AlertSetting_Comment_TextView.setText("댓글 알림 (켜짐)");
        }
        else {
            AlertSetting_Comment_TextView.setText("댓글 알림 (꺼짐)");
        }
        AlertSetting_Like_Switch.setChecked(((MainActivity)getActivity()).userInfo.isLikeAlert());
        if (((MainActivity)getActivity()).userInfo.isLikeAlert()) {
            AlertSetting_Like_TextView.setText("좋아요 알림 (켜짐)");
        }
        else {
            AlertSetting_Like_TextView.setText("좋아요 알림 (꺼짐)");
        }
        Log.d(TAG, "Tag_Swtich : " + ((MainActivity)getActivity()).userInfo.isTagAlert());
        AlertSetting_Tag_Switch.setChecked(((MainActivity)getActivity()).userInfo.isTagAlert());
        if (((MainActivity)getActivity()).userInfo.isTagAlert()) {
            AlertSetting_Tag_TextView.setText("댓글 태그 알림 (켜짐)");
        }
        else {
            AlertSetting_Tag_TextView.setText("댓글 태그 알림 (꺼짐)");
        }

        //댓글 알림 Switch setOnCheckedChangeListener
        AlertSetting_Comment_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //스위치가 off -> on 으로 변경되었을 경우
               if (isChecked) {
                   AlertSetting_Comment_TextView.setText("댓글 알림 (켜짐)");
                   DBCommunicationTask task = new DBCommunicationTask();
                   task.setContext(((MainActivity)getActivity()));
                   task.execute("UpdateAlertSetting.php", "UpdateAlertSetting", Long.toString(((MainActivity)getActivity()).userInfo.getID()), "CommentAlert", "true");
                   ((MainActivity)getActivity()).userInfo.setCommentAlert(true);
               }
               //스위치가 on -> off 으로 변경되었을 경우
               else {
                   AlertSetting_Comment_TextView.setText("댓글 알림 (꺼짐)");
                   DBCommunicationTask task = new DBCommunicationTask();
                   task.setContext(((MainActivity)getActivity()));
                   task.execute("UpdateAlertSetting.php", "UpdateAlertSetting", Long.toString(((MainActivity)getActivity()).userInfo.getID()), "CommentAlert", "false");
                   ((MainActivity)getActivity()).userInfo.setCommentAlert(false);
               }
            }
        });

        //좋아요 알림 Switch setOnCheckedChangeListener
        AlertSetting_Like_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //스위치가 off -> on 으로 변경되었을 경우
                if (isChecked) {
                    AlertSetting_Like_TextView.setText("좋아요 알림 (켜짐)");
                    DBCommunicationTask task = new DBCommunicationTask();
                    task.setContext(((MainActivity)getActivity()));
                    task.execute("UpdateAlertSetting.php", "UpdateAlertSetting", Long.toString(((MainActivity)getActivity()).userInfo.getID()), "LikeAlert", "true");
                    ((MainActivity)getActivity()).userInfo.setLikeAlert(true);
                }
                //스위치가 on -> off 으로 변경되었을 경우
                else {
                    AlertSetting_Like_TextView.setText("좋아요 알림 (꺼짐)");
                    DBCommunicationTask task = new DBCommunicationTask();
                    task.setContext(((MainActivity)getActivity()));
                    task.execute("UpdateAlertSetting.php", "UpdateAlertSetting", Long.toString(((MainActivity)getActivity()).userInfo.getID()), "LikeAlert", "false");
                    ((MainActivity)getActivity()).userInfo.setLikeAlert(false);
                }
            }
        });

        //댓글 태그 알림 Switch setOnCheckedChangeListener
        AlertSetting_Tag_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //스위치가 off -> on 으로 변경되었을 경우
                if (isChecked) {
                    AlertSetting_Tag_TextView.setText("댓글 태그 알림 (켜짐)");
                    DBCommunicationTask task = new DBCommunicationTask();
                    task.setContext(((MainActivity)getActivity()));
                    task.execute("UpdateAlertSetting.php", "UpdateAlertSetting", Long.toString(((MainActivity)getActivity()).userInfo.getID()), "TagAlert", "true");
                    ((MainActivity)getActivity()).userInfo.setTagAlert(true);
                }
                //스위치가 on -> off 으로 변경되었을 경우
                else {
                    AlertSetting_Tag_TextView.setText("댓글 태그 알림 (꺼짐)");
                    DBCommunicationTask task = new DBCommunicationTask();
                    task.setContext(((MainActivity)getActivity()));
                    task.execute("UpdateAlertSetting.php", "UpdateAlertSetting", Long.toString(((MainActivity)getActivity()).userInfo.getID()), "TagAlert", "false");
                    ((MainActivity)getActivity()).userInfo.setTagAlert(false);
                }
            }
        });

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }
}
