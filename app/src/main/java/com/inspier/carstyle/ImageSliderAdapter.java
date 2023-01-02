package com.inspier.carstyle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.MyViewHolder> {

    private ArrayList<String> sliderImage;

    public ImageSliderAdapter(ArrayList<String> sliderImage) {
        this.sliderImage = sliderImage;
    }

    @NonNull
    @Override
    public ImageSliderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderAdapter.MyViewHolder holder, int position) {
        holder.bindSliderImage(sliderImage.get(position % sliderImage.size()));
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView slider_imageview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            slider_imageview = itemView.findViewById(R.id.slider_imageview);
        }

        public void bindSliderImage(String imageURL) {
            try {
                PhotoGetTask task = new PhotoGetTask();
                slider_imageview.setImageBitmap(task.execute(imageURL).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
