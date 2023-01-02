package com.inspier.carstyle;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class PostWriteFragment extends Fragment {

    //View 객체 생성
    private FrameLayout Community_Post_Write_Back;
    private TextView Community_Post_Write_Finish;
    private EditText Community_Post_Write_Title;
    private EditText Community_Post_Write_Contents;
    private ImageView Community_Post_Write_ImageView1;
    private ImageView Community_Post_Write_ImageView2;
    private ImageView Community_Post_Write_ImageView3;
    private ImageView Community_Post_Write_ImageView4;
    private LinearLayout Post_Write_UserCar_Button;
    private TextView Post_Write_UserCar_TextView;
    private LinearLayout Post_Write_FreeBoard_Button;
    private TextView Post_Write_FreeBoard_TextView;
    private LinearLayout Post_Write_UsedMarket_Button;
    private TextView Post_Write_UsedMarket_TextView;
    private NestedScrollView Post_Write_ScrollView;

    //Fragment 객체
    private CommunityFragment communityFragment;

    //갤러리 정적 변수
    private static final int PICK_FROM_ALBUM_ONE = 1;
    private static final int PICK_FROM_ALBUM_TWO = 2;
    private static final int PICK_FROM_ALBUM_THREE = 3;
    private static final int PICK_FROM_ALBUM_FOUR = 4;

    //받아온 사진 파일을 임시로 저장하는 객체
    private File tempFile1;
    private File tempFile2;
    private File tempFile3;
    private File tempFile4;

    //선택한 게시판을 값으로 저장하는 변수
    private int BoardStatus;

    //사진 갯수를 저장하는 변수
    private int photoCount;

    //PostWriteFragment 생성자
    public PostWriteFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_postwrite, container, false); //View 제작

        //View 객체 연결
        Community_Post_Write_Back = (FrameLayout) view.findViewById(R.id.Community_Post_Write_Back);
        Community_Post_Write_Finish = (TextView) view.findViewById(R.id.Community_Post_Write_Finish);
        Community_Post_Write_Title = (EditText) view.findViewById(R.id.Community_Post_Write_Title);
        Community_Post_Write_Contents = (EditText) view.findViewById(R.id.Community_Post_Write_Contents);
        Community_Post_Write_ImageView1 = (ImageView) view.findViewById(R.id.Community_Post_Write_ImageView1);
        Community_Post_Write_ImageView2 = (ImageView) view.findViewById(R.id.Community_Post_Write_ImageView2);
        Community_Post_Write_ImageView3 = (ImageView) view.findViewById(R.id.Community_Post_Write_ImageView3);
        Community_Post_Write_ImageView4 = (ImageView) view.findViewById(R.id.Community_Post_Write_ImageView4);
        Post_Write_UserCar_Button = (LinearLayout) view.findViewById(R.id.Post_Write_UserCar_Button);
        Post_Write_UserCar_TextView = (TextView) view.findViewById(R.id.Post_Write_UserCar_TextView);
        Post_Write_FreeBoard_Button = (LinearLayout) view.findViewById(R.id.Post_Write_FreeBoard_Button);
        Post_Write_FreeBoard_TextView = (TextView) view.findViewById(R.id.Post_Write_FreeBoard_TextView);
        Post_Write_UsedMarket_Button = (LinearLayout) view.findViewById(R.id.Post_Write_UsedMarket_Button);
        Post_Write_UsedMarket_TextView = (TextView) view.findViewById(R.id.Post_Write_UsedMarket_TextView);
        Post_Write_ScrollView = (NestedScrollView) view.findViewById(R.id.Post_Write_ScrollView);

        //File 데이터 초기화
        tempFile1 = null;
        tempFile2 = null;
        tempFile3 = null;
        tempFile4 = null;

        //권한 설정
        tedPermission();

        //게시판 선택 상태 초기화
        BoardStatus = 0;

        //사진 개수 초기화
        photoCount = 0;

        Community_Post_Write_Title.setText("");
        Community_Post_Write_Contents.setText("");

        //뒤로 가기 버튼 OnClickListener
        Community_Post_Write_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communityFragment = ((MainActivity)getActivity()).fragmentMaintenance.getCommunityFragment();
                ((MainActivity)getActivity()).replaceFragment(communityFragment);
            }
        });

        //게시판 OnClickListener
        Post_Write_UserCar_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //선택 차종 게시판 활성화
                Post_Write_UserCar_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_selected));
                Post_Write_UserCar_TextView.setTextColor(Color.parseColor("#262C3A"));

                //자유 게시판 비활성화
                Post_Write_FreeBoard_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
                Post_Write_FreeBoard_TextView.setTextColor(Color.parseColor("#666666"));

                //중고 장터 비활성화
                Post_Write_UsedMarket_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
                Post_Write_UsedMarket_TextView.setTextColor(Color.parseColor("#666666"));

                BoardStatus = 0;
            }
        });

        Post_Write_FreeBoard_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //선택 차종 게시판 비활성화
                Post_Write_UserCar_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
                Post_Write_UserCar_TextView.setTextColor(Color.parseColor("#666666"));

                //자유 게시판 활성화
                Post_Write_FreeBoard_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_selected));
                Post_Write_FreeBoard_TextView.setTextColor(Color.parseColor("#262C3A"));

                //중고 장터 비활성화
                Post_Write_UsedMarket_Button.setBackground(ContextCompat.getDrawable(((MainActivity)getActivity()), R.drawable.community_nav_none_selected));
                Post_Write_UsedMarket_TextView.setTextColor(Color.parseColor("#666666"));

                BoardStatus = 1;
            }
        });

        Post_Write_UsedMarket_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //선택 차종 게시판 비활성화
                Post_Write_UserCar_Button.setBackground(ContextCompat.getDrawable(((MainActivity) getActivity()), R.drawable.community_nav_none_selected));
                Post_Write_UserCar_TextView.setTextColor(Color.parseColor("#666666"));

                //자유 게시판 비활성화
                Post_Write_FreeBoard_Button.setBackground(ContextCompat.getDrawable(((MainActivity) getActivity()), R.drawable.community_nav_none_selected));
                Post_Write_FreeBoard_TextView.setTextColor(Color.parseColor("#666666"));

                //중고 장터 활성화
                Post_Write_UsedMarket_Button.setBackground(ContextCompat.getDrawable(((MainActivity) getActivity()), R.drawable.community_nav_selected));
                Post_Write_UsedMarket_TextView.setTextColor(Color.parseColor("#262C3A"));

                BoardStatus = 2;
            }
        });

        //사진 등록 OnClickListener
        Community_Post_Write_ImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PostWriteFragment", "Click");
                //사진이 하나도 없을 때
                if(photoCount == 0) {
                    goToAlbum(1);
                    photoCount++;
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

        Community_Post_Write_ImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진이 하나 밖에 없을 때
                if (photoCount == 1) {
                    goToAlbum(2);
                    photoCount++;
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

        Community_Post_Write_ImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진이 두개 밖에 없을 때
                if (photoCount == 2) {
                    goToAlbum(3);
                    photoCount++;
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

        Community_Post_Write_ImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진이 세개 밖에 없을 때
                if (photoCount == 3) {
                    goToAlbum(4);
                    photoCount++;
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

        //작성 완료 OnClickListener
        Community_Post_Write_Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = new Date(System.currentTimeMillis()).getTime();
                String photo = "";

                //공백 체크 후 작업 진행
                if(Community_Post_Write_Title.getText().toString().replace(" ", "").equals("")) {
                    Community_Post_Write_Title.setHint("제목을 입력해 주세요");
                    Community_Post_Write_Title.setHintTextColor(Color.parseColor("#DD301F"));
                }
                else if(Community_Post_Write_Contents.getText().toString().replace(" ", "").equals("")) {
                    Community_Post_Write_Contents.setHint("내용을 입력해 주세요");
                    Community_Post_Write_Contents.setHintTextColor(Color.parseColor("#DD301F"));
                    scrollToView(Community_Post_Write_Contents, Post_Write_ScrollView, 0);
                }
                else {
                    InputMethodManager manager = (InputMethodManager) ((MainActivity)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(((MainActivity)getActivity()).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    //ProgressDialog 실행
                    CustomProgressDialog customProgressDialog = new CustomProgressDialog(((MainActivity)getActivity()));
                    customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    customProgressDialog.show();

                    //사진 FTP 전송
                    if (tempFile1 != null) {
                        UploadPhotoTask task1 = new UploadPhotoTask(tempFile1);
                        task1.execute("" + ((MainActivity) getActivity()).userInfo.getID(), "" + now + "1.jpg");
                        photo = photo + "/" + ((MainActivity)getActivity()).userInfo.getID() + "/" + now + "1.jpg#";
                    }
                    if (tempFile2 != null) {
                        UploadPhotoTask task2 = new UploadPhotoTask(tempFile2);
                        task2.execute("" + ((MainActivity) getActivity()).userInfo.getID(), "" + now + "2.jpg");
                        photo = photo + "/" + ((MainActivity)getActivity()).userInfo.getID() + "/" + now + "2.jpg#";
                    }
                    if (tempFile3 != null) {
                        UploadPhotoTask task3 = new UploadPhotoTask(tempFile3);
                        task3.execute("" + ((MainActivity) getActivity()).userInfo.getID(), "" + now + "3.jpg");
                        photo = photo + "/" + ((MainActivity)getActivity()).userInfo.getID() + "/" + now + "3.jpg#";
                    }
                    if (tempFile4 != null) {
                        UploadPhotoTask task4 = new UploadPhotoTask(tempFile4);
                        task4.execute("" + ((MainActivity) getActivity()).userInfo.getID(), "" + now + "4.jpg");
                        photo = photo + "/" + ((MainActivity)getActivity()).userInfo.getID() + "/" + now + "4.jpg#";
                    }

                    //사용자 차종 게시판일때
                    if(BoardStatus == 0) {
                        DBCommunicationTask task = new DBCommunicationTask();
                        task.setContext(((MainActivity)getActivity()));
                        task.execute("InsertCommunityPost.php",
                                "InsertCommunityPost",
                                ((MainActivity)getActivity()).userInfo.getUserCar(),
                                ((MainActivity)getActivity()).userInfo.getNickname(),
                                Community_Post_Write_Title.getText().toString(),
                                Community_Post_Write_Contents.getText().toString(),
                                photo);
                    }
                    //자유 게시판일때
                    else if(BoardStatus == 1) {
                        DBCommunicationTask task = new DBCommunicationTask();
                        task.setContext(((MainActivity)getActivity()));
                        task.execute("InsertCommunityPost.php",
                                "InsertCommunityPost",
                                "FreeBoard",
                                ((MainActivity)getActivity()).userInfo.getNickname(),
                                Community_Post_Write_Title.getText().toString(),
                                Community_Post_Write_Contents.getText().toString(),
                                photo);
                    }
                    //중고장터일때
                    else if(BoardStatus == 2) {
                        DBCommunicationTask task = new DBCommunicationTask();
                        task.setContext(((MainActivity)getActivity()));
                        task.execute("InsertCommunityPost.php",
                                "InsertCommunityPost",
                                "UsedMarket",
                                ((MainActivity)getActivity()).userInfo.getNickname(),
                                Community_Post_Write_Title.getText().toString(),
                                Community_Post_Write_Contents.getText().toString(),
                                photo);
                    }

                    customProgressDialog.dismiss();
                    communityFragment = ((MainActivity)getActivity()).fragmentMaintenance.getCommunityFragment();
                    ((MainActivity)getActivity()).replaceFragment(communityFragment);
                }
            }
        });

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
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
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

                if(requestCode == PICK_FROM_ALBUM_ONE)
                    tempFile1 = new File(cursor.getString(column_index));
                else if(requestCode == PICK_FROM_ALBUM_TWO)
                    tempFile2 = new File(cursor.getString(column_index));
                else if(requestCode == PICK_FROM_ALBUM_THREE)
                    tempFile3 = new File(cursor.getString(column_index));
                else if(requestCode == PICK_FROM_ALBUM_FOUR)
                    tempFile4 = new File(cursor.getString(column_index));
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
            Community_Post_Write_ImageView1.setImageBitmap(originalBm);
            Community_Post_Write_ImageView2.setVisibility(View.VISIBLE);
        }
        else if(imgview_num == PICK_FROM_ALBUM_TWO) {
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile2.getAbsolutePath(), options);
            Community_Post_Write_ImageView2.setImageBitmap(originalBm);
            Community_Post_Write_ImageView3.setVisibility(View.VISIBLE);
        }
        else if(imgview_num == PICK_FROM_ALBUM_THREE) {
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile3.getAbsolutePath(), options);
            Community_Post_Write_ImageView3.setImageBitmap(originalBm);
            Community_Post_Write_ImageView4.setVisibility(View.VISIBLE);
        }
        else if (imgview_num == PICK_FROM_ALBUM_FOUR) {
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile4.getAbsolutePath(), options);
            Community_Post_Write_ImageView4.setImageBitmap(originalBm);
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

    //Image를 삭제하는 함수
    private void deleteImage(int imgview_num) {

        if(imgview_num < 4) {
            if(imgview_num == 1) {
                BitmapDrawable d = (BitmapDrawable) Community_Post_Write_ImageView2.getDrawable();
                Bitmap b = d.getBitmap();

                //데이터 옮겨오기
                Community_Post_Write_ImageView1.setImageBitmap(b);
                tempFile1 = tempFile2;
            }
            else if(imgview_num == 2) {
                BitmapDrawable d = (BitmapDrawable) Community_Post_Write_ImageView3.getDrawable();
                Bitmap b = d.getBitmap();

                //데이터 옮겨오기
                Community_Post_Write_ImageView2.setImageBitmap(b);
                tempFile2 = tempFile3;
            }
            else if(imgview_num == 3) {
                BitmapDrawable d = (BitmapDrawable) Community_Post_Write_ImageView4.getDrawable();
                Bitmap b = d.getBitmap();

                //데이터 옮겨오기
                Community_Post_Write_ImageView3.setImageBitmap(b);
                tempFile3 = tempFile4;
            }

            //다음 사진이 있을 때
            if(imgview_num + 1 <= photoCount) {
                deleteImage(imgview_num + 1);
            }
            //다음 사진이 없을 때
            else {
                if(imgview_num == 1) {
                    Community_Post_Write_ImageView2.setImageResource(R.drawable.photo_plus);
                    Community_Post_Write_ImageView2.setVisibility(View.GONE);
                }
                else if(imgview_num == 2) {
                    Community_Post_Write_ImageView3.setImageResource(R.drawable.photo_plus);
                    Community_Post_Write_ImageView3.setVisibility(View.GONE);
                }
                else if(imgview_num == 3) {
                    Community_Post_Write_ImageView4.setImageResource(R.drawable.photo_plus);
                    Community_Post_Write_ImageView4.setVisibility(View.GONE);
                }
            }
        }
        //마지막 ImageView를 삭제할 때
        else if(imgview_num == 4){
            Community_Post_Write_ImageView4.setImageResource(R.drawable.photo_plus);
            tempFile4 = null;
        }
    }
}
