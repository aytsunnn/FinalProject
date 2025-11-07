package com.example.finalproject.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.example.finalproject.data.User;

public class RegisterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText emailRegister = view.findViewById(R.id.emailRegister);
        EditText passwordRegister = view.findViewById(R.id.passwordRegister);
        EditText passwordConfirm = view.findViewById(R.id.passwordConfirm);
        Button saveButton = view.findViewById(R.id.saveRegisterButton);
        Button backToLoginButton = view.findViewById(R.id.backToLoginButton);

        DatabaseAdapter db = new DatabaseAdapter(requireContext()).open();

        saveButton.setOnClickListener(v -> {
            String email = emailRegister.getText().toString().trim();
            String password = passwordRegister.getText().toString().trim();
            String confirm = passwordConfirm.getText().toString().trim();

            // Проверки на заполнение
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm)) {
                Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Некорректный email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка длины пароля
            if (password.length() < 6) {
                Toast.makeText(getContext(), "Пароль должен быть не короче 6 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка совпадения паролей
            if (!password.equals(confirm)) {
                Toast.makeText(getContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                return;
            }

            // Попытка регистрации
            long result = db.registerUser(new User(email, password));
            if (result != -1) {
                SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                prefs.edit().putBoolean("logged_in", true).apply();

                Toast.makeText(getContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), MainMenuActivity.class);
                startActivity(intent);
                requireActivity().finish();
            } else {
                Toast.makeText(getContext(), "Ошибка: пользователь уже существует", Toast.LENGTH_SHORT).show();
            }
        });

        backToLoginButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.loginFragment));
    }
}
