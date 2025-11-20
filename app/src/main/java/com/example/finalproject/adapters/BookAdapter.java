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

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    public interface OnBookClickListener {
        void onBookClick(long id);
        void onFavoriteToggle(Book book);
    }

    private final List<Book> books;
    private final OnBookClickListener listener;

    public BookAdapter(List<Book> books, OnBookClickListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText("Название: " +book.getTitle());
        holder.desc.setText("Описание: " + book.getDescription());
        holder.price.setText("Цена: " + book.getPrice() + " ₽");
        holder.favButton.setImageResource(
                book.isFavorite() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off
        );

        holder.itemView.setOnClickListener(v -> listener.onBookClick(book.getId()));
        holder.favButton.setOnClickListener(v -> listener.onFavoriteToggle(book));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, price;
        ImageButton favButton;
        ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.bookTitle);
            desc = v.findViewById(R.id.bookDesc);
            price = v.findViewById(R.id.bookPrice);
            favButton = v.findViewById(R.id.favButton);
        }
    }
}
