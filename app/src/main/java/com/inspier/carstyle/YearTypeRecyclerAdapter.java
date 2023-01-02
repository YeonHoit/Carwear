package com.inspier.carstyle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class YearTypeRecyclerAdapter extends RecyclerView.Adapter<YearTypeRecyclerAdapter.YearTypeItemViewHolder> {

    //adapter에 들어가는 ArrayList
    public ArrayList<String> YearTypeListData = new ArrayList<>();

    //Custom OnClickListener 인터페이스 정의
    public interface OnYearTypeClickListener {
        void onYearTypeItemClick(View v, int position);
    }

    //Custom OnClickListener 객체 생성
    private YearTypeRecyclerAdapter.OnYearTypeClickListener mOnYearTypeClickListener = null;

    //Custom OnClickListener Setting
    public void setOnYearTypeClickListener(YearTypeRecyclerAdapter.OnYearTypeClickListener mOnYearTypeClickListener) {
        this.mOnYearTypeClickListener = mOnYearTypeClickListener;
    }

    @NonNull
    @Override
    public YearTypeRecyclerAdapter.YearTypeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용하여 cartype_item.xml을 inflate
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartype_item, parent, false);
        return new YearTypeRecyclerAdapter.YearTypeItemViewHolder(view);
    }

    //Item을 점진적으로 보여주는(Bind 하는) 함수
    @Override
    public void onBindViewHolder(@NonNull YearTypeRecyclerAdapter.YearTypeItemViewHolder holder, int position) {
        holder.onBind(YearTypeListData.get(position));
    }

    //RecyclerView에 보여지는 Item의 총 개수를 반환하는 함수
    @Override
    public int getItemCount() {
        return YearTypeListData.size();
    }

    //외부에서 Item을 추가시키는 함수
    void addItem(String data) {
        YearTypeListData.add(data);
    }

    class YearTypeItemViewHolder extends RecyclerView.ViewHolder {

        private TextView YearType_TextView;

        YearTypeItemViewHolder(View itemView) {
            super(itemView);

            YearType_TextView = (TextView) itemView.findViewById(R.id.CarType_TextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mOnYearTypeClickListener != null) {
                            mOnYearTypeClickListener.onYearTypeItemClick(v, pos);
                        }
                    }
                }
            });
        }

        void onBind(String data) {
            YearType_TextView.setText(data);
        }
    }
}
