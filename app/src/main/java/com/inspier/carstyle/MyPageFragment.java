package com.inspier.carstyle;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MyPageFragment extends Fragment {

    private static final String TAG = "MyPageFragment";

    //View 객체 생성
    private FrameLayout MyPage_Back_Button;
    private TextView MyPage_Finish;
    private RoundImageView MyPage_Profile_ImageView;
    private ImageView MyPage_Profile_Select;
    private EditText MyPage_NickName_EditText;
    private EditText MyPage_Email_EditText;
    private EditText MyPage_PhoneNumber_EditText;

    //Setting 객체 생성
    String Photo = null;
    String NickName = null;
    String Email = null;
    String PhoneNumber = null;
    private Dialog user_dialog = null;
    private Dialog overlap_dialog = null;

    //Fragment 객체 생성
    private MenuFragment menuFragment;

    //사진관련 객체 생성
    private File tempFile;
    private static final int PICK_FROM_ALBUM = 1;
    private boolean isProfileEmpty;

    public MyPageFragment() {
        //MyPageFragment 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        //View 객체 초기화
        MyPage_Back_Button = (FrameLayout) view.findViewById(R.id.MyPage_Back_Button);
        MyPage_Finish = (TextView) view.findViewById(R.id.MyPage_Finish);
        MyPage_Profile_ImageView = (RoundImageView) view.findViewById(R.id.MyPage_Profile_ImageView);
        MyPage_Profile_Select = (ImageView) view.findViewById(R.id.MyPage_Profile_Select);
        MyPage_NickName_EditText = (EditText) view.findViewById(R.id.MyPage_NickName_EditText);
        MyPage_Email_EditText = (EditText) view.findViewById(R.id.MyPage_Email_EditText);
        MyPage_PhoneNumber_EditText = (EditText) view.findViewById(R.id.MyPage_PhoneNumber_EditText);

        //View 기본 세팅
        MyPage_Profile_ImageView.setRectRadius(200f);
        MyPage_PhoneNumber_EditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        //뒤로 가기 버튼 setOnClickListener
        MyPage_Back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFragment = ((MainActivity)getActivity()).fragmentMaintenance.getMenuFragment();
                ((MainActivity)getActivity()).replaceFragment(menuFragment);
            }
        });

        //수정 완료 버튼 setOnClickListener
        MyPage_Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //수정 완료 여부 물어보는 Dialog 표시 후 서버 Update 진행
                user_dialog = new Dialog(((MainActivity)getActivity()));
                user_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                user_dialog.setContentView(R.layout.custom_user_dialog);

                TextView user_cancel = user_dialog.findViewById(R.id.user_cancel);
                TextView user_run = user_dialog.findViewById(R.id.user_run);

                user_dialog.show();

                //취소 setOnClickListener
                user_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user_dialog.dismiss();
                    }
                });

                //변경 setOnClickListener
                user_run.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user_dialog.dismiss();

                        //닉네임 중복체크
                        String NewNickName = MyPage_NickName_EditText.getText().toString();
                        boolean check = true;
                        //기존 닉네임과 다를때만 닉네임 중복 체크 진행
                        if (!NickName.equals(NewNickName)) {
                            try {
                                DBCommunicationTask checktask = new DBCommunicationTask();
                                checktask.setContext(((MainActivity)getActivity()));
                                String result = checktask.execute("NickNameOverlapCheck.php", "NickNameOverlapCheck", NewNickName).get();

                                //중복이 있을 때
                                if (result.equals("False")) {
                                    Log.d(TAG, "false");
                                    check = false;

                                    //이미 사용중인 닉네임이라고 알려주는 Dialog
                                    overlap_dialog = new Dialog(((MainActivity)getActivity()));
                                    overlap_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    overlap_dialog.setContentView(R.layout.custom_overlap_dialog);

                                    TextView overlap_ok = overlap_dialog.findViewById(R.id.overlap_ok);

                                    overlap_dialog.show();

                                    overlap_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            overlap_dialog.dismiss();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        //중복 체크 통과 시 사용자 정보 Update 진행
                        if (check) {
                            Log.d(TAG, "true");

                            //현재 시간 가져오기
                            long now = new Date(System.currentTimeMillis()).getTime();
                            String photo = "";

                            //CustomProgressDialog 실행
                            CustomProgressDialog customProgressDialog = new CustomProgressDialog(((MainActivity)getActivity()));
                            customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            customProgressDialog.show();

                            InputMethodManager manager = (InputMethodManager) ((MainActivity)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (((MainActivity)getActivity()).getCurrentFocus() != null) {
                                manager.hideSoftInputFromWindow(((MainActivity) getActivity()).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }

                            //사진이 변경 되었을 때
                            if (tempFile != null) {
                                UploadPhotoTask uploadPhotoTask = new UploadPhotoTask(tempFile);
                                uploadPhotoTask.execute(Long.toString(((MainActivity)getActivity()).userInfo.getID()), now + "0.jpg");
                                photo = "/" + ((MainActivity)getActivity()).userInfo.getID() + "/" + now + "0.jpg";
                            }

                            //사진이 변경 되지 않아도 기존 사진을 유지해야 할 때
                            if (tempFile == null && !((MainActivity)getActivity()).userInfo.getPhoto().equals("") && !isProfileEmpty) {
                                photo = ((MainActivity)getActivity()).userInfo.getPhoto();
                            }

                            //Update 진행
                            DBCommunicationTask task = new DBCommunicationTask();
                            task.setContext(((MainActivity)getActivity()));
                            task.execute("UpdateUserInfo.php",
                                    "UpdateUserInfo",
                                    ((MainActivity)getActivity()).userInfo.getNickname(),
                                    MyPage_NickName_EditText.getText().toString(),
                                    photo,
                                    MyPage_Email_EditText.getText().toString(),
                                    MyPage_PhoneNumber_EditText.getText().toString());

                            ((MainActivity)getActivity()).userInfo.setPhoto(photo);
                            ((MainActivity)getActivity()).userInfo.setNickname(MyPage_NickName_EditText.getText().toString());
                            ((MainActivity)getActivity()).userInfo.setEmail(MyPage_Email_EditText.getText().toString());
                            ((MainActivity)getActivity()).userInfo.setPhone(MyPage_PhoneNumber_EditText.getText().toString());

                            customProgressDialog.dismiss();

                            menuFragment = ((MainActivity)getActivity()).fragmentMaintenance.getMenuFragment();
                            ((MainActivity)getActivity()).replaceFragment(menuFragment);
                        }
                    }
                });
            }
        });

        //프로필 사진 Setting
        Photo = ((MainActivity)getActivity()).userInfo.getPhoto();
        tempFile = null;

        //프로필 사진이 없다면 기본 사진을 사용
        if (Photo.equals("")) {
            MyPage_Profile_ImageView.setImageResource(R.drawable.user);
            isProfileEmpty = true;
        }
        //프로필 사진이 설정 되어 있다면 프로필 사진을 불러옴
        else {
            try {
                PhotoGetTask phototask = new PhotoGetTask();
                MyPage_Profile_ImageView.setImageBitmap(phototask.execute(Photo).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            isProfileEmpty = false;
        }

        //앨범 접근 권한 Check
        tedPermission();

        //프로필 사진 변경 OnClickListener 정의
        View.OnClickListener profileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진이 없을 때
                if (isProfileEmpty) {
                    goToAlbum();
                }
                //사진이 있을 때
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(((MainActivity)getActivity()));
                    builder.setItems(R.array.retouch_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //수정을 선택했을 때
                            if (which == 0) {
                                goToAlbum();
                            }
                            //삭제를 선택했을 때
                            else if (which == 1) {
                                MyPage_Profile_ImageView.setImageResource(R.drawable.user);
                                isProfileEmpty = true;
                            }
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        };

        //프로필 사진 변경 setOnClickListener
        MyPage_Profile_ImageView.setOnClickListener(profileListener);
        MyPage_Profile_Select.setOnClickListener(profileListener);

        //닉네임 Setting
        NickName = ((MainActivity)getActivity()).userInfo.getNickname();
        MyPage_NickName_EditText.setText(NickName);

        //이메일 Setting
        Email = ((MainActivity)getActivity()).userInfo.getEmail();
        MyPage_Email_EditText.setText(Email);

        //전화번호 Setting
        PhoneNumber = ((MainActivity)getActivity()).userInfo.getPhone();
        MyPage_PhoneNumber_EditText.setText(PhoneNumber);

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }

    //권한 설정 함수
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                //권한 요청 성공
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                //권한 요청 실패
            }
        };

        TedPermission.with(((MainActivity)getActivity()))
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    //앨범 접근하는 함수
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //취소 했을 때
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(((MainActivity)getActivity()), "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();

            Cursor cursor = null;

            try {
                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = ((MainActivity)getActivity()).getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            //ImageView에 Image 세팅
            setImage();
        }
    }

    //받아온 Image를 ImageView에 세팅하는 함수
    private void setImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();

        ExifInterface exif = null;

        try {
            exif = new ExifInterface(tempFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        Bitmap cameraRotated = rotateBitmap(originalBm, orientation);

        MyPage_Profile_ImageView.setImageBitmap(cameraRotated);
    }

    //자동 회전 방지 함수
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}
