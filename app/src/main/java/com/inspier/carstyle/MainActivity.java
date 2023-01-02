package com.inspier.carstyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    FragmentMaintenance fragmentMaintenance;
    private AndroidFragment androidFragment; //AndroidFragment 객체 생성
    private UnityFragment unityFragment; //UnityFragment 객체 생성
    public UserInfo userInfo; //사용자의 정보를 저장하는 객체

    public String mode; //알림창을 클릭하여 들어왔는지에 대한 유무 체크를 하는 객체 (normal, alert)

    public String token; //FCM 기기 토큰을 저장하는 객체
    private HttpConnection httpConn = HttpConnection.getInstance(); //Http통신 Class 객체

    public CustomProgressDialog customProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d("MainActivity token", FirebaseInstanceId.getInstance().getToken());
        token = FirebaseInstanceId.getInstance().getToken();

        //로그인시 받아온 User 정보 가져오기
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("UserInfo");
        mode = intent.getStringExtra("Mode");
        Log.d("MainActivity", "" + userInfo.getID());
        Log.d("MainActivity", mode);
        Log.d("MainActivity", "LikeAlert : " + userInfo.isLikeAlert());

        //서버에 보내기
        sendData(token, String.valueOf(userInfo.getID()));

        //JSON Write 필요 데이터
        long ID = userInfo.getID();
        String resolution = "M";

        //내부 저장소 JSON 폴더 있는지 체크
        String path = getFilesDir().getAbsolutePath() + "/JSON";
        File dir_json = new File(path);
        if (dir_json.exists() == false) {
            //JSON 폴더가 없다면 생성
            dir_json.mkdir();
        }

        //user.json 체크
        File user_json_file = new File(path + "/user.json");
        if (user_json_file.exists() == true) {
            //user.json 파일이 존재한다면 파싱
            try {
                BufferedReader br = new BufferedReader(new FileReader(user_json_file));
                String readStr = "";
                String str = null;
                while((str = br.readLine()) != null) {
                    readStr += str + "\n";
                }
                br.close();
                JSONObject object = new JSONObject(readStr);
                resolution = object.getString("resolution");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //user.json Write
        String text = "{ \"ID\" : " + ID + ", \"resolution\" : \"" + resolution + "\" }";
        try {
            FileWriter fw = new FileWriter(user_json_file);
            fw.write(text);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentMaintenance = new FragmentMaintenance();

        //Fragment 생성 및 초기화
        fragmentMaintenance.setAndroidFragment(new AndroidFragment());
        fragmentMaintenance.setHomeFragment(new HomeFragment());
        fragmentMaintenance.setCommunityFragment(new CommunityFragment());
        fragmentMaintenance.setTuningShopFragment(new TuningShopFragment());
        fragmentMaintenance.setAlertFragment(new AlertFragment());
        fragmentMaintenance.setMenuFragment(new MenuFragment());
        fragmentMaintenance.setSelectManufacturerFragment(new SelectManufacturerFragment());
        fragmentMaintenance.setContentsFragment(new ContentsFragment());
        fragmentMaintenance.setPostWriteFragment(new PostWriteFragment());
        fragmentMaintenance.setCommentsFragment(new CommentsFragment());
        fragmentMaintenance.setPostReTouchFragment(new PostReTouchFragment());
        fragmentMaintenance.setMyPageFragment(new MyPageFragment());
        fragmentMaintenance.setAlertSettingFragment(new AlertSettingFragment());
        fragmentMaintenance.setAccountSettingFragment(new AccountSettingFragment());
        fragmentMaintenance.setSelectCarTypeFragment(new SelectCarTypeFragment());
        fragmentMaintenance.setSelectYearTypeFragment(new SelectYearTypeFragment());
        fragmentMaintenance.setUnityFragment(new UnityFragment());

        //접속 시 초기 화면을 AndroidFragment로 지정
        androidFragment = fragmentMaintenance.getAndroidFragment();
        FragmentTransaction android_unity_transaction = getSupportFragmentManager().beginTransaction();
        android_unity_transaction.add(R.id.android_unity_layout, androidFragment).commit();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //CustomProgressDialog 제작
        customProgressDialog = new CustomProgressDialog(MainActivity.this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    //AndroidFragment의 초기 화면을 HomeFragment로 세팅
    public void setHomeFragment() {
        HomeFragment homeFragment = fragmentMaintenance.getHomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.android_fragment_layout, homeFragment).commit();
    }

    //AndroidFragment의 초기 화면을 AlertFragment로 세팅
    public void setAlertFragment() {
        AlertFragment alertFragment = fragmentMaintenance.getAlertFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.android_fragment_layout, alertFragment);
    }

    //AndroidFragment의 화면을 변경
    public void replaceFragment(Fragment fragment) {
        customProgressDialog.show();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.android_fragment_layout, fragment).commit();
    }

    //화면을 Unity로 변경
    public void switch_Unity(UnityFragment unityFragment) {
        this.unityFragment = unityFragment;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        UnityShowTask task = new UnityShowTask();
        task.execute(unityFragment);
    }

    //Unity 화면을 띄우는데 필요한 비동기 Task Class
    public class UnityShowTask extends AsyncTask<UnityFragment, Void, UnityFragment> {
        @Override
        protected UnityFragment doInBackground(UnityFragment... unityFragments) {
            UnityFragment unityFragment = unityFragments[0];
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.android_unity_layout, unityFragment).commit();
            return unityFragment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(UnityFragment unityFragment) {
            super.onPostExecute(unityFragment);
            unityFragment.mUnityPlayer.windowFocusChanged(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.android_unity_layout);
        if(fragment instanceof UnityFragment) {
            setIntent(intent);
            unityFragment.mUnityPlayer.newIntent(intent);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.android_unity_layout);
        if(fragment instanceof UnityFragment) {
            if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
                unityFragment.mUnityPlayer.lowMemory();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.android_unity_layout);
        if(fragment instanceof UnityFragment) {
            unityFragment.mUnityPlayer.windowFocusChanged(hasFocus);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.android_unity_layout);
        if(fragment instanceof UnityFragment) {
            if(event.getAction() == KeyEvent.ACTION_MULTIPLE) {
                return unityFragment.mUnityPlayer.injectEvent(event);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.android_unity_layout);
        if(fragment instanceof UnityFragment) {
            return unityFragment.mUnityPlayer.injectEvent(event);
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.android_unity_layout);
        if(fragment instanceof UnityFragment) {
            return unityFragment.mUnityPlayer.injectEvent(event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.android_unity_layout);
        if(fragment instanceof UnityFragment) {
            return unityFragment.mUnityPlayer.injectEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.android_unity_layout);
        if(fragment instanceof UnityFragment) {
            return unityFragment.mUnityPlayer.injectEvent(event);
        }
        return super.onGenericMotionEvent(event);
    }

    //서버로 전송하는 function
    private void sendData(final String send_token, final String send_id) {
        new Thread() {
            public void run() {
                httpConn.requestWebServer(send_token, send_id, callback);
            }
        }.start();
    }

    private final Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("MainActivity", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            Log.d("MainActivity", "서버에서 응답한 Body: " + body);
        }
    };

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}