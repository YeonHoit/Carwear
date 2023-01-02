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

//알림 목록 RecyclerAdapter Class
public class AlertRecyclerAdapter extends RecyclerView.Adapter<AlertRecyclerAdapter.AlertItemViewHolder> {

    //adapter에 들어가는 ArrayList
    private ArrayList<AlertData> AlertListData = new ArrayList<>();

    //Custom OnClickListener 인터페이스 정의
    public interface OnAlertItemClickListener {
        void onAlertItemClick(View v, int position, int target_num);
    }

    //OnClickListener 객체 생성
    private OnAlertItemClickListener mOnAlertItemClickListener = null;

    //OnAlertItemClickListener 객체 참조를 어댑터에 전달하는 함수
    public void setOnAlertItemClickListener(OnAlertItemClickListener mOnAlertItemClickListener) {
        this.mOnAlertItemClickListener = mOnAlertItemClickListener;
    }

    @NonNull
    @Override
    public AlertRecyclerAdapter.AlertItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용하여 alert_item.xml을 inflate
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_item, parent, false);
        return new AlertRecyclerAdapter.AlertItemViewHolder(view);
    }

    //Item을 점진적으로 보여주는(Bind 하는) 함수
    @Override
    public void onBindViewHolder(@NonNull AlertRecyclerAdapter.AlertItemViewHolder holder, int position) {
        holder.onBind(AlertListData.get(position));
    }

    //RecyclerView에 보여지는 Item의 총 개수를 반환하는 함수
    @Override
    public int getItemCount() {
        return AlertListData.size();
    }

    //외부에서 Item을 추가시키는 함수
    void addItem(AlertData data) {
        AlertListData.add(data);
    }

    //AlertRecyclerView의 ViewHolder
    class AlertItemViewHolder extends RecyclerView.ViewHolder {

        private TextView Alert_Title;
        private TextView Alert_Contents;
        private TextView Alert_Date;
        private ImageView Alert_ImageView;

        AlertItemViewHolder(View itemView) {
            super(itemView);

            Alert_Title = (TextView) itemView.findViewById(R.id.Alert_Title);
            Alert_Contents = (TextView) itemView.findViewById(R.id.Alert_Contents);
            Alert_Date = (TextView) itemView.findViewById(R.id.Alert_Date);
            Alert_ImageView = (ImageView) itemView.findViewById(R.id.Alert_ImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mOnAlertItemClickListener != null) {
                            mOnAlertItemClickListener.onAlertItemClick(v, pos, AlertListData.get(pos).getAlert_Target_Num());
                        }
                    }
                }
            });
        }

        void onBind(AlertData data) {
            Alert_Title.setText(data.getAlert_Title());
            Alert_Contents.setText(data.getAlert_Contents());
            Alert_Date.setText(new DateCalculate().cal(data.getAlert_Date()));

            if (data.getAlert_Type().equals("Notice")) {
                Alert_ImageView.setImageResource(R.drawable.notice_alert);
            }
            else if (data.getAlert_Type().equals("Like")) {
                Alert_ImageView.setImageResource(R.drawable.heart_alert);
            }
            else if (data.getAlert_Type().equals("Comment")) {
                Alert_ImageView.setImageResource(R.drawable.comment_alert);
            }
            else if (data.getAlert_Type().equals("Tag")) {
                Alert_ImageView.setImageResource(R.drawable.tag_alert);
            }
        }
    }
}
