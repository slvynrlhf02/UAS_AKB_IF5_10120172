package com.UAS_AKB_IF5_10120172.view.activity;


import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.UAS_AKB_IF5_10120172.R;
import com.UAS_AKB_IF5_10120172.databinding.ActivityLoginBinding;
import com.UAS_AKB_IF5_10120172.databinding.ActivityRegisterBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        auth = FirebaseAuth.getInstance();
        binding.regisButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user with email and password
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration successful, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(this, "Akun Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                        updateUI(user);
                    } else {
                        // If registration fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            // Registration was successful, you can perform actions here if needed
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

// 10120172
// Silvyani Nurlaila Husnina Fajrin
// IF5