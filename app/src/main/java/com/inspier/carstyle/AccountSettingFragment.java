package com.inspier.carstyle;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

public class AccountSettingFragment extends Fragment {

    private static final String TAG = "AccountSettingFragment";

    //View 객체 생성
    private FrameLayout AccountSetting_Back_Button;
    private TextView AccountSetting_Drop_Out;

    //Fragment 객체 생성
    private MenuFragment menuFragment;

    //CustomDialog 객체 생성
    private Dialog dropout_dialog = null;

    public AccountSettingFragment() {
        //AccountSettingFragment 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_setting, container, false);

        //View 객체 초기화
        AccountSetting_Back_Button = (FrameLayout) view.findViewById(R.id.AccountSetting_Back_Button);
        AccountSetting_Drop_Out = (TextView) view.findViewById(R.id.AccountSetting_Drop_Out);

        //뒤로 가기 버튼 setOnClickListener
        AccountSetting_Back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFragment = ((MainActivity)getActivity()).fragmentMaintenance.getMenuFragment();
                ((MainActivity)getActivity()).replaceFragment(menuFragment);
            }
        });

        //회원 탈퇴 버튼 setOnClickListener
        AccountSetting_Drop_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "AccountSetting_Drop_Out");

                //탈퇴 여부를 물어보는 CustomDialog
                dropout_dialog = new Dialog(((MainActivity)getActivity()));
                dropout_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dropout_dialog.setContentView(R.layout.custom_dropout_dialog);

                TextView dropout_cancel = dropout_dialog.findViewById(R.id.dropout_cancel);
                TextView dropout_run = dropout_dialog.findViewById(R.id.dropout_run);

                dropout_dialog.show();

                //취소를 눌렀을 때
                dropout_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dropout_dialog.dismiss();
                    }
                });

                //탈퇴를 눌렀을 때
                dropout_run.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dropout_dialog.dismiss();

                        //DB 유저 정보 삭제
                        DBCommunicationTask task = new DBCommunicationTask();
                        task.setContext(((MainActivity)getActivity()));
                        task.execute("DeleteUserInfo.php", "DeleteUserInfo", Long.toString(((MainActivity)getActivity()).userInfo.getID()), ((MainActivity)getActivity()).userInfo.getNickname());

                        //로그인 방식이 Kakao일때
                        if (((MainActivity)getActivity()).userInfo.getLogin().equals("Kakao")) {
                            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                    Log.d(TAG, "Logout!");
                                    ((MainActivity)getActivity()).finish();
                                }
                            });
                        }
                        //로그인 방식이 Naver일때
                        else if (((MainActivity)getActivity()).userInfo.getLogin().equals("Naver")) {
                            OAuthLogin.getInstance().logout(((MainActivity)getActivity()));
                            ((MainActivity)getActivity()).finish();
                        }
                    }
                });
            }
        });

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }
}
