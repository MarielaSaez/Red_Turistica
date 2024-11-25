package com.example.appchat.viewmodel;
import static android.content.Intent.getIntent;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.appchat.providers.AuthProvider;
import com.example.appchat.view.HomeActivity;

public class MainViewModel extends ViewModel {
    public final AuthProvider authProvider;
    public MainViewModel(){
        authProvider=new AuthProvider();
    }

    public LiveData<String> login(String email, String password) {
        MutableLiveData<String> loginResult = new MutableLiveData<>();
        authProvider.signIn(email, password).observeForever(userId -> {
            loginResult.setValue(userId);
        });
        return loginResult;
    }

    public LiveData<Boolean> verificarSesionActiva(){
        MutableLiveData<Boolean> si=new MutableLiveData<>();

        if (authProvider.getCurrentUserID() != null) {

             si.setValue(true);
        } else {
            si.setValue(false);
        }
        return si;
    }
}