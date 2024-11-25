package com.example.appchat.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import com.example.appchat.model.Post;
import com.example.appchat.providers.PostProvider;
import java.util.List;

public class PostViewModel extends ViewModel {
    private final MutableLiveData<String> postSuccess = new MutableLiveData<>();
    private final PostProvider postProvider;
    private LiveData<List<Post>> posts;

    public PostViewModel() {
        posts = new MutableLiveData<>();
        postProvider = new PostProvider();
    }

    public LiveData<String> getPostSuccess() {
        return postSuccess;
    }

    public void publicar(Post post) {
        postProvider.addPost(post)
                .observeForever(result -> {
                    postSuccess.setValue(result);
                });
    }

    public LiveData<List<Post>> getPosts() {
        posts = postProvider.getPostsByCurrentUser();
        return posts;
    }
}


