package com.example.mvvmapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mvvmapp.dao.TvShowDao;
import com.example.mvvmapp.models.TvShow;

@Database(entities = TvShow.class, version = 1, exportSchema = false)
public abstract class TvShowsDataBase extends RoomDatabase {
    private static TvShowsDataBase tvShowsDataBase;

    public static synchronized TvShowsDataBase getTvShowDataBase(Context context) {
        if (tvShowsDataBase == null) {
            tvShowsDataBase = Room.databaseBuilder(
                    context,
                    TvShowsDataBase.class,
                    "tv_shows_db"
            ).build();
        }

        return tvShowsDataBase;
    }

    public abstract TvShowDao tvShowDao();
}
