package com.example.appchat.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appchat.model.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PostViewModel extends ViewModel {

    private final MutableLiveData<Boolean> postSuccess = new MutableLiveData<>();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public LiveData<Boolean> getPostSuccess() {
        return postSuccess;
    }

    public void publishPost(Post post) {
        DatabaseReference postRef = database.getReference("posts").push();
        postRef.setValue(post).addOnCompleteListener(task -> postSuccess.setValue(task.isSuccessful()));
    }
}

