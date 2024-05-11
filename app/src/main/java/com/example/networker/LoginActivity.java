package com.example.networker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.networker.database.domain.DummyDeviceBackend;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getName();
    private static final String PREF_KEY = LoginActivity.class.getPackage().toString();
    private static final String PREF_STICKY_CREDS = "sticky_creds";
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private final DummyDeviceBackend dbBackend = new DummyDeviceBackend();
    private CheckBox stickyCredsInput;
    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onStart() {
        super.onStart();

        if (getStickyCredEmail() != null) {
            emailInput.setText(getStickyCredEmail());
            stickyCredsInput.setChecked(true);
        }
        if (getStickyCredPassword() != null) {
            passwordInput.setText(getStickyCredPassword());
            stickyCredsInput.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        stickyCredsInput = findViewById(R.id.sticky_creds);
        Button loginBtn = findViewById(R.id.login_btn);
        Button registerBtn = findViewById(R.id.register_btn);

        loginBtn.setOnClickListener(
                l -> login(
                        emailInput.getText().toString(),
                        passwordInput.getText().toString(),
                        stickyCredsInput.isChecked()
                )
        );
        registerBtn.setOnClickListener(
                l -> register(
                        emailInput.getText().toString(),
                        passwordInput.getText().toString(),
                        stickyCredsInput.isChecked()
                )
        );
    }

    @Override
    protected void onPause() {
        if (stickyCredsInput != null && !stickyCredsInput.isChecked()) {
            setStickyCreds(null, null);
        }
        super.onPause();
    }

    public void login(String email, String password, boolean isStickyCred) {
        if (getStickyCredEmail() == null && getStickyCredPassword() == null && isStickyCred) {
            setStickyCreds(email, password);
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (isStickyCred) {
                                setStickyCreds(email, password);
                            }

                            Intent dst = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(dst);
                        }
                        else {
                            Toast toast = Toast.makeText(LoginActivity.this,
                                    "Login failed; " + String.valueOf(task.getException().getMessage()), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
    }

    public void register(String email, String password, boolean isStickyCred) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            login(email, password, isStickyCred);
                        }
                        else {
                            Toast toast = Toast.makeText(LoginActivity.this,
                                    "Registration failed; " + String.valueOf(task.getException().getMessage()), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
    }

    public void setStickyCreds(String email, String password) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_STICKY_CREDS+"_email", email);
        editor.putString(PREF_STICKY_CREDS+"_password", password);
        editor.apply();
    }

    public String getStickyCredEmail() {
        return preferences.getString(PREF_STICKY_CREDS+"_email", null);
    }
    public String getStickyCredPassword() {
        return preferences.getString(PREF_STICKY_CREDS+"_password", null);
    }
}