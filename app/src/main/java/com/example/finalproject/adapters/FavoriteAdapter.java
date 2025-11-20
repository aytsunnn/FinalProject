package com.example.finalproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.data.Book;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    public interface OnRemoveFavoriteListener {
        void onRemove(Book book);
    }

    private final List<Book> favorites;
    private final OnRemoveFavoriteListener listener;

    public FavoriteAdapter(List<Book> favorites, OnRemoveFavoriteListener listener) {
        this.favorites = favorites;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_favorite_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = favorites.get(position);
        holder.title.setText(book.getTitle());
        holder.desc.setText(book.getDescription());
        holder.date.setText("Добавлено: " + (book.getFavoriteDate() != null ? book.getFavoriteDate() : "-"));
        holder.removeButton.setOnClickListener(v -> listener.onRemove(book));
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, date;
        ImageButton removeButton;
        ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.favBookTitle);
            desc = v.findViewById(R.id.favBookDesc);
            date = v.findViewById(R.id.favBookDate);
            removeButton = v.findViewById(R.id.removeFavButton);
        }
    }
}
