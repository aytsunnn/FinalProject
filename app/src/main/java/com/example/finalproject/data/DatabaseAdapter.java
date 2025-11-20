package com.example.finalproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DatabaseAdapter(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public DatabaseAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long registerUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        return db.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    public boolean checkUser(String email, String password) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                null,
                DatabaseHelper.COLUMN_EMAIL + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public long insertBook(Book book) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, book.getTitle());
        values.put(DatabaseHelper.COLUMN_DESC, book.getDescription());
        values.put(DatabaseHelper.COLUMN_PRICE, book.getPrice());
        values.put(DatabaseHelper.COLUMN_FAVORITE, book.isFavorite() ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_FAVORITE_DATE, book.getFavoriteDate());
        return db.insert(DatabaseHelper.TABLE_BOOKS, null, values);
    }

    public void updateBook(Book book) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, book.getTitle());
        values.put(DatabaseHelper.COLUMN_DESC, book.getDescription());
        values.put(DatabaseHelper.COLUMN_PRICE, book.getPrice());
        values.put(DatabaseHelper.COLUMN_FAVORITE, book.isFavorite() ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_FAVORITE_DATE, book.getFavoriteDate());
        db.update(DatabaseHelper.TABLE_BOOKS, values, DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(book.getId())});
    }

    public void deleteBook(long id) {
        db.delete(DatabaseHelper.TABLE_BOOKS, DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_BOOKS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESC));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
                boolean favorite = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAVORITE)) == 1;
                String favDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAVORITE_DATE));
                list.add(new Book(id, title, desc, price, favorite, favDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Book> getFavorites() {
        List<Book> list = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_BOOKS, null,
                DatabaseHelper.COLUMN_FAVORITE + "=?", new String[]{"1"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESC));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
                boolean favorite = true;
                String favDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAVORITE_DATE));

                list.add(new Book(id, title, desc, price, favorite, favDate)); // ← исправлено!
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public Book getBook(long id) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_BOOKS, null,
                DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor.moveToFirst()) {
            Book b = new Book(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESC)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAVORITE)) == 1

            );
            cursor.close();
            return b;
        }
        cursor.close();
        return null;
    }
}
