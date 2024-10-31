package com.example.appchat.view;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.appchat.databinding.ActivityMainBinding;
import com.example.appchat.util.Validaciones;
import com.example.appchat.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        manejarEventos();
    }

    private void manejarEventos() {
        binding.tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.itUsuario.getText().toString().trim();
                String pass = binding.itPassword.getText().toString().trim();

                if (!Validaciones.validarMail(email)) {
                    showToast("Email incorrecto");
                    return;
                }
                if (!Validaciones.controlarPasword(pass)) {
                    showToast("Password incorrecto");
                    return;
                }

                // Observa el resultado del login y actúa en consecuencia
                viewModel.login(email, pass).observe(MainActivity.this, loginSuccessful -> {
                    if (loginSuccessful) {
                        Intent intent=new Intent(MainActivity.this,UserActivity.class);
                        startActivity(intent);
                    } else {
                        showToast("Login fallido");
                    }
                });
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
@Override
protected void onResume() {
    super.onResume();
    limpiarCampos();
}
    private void limpiarCampos() {
        binding.itUsuario.setText("");
        binding.itPassword.setText("");

    }
}
