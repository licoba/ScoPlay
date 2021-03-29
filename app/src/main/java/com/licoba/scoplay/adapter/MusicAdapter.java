package com.licoba.scoplay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.licoba.scoplay.R;
import com.licoba.scoplay.model.MusicModel;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {


    List<MusicModel> localDataSet;
    private OnItemClickListener listener;
    private OnItemChildClickListener childClickListener;


    public MusicAdapter() {

    }

    public void setLocalDataSet(List<MusicModel> localDataSet) {
        this.localDataSet = localDataSet;
    }

    public MusicAdapter(List<MusicModel> dataSet) {
        localDataSet = dataSet;
    }

    public MusicAdapter(List<MusicModel> dataSet, OnItemChildClickListener listener) {
        localDataSet = dataSet;
        this.childClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(MusicModel item);
    }


    public interface OnItemChildClickListener {
        void onItemChildClick(MusicModel item, int index);
    }

    public void setChildClickListener(OnItemChildClickListener childClickListener) {
        this.childClickListener = childClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_play, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getPlayButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childClickListener != null)
                    childClickListener.onItemChildClick(localDataSet.get(position), position);
            }
        });
        holder.getTextView().setText(localDataSet.get(position).getFilename());
        holder.getPlayButton().setImageResource(localDataSet.get(position).isPlaying() ? R.mipmap.stop : R.mipmap.play);
    }


    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageButton playButton;


        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textView);
            playButton = view.findViewById(R.id.playButton);
        }

        public TextView getTextView() {
            return textView;
        }


        public ImageButton getPlayButton() {
            return playButton;
        }
    }

}
