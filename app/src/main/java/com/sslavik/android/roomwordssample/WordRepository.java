package com.sslavik.android.roomwordssample;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;
    List<Word> lista = new ArrayList<>();
    private static WordRepository instance;

    public static WordRepository getInstance(Application application){
        if(instance == null)
            instance = new WordRepository(application);
        return instance;
    }

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();

        mAllWords = mWordDao.getAlphabetizedWords();
        // Si los datos de las palabras pertenecen a un ContentProvider
        // 1. Se tiene que obtener el cursor
        // 2. Tendremos que pasar los datos a una estructura. En nuestro caso LiveData<List<Word>>

        // Application tiene un metodo getContentResolver() Nos devuelve todos los Cursos obtenidos desde la URI PASADA
        Cursor c = application.getContentResolver().query(WordContentProvider.WORD_URI,
                null, null, null, null);

        if(c.moveToFirst()) {
            do {
                lista.add(new Word(c.getString(0))); // ES 0 PORQUE SOLO HAY 1 COLUMNA
            } while (c.moveToNext());
        }
        c.close();

    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    List<Word> getAllWordsList() {
        return lista;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Word word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWordDao.insert(word);
        });
    }
}
