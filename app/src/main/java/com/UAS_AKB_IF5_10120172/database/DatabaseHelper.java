package com.UAS_AKB_IF5_10120172.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.UAS_AKB_IF5_10120172.model.Note;
import com.UAS_AKB_IF5_10120172.NoteInterface;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {

    private static final String NOTES_PATH = "catatan";
    private DatabaseReference databaseReference;

    public DatabaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference(NOTES_PATH);
    }

    public DatabaseReference getNotesReference() {
        return databaseReference;
    }

    // No need for create, update, and delete methods. Firebase handles these operations automatically.
}


// 10120172
// Silvyani Nurlaila Husnina Fajrin
// IF5