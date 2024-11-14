package com.example.appchat.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public UserViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (UserViewModel.class.isAssignableFrom(modelClass)) {
            // Crear la instancia sin forzar el cast
            return (T) new UserViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

