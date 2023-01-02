package com.inspier.carstyle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class PostReTouchFragment extends Fragment {

    //수정할 커뮤니티 글 객체
    private CommunityPostData communityPostData;

    //View 객체
    private FrameLayout Community_Post_Retouch_Back;
    private TextView Community_Post_Retouch_Finish;
    private LinearLayout Post_Retouch_UserCar_Button;
    private TextView Post_Retouch_UserCar_TextView;
    private LinearLayout Post_Retouch_FreeBoard_Button;
    private TextView Post_Retouch_FreeBoard_TextView;
    private LinearLayout Post_Retouch_UsedMarket_Button;
    private TextView Post_Retouch_UsedMarket_TextView;
    private EditText Community_Post_Retouch_Title;
    private NestedScrollView Post_Retouch_ScrollView;
    private EditText Community_Post_Retouch_Contents;
    private ImageView Community_Post_Retouch_ImageView1;
    private ImageView Community_Post_Retouch_ImageView2;
    private ImageView Community_Post_Retouch_ImageView3;
    private ImageView Community_Post_Retouch_ImageView4;

    //Fragment 객체
    private CommunityFragment communityFragment;

    //게시판 선택 변수
    private int BoardStatus;

    //사진 목록 Array
    private String[] array;
    private ArrayList<String> photo_name;

    //수정 할 파일 Array
    private boolean[] retouch_photo;

    //받아온 사진 파일을 임시로 저장하는 객체
    private File tempFile1;
    private File tempFile2;
    private File tempFile3;
    private File tempFile4;

    //갤러리 정적 변수
    private static final int PICK_FROM_ALBUM_ONE = 1;
    private static final int PICK_FROM_ALBUM_TWO = 2;
    private static final int PICK_FROM_ALBUM_THREE = 3;
    private static final int PICK_FROM_ALBUM_FOUR = 4;

    //사진 파일 갯수를 저장하는 변수
    int photoCount;

    //PostReTouchFragment 생성자
    public PostReTouchFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_retouch, container, false); //View 제작

        //객체 연결 및 초기화
        Community_Post_Retouch_Back = (FrameLayout) view.findViewById(R.id.Community_Post_Retouch_Back);
        Community_Post_Retouch_Finish = (TextView) view.findViewById(R.id.Community_Post_Retouch_Finish);
        Post_Retouch_UserCar_Button = (LinearLayout) view.findViewById(R.id.Post_Retouch_UserCar_Button);
        Post_Retouch_UserCar_TextView = (TextView) view.findViewById(R.id.Post_Retouch_UserCar_TextView);
        Post_Retouch_FreeBoard_Button = (LinearLayout) view.findViewById(R.id.Post_Retouch_FreeBoard_Button);
        Post_Retouch_FreeBoard_TextView = (TextView) view.findViewById(R.id.Post_Retouch_FreeBoard_TextView);
        Post_Retouch_UsedMarket_Button = (LinearLayout) view.findViewById(R.id.Post_Retouch_UsedMarket_Button);
        Post_Retouch_UsedMarket_TextView = (TextView) view.findViewById(R.id.Post_Retouch_UsedMarket_TextView);
        Community_Post_Retouch_Title = (EditText) view.findViewById(R.id.Community_Post_Retouch_Title);
        Post_Retouch_ScrollView = (NestedScrollView) view.findViewById(R.id.Post_Retouch_ScrollView);
        Community_Post_Retouch_Contents = (EditText) view.findViewById(R.id.Community_Post_Retouch_Contents);
        Community_Post_Retouch_ImageView1 = (ImageView) view.findViewById(R.id.Community_Post_Retouch_ImageView1);
        Community_Post_Retouch_ImageView2 = (ImageView) view.findViewById(R.id.Community_Post_Retouch_ImageView2);
        Community_Post_Retouch_ImageView3 = (ImageView) view.findViewById(R.id.Community_Post_Retouch_ImageView3);
        Community_Post_Retouch_ImageView4 = (ImageView) view.findViewById(R.id.Community_Post_Retouch_ImageView4);

        //사진 임시 파일 초기화
        tempFile1 = null;
        tempFile2 = null;
        tempFile3 = null;
        tempFile4 = null;

        tedPermission();

        //글 제목과 내용 setText
        Community_Post_Retouch_Title.setText(communityPostData.getCommunity_Post_Title());
        Community_Post_Retouch_Contents.setText(communityPostData.getCommunity_Post_Contents());

        //사진 관련 배열 초기화
        array = communityPostData.getCommunity_Post_Photo().split("#");
        retouch_photo = new boolean[4];
        Arrays.fill(retouch_photo, false);
        photo_name = new ArrayList<>();

        //사진 갯수 초기화
        if(array[0].equals("")) {
            photoCount = 0;
        }
        else {
            photoCount = array.length;
        }

        //사진 이름 관리
        for(int i = 0; i < photoCount; i++) {
            photo_name.add(array[i]);
        }

        Log.d("PostReTouchFragment",  "" + array.length);

        //ImageView 세팅
        //이미지가 1개 있을 때
        if(array.length == 1 && !array[0].equals("")) {
            try {
                PhotoGetTask task1 = new PhotoGetTask();
                Community_Post_Retouch_ImageView1.setImageBitmap(task1.execute(array[0]).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Community_Post_Retouch_ImageView2.setVisibility(View.VISIBLE);
        }
        //이미지가 2개 있을 때
        else if(array.length == 2) {
            Community_Post_Retouch_ImageView2.setVisibility(View.VISIBLE);
            try {
                PhotoGetTask task1 = new PhotoGetTask();
                Community_Post_Retouch_ImageView1.setImageBitmap(task1.execute(array[0]).get());
                PhotoGetTask task2 = new PhotoGetTask();
                Community_Post_Retouch_ImageView2.setImageBitmap(task2.execute(array[1]).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Community_Post_Retouch_ImageView3.setVisibility(View.VISIBLE);
        }
        //이미지가 3개 있을 때
        else if(array.length == 3) {
            Community_Post_Retouch_ImageView2.setVisibility(View.VISIBLE);
            Community_Post_Retouch_ImageView3.setVisibility(View.VISIBLE);
            try {
                PhotoGetTask task1 = new PhotoGetTask();
                Community_Post_Retouch_ImageView1.setImageBitmap(task1.execute(array[0]).get());
                PhotoGetTask task2 = new PhotoGetTask();
                Community_Post_Retouch_ImageView2.setImageBitmap(task2.execute(array[1]).get());
                PhotoGetTask task3 = new PhotoGetTask();
                Community_Post_Retouch_ImageView3.setImageBitmap(task3.execute(array[2]).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Community_Post_Retouch_ImageView4.setVisibility(View.VISIBLE);
        }
        //이미지가 4개 있을 때
        else if(array.length == 4) {
            Community_Post_Retouch_ImageView2.setVisibility(View.VISIBLE);
            Community_Post_Retouch_ImageView3.setVisibility(View.VISIBLE);
            Community_Post_Retouch_ImageView4.setVisibility(View.VISIBLE);
            try {
                PhotoGetTask task1 = new PhotoGetTask();
                Community_Post_Retouch_ImageView1.setImageBitmap(task1.execute(array[0]).get());
                PhotoGetTask task2 = new PhotoGetTask();
                Community_Post_Retouch_ImageView2.setImageBitmap(task2.execute(array[1]).get());
                PhotoGetTask task3 = new PhotoGetTask();
                Community_Post_Retouch_ImageView3.setImageBitmap(task3.execute(array[2]).get());
                PhotoGetTask task4 = new PhotoGetTask();
                Community_Post_Retouch_ImageView4.setImageBitmap(task4.execute(array[3]).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //자유 게시판 글이였을 때
        if (communityPostData.getCommunity_Post_Board_Name().equals("FreeBoard")) {
            NavSetting_Free();
            BoardStatus = 1;
        }
        //중고 장터 글이였을 때
        else if (communityPostData.getCommunity_Post_Board_Name().equals("UsedMarket")) {
            NavSetting_UsedMarket();
            BoardStatus = 2;
        }
        //선택 차종 글이였을 때
        else {
            NavSetting_SelectCar();
            BoardStatus = 0;
        }


        //뒤로 가기 setOnClickListener
        Community_Post_Retouch_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communityFragment = ((MainActivity)getActivity()).fragmentMaintenance.getCommunityFragment();
                ((MainActivity)getActivity()).replaceFragment(communityFragment);
            }
        });

        //수정 완료 setOnClickListener
        Community_Post_Retouch_Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = new Date(System.currentTimeMillis()).getTime();
                String photo = "";
                //공백 체크 후 작업 진행
                if (Community_Post_Retouch_Title.getText().toString().replace(" ", "").equals("")) {
                    Community_Post_Retouch_Title.setHint("제목을 입력해 주세요");
                    Community_Post_Retouch_Title.setHintTextColor(Color.parseColor("#DD301F"));
                }
                else if (Community_Post_Retouch_Contents.getText().toString().replace(" ", "").equals("")) {
                    Community_Post_Retouch_Contents.setHint("내용을 입력해 주세요");
                    Community_Post_Retouch_Contents.setHintTextColor(Color.parseColor("#DD301F"));
                    scrollToView(Community_Post_Retouch_Contents, Post_Retouch_ScrollView, 0);
                }
                else {
                    if (((MainActivity)getActivity()).getCurrentFocus() != null) {
                        InputMethodManager manager = (InputMethodManager) ((MainActivity) getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(((MainActivity) getActivity()).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    //ProgressDialog 실행
                    CustomProgressDialog customProgressDialog = new CustomProgressDialog(((MainActivity)getActivity()));
                    customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    customProgressDialog.show();

                    //새로 업로드된 사진 체크
                    for(int i = 0; i < photoCount ; i++) {
                        //사진이 변경되었을 때
                        if (retouch_photo[i]) {
                            if (i == 0) {
                                UploadPhotoTask task1 = new UploadPhotoTask(tempFile1);
                                task1.execute("" + ((MainActivity)getActivity()).userInfo.getID(), "" + now + "1.jpg");
                                photo = photo + "/" + ((MainActivity)getActivity()).userInfo.getID() + "/" + now + "1.jpg#";
                            }
                            else if (i == 1) {
                                UploadPhotoTask task2 = new UploadPhotoTask(tempFile2);
                                task2.execute("" + ((MainActivity)getActivity()).userInfo.getID(), "" + now + "2.jpg");
                                photo = photo + "/" + ((MainActivity)getActivity()).userInfo.getID() + "/" + now + "2.jpg#";
                            }
                            else if (i == 2) {
                                UploadPhotoTask task3 = new UploadPhotoTask(tempFile3);
                                task3.execute("" + ((MainActivity)getActivity()).userInfo.getID(), "" + now + "3.jpg");
                                photo = photo + "/" + ((MainActivity)getActivity()).userInfo.getID() + "/" + now + "3.jpg#";
                            }
                            else if (i == 3) {
                                UploadPhotoTask task4 = new UploadPhotoTask(tempFile4);
                                task4.execute("" + ((MainActivity)getActivity()).userInfo.getID(), "" + now + "4.jpg");
                                photo = photo + "/" + ((MainActivity)getActivity()).userInfo.getID() + "/" + now + "4.jpg#";
                            }
                        }
                        //기존에 있던 사진일 때
                        else {
                            photo = photo + photo_name.get(i) + "#";
                        }
                    }

                    //사용자 차종 게시판일때
                    if(BoardStatus == 0) {
                        DBCommunicationTask task = new DBCommunicationTask();
                        task.setContext(((MainActivity)getActivity()));
                        task.execute("UpdateCommunityPost.php",
                                "UpdateCommunityPost",
                                "" + communityPostData.getCommunity_Post_Index_Num(),
                                ((MainActivity)getActivity()).userInfo.getUserCar(),
                                Community_Post_Retouch_Title.getText().toString(),
                                Community_Post_Retouch_Contents.getText().toString(),
                                photo);
                    }
                    //자유 게시판일때
                    else if(BoardStatus == 1) {
                        DBCommunicationTask task = new DBCommunicationTask();
                        task.setContext(((MainActivity)getActivity()));
                        task.execute("UpdateCommunityPost.php",
                                "UpdateCommunityPost",
                                "" + communityPostData.getCommunity_Post_Index_Num(),
                                "FreeBoard",
                                Community_Post_Retouch_Title.getText().toString(),
                                Community_Post_Retouch_Contents.getText().toString(),
                                photo);
                    }
                    //중고장터일때
                    else if(BoardStatus == 2) {
                        DBCommunicationTask task = new DBCommunicationTask();
                        task.setContext(((MainActivity)getActivity()));
                        task.execute("UpdateCommunityPost.php",
                                "UpdateCommunityPost",
                                "" + communityPostData.getCommunity_Post_Index_Num(),
                                "UsedMarket",
                                Community_Post_Retouch_Title.getText().toString(),
                                Community_Post_Retouch_Contents.getText().toString(),
                                photo);
                    }

                    customProgressDialog.dismiss();
                    communityFragment = ((MainActivity)getActivity()).fragmentMaintenance.getCommunityFragment();
                    ((MainActivity)getActivity()).replaceFragment(communityFragment);
                }
            }
        });

        //게시판 setOnClickListener
        Post_Retouch_UserCar_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavSetting_SelectCar();
            }
        });

        Post_Retouch_FreeBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavSetting_Free();
            }
        });

        Post_Retouch_UsedMarket_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavSetting_UsedMarket();
            }
        });

        //ImageView setOnClickListener
        Community_Post_Retouch_ImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진이 하나도 없을 때
                if(photoCount == 0) {
                    goToAlbum(1);
                    photoCount++;
                    photo_name.add("");
                }
                //사진이 있으면 수정, 삭제가 가능
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(((MainActivity) getActivity()));

                    builder.setItems(R.array.retouch_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //수정을 선택했을 때
                            if (which == 0) {
                                goToAlbum(1);
                            }
                            //삭제를 선택했을 때
                            else if (which == 1) {
                                photo_name.remove(0);
                                deleteImage(1);
                                photoCount--;
                            }
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        Community_Post_Retouch_ImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진이 하나 밖에 없을 때
                if (photoCount == 1) {
                    goToAlbum(2);
                    photoCount++;
                    photo_name.add("");
                }
                //사진이 있으면 수정, 삭제가 가능
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(((MainActivity)getActivity()));

                    builder.setItems(R.array.retouch_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //수정을 선택했을 때
                            if (which == 0) {
                                goToAlbum(2);
                            }
                            //삭제를 선택했을 때
                            else if (which == 1) {
                                photo_name.remove(1);
                                deleteImage(2);
                                photoCount--;
                            }
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        Community_Post_Retouch_ImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진이 두개 밖에 없을 때
                if (photoCount == 2) {
                    goToAlbum(3);
                    photoCount++;
                    photo_name.add("");
                }
                //사진이 있으면 수정, 삭제가 가능
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(((MainActivity)getActivity()));

                    builder.setItems(R.array.retouch_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //수정을 선택했을 때
                            if (which == 0) {
                                goToAlbum(3);
                            }
                            //삭제를 선택했을 때
                            else if (which == 1) {
                                photo_name.remove(2);
                                deleteImage(3);
                                photoCount--;
                            }
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        Community_Post_Retouch_ImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진이 세개 밖에 없을 때
                if (photoCount == 3) {
                    goToAlbum(4);
                    photoCount++;
                    photo_name.add("");
                }
                //사진이 있으면 수정, 삭제가 가능
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(((MainActivity)getActivity()));

                    builder.setItems(R.array.retouch_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //수정을 선택했을 때
                            if (which == 0) {
                                goToAlbum(4);
                            }
                            //삭제를 선택했을 때
                            else if (which == 1) {
                                photo_name.remove(3);
                                deleteImage(4);
                                photoCount--;
                            }
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }

    public void setCommunityPostData(CommunityPostData communityPostData) {
        this.communityPostData = communityPostData;
    }

    private void NavSetting_SelectCar() {
        //선택 차종 게시판 활성화
        Post_Retouch_UserCar_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_selected));
        Post_Retouch_UserCar_TextView.setTextColor(Color.parseColor("#262C3A"));

        //자유 게시판 비활성화
        Post_Retouch_FreeBoard_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Post_Retouch_FreeBoard_TextView.setTextColor(Color.parseColor("#666666"));

        //중고 장터 비활성화
        Post_Retouch_UsedMarket_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Post_Retouch_UsedMarket_TextView.setTextColor(Color.parseColor("#666666"));

        BoardStatus = 0;
    }

    private void NavSetting_Free() {
        //선택 차종 게시판 비활성화
        Post_Retouch_UserCar_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Post_Retouch_UserCar_TextView.setTextColor(Color.parseColor("#666666"));

        //자유 게시판 활성화
        Post_Retouch_FreeBoard_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_selected));
        Post_Retouch_FreeBoard_TextView.setTextColor(Color.parseColor("#262C3A"));

        //중고 장터 비활성화
        Post_Retouch_UsedMarket_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Post_Retouch_UsedMarket_TextView.setTextColor(Color.parseColor("#666666"));

        BoardStatus = 1;
    }

    private void NavSetting_UsedMarket() {
        //선택 차종 게시판 비활성화
        Post_Retouch_UserCar_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Post_Retouch_UserCar_TextView.setTextColor(Color.parseColor("#666666"));

        //자유 게시판 비활성화
        Post_Retouch_FreeBoard_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
        Post_Retouch_FreeBoard_TextView.setTextColor(Color.parseColor("#666666"));

        //중고 장터 활성화
        Post_Retouch_UsedMarket_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_selected));
        Post_Retouch_UsedMarket_TextView.setTextColor(Color.parseColor("#262C3A"));

        BoardStatus = 2;
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
    private void goToAlbum(int imgview_num) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        if(imgview_num == 1) {
            startActivityForResult(intent, PICK_FROM_ALBUM_ONE);
        }
        else if(imgview_num == 2) {
            startActivityForResult(intent, PICK_FROM_ALBUM_TWO);
        }
        else if(imgview_num == 3) {
            startActivityForResult(intent, PICK_FROM_ALBUM_THREE);
        }
        else if(imgview_num == 4) {
            startActivityForResult(intent, PICK_FROM_ALBUM_FOUR);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //취소 했을 때
        if(resultCode != Activity.RESULT_OK) {
            Toast.makeText(((MainActivity)getActivity()), "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            return;
        }

        if (requestCode == PICK_FROM_ALBUM_ONE || requestCode == PICK_FROM_ALBUM_TWO || requestCode == PICK_FROM_ALBUM_THREE || requestCode == PICK_FROM_ALBUM_FOUR) {
            Uri photoUri = data.getData();

            Cursor cursor = null;

            try {
                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = ((MainActivity)getActivity()).getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                if(requestCode == PICK_FROM_ALBUM_ONE) {
                    tempFile1 = new File(cursor.getString(column_index));
                    retouch_photo[0] = true;
                }
                else if(requestCode == PICK_FROM_ALBUM_TWO) {
                    tempFile2 = new File(cursor.getString(column_index));
                    retouch_photo[1] = true;
                }
                else if(requestCode == PICK_FROM_ALBUM_THREE) {
                    tempFile3 = new File(cursor.getString(column_index));
                    retouch_photo[2] = true;
                }
                else if(requestCode == PICK_FROM_ALBUM_FOUR) {
                    tempFile4 = new File(cursor.getString(column_index));
                    retouch_photo[3] = true;
                }
            } finally {
                if(cursor != null) {
                    cursor.close();
                }
            }

            //ImageView에 Image 세팅
            setImage(requestCode);
        }
    }

    //받아온 Image를 ImageView에 세팅하는 함수
    private void setImage(int imgview_num) {

        BitmapFactory.Options options = new BitmapFactory.Options();

        if(imgview_num == PICK_FROM_ALBUM_ONE) {
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile1.getAbsolutePath(), options);
            Community_Post_Retouch_ImageView1.setImageBitmap(originalBm);
            Community_Post_Retouch_ImageView2.setVisibility(View.VISIBLE);
        }
        else if(imgview_num == PICK_FROM_ALBUM_TWO) {
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile2.getAbsolutePath(), options);
            Community_Post_Retouch_ImageView2.setImageBitmap(originalBm);
            Community_Post_Retouch_ImageView3.setVisibility(View.VISIBLE);
        }
        else if(imgview_num == PICK_FROM_ALBUM_THREE) {
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile3.getAbsolutePath(), options);
            Community_Post_Retouch_ImageView3.setImageBitmap(originalBm);
            Community_Post_Retouch_ImageView4.setVisibility(View.VISIBLE);
        }
        else if (imgview_num == PICK_FROM_ALBUM_FOUR) {
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile4.getAbsolutePath(), options);
            Community_Post_Retouch_ImageView4.setImageBitmap(originalBm);
        }
    }

    //Image를 삭제하는 함수
    private void deleteImage(int imgview_num) {

        if(imgview_num < 4) {
            if(imgview_num == 1) {
                BitmapDrawable d = (BitmapDrawable) Community_Post_Retouch_ImageView2.getDrawable();
                Bitmap b = d.getBitmap();

                //데이터 옮겨오기
                Community_Post_Retouch_ImageView1.setImageBitmap(b);
                tempFile1 = tempFile2;
                retouch_photo[0] = retouch_photo[1];
            }
            else if(imgview_num == 2) {
                BitmapDrawable d = (BitmapDrawable) Community_Post_Retouch_ImageView3.getDrawable();
                Bitmap b = d.getBitmap();

                //데이터 옮겨오기
                Community_Post_Retouch_ImageView2.setImageBitmap(b);
                tempFile2 = tempFile3;
                retouch_photo[1] = retouch_photo[2];
            }
            else if(imgview_num == 3) {
                BitmapDrawable d = (BitmapDrawable) Community_Post_Retouch_ImageView4.getDrawable();
                Bitmap b = d.getBitmap();

                //데이터 옮겨오기
                Community_Post_Retouch_ImageView3.setImageBitmap(b);
                tempFile3 = tempFile4;
                retouch_photo[2] = retouch_photo[3];
            }

            //다음 사진이 있을 때
            if(imgview_num + 1 <= photoCount) {
                deleteImage(imgview_num + 1);
            }
            //다음 사진이 없을 때
            else {
                if(imgview_num == 1) {
                    Community_Post_Retouch_ImageView2.setImageResource(R.drawable.photo_plus);
                    Community_Post_Retouch_ImageView2.setVisibility(View.GONE);
                }
                else if(imgview_num == 2) {
                    Community_Post_Retouch_ImageView3.setImageResource(R.drawable.photo_plus);
                    Community_Post_Retouch_ImageView3.setVisibility(View.GONE);
                }
                else if(imgview_num == 3) {
                    Community_Post_Retouch_ImageView4.setImageResource(R.drawable.photo_plus);
                    Community_Post_Retouch_ImageView4.setVisibility(View.GONE);
                }
            }
        }
        //마지막 ImageView를 삭제할 때
        else if(imgview_num == 4){
            Community_Post_Retouch_ImageView4.setImageResource(R.drawable.photo_plus);
            tempFile4 = null;
            retouch_photo[3] = false;
        }
    }

    public static void scrollToView(View view, final NestedScrollView scrollView, int count) {
        if(view != null && view != scrollView) {
            count += view.getTop();
            scrollToView((View) view.getParent(), scrollView, count);
        }
        else if(scrollView != null) {
            final int finalCount = count;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, finalCount);
                }
            }, 200);
        }
    }
}
