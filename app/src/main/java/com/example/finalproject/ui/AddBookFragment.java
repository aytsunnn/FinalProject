package com.example.finalproject.ui;

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
import com.example.finalproject.R;
import com.example.finalproject.data.Book;
import com.example.finalproject.data.DatabaseAdapter;

public class AddBookFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText titleInput = view.findViewById(R.id.inputTitle);
        EditText descInput = view.findViewById(R.id.inputDesc);
        EditText priceInput = view.findViewById(R.id.inputPrice);
        Button saveButton = view.findViewById(R.id.saveBookButton);

        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String desc = descInput.getText().toString().trim();
            String priceStr = priceInput.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(priceStr)) {
                Toast.makeText(requireContext(), "Введите название и цену", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            DatabaseAdapter db = new DatabaseAdapter(requireContext()).open();
            db.insertBook(new Book(-1, title, desc, price, false, null));
            db.close();

            Toast.makeText(requireContext(), "Книга добавлена!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
}
