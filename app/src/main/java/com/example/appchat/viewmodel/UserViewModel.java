package com.example.appchat.viewmodel;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.appchat.model.User;
import com.example.appchat.providers.AuthProvider;
import com.example.appchat.providers.UserProvider;
import androidx.lifecycle.Observer;


public class UserViewModel extends ViewModel {
    private final AuthProvider authProvider;
    private final UserProvider userProvider;
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<String> operationStatus = new MutableLiveData<>();

    public UserViewModel() {
        authProvider = new AuthProvider();
        userProvider = new UserProvider();
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getOperationStatus() {
        return operationStatus;
    }

    public void createUser(User user, LifecycleOwner lifecycleOwner) {
        authProvider.signUp(user.getEmail(), user.getPassword()).observe(lifecycleOwner, uid -> {
            if (uid != null) {
                user.setId(uid);
                userProvider.createUser(user).observe(lifecycleOwner, status -> {
                    if (status != null) {
                        operationStatus.setValue(status);

                    } else {
                        operationStatus.setValue("Error al crear usuario en Firestore");
                    }
                });
            } else {
                operationStatus.setValue("Error al registrar usuario en FirebaseAuth");
            }
        });
    }


    public void updateUser(User user, LifecycleOwner lifecycleOwner) {
        LiveData<String> result = userProvider.updateUser(user);
        result.observe(lifecycleOwner, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                operationStatus.setValue(result.getValue());
            }
        });
    }

    public void deleteUser(String userId, LifecycleOwner lifecycleOwner) {
        LiveData<String> result = userProvider.deleteUser(userId);
        result.observe(lifecycleOwner, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                operationStatus.setValue(status);
            }
        });
    }

    public void getUser(String email, LifecycleOwner lifecycleOwner) {
        LiveData<User> user = userProvider.getUser(email);
        user.observe(lifecycleOwner, new Observer<User>() {
            @Override
            public void onChanged(User foundUser) {
                if (foundUser != null) {
                    Log.d("User Info", "ID: " + foundUser.getId() + ", Username: " + foundUser.getUsername());
                    currentUser.setValue(foundUser);
                } else {
                    operationStatus.setValue("No encontrado");
                }
            }
        });
    }

}
