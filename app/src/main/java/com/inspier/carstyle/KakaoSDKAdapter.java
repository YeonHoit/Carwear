package com.inspier.carstyle;

import android.content.Context;

import androidx.annotation.Nullable;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

public class KakaoSDKAdapter extends KakaoAdapter {

    //로그인 시 사용 될, Session의 옵션 설정을 위한 인터페이스
    @Override
    public ISessionConfig getSessionConfig() {
        return new ISessionConfig() {
            @Override
            public AuthType[] getAuthTypes() {
                //로그인 시 인증 타입 지정
                //KAKAO_TALK : 카카오톡 로그인 타입
                //KAKAO_STORY : 카카오스토리 로그인 타입
                //KAKAO_ACCOUNT : 웹뷰 다이얼로그를 통한 계정연결 타입
                //KAKAO_TALK_EXCLUDE_NATIVE_LOGIN : 카카오톡 로그인 타입과 함께 계정생성을 위한 버튼을 함께 제공
                //KAKAO_LOGIN_ALL : 모든 로그인 방식을 제공
                return new AuthType[]{AuthType.KAKAO_ACCOUNT};
            }

            @Override
            public boolean isUsingWebviewTimer() {
                //로그인 웹뷰에서 pause와 resume시에 타이머를 설정하여, CPU의 소모를 절약 할 지에 대한 여부 지정
                //true로 지정할 경우, 로그인 웹뷰의 onPause()와 onResume()에 타이머를 설정해야 함
                return false;
            }

            @Override
            public boolean isSecureMode() {
                //로그인 시 토큰을 저장할 때의 암호화 여부를 지정함
                return false;
            }

            @Nullable
            @Override
            public ApprovalType getApprovalType() {
                //일반 사용자가 아닌 Kakao와 제휴 된 앱에서 사용되는 값
                //값을 지정하지 않을 경우, ApprovalType.INDIVIDUAL 값으로 사용
                return ApprovalType.INDIVIDUAL;
            }

            @Override
            public boolean isSaveFormData() {
                //로그인 웹뷰에서 email 입력 폼의 데이터 저장 여부 지정
                return false;
            }
        };
    }

    //Application이 가지고 있는 정보를 얻기 위한 인터페이스
    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {
            @Override
            public Context getApplicationContext() {
                return GlobalApplication.getGlobalApplicationContext();
            }
        };
    }
}
