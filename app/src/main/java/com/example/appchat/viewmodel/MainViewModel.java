package com.example.appchat.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.appchat.providers.AuthProvider;


public class MainViewModel extends ViewModel {
    public final AuthProvider authProvider;

    public MainViewModel(){
        authProvider=new AuthProvider();
    }

    public LiveData<Boolean> login(String email, String password) {
        MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
        authProvider.signIn(email, password).observeForever(userId -> {
            loginResult.setValue(userId != null);
        });
        return loginResult;
    }

}