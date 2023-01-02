package com.inspier.carstyle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//제조사 RecyclerAdapter Class
public class ManufacturerRecyclerAdapter extends RecyclerView.Adapter<ManufacturerRecyclerAdapter.ManufacturerItemViewHolder> {

    //adapter에 들어가는 ArrayList
    private ArrayList<ManufacturerData> manufacturerListData = new ArrayList<>();

    @NonNull
    @Override
    public ManufacturerRecyclerAdapter.ManufacturerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //LayoutInflater를 이용하여 manufacturer_item.xml을 inflate
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manufacturer_item, parent, false);
        return new ManufacturerItemViewHolder(view);
    }

    //Item을 점진적으로 보여주는(Bind 하는) 함수
    @Override
    public void onBindViewHolder(@NonNull ManufacturerItemViewHolder holder, int position) {
        holder.onBind(manufacturerListData.get(position));
    }

    //RecyclerView에 보여지는 Item의 총 개수를 반환하는 함수
    @Override
    public int getItemCount() {
        return manufacturerListData.size();
    }

    //외부에서 Item을 추가시키는 함수
    void addItem(ManufacturerData data) {
        manufacturerListData.add(data);
    }

    //ManufacturerRecyclerView의 ViewHolder
    class ManufacturerItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView ManufacturerImageView;
        private TextView ManufacturerTextView;

        ManufacturerItemViewHolder(View itemView) {
            super(itemView);

            ManufacturerImageView = (ImageView) itemView.findViewById(R.id.Manufacturer_ImageView);
            ManufacturerTextView = (TextView) itemView.findViewById(R.id.Manufacturer_TextView);
        }

        void onBind(ManufacturerData data) {
            ManufacturerImageView.setImageResource(data.getResId());
            ManufacturerTextView.setText(data.getManufacturerName());
        }
    }
}
