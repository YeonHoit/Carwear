package com.inspier.carstyle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//커뮤니티 글 목록 RecyclerAdapter Class
public class CommunityPostRecyclerAdapter extends RecyclerView.Adapter<CommunityPostRecyclerAdapter.CommunityPostItemViewHolder> {

    //adapter에 들어가는 ArrayList
    private ArrayList<CommunityPostData> CommunityPostListData = new ArrayList<>();

    //Custom OnClickListener 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    public interface OnDotsClickListener {
        void onDotsClick(View v, int position, CommunityPostData data);
    }

    //OnClickListener 객체 생성
    private OnItemClickListener mListener = null;
    private OnDotsClickListener mDotsListener = null;

    //OnItemClickListener 객체 참조를 어댑터에 전달하는 함수
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    //OnDotsClickListener 객체 참조를 어댑터에 전달하는 함수
    public void setOnDotsClickListener(OnDotsClickListener listener) {
        this.mDotsListener = listener;
    }

    @NonNull
    @Override
    public CommunityPostRecyclerAdapter.CommunityPostItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //LayoutInflater를 이용하여 community_post_item.xml을 inflate
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_post_item, parent, false);
        return new CommunityPostItemViewHolder(view);
    }

    //Item을 점진적으로 보여주는(Bind 하는) 함수
    @Override
    public void onBindViewHolder(@NonNull CommunityPostRecyclerAdapter.CommunityPostItemViewHolder holder, int position) {
        holder.onBind(CommunityPostListData.get(position));
    }

    //RecyclerView에 보여지는 Item의 총 개수를 반환하는 함수
    @Override
    public int getItemCount() {
        return CommunityPostListData.size();
    }

    //외부에서 Item을 추가시키는 함수
    void addItem(CommunityPostData data) {
        CommunityPostListData.add(data);
    }

    //CommunityPostRecyclerView의 ViewHolder
    class CommunityPostItemViewHolder extends RecyclerView.ViewHolder {

        private TextView Community_Post_Board_Name;
        private TextView Community_Post_Title;
        private TextView Community_Post_Contents;
        private ImageView Community_Post_ImageView;
        private TextView Community_Post_NickName;
        private TextView Community_Post_WriteTime;
        private TextView Community_Post_Views;
        private FrameLayout Community_Post_Dots;

        CommunityPostItemViewHolder(View itemView) {
            super(itemView);

            Community_Post_Board_Name = itemView.findViewById(R.id.Community_Post_Board_Name);
            Community_Post_Title = itemView.findViewById(R.id.Community_Post_Title);
            Community_Post_Contents = itemView.findViewById(R.id.Community_Post_Contents);
            Community_Post_ImageView = itemView.findViewById(R.id.Community_Post_ImageView);
            Community_Post_NickName = itemView.findViewById(R.id.Community_Post_NickName);
            Community_Post_WriteTime = itemView.findViewById(R.id.Community_Post_WriteTime);
            Community_Post_Views = itemView.findViewById(R.id.Community_Post_Views);
            Community_Post_Dots = itemView.findViewById(R.id.Community_Post_Dots);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("RecyclerAdapter", "" + getAdapterPosition());
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });

            Community_Post_Dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("RecyclerAdapter", "Dots!!!" + getAdapterPosition());
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(mDotsListener != null) {
                            mDotsListener.onDotsClick(v, pos, CommunityPostListData.get(pos));
                        }
                    }
                }
            });
        }

        void onBind(CommunityPostData data) {
            Community_Post_Board_Name.setText(data.getCommunity_Post_Board_Name());
            Community_Post_Title.setText(data.getCommunity_Post_Title());
            Community_Post_Contents.setText(data.getCommunity_Post_Contents());
            if(data.getCommunity_Post_Photo().equals("")) {
                Community_Post_ImageView.setVisibility(View.GONE);
            }
            else {
                //Community_Post_ImageView.setImageResource(data.getCommunity_Post_ImageView_resId());
                try {
                    PhotoGetTask task = new PhotoGetTask();
                    String [] array = data.getCommunity_Post_Photo().split("#");
                    Community_Post_ImageView.setImageBitmap(task.execute(array[0]).get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Community_Post_NickName.setText(data.getCommunity_Post_NickName());
            Community_Post_WriteTime.setText(new DateCalculate().cal(data.getCommunity_Post_WriteTime()));
            Community_Post_Views.setText(data.getCommunity_Post_Views());
        }
    }
}
