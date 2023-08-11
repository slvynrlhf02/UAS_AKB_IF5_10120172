package com.UAS_AKB_IF5_10120172.view.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.UAS_AKB_IF5_10120172.view.activity.AddNoteActivity;
import com.UAS_AKB_IF5_10120172.view.activity.MainActivity;
import com.UAS_AKB_IF5_10120172.R;
import com.UAS_AKB_IF5_10120172.adapter.NoteAdapter;
import com.UAS_AKB_IF5_10120172.model.Note;
import com.UAS_AKB_IF5_10120172.database.DatabaseHelper;
import com.UAS_AKB_IF5_10120172.NoteInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {
// 10120205 - Raya Adhary - IF5

    private MainActivity mainActivity;
    private List<Note> notes;
    private DatabaseReference notesReference;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private FloatingActionButton addButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_note, container, false);
        return mView;
    }

    private String getCurrentUserUid() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().hide();

        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.mynote);
        addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddNoteActivity.class));
        });

        // Initialize RecyclerView
        setupRecyclerView();

        // Fetch data from Firebase and update RecyclerView
        fetchNotesFromFirebase();
    }

    // Create a method to initialize and set up the RecyclerView
    private void setupRecyclerView() {
        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter(notes);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    // Create a method to fetch data from Firebase Realtime Database
    private void fetchNotesFromFirebase() {
        String currentUserUid = getCurrentUserUid();
        notesReference = new DatabaseHelper().getNotesReference().child(currentUserUid);
        notesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Note note = dataSnapshot.getValue(Note.class);
                if (note != null) {
                    notes.add(note);
                    noteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Note updatedNote = dataSnapshot.getValue(Note.class);
                if (updatedNote != null) {
                    String noteId = dataSnapshot.getKey();
                    for (int i = 0; i < notes.size(); i++) {
                        if (notes.get(i).getId().equals(noteId)) {
                            notes.set(i, updatedNote);
                            noteAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String removedNoteId = dataSnapshot.getKey();
                for (int i = 0; i < notes.size(); i++) {
                    if (notes.get(i).getId().equals(removedNoteId)) {
                        notes.remove(i);
                        noteAdapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Not needed for this implementation
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Not needed for this implementation
            }
        });
    }
}

// 10120172
// Silvyani Nurlaila Husnina Fajrin
// IF5