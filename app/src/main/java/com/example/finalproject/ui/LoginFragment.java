package com.example.finalproject.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.finalproject.MainMenuActivity;
import com.example.finalproject.R;
import com.example.finalproject.data.DatabaseAdapter;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText emailInput = view.findViewById(R.id.emailInput);
        EditText passwordInput = view.findViewById(R.id.passwordInput);
        Button loginButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerButton);

        DatabaseAdapter db = new DatabaseAdapter(requireContext()).open();

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.checkUser(email, password)) {
                SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                prefs.edit().putBoolean("logged_in", true).apply();

                Toast.makeText(getContext(), "Добро пожаловать!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), MainMenuActivity.class);
                startActivity(intent);
                requireActivity().finish(); // чтобы не вернуться на экран логина
            } else {
                Toast.makeText(getContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.registerFragment));
    }
}
