package com.example.appchat.view;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.appchat.databinding.ActivityRegisterBinding;
import com.example.appchat.model.User;
import com.example.appchat.util.Validaciones;
import com.example.appchat.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        manejarEventos();
    }
    private void manejarEventos() {
        // Evento volver a login
        binding.circleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Evento de registro
        binding.btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarRegistro();
            }
        });
    }

    private void realizarRegistro() {
        String usuario = binding.itUsuario.getText().toString().trim();
        String email = binding.itEmail.getText().toString().trim();
        String pass = binding.itPassword.getText().toString().trim();
        String pass1 = binding.itPassword1.getText().toString().trim();

        if (!Validaciones.validarUsuario(usuario)) {
            showToast("Usuario incorrecto");
            return;
        }

        if (!Validaciones.validarMail(email)) {
            showToast("El correo no es válido");
            return;
        }

        String passError = Validaciones.validarPass(pass, pass1);
        if (passError != null) {
            showToast(passError);
            return;
        }

        User user = new User(usuario, email, pass);
        viewModel.register(user).observe(this, result -> {
            showToast(result);
        });
    }


    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
        finish();
    }
}
