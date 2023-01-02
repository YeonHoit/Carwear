package com.inspier.carstyle;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {

    private static String NAVER_CLIENT_ID = "8QZ5rHTUAuNUbYoLiDBz"; //네이버 아이디로 로그인 API 접근 clientID
    private static String NAVER_CLIENT_SECRET = "gE7yb3pXBZ"; //네이버 아이디로 로그인 API 접근 clientSECRET
    private static String NAVER_CLIENT_NAME = "CarStyle"; //네이버 아이디로 로그인 API 접근 APP NAME

    public static OAuthLoginButton Naver_Login_Button; //네이버 로그인 버튼 객체
    public static OAuthLogin NaverLoginInstance; //네이버 로그인 객체
    public static ImageView Naver_custom; //네이버 커스텀 버튼

    public static Context mContext; //로그인에 필요한 Context

    public SessionCallback sessionCallback; //카카오 로그인 SessionCallback 객체
    public static LoginButton Kakao_Login_Button; //카카오 로그인 버튼 객체
    public static ImageView Kakao_custom; //카카오 커스텀 버튼

    public UserInfo userInfo = new UserInfo(); //사용자의 정보를 저장하는 객체

    String mode = "normal"; //알림창을 클릭하여 들어왔는지에 대한 유무 체크를 하는 객체

    String result_NaverDB = null;
    String result_KakaoDB = null;

    private Dialog network_dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.getString("Mode") != null && !extras.getString("Mode").equalsIgnoreCase("")) {
                mode = extras.getString("Mode");
            }
        }

        //Context 주입
        mContext = LoginActivity.this;

        //네이버 로그인 구현
        //1. 초기화
        NaverLoginInstance = OAuthLogin.getInstance();
        NaverLoginInstance.showDevelopersLog(true);
        NaverLoginInstance.init(mContext, NAVER_CLIENT_ID, NAVER_CLIENT_SECRET, NAVER_CLIENT_NAME);

        //2. 로그인 버튼 세팅
        Naver_Login_Button = (OAuthLoginButton) findViewById(R.id.Naver_Login_Button);
        Naver_Login_Button.setOAuthLoginHandler(NaverLoginHandler);
        Naver_custom = (ImageView) findViewById(R.id.Naver_custom);

        Naver_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Naver_Login_Button.performClick();
            }
        });

        //카카오 로그인 구현
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

        Kakao_Login_Button = (LoginButton) findViewById(R.id.Kakao_Login_Button);
        Kakao_custom = (ImageView) findViewById(R.id.Kakao_custom);

        Kakao_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if (status == NetworkStatus.TYPE_NOT_CONNECTED) {
                    Log.d("LoginActivity", "Network not conneted");
                    network_dialog = new Dialog(LoginActivity.this);
                    network_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    network_dialog.setContentView(R.layout.custom_network_dialog);

                    TextView network_ok = (TextView) network_dialog.findViewById(R.id.network_ok);

                    network_dialog.show();

                    network_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            network_dialog.dismiss();
                        }
                    });
                }
                else {
                    Kakao_Login_Button.performClick();
                }
            }
        });
    }

    //네이버 아이디로 로그인 API 전용 Handler
    private OAuthLoginHandler NaverLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            //로그인 인증 성공했을 시
            if(success) {
                //인증 정보 가져오기
                String accessToken = NaverLoginInstance.getAccessToken(mContext);
                String refreshToken = NaverLoginInstance.getRefreshToken(mContext);
                long expiresAt = NaverLoginInstance.getExpiresAt(mContext);
                String tokenType = NaverLoginInstance.getTokenType(mContext);

                //사용자 정보 JSON 파일 저장할 변수
                String result, id = "";

                //사용자 정보 가져오기
                NaverApiTask naverApiTask = new NaverApiTask(accessToken);
                try {
                    result = naverApiTask.execute().get();

                    //결과 값을 JSONObject로 변환 후 값을 가져오기
                    JSONObject object = new JSONObject(result);

                    //JSON 형식으로 데이터를 가지고 오는데에 성공 했을 시
                    if(object.getString("resultcode").equals("00")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        id = jsonObject.getString("id");
                        Log.v("ID", id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                DBCommunicationTask NaverDBTask = new DBCommunicationTask();
                try {
                    //DB 연결을 통해 사용자의 정보를 저장, 불러오기 진행
                    String result_NaverDB = NaverDBTask.execute("UserInfo.php", "Login", "Naver", id).get();
                    Log.d("NaverMessage", result_NaverDB);

                    //결과 값을 JSONObject로 변환 후 값을 가져오기
                    JSONObject object = new JSONObject(result_NaverDB);

                    //JSON 형식으로 데이터를 가지고 오는데에 성공 했을 시
                    JSONObject jsonObject = new JSONObject(object.getString("UserInfo"));

                    //정보 저장
                    userInfo.setIndex_ID(Integer.parseInt(jsonObject.getString("Index_ID")));
                    userInfo.setID(Long.parseLong(jsonObject.getString("ID")));
                    userInfo.setLogin(jsonObject.getString("Login"));
                    userInfo.setNickname(jsonObject.getString("Nickname"));
                    userInfo.setUserCar(jsonObject.getString("UserCar"));
                    Log.d("LoginActivity", "UserCar: " + userInfo.getUserCar());
                    userInfo.setPhoto(jsonObject.getString("Photo"));
                    userInfo.setEmail(jsonObject.getString("Email"));
                    userInfo.setPhone(jsonObject.getString("Phone"));
                    if (jsonObject.getInt("NoticeAlert") == 1) {
                        userInfo.setNoticeAlert(true);
                    }
                    else {
                        userInfo.setNoticeAlert(false);
                    }
                    if (jsonObject.getInt("CommentAlert") == 1) {
                        userInfo.setCommentAlert(true);
                    }
                    else {
                        userInfo.setCommentAlert(false);
                    }
                    if (jsonObject.getInt("LikeAlert") == 1) {
                        userInfo.setLikeAlert(true);
                    }
                    else {
                        userInfo.setLikeAlert(false);
                    }
                    if (jsonObject.getInt("TagAlert") == 1) {
                        userInfo.setTagAlert(true);
                    }
                    else {
                        userInfo.setTagAlert(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("UserInfo", userInfo);
                intent.putExtra("Mode", mode);
                startActivity(intent);
            }
            //로그인 인증 실패했을 시
            else {
                String errorCode = NaverLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = NaverLoginInstance.getLastErrorDesc(mContext);
                Log.e("ErrorCode: ", errorCode);
                Log.e("ErrorDesc: ", errorDesc);
            }
        }
    };

    //로그인 결과를 전달 받기 위한 Callback 클래스
    public class SessionCallback implements ISessionCallback {

        //로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        //로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback:", "onSessionOpenFailed: " + exception.getMessage());
            network_dialog = new Dialog(LoginActivity.this);
            network_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            network_dialog.setContentView(R.layout.custom_network_dialog);

            TextView network_ok = (TextView) network_dialog.findViewById(R.id.network_ok);

            network_dialog.show();

            network_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    network_dialog.dismiss();
                }
            });
        }

        //사용자 정보 요청
        public void requestMe() {

            //사용자 정보 요청 결과에 대한 Callback
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("SessionCallback:", "onSessionClosed: " + errorResult.getErrorMessage());
                    network_dialog = new Dialog(LoginActivity.this);
                    network_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    network_dialog.setContentView(R.layout.custom_network_dialog);

                    TextView network_ok = (TextView) network_dialog.findViewById(R.id.network_ok);

                    network_dialog.show();

                    network_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            network_dialog.dismiss();
                        }
                    });
                }

                @Override
                public void onSuccess(MeV2Response result) {

                    //ProgressDialog 실행
                    CustomProgressDialog customProgressDialog = new CustomProgressDialog(LoginActivity.this);
                    customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    customProgressDialog.show();

                    long id = result.getId();
                    Log.d("ID : ", "" + id);

                    DBCommunicationTask KakaoDBTask = new DBCommunicationTask();
                    KakaoDBTask.setContext(LoginActivity.this);
                    try {
                        //DB 연결을 통해 사용자의 정보를 저장, 불러오기 진행
                        String result_KakaoDB = KakaoDBTask.execute("UserInfo.php", "Login", "Kakao", String.valueOf(id)).get();
                        Log.d("KakaoMessage", result_KakaoDB);

                        //결과 값을 JSONObject로 변환 후 값을 가져오기
                        JSONObject object = new JSONObject(result_KakaoDB);

                        //JSON 형식으로 데이터를 가지고 오는데에 성공 했을 시
                        JSONObject jsonObject = new JSONObject(object.getString("UserInfo"));

                        //정보 저장
                        userInfo.setIndex_ID(Integer.parseInt(jsonObject.getString("Index_ID")));
                        userInfo.setID(Long.parseLong(jsonObject.getString("ID")));
                        userInfo.setLogin(jsonObject.getString("Login"));
                        userInfo.setNickname(jsonObject.getString("Nickname"));
                        userInfo.setUserCar(jsonObject.getString("UserCar"));
                        userInfo.setPhoto(jsonObject.getString("Photo"));
                        userInfo.setEmail(jsonObject.getString("Email"));
                        userInfo.setPhone(jsonObject.getString("Phone"));
                        if (jsonObject.getInt("NoticeAlert") == 1) {
                            userInfo.setNoticeAlert(true);
                        }
                        else {
                            userInfo.setNoticeAlert(false);
                        }
                        if (jsonObject.getInt("CommentAlert") == 1) {
                            userInfo.setCommentAlert(true);
                        }
                        else {
                            userInfo.setCommentAlert(false);
                        }
                        if (jsonObject.getInt("LikeAlert") == 1) {
                            userInfo.setLikeAlert(true);
                        }
                        else {
                            userInfo.setLikeAlert(false);
                        }
                        if (jsonObject.getInt("TagAlert") == 1) {
                            userInfo.setTagAlert(true);
                        }
                        else {
                            userInfo.setTagAlert(false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("UserInfo", userInfo);
                    intent.putExtra("Mode", mode);
                    startActivity(intent);
                }
            });
        }
    }
}
