package com.sslavik.android.roomwordssample;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Debug;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.URI;

public class WordContentProvider extends ContentProvider {

    /**
     * Se definen los atributos publicos que neceistan el resto de las activities para acceder al contentPovider
     *
     * Si suele haber muchos campos estaticos se suele crear una clase WordContentProviderCONTRACT
     */
    public static final String AUTHORITY = "com.sslavik.android.roomwordssample";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    // TABLAS
    public static final String WORD_PATH = "word"; // EL NOMBRE DE LA TABLA NO TIENE POR QUÉ SER IGUAL A LA ORIGINAL ES SOLO UNA REFERENCIA
    public static final Uri WORD_URI = Uri.withAppendedPath(AUTHORITY_URI, WORD_PATH);

    // COLUMNAS DE TABLA

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
            cursor = WordRoomDatabase.getDatabase(getContext()).wordDao().getWordsWithCursor();
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
