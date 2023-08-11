package com.UAS_AKB_IF5_10120172.view.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.UAS_AKB_IF5_10120172.R;
import com.UAS_AKB_IF5_10120172.database.DatabaseHelper;
import com.UAS_AKB_IF5_10120172.model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;
import java.util.Random;

public class AddNoteActivity extends AppCompatActivity {


    ImageButton button;
    EditText editTitle;
    EditText editCategory;
    EditText editDesc;
    Button addButton;
    Button deleteButton;
    TextView titleAdd;

    private DatabaseReference notesReference;
    Note note = null;

    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getSupportActionBar().hide();
        note = (Note) getIntent().getSerializableExtra("Note");
        button = findViewById(R.id.back);
        editTitle = findViewById(R.id.title);
        editCategory = findViewById(R.id.category);
        editDesc = findViewById(R.id.txt_desc);
        addButton = findViewById(R.id.buttonAdd);
        deleteButton = findViewById(R.id.buttonDelete);
        titleAdd = findViewById(R.id.txt_add);
        notesReference = new DatabaseHelper().getNotesReference();


        button.setOnClickListener(v -> {
            finish();
        });


        if (note == null) {
            deleteButton.setVisibility(View.GONE);

            addButton.setOnClickListener(v -> {
                if (editTitle.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Judul Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editCategory.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Kategori Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editDesc.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Isi Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
                Date d = new Date();
                CharSequence date = DateFormat.format("EEEE, d MMM yyyy HH:mm", d.getTime());
                String newNoteId = notesReference.push().getKey();
                Note n = new Note(
                        newNoteId,
                        editTitle.getText().toString(),
                        editCategory.getText().toString(),
                        editDesc.getText().toString(),
                        date + ""
                );


                if (newNoteId != null) {
                    notesReference.child(userId).child(newNoteId).setValue(n);
                    finish();
                    Toast.makeText(this, "Catatan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
//                    notification();
                    // After saving the note, send a cloud message
                    String title = "Data telah berhasil disimpan";
                    notification(title);

                } else {
                    Toast.makeText(this, "Failed to add note: Invalid note ID", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            editTitle.setText(note.getTitle());
            editCategory.setText(note.getCategory());
            editDesc.setText(note.getDesc());

            deleteButton.setVisibility(View.VISIBLE);
            titleAdd.setText("Edit Catatan");

            addButton.setOnClickListener(v -> {
                if (editTitle.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Judul Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editCategory.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Kategori Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editDesc.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Isi Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }

                Date d = new Date();
                CharSequence date = DateFormat.format("EEEE, d MMMM yyyy HH:mm", d.getTime());

                note.setTitle(editTitle.getText().toString());
                note.setCategory(editCategory.getText().toString());
                note.setDesc(editDesc.getText().toString());
                note.setDate((String) date);
                String noteId = note.getId();
                notesReference.child(userId).child(noteId).setValue(note);
                Toast.makeText(this, "Catatan berhasil diedit", Toast.LENGTH_SHORT).show();
                String title = "Data telah berhasil diubah";
                notification(title);
                finish();
            });
        }

        deleteButton.setOnClickListener(v -> {
            if (note != null) {
                showDeleteConfirmationDialog(note);
            } else {
                Toast.makeText(this, "Catatan tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDeleteConfirmationDialog(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Hapus");
        builder.setMessage("Apakah Anda yakin ingin menghapus catatan ini?");
        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String noteId = note.getId();
                if (noteId != null) {
                    notesReference.child(userId).child(noteId).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                finish();
                                Toast.makeText(AddNoteActivity.this, "Catatan berhasil dihapus", Toast.LENGTH_SHORT).show();
                                String title = "Data telah berhasil dihapus";
                                notification(title);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(AddNoteActivity.this, "Gagal menghapus catatan", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(AddNoteActivity.this, "Gagal menghapus catatan: ID catatan tidak valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, dismiss the dialog
            }
        });
        builder.create().show();
    }



    private void notification(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                .setContentText("Catatan")
                .setSmallIcon(R.drawable.icon_notes)
                .setAutoCancel(true)
                .setContentText(text);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        managerCompat.notify(999, builder.build());
    }
}


// 10120172
// Silvyani Nurlaila Husnina Fajrin
// IF5