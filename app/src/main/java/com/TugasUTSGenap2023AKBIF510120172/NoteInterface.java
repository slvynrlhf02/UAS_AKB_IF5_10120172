package com.TugasUTSGenap2023AKBIF510120172;

import android.database.Cursor;

import com.TugasUTSGenap2023AKBIF510120172.model.Note;

public interface NoteInterface {

    public Cursor read();
    public boolean create(Note note);
    public boolean update(Note note);
    public boolean delete(String id);
}


// 10120172
// Silvyani Nurlaila Husnina Fajrin
// IF-5

