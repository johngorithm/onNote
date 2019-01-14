package com.jxw.onnote.data.migrations;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

public class Migrations {
    public static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE note_table ADD COLUMN update_at TEXT");
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL("CREATE TABLE new_note_table (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT, description TEXT, priority INTEGER NOT NULL, created_at TEXT, updated_at TEXT)");
            database.execSQL("INSERT INTO new_note_table (id, title, description, priority, created_at, updated_at) SELECT id, title, description, priority, createdAt, update_at FROM note_table");
            // REMOVE THE OLD TABLE
            database.execSQL("DROP TABLE note_table");
            // RENAME THE NEW TABLE
            database.execSQL("ALTER TABLE new_note_table RENAME TO note_table");
        }
    };
}
