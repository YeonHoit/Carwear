package com.inspier.carstyle;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

//TuningShop Fragment
public class TuningShopFragment extends Fragment implements OnMapReadyCallback {

    //네이버 MapView 객체
    private MapView mapView;

    //오버레이 관련 View 객체
    private RelativeLayout Marker_Info;
    private ImageView Tuning_Shop_Image;
    private TextView Tuning_Shop_Title;
    private TextView Tuning_Shop_Address;
    private TextView Tuning_Shop_PhoneNumber;
    private TextView Tuning_Shop_Reserve;
    private TextView Tuning_Shop_Inquire;

    //튜닝 샵 ArrayList 객체
    private ArrayList<String> shop_address_list;
    private ArrayList<Point> shop_point_list;
    private ArrayList<Marker> shop_marker_list;
    private ArrayList<TuningShopData> shop_data_list;

    public TuningShopFragment() {
        //TuningShopFragment의 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tuning_shop, container, false); //View 제작

        //튜닝 샵 ArrayList 객체 초기화
        shop_address_list = new ArrayList<>();
        shop_point_list = new ArrayList<>();
        shop_marker_list = new ArrayList<>();
        shop_data_list = new ArrayList<>();

        //네이버 MapView 객체 초기화
        mapView = view.findViewById(R.id.NaverMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //오버레이 관련 View 객체 초기화
        Marker_Info = (RelativeLayout) view.findViewById(R.id.Marker_Info);
        Tuning_Shop_Image = (ImageView) view.findViewById(R.id.Tuning_Shop_Image);
        Tuning_Shop_Title = (TextView) view.findViewById(R.id.Tuning_Shop_Title);
        Tuning_Shop_Address = (TextView) view.findViewById(R.id.Tuning_Shop_Address);
        Tuning_Shop_PhoneNumber = (TextView) view.findViewById(R.id.Tuning_Shop_PhoneNumber);
        Tuning_Shop_Reserve = (TextView) view.findViewById(R.id.Tuning_Shop_Reserve);
        Tuning_Shop_Inquire = (TextView) view.findViewById(R.id.Tuning_Shop_Inquire);

        return view;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setMapType(NaverMap.MapType.Basic);

        //해당 마커에 해당하는 정보창 띄우기
        Marker.OnClickListener onClickListener_marker = new Marker.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                Marker_Info.setVisibility(View.VISIBLE);
                int index = Integer.parseInt(overlay.getTag().toString());

                try {
                    PhotoGetTask task = new PhotoGetTask();
                    Bitmap bitmap = task.execute(shop_data_list.get(index).getPhoto()).get();

                    Tuning_Shop_Image.setImageBitmap(bitmap);
                    Tuning_Shop_Title.setText(shop_data_list.get(index).getName());
                    Tuning_Shop_Address.setText(shop_data_list.get(index).getAddress());
                    Tuning_Shop_PhoneNumber.setText(shop_data_list.get(index).getPhoneNumber());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }
        };

        CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(new LatLng(35.1220654, 129.1016625), 13);
        naverMap.moveCamera(cameraUpdate);

        //정보창 없애기
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                Marker_Info.setVisibility(View.GONE);
            }
        });

        //shop_address_list.add("부산광역시 남구 신선로 428");
        //튜닝샵 리스트 가지고 오기
        try {
            DBCommunicationTask task = new DBCommunicationTask();
            task.setContext(((MainActivity)getActivity()));
            String result = task.execute("GetTuningShopList.php", "GetTuningShopList").get();

            //JSON 파싱
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("TuningShopList");

            for(int i = 0; i < jsonArray.length(); i++) {
                TuningShopData data = new TuningShopData();
                JSONObject object = jsonArray.getJSONObject(i);

                data.setIndex_num(object.getInt("Index_num"));
                data.setName(object.getString("Name"));
                data.setAddress(object.getString("Address"));
                data.setPhoto(object.getString("Photo"));
                data.setPhoneNumber(object.getString("PhoneNumber"));

                shop_address_list.add(object.getString("Address"));
                shop_data_list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            NaverMapTask naverMapTask = new NaverMapTask();
            String result = naverMapTask.execute().get();

            Log.d("MarkerCount", "" + shop_point_list.size());

            //Marker 세팅
            for (int i = 0; i < shop_point_list.size(); i++) {

                Point point = shop_point_list.get(i);

                Log.d("Marker", "" + point.getLatitude() + ", " + point.getLongitude());

                Marker marker_for = new Marker();

                marker_for.setPosition(new LatLng(point.getLatitude(), point.getLongitude()));
                marker_for.setIcon(OverlayImage.fromResource(R.drawable.marker_icon));

                shop_marker_list.add(marker_for);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //지도에 Marker 생성
        for (int i = 0; i < shop_marker_list.size(); i++) {
            shop_marker_list.get(i).setMap(naverMap);
            shop_marker_list.get(i).setOnClickListener(onClickListener_marker);
            shop_marker_list.get(i).setTag(i);
        }

        ((MainActivity)getActivity()).customProgressDialog.dismiss();
    }

    public class NaverMapTask extends AsyncTask<Void, Void, String> {

        //ProgressDialog 객체
        CustomProgressDialog customProgressDialog = null;

        String clientID = "0jzjjix55l";
        String clientSecret = "hM7QRe9eyN5JkCpAjqtLISIs1SsJ0PPzo2Y1CPQb";

        @Override
        protected void onPreExecute() {
            customProgressDialog = new CustomProgressDialog(((MainActivity)getActivity()));
            customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            //불러올 튜닝 샵 개수 만큼 돌아가는 for
            for(int i = 0; i < shop_address_list.size(); i++) {

                Point point = new Point();
                String json = null;
                String address = shop_address_list.get(i);

                try {
                    address = URLEncoder.encode(address, "UTF-8");
                    String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + address;
                    URL url = new URL(apiURL);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientID);
                    con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

                    int responseCode = con.getResponseCode();
                    BufferedReader br;

                    //정상적으로 호출 되었을 때
                    if(responseCode == 200) {
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    }
                    //에러가 발생 했을 때
                    else {
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }

                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    json = response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(json == null) {
                    Log.d("NaverMapTask", "json null");
                    return null;
                }

                Log.d("NaverMapTask", "json => " + json);

                //JSON 파싱
                try {
                    JSONObject jsonObject_all = new JSONObject(json);
                    JSONArray jsonArray = jsonObject_all.getJSONArray("addresses");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String x = jsonObject.getString("x");
                    String y = jsonObject.getString("y");

                    Log.d("TuningShopFragment", "x : " + x);
                    Log.d("TuningShopFragment", "y : " + y);

                    point.setLongitude(Double.parseDouble(x));
                    point.setLatitude(Double.parseDouble(y));

                    shop_point_list.add(point);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            customProgressDialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
