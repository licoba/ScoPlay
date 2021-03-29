package com.licoba.scoplay.ui.main;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.licoba.scoplay.R;
import com.licoba.scoplay.adapter.MusicAdapter;
import com.licoba.scoplay.model.MusicModel;

import java.util.List;

public class MainFragment extends Fragment {

    private MusicViewModel musicViewModel;
    final String TAG = "MainFragment";
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private MediaPlayer mediaPlayer;
    private Thread playThread;

    public static MainFragment newInstance(int index) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        musicViewModel = new ViewModelProvider(this).get(MusicViewModel.class);

        musicAdapter = new MusicAdapter();
        musicAdapter.setChildClickListener(new MusicAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(MusicModel item, int position) {
                Log.e(TAG, "child点击：" + position + " " + item);
                playOrPause(item, position);
            }
        });
        musicViewModel.getMusics().observe(this, musics -> {
            // update UI
            Log.e(TAG, "数据加载完成，musics：" + musics);
            updateUI(musics);
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = root.findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return root;
    }


    public void updateUI(List<MusicModel> musicModels) {
        musicAdapter.setLocalDataSet(musicModels);
        recyclerView.setAdapter(musicAdapter);
        musicAdapter.notifyDataSetChanged();

    }


    public void playOrPause(MusicModel item, int position) {
        startPlayMute(item, position);
    }


    // 播放静音音频
    private void startPlayMute(MusicModel item, int position) {



        Thread playThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        Snackbar.make(getView(), "请等待播放完成", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        return;
                    }
                    Resources res =  getContext().getResources();
                    int soundId = res.getIdentifier(item.getFilename(), "raw", getContext().getPackageName());
                    mediaPlayer = MediaPlayer.create(getContext(), soundId);
                    mediaPlayer.start();
                    updateItem(position, true);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (mediaPlayer == null) {
                                return;
                            }
                            Log.e(TAG, "播放完成");
                            updateItem(position, false);
                        }
                    });
                } catch (Exception e) {
                }
            }
        });
        playThread.start();
    }

    public void updateItem(int position, boolean isPlaying) {
        List<MusicModel> musicModels = musicViewModel.getMusics().getValue();
        musicModels.get(position).setPlaying(isPlaying);
        musicViewModel.setMusics(musicModels);
    }

}
