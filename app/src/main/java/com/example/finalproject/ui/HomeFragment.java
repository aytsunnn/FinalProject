package com.example.finalproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.adapters.BookAdapter;
import com.example.finalproject.data.Book;
import com.example.finalproject.data.DatabaseAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private DatabaseAdapter db;
    private RecyclerView recyclerView;
    private TextView emptyText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewBooks);
        emptyText = view.findViewById(R.id.emptyText);
        Button addButton = view.findViewById(R.id.addBookButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        db = new DatabaseAdapter(requireContext()).open();

        loadBooks();

        addButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainMenuNavHost, new AddBookFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void loadBooks() {
        List<Book> books = db.getAllBooks();
        if (books.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            BookAdapter adapter = new BookAdapter(books, new BookAdapter.OnBookClickListener() {
                @Override
                public void onBookClick(long id) {
                    BookDetailsFragment details = BookDetailsFragment.newInstance(id);
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainMenuNavHost, details)
                            .addToBackStack(null)
                            .commit();
                }

                @Override
                public void onFavoriteToggle(Book book) {
                    book.setFavorite(!book.isFavorite());
                    if (book.isFavorite()) {
                        String date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date());
                        book.setFavoriteDate(date);
                    } else {
                        book.setFavoriteDate(null);
                    }
                    db.updateBook(book);
                    loadBooks();
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db != null) loadBooks();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
