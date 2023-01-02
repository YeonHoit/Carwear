package com.inspier.carstyle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class SelectManufacturerFragment extends Fragment {

    //버튼 객체 생성
    private FrameLayout Manu_hyundai;
    private FrameLayout Manu_kia;
    private FrameLayout Manu_chevrolet;
    private FrameLayout Manu_renault;
    private FrameLayout Manu_ssangyong;
    private FrameLayout Manu_genesis;
    private FrameLayout Manu_alfaromeo;
    private FrameLayout Manu_audi;
    private FrameLayout Manu_benz;
    private FrameLayout Manu_bmw;
    private FrameLayout Manu_cadillac;
    private FrameLayout Manu_chrysler;
    private FrameLayout Manu_citroen;
    private FrameLayout Manu_dodge;
    private FrameLayout Manu_ferrari;
    private FrameLayout Manu_fiat;
    private FrameLayout Manu_ford;
    private FrameLayout Manu_honda;
    private FrameLayout Manu_hummer;
    private FrameLayout Manu_infiniti;
    private FrameLayout Manu_jaguar;
    private FrameLayout Manu_jeep;
    private FrameLayout Manu_landrover;
    private FrameLayout Manu_lexus;
    private FrameLayout Manu_lincoln;
    private FrameLayout Manu_maserati;
    private FrameLayout Manu_mazda;
    private FrameLayout Manu_mini;
    private FrameLayout Manu_mitsubishi;
    private FrameLayout Manu_nissan;
    private FrameLayout Manu_peugeot;
    private FrameLayout Manu_porsche;
    private FrameLayout Manu_subaru;
    private FrameLayout Manu_suzuki;
    private FrameLayout Manu_tesla;
    private FrameLayout Manu_toyota;
    private FrameLayout Manu_volkswagen;
    private FrameLayout Manu_volvo;

    private Button SelectManuFacturer_Close_Button; //닫기 Button 객체 생성

    //Fragment 객체 생성
    private HomeFragment homeFragment;
    private SelectCarTypeFragment selectCarTypeFragment;

    public SelectManufacturerFragment() {
        //SelectManufacturerFragment의 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_manufacturer, container, false); //View 제작

        //View 객체 연결
        Manu_hyundai = (FrameLayout) view.findViewById(R.id.Manu_hyundai);
        Manu_kia = (FrameLayout) view.findViewById(R.id.Manu_kia);
        Manu_chevrolet = (FrameLayout) view.findViewById(R.id.Manu_chevrolet);
        Manu_renault = (FrameLayout) view.findViewById(R.id.Manu_renalut);
        Manu_ssangyong = (FrameLayout) view.findViewById(R.id.Manu_ssangyong);
        Manu_genesis = (FrameLayout) view.findViewById(R.id.Manu_genesis);
        Manu_alfaromeo = (FrameLayout) view.findViewById(R.id.Manu_alfaromeo);
        Manu_audi = (FrameLayout) view.findViewById(R.id.Manu_audi);
        Manu_benz = (FrameLayout) view.findViewById(R.id.Manu_benz);
        Manu_bmw = (FrameLayout) view.findViewById(R.id.Manu_bmw);
        Manu_cadillac = (FrameLayout) view.findViewById(R.id.Manu_cadillac);
        Manu_chrysler = (FrameLayout) view.findViewById(R.id.Manu_chrysler);
        Manu_citroen = (FrameLayout) view.findViewById(R.id.Manu_citroen);
        Manu_dodge = (FrameLayout) view.findViewById(R.id.Manu_dodge);
        Manu_ferrari = (FrameLayout) view.findViewById(R.id.Manu_ferrari);
        Manu_fiat = (FrameLayout) view.findViewById(R.id.Manu_fiat);
        Manu_ford = (FrameLayout) view.findViewById(R.id.Manu_ford);
        Manu_honda = (FrameLayout) view.findViewById(R.id.Manu_honda);
        Manu_hummer = (FrameLayout) view.findViewById(R.id.Manu_hummer);
        Manu_infiniti = (FrameLayout) view.findViewById(R.id.Manu_infiniti);
        Manu_jaguar = (FrameLayout) view.findViewById(R.id.Manu_jaguar);
        Manu_jeep = (FrameLayout) view.findViewById(R.id.Manu_jeep);
        Manu_landrover = (FrameLayout) view.findViewById(R.id.Manu_landrover);
        Manu_lexus = (FrameLayout) view.findViewById(R.id.Manu_lexus);
        Manu_lincoln = (FrameLayout) view.findViewById(R.id.Manu_lincoln);
        Manu_maserati = (FrameLayout) view.findViewById(R.id.Manu_maserati);
        Manu_mazda = (FrameLayout) view.findViewById(R.id.Manu_mazda);
        Manu_mini = (FrameLayout) view.findViewById(R.id.Manu_mini);
        Manu_mitsubishi = (FrameLayout) view.findViewById(R.id.Manu_mitsubishi);
        Manu_nissan = (FrameLayout) view.findViewById(R.id.Manu_nissan);
        Manu_peugeot = (FrameLayout) view.findViewById(R.id.Manu_peugeot);
        Manu_porsche = (FrameLayout) view.findViewById(R.id.Manu_porsche);
        Manu_subaru = (FrameLayout) view.findViewById(R.id.Manu_subaru);
        Manu_suzuki = (FrameLayout) view.findViewById(R.id.Manu_suzuki);
        Manu_tesla = (FrameLayout) view.findViewById(R.id.Manu_tesla);
        Manu_toyota = (FrameLayout) view.findViewById(R.id.Manu_toyota);
        Manu_volkswagen = (FrameLayout) view.findViewById(R.id.Manu_volkswagen);
        Manu_volvo = (FrameLayout) view.findViewById(R.id.Manu_volvo);

        SelectManuFacturer_Close_Button = (Button) view.findViewById(R.id.SelectManuFacturer_Close_Button);

        //닫기 버튼 OnClickListener
        SelectManuFacturer_Close_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment = ((MainActivity)getActivity()).fragmentMaintenance.getHomeFragment();
                ((MainActivity)getActivity()).replaceFragment(homeFragment);
            }
        });

        //버튼 OnClickListener 정의
        View.OnClickListener btn_onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String manu = null;

                switch (v.getId()) {
                    case R.id.Manu_hyundai:
                        manu = "hyundai";
                        break;
                    case R.id.Manu_kia:
                        manu = "kia";
                        break;
                    case R.id.Manu_chevrolet:
                        manu = "chevrolet";
                        break;
                    case R.id.Manu_renalut:
                        manu = "renault";
                        break;
                    case R.id.Manu_ssangyong:
                        manu = "ssangyong";
                        break;
                    case R.id.Manu_genesis:
                        manu = "genesis";
                        break;
                    case R.id.Manu_alfaromeo:
                        manu = "alfaromeo";
                        break;
                    case R.id.Manu_audi:
                        manu = "audi";
                        break;
                    case R.id.Manu_benz:
                        manu = "benz";
                        break;
                    case R.id.Manu_bmw:
                        manu = "bmw";
                        break;
                    case R.id.Manu_cadillac:
                        manu = "cadillac";
                        break;
                    case R.id.Manu_chrysler:
                        manu = "chrysler";
                        break;
                    case R.id.Manu_citroen:
                        manu = "citroen";
                        break;
                    case R.id.Manu_dodge:
                        manu = "dodge";
                        break;
                    case R.id.Manu_ferrari:
                        manu = "ferrari";
                        break;
                    case R.id.Manu_fiat:
                        manu = "fiat";
                        break;
                    case R.id.Manu_ford:
                        manu = "ford";
                        break;
                    case R.id.Manu_honda:
                        manu = "honda";
                        break;
                    case R.id.Manu_hummer:
                        manu = "hummer";
                        break;
                    case R.id.Manu_infiniti:
                        manu = "infiniti";
                        break;
                    case R.id.Manu_jaguar:
                        manu = "jaguar";
                        break;
                    case R.id.Manu_jeep:
                        manu = "jeep";
                        break;
                    case R.id.Manu_landrover:
                        manu = "landrover";
                        break;
                    case R.id.Manu_lexus:
                        manu = "lexus";
                        break;
                    case R.id.Manu_lincoln:
                        manu = "lincoln";
                        break;
                    case R.id.Manu_maserati:
                        manu = "maserati";
                        break;
                    case R.id.Manu_mazda:
                        manu = "mazda";
                        break;
                    case R.id.Manu_mini:
                        manu = "mini";
                        break;
                    case R.id.Manu_mitsubishi:
                        manu = "mitsubishi";
                        break;
                    case R.id.Manu_nissan:
                        manu = "nissan";
                        break;
                    case R.id.Manu_peugeot:
                        manu = "peugeot";
                        break;
                    case R.id.Manu_porsche:
                        manu = "porsche";
                        break;
                    case R.id.Manu_subaru:
                        manu = "subaru";
                        break;
                    case R.id.Manu_suzuki:
                        manu = "suzuki";
                        break;
                    case R.id.Manu_tesla:
                        manu = "tesla";
                        break;
                    case R.id.Manu_toyota:
                        manu = "toyota";
                        break;
                    case R.id.Manu_volkswagen:
                        manu = "volkswagen";
                        break;
                    case R.id.Manu_volvo:
                        manu = "volvo";
                        break;
                }

                selectCarTypeFragment = new SelectCarTypeFragment();
                selectCarTypeFragment.setManufacturer(manu);
                ((MainActivity)getActivity()).replaceFragment(selectCarTypeFragment);
            }
        };

        //버튼 OnClickListener Set
        Manu_hyundai.setOnClickListener(btn_onClickListener);
        Manu_kia.setOnClickListener(btn_onClickListener);
        Manu_chevrolet.setOnClickListener(btn_onClickListener);
        Manu_renault.setOnClickListener(btn_onClickListener);
        Manu_ssangyong.setOnClickListener(btn_onClickListener);
        Manu_genesis.setOnClickListener(btn_onClickListener);
        Manu_alfaromeo.setOnClickListener(btn_onClickListener);
        Manu_audi.setOnClickListener(btn_onClickListener);
        Manu_benz.setOnClickListener(btn_onClickListener);
        Manu_bmw.setOnClickListener(btn_onClickListener);
        Manu_cadillac.setOnClickListener(btn_onClickListener);
        Manu_chrysler.setOnClickListener(btn_onClickListener);
        Manu_citroen.setOnClickListener(btn_onClickListener);
        Manu_dodge.setOnClickListener(btn_onClickListener);
        Manu_ferrari.setOnClickListener(btn_onClickListener);
        Manu_fiat.setOnClickListener(btn_onClickListener);
        Manu_ford.setOnClickListener(btn_onClickListener);
        Manu_honda.setOnClickListener(btn_onClickListener);
        Manu_hummer.setOnClickListener(btn_onClickListener);
        Manu_infiniti.setOnClickListener(btn_onClickListener);
        Manu_jaguar.setOnClickListener(btn_onClickListener);
        Manu_jeep.setOnClickListener(btn_onClickListener);
        Manu_landrover.setOnClickListener(btn_onClickListener);
        Manu_lexus.setOnClickListener(btn_onClickListener);
        Manu_lincoln.setOnClickListener(btn_onClickListener);
        Manu_maserati.setOnClickListener(btn_onClickListener);
        Manu_mazda.setOnClickListener(btn_onClickListener);
        Manu_mini.setOnClickListener(btn_onClickListener);
        Manu_mitsubishi.setOnClickListener(btn_onClickListener);
        Manu_nissan.setOnClickListener(btn_onClickListener);
        Manu_peugeot.setOnClickListener(btn_onClickListener);
        Manu_porsche.setOnClickListener(btn_onClickListener);
        Manu_subaru.setOnClickListener(btn_onClickListener);
        Manu_suzuki.setOnClickListener(btn_onClickListener);
        Manu_tesla.setOnClickListener(btn_onClickListener);
        Manu_toyota.setOnClickListener(btn_onClickListener);
        Manu_volkswagen.setOnClickListener(btn_onClickListener);
        Manu_volvo.setOnClickListener(btn_onClickListener);

        ((MainActivity)getActivity()).customProgressDialog.dismiss();

        return view;
    }
}
