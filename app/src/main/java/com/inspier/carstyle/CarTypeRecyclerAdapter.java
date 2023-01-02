package com.inspier.carstyle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CarTypeRecyclerAdapter extends RecyclerView.Adapter<CarTypeRecyclerAdapter.CarTypeItemViewHolder> {

    //adapter에 들어가는 ArrayList
    public ArrayList<String> CarTypeListData = new ArrayList<>();

    //Custom OnClickListener 인터페이스 정의
    public interface OnCarTypeClickListener {
        void onCarTypeItemClick(View v, int position);
    }

    //Custom OnClickListener 객체 생성
    private OnCarTypeClickListener mOnCarTypeClickListener = null;

    //Custom OnClickListener Setting
    public void setOnCarTypeClickListener(OnCarTypeClickListener mOnCarTypeClickListener) {
        this.mOnCarTypeClickListener = mOnCarTypeClickListener;
    }

    @NonNull
    @Override
    public CarTypeRecyclerAdapter.CarTypeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용하여 cartype_item.xml을 inflate
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartype_item, parent, false);
        return new CarTypeRecyclerAdapter.CarTypeItemViewHolder(view);
    }

    //Item을 점진적으로 보여주는(Bind 하는) 함수
    @Override
    public void onBindViewHolder(@NonNull CarTypeRecyclerAdapter.CarTypeItemViewHolder holder, int position) {
        holder.onBind(CarTypeListData.get(position));
    }

    //RecyclerView에 보여지는 Item의 총 개수를 반환하는 함수
    @Override
    public int getItemCount() {
        return CarTypeListData.size();
    }

    //외부에서 Item을 추가시키는 함수
    void addItem(String data) {
        CarTypeListData.add(data);
    }

    class CarTypeItemViewHolder extends RecyclerView.ViewHolder {

        private TextView CarType_TextView;

        CarTypeItemViewHolder(View itemView) {
            super(itemView);

            CarType_TextView = (TextView) itemView.findViewById(R.id.CarType_TextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mOnCarTypeClickListener != null) {
                            mOnCarTypeClickListener.onCarTypeItemClick(v, pos);
                        }
                    }
                }
            });
        }

        void onBind(String data) {
            CarType_TextView.setText(data);
        }
    }
}
