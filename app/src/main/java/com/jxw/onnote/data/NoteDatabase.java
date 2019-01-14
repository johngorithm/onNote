package com.jxw.onnote.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.jxw.onnote.data.migrations.Migrations;

import java.util.Date;

@Database(entities = {Note.class}, version = 4)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;
    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getDBInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")
                    .addMigrations(MIGRATION_1_2, Migrations.MIGRATION_2_3, Migrations.MIGRATION_3_4)
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }



    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private PopulateDBAsyncTask(NoteDatabase database) {
            this.noteDao = database.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            noteDao.insert(new Note("Keeping to time", "This is the description and a detail explanation of how to keep to time", 6, new Date().toString(), null));
            noteDao.insert(new Note("Learning React", "This is the description and a detail explanation of how to Learn React js", 3, new Date().toString(), null));
            noteDao.insert(new Note("Working with Andela", "This is the description and a detail explanation of how working in Andela is", 2, new Date().toString(), null));
            noteDao.insert(new Note("Business Today", "This is the description and a detail explanation of today's economy and business world", 1, new Date().toString(), null));
            return null;
        }
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //
        }
    };
}
