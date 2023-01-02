package com.inspier.carstyle;

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

//커뮤니티 댓글 RecyclerAdapter Class
public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.CommentsItemViewHolder> {

    //adapter에 들어가는 ArrayList
    public ArrayList<CommentsData> CommentsListData = new ArrayList<>();

    //Custom OnClickListener 인터페이스 정의
    public interface OnCommentsDotsClickListener {
        void onCommentsDotsClick(View v, int position, CommentsData data);
    }

    //OnClickListener 객체 생성
    private OnCommentsDotsClickListener mCommentsDotsListener = null;

    //OnCommentsDotsClickListener 객체 참조를 어댑터에 전달하는 함수
    public void setOnCommentsDotsClickListener(OnCommentsDotsClickListener mCommentsDotsListener) {
        this.mCommentsDotsListener = mCommentsDotsListener;
    }

    @NonNull
    @Override
    public CommentsRecyclerAdapter.CommentsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //LayoutInflater를 이용하여 community_post_item.xml을 inflate
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentsRecyclerAdapter.CommentsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsRecyclerAdapter.CommentsItemViewHolder holder, int position) {
        holder.onBind(CommentsListData.get(position));
    }

    //RecyclerView에 보여지는 Item의 총 개수를 반환하는 함수
    @Override
    public int getItemCount() {
        return CommentsListData.size();
    }

    //외부에서 Item을 추가시키는 함수
    void addItem(CommentsData data) {
        CommentsListData.add(data);
    }

    //CommunityPostRecyclerView의 ViewHolder
    class CommentsItemViewHolder extends RecyclerView.ViewHolder {

        private TextView Community_Comments_NickName;
        private TextView Community_Comments_DateTime;
        private TextView Community_Comments_Comment;
        private FrameLayout Community_Comments_Dots;

        CommentsItemViewHolder(View itemView) {
            super(itemView);

            Community_Comments_NickName = (TextView) itemView.findViewById(R.id.Community_Comments_NickName);
            Community_Comments_DateTime = (TextView) itemView.findViewById(R.id.Community_Comments_DateTime);
            Community_Comments_Comment = (TextView) itemView.findViewById(R.id.Community_Comments_Comment);
            Community_Comments_Dots = (FrameLayout) itemView.findViewById(R.id.Community_Comments_Dots);

            Community_Comments_Dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mCommentsDotsListener != null) {
                            mCommentsDotsListener.onCommentsDotsClick(v, pos, CommentsListData.get(pos));
                        }
                    }
                }
            });
        }

        void onBind(CommentsData data) {
            Community_Comments_NickName.setText(data.getCommunity_Comments_NickName());
            Community_Comments_DateTime.setText(new DateCalculate().cal(data.getCommunity_Comments_DateTime()));
            Community_Comments_Comment.setText(data.getCommunity_Comments_Comment());
        }
    }
}
