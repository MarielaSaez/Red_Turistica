package com.example.appchat.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ImageViewModel extends ViewModel {
    private final MutableLiveData<List<String>> imagenesUrls = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<String>> getImagenesUrls() {
        return imagenesUrls;
    }

    public void agregarImagenUrl(String url) {
        List<String> currentUrls = new ArrayList<>(imagenesUrls.getValue());
        currentUrls.add(url);
        imagenesUrls.setValue(currentUrls);
    }
}