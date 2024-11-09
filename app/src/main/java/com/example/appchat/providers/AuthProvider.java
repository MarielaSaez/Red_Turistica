package com.example.appchat.providers;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
public class AuthProvider {
    private FirebaseAuth mAuth;
    public AuthProvider() {
        mAuth = FirebaseAuth.getInstance();
    }
    private LiveData<String> helperAuth(Task<AuthResult> task) {
        MutableLiveData<String> authResult = new MutableLiveData<>();
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                    authResult.setValue(mAuth.getCurrentUser().getUid());
                } else {
                    authResult.setValue(null);
                }
            }
        });
        return authResult;
    }
    // Inicio de sesión a Firebase
    public LiveData<String> signIn(String email, String password) {
        return helperAuth(mAuth.signInWithEmailAndPassword(email, password));
    }
    // Registro en Firebase
    public LiveData<String> signUp(String email, String password) {
        return helperAuth(mAuth.createUserWithEmailAndPassword(email, password));
    }
    public String getCurrentUserID() {
        return mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
    }
}