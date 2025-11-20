package com.example.finalproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.adapters.FavoriteAdapter;
import com.example.finalproject.data.Book;
import com.example.finalproject.data.DatabaseAdapter;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyText;
    private DatabaseAdapter db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        emptyText = view.findViewById(R.id.emptyFavoritesText);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        db = new DatabaseAdapter(requireContext()).open();
        loadFavorites();
    }

    private void loadFavorites() {
        List<Book> favorites = db.getFavorites();
        if (favorites.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            FavoriteAdapter adapter = new FavoriteAdapter(favorites, book -> {
                book.setFavorite(false);
                book.setFavoriteDate(null);
                db.updateBook(book);
                loadFavorites();
            });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
