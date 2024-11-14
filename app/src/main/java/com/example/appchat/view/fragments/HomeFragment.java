package com.example.appchat.view.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import com.example.appchat.R;
import com.example.appchat.databinding.FragmentHomeBinding;

import com.example.appchat.view.UploadActivity;

import androidx.fragment.app.Fragment;
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private static final String ARG_USER_ID = "user_id";
    private String userId; // Variable para almacenar el userId

    public HomeFragment() {
        // Constructor vacío
    }

    public static HomeFragment newInstance(String userId) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID, null);
        }
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

        // Utiliza el userId cuando se haga clic en el botón
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UploadActivity.class);
                intent.putExtra("user_id", userId); // Pasar userId a UploadActivity
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiamos la referencia del binding para evitar fugas de memoria
        binding = null;
    }
}

