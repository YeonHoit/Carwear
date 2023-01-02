package com.inspier.carstyle;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.unity3d.player.IUnityPlayerLifecycleEvents;
import com.unity3d.player.UnityPlayer;

public class UnityFragment extends Fragment implements IUnityPlayerLifecycleEvents {

    protected UnityPlayer mUnityPlayer; //UnityPlayer View 객체

    public UnityFragment() {
        //UnityFragment의 생성자
    }

    protected String updateUnityCommandLineArguments(String cmdLine) {
        return cmdLine;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String cmdLine = updateUnityCommandLineArguments(((MainActivity)getActivity()).getIntent().getStringExtra("unity"));
        ((MainActivity)getActivity()).getIntent().putExtra("unity", cmdLine);

        mUnityPlayer = new UnityPlayer(getActivity(), this);
        return mUnityPlayer;
    }

    @Override
    public void onUnityPlayerUnloaded() {
        FragmentTransaction transaction = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.android_unity_layout, ((MainActivity)getActivity()).fragmentMaintenance.getAndroidFragment()).commit();
    }

    @Override
    public void onUnityPlayerQuitted() {
        Process.killProcess(Process.myPid());
    }

    @Override
    public void onDestroy() {
        mUnityPlayer.destroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        mUnityPlayer.configurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
}
