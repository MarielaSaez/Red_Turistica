package com.example.appchat.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appchat.model.Post;
import com.example.appchat.providers.PostProvider;

public class UploadViewModel extends ViewModel {
    private final MutableLiveData<Boolean> postSuccess = new MutableLiveData<>();
    private final PostProvider postProvider;

    public LiveData<Boolean> getPostSuccess() {
        return postSuccess;
    }
    public UploadViewModel() {
        postProvider = new PostProvider();
    }
    public void publicar(Post post) {
        postProvider.addPost(post)
                .observeForever(result -> {
                    if ("Post publicado".equals(result)) {
                        postSuccess.setValue(true);
                    } else {
                        postSuccess.setValue(false);
                    }
                });
    }
}
