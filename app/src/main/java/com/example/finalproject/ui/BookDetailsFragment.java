package com.example.finalproject.ui;

import android.os.Bundle;
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

public class BookDetailsFragment extends Fragment {

    private static final String ARG_BOOK_ID = "book_id";
    private long bookId;
    private Book book;

    public static BookDetailsFragment newInstance(long id) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_BOOK_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            bookId = getArguments().getLong(ARG_BOOK_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText titleEdit = view.findViewById(R.id.editBookTitle);
        EditText descEdit = view.findViewById(R.id.editBookDesc);
        EditText priceEdit = view.findViewById(R.id.editBookPrice);
        Button saveButton = view.findViewById(R.id.saveBookChangesButton);
        Button deleteButton = view.findViewById(R.id.deleteBookButton);

        DatabaseAdapter db = new DatabaseAdapter(requireContext()).open();
        book = db.getBook(bookId);
        db.close();

        if (book != null) {
            titleEdit.setText(book.getTitle());
            descEdit.setText(book.getDescription());
            priceEdit.setText(String.valueOf(book.getPrice()));
        }

        saveButton.setOnClickListener(v -> {
            book.setTitle(titleEdit.getText().toString());
            book.setDescription(descEdit.getText().toString());
            book.setPrice(Double.parseDouble(priceEdit.getText().toString()));

            DatabaseAdapter db1 = new DatabaseAdapter(requireContext()).open();
            db1.updateBook(book);
            db1.close();

            Toast.makeText(requireContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        deleteButton.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Удаление книги")
                    .setMessage("Удалить эту книгу?")
                    .setPositiveButton("Да", (d, w) -> {
                        DatabaseAdapter db2 = new DatabaseAdapter(requireContext()).open();
                        db2.deleteBook(bookId);
                        db2.close();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    })
                    .setNegativeButton("Отмена", (d, w) -> d.dismiss())
                    .show();
        });
    }
}
