package com.example.appchat.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appchat.R;
import com.example.appchat.adapters.PostAdapter;
import com.example.appchat.databinding.FragmentHomeBinding;
import com.example.appchat.providers.AuthProvider;
import com.example.appchat.view.MainActivity;
import com.example.appchat.view.PostActivity;
import com.example.appchat.viewmodel.PostViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private AuthProvider authProvider;
    private PostViewModel postViewModel; // ViewModel que gestionará los posts

    public HomeFragment() {
        // Constructor vacío requerido
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authProvider = new AuthProvider(getContext());
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);  // Obtener el ViewModel
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // barra de herramientas
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.tools);

        // abrir PostActivity
        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PostActivity.class);
            startActivity(intent);
        });

        // RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Observar vm de los posts
        postViewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            if (posts != null) {
                PostAdapter adapter = new PostAdapter(posts);
                binding.recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        setupMenu();
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.itemLogout) {
                    authProvider.logout().observe(getViewLifecycleOwner(), logoutResult -> {
                        if (logoutResult != null && logoutResult) {
                            Log.d("AuthProvider", "Cierre de sesión exitoso, redirigiendo a MainActivity...");
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("fromLogout", true);
                            startActivity(intent);
                        } else {
                            Log.e("AuthProvider", "Error al cerrar sesión.");
                            Toast.makeText(getContext(), "Error al cerrar sesión. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiar el binding para evitar fugas de memoria
        binding = null;
    }
}



