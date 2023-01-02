package com.inspier.carstyle;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

//Menu Fragment
public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";

    //View 객체 생성
    private RoundImageView Menu_Profile_ImageView;
    private TextView Menu_Profile_NickName;
    private TextView Menu_Profile_UserCar;
    private FrameLayout Menu_MyPage;
    private RelativeLayout Menu_Matching;
    private RelativeLayout Menu_Inquire_One_One;
    private RelativeLayout Menu_Zzim_Shop;
    private RelativeLayout Menu_Alert_Setting;
    private RelativeLayout Menu_Hot_List;
    private RelativeLayout Menu_Account_Setting;
    private RelativeLayout Menu_Info_Layout;
    private TextView Menu_Version_New;
    private RelativeLayout Menu_Logout;

    //Setting 객체 생성
    String Photo = null;
    String NickName = null;
    String UserCar = null;
    String NewVersion = null;
    private Dialog logout_dialog = null;

    //Fragment 객체 생성
    private MyPageFragment myPageFragment;
    private AlertSettingFragment alertSettingFragment;
    private AccountSettingFragment accountSettingFragment;

    public MenuFragment() {
        //MenuFragment의 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false); //View 제작

        //View 객체 초기화
        Menu_Profile_ImageView = (RoundImageView) view.findViewById(R.id.Menu_Profile_ImageView);
        Menu_Profile_NickName = (TextView) view.findViewById(R.id.Menu_Profile_NickName);
        Menu_Profile_UserCar = (TextView) view.findViewById(R.id.Menu_Profile_UserCar);
        Menu_MyPage = (FrameLayout) view.findViewById(R.id.Menu_MyPage);
        Menu_Matching = (RelativeLayout) view.findViewById(R.id.Menu_Matching);
        Menu_Inquire_One_One = (RelativeLayout) view.findViewById(R.id.Menu_Inquire_One_One);
        Menu_Zzim_Shop = (RelativeLayout) view.findViewById(R.id.Menu_Zzim_Shop);
        Menu_Alert_Setting = (RelativeLayout) view.findViewById(R.id.Menu_Alert_Setting);
        Menu_Hot_List = (RelativeLayout) view.findViewById(R.id.Menu_Hot_List);
        Menu_Account_Setting = (RelativeLayout) view.findViewById(R.id.Menu_Account_Setting);
        Menu_Info_Layout = (RelativeLayout) view.findViewById(R.id.Menu_Info_Layout);
        Menu_Version_New = (TextView) view.findViewById(R.id.Menu_Version_New);
        Menu_Logout = (RelativeLayout) view.findViewById(R.id.Menu_Logout);

        //프로필 사진 Setting
        Photo = ((MainActivity)getActivity()).userInfo.getPhoto();

        //프로필 사진이 없다면 기본 사진을 사용
        if (Photo.equals("")) {
            Menu_Profile_ImageView.setImageResource(R.drawable.user);
        }
        //프로필 사진이 설정 되어 있다면 프로필 사진을 불러옴
        else {
            try {
                PhotoGetTask phototask = new PhotoGetTask();
                Menu_Profile_ImageView.setImageBitmap(phototask.execute(Photo).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //프로필 ImageView 둥글게
        Menu_Profile_ImageView.setRectRadius(200f);

        //닉네임 Setting
        NickName = ((MainActivity)getActivity()).userInfo.getNickname();
        Menu_Profile_NickName.setText("안녕하세요 " + NickName + "님!");

        //대표 차종 Setting
        UserCar = ((MainActivity)getActivity()).userInfo.getUserCar();
        Menu_Profile_UserCar.setText("대표 차종 : " + UserCar);

        //MyPage setOnclickListener
        Menu_MyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myPageFragment = ((MainActivity)getActivity()).fragmentMaintenance.getMyPageFragment();
                myPageFragment = new MyPageFragment();
                ((MainActivity)getActivity()).replaceFragment(myPageFragment);
            }
        });

        //버튼 setOnClickListener
        Menu_Matching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(((MainActivity)getActivity()).getApplicationContext(), "준비중 입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Menu_Inquire_One_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Menu_Inquire_One_One");
                Toast.makeText(((MainActivity)getActivity()).getApplicationContext(), "준비중 입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Menu_Zzim_Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Menu_Zzim_Shop");
                Toast.makeText(((MainActivity)getActivity()).getApplicationContext(), "준비중 입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Menu_Alert_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Menu_Alert_Setting");
                alertSettingFragment = ((MainActivity)getActivity()).fragmentMaintenance.getAlertSettingFragment();
                ((MainActivity)getActivity()).replaceFragment(alertSettingFragment);
            }
        });

        Menu_Hot_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Menu_Hot_List");
            }
        });

        Menu_Account_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Menu_Account_Setting");
                accountSettingFragment = ((MainActivity)getActivity()).fragmentMaintenance.getAccountSettingFragment();
                ((MainActivity)getActivity()).replaceFragment(accountSettingFragment);
            }
        });

        //이용 안내 setOnClickListener
        Menu_Info_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Menu_Info_Layout");
            }
        });

        //최신 버전 정보 불러오기
        try {
            DBCommunicationTask versiontask = new DBCommunicationTask();
            versiontask.setContext(((MainActivity)getActivity()));
            NewVersion = versiontask.execute("VersionInfo.php", "VersionInfo").get();
            Menu_Version_New.setText(NewVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //로그아웃 setOnClickListener
        Menu_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그아웃 여부를 물어보는 CustomDialog
                logout_dialog = new Dialog(((MainActivity)getActivity()));
                logout_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                logout_dialog.setContentView(R.layout.custom_logout_dialog);

                TextView logout_cancel = logout_dialog.findViewById(R.id.logout_cancel);
                TextView logout_textview = logout_dialog.findViewById(R.id.logout_run);

                logout_dialog.show();

                logout_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout_dialog.dismiss();
                    }
                });

                logout_textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //로그인 방식이 Kakao일때
                        if (((MainActivity)getActivity()).userInfo.getLogin().equals("Kakao")) {
                            logout_dialog.dismiss();
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
                            logout_dialog.dismiss();
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
