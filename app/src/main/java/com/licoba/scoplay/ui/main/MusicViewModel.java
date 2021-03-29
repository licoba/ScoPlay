package com.licoba.scoplay.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.licoba.scoplay.R;
import com.licoba.scoplay.model.MusicModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MusicViewModel extends ViewModel {
    private MutableLiveData<List<MusicModel>> liveData;

    public LiveData<List<MusicModel>> getMusics() {
        if (liveData == null) {
            liveData = new MutableLiveData<List<MusicModel>>();
            loadUsers();
        }
        return liveData;
    }

    public void setMusics(List<MusicModel> newData) {
        if (newData == null||newData.size()==0) {
            return;
        }
       this.liveData.postValue(newData);
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
        listRaw();
    }


    public void listRaw() {
        Field[] fields = R.raw.class.getFields();
        List<MusicModel> list = new ArrayList<>();
        for (int count = 0; count < fields.length; count++) {
            Log.e("Raw Asset", fields[count].getName());
            MusicModel musicModel = new MusicModel(fields[count].getName());
            list.add(musicModel);
        }

        liveData.postValue(list);
    }

}
