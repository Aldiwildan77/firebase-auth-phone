package com.zaydhisyam.cobafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class InputCodeActivity extends AppCompatActivity {

    public final static String EXTRA_VERF_ID = "extra_verf_id";

    private EditText et_input_code;
    private ImageView bg_transparent;
    private ProgressBar progress_bar;
    private TextView tv_loading;

    private FirebaseAuth firebaseAuth;

    private String verification_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);
        set_ui();

        firebaseAuth = FirebaseAuth.getInstance();

        verification_id = getIntent().getStringExtra(EXTRA_VERF_ID);

        et_input_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start == 5) {
                    //hide keyboard
                    InputMethodManager imm = (InputMethodManager) et_input_code.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_input_code.getWindowToken(), 0);

                    //get input text
                    String code = et_input_code.getText().toString();
                    if (TextUtils.isEmpty(code))
                        et_input_code.setError(getString(R.string.err_empty_field));
                    else
                        do_sign_in(verification_id, code);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    private void set_ui() {
        getSupportActionBar().setTitle(R.string.actionbar_input_code);
        et_input_code = findViewById(R.id.et_input_code);
        bg_transparent = findViewById(R.id.bg_transparent);
        progress_bar = findViewById(R.id.progress_bar);
        tv_loading = findViewById(R.id.tv_loading);
    }

    private void do_sign_in(String verification_id, String code) {
        //visible loading view
        bg_transparent.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.VISIBLE);
        tv_loading.setVisibility(View.VISIBLE);

        //make PhoneAuthCredential object
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, code);

        //do sign in
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(InputCodeActivity.this, R.string.toast_login, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(InputCodeActivity.this, MainActivity.class));
                        }
                        else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(InputCodeActivity.this, R.string.err_wrong_code, Toast.LENGTH_SHORT).show();
                                et_input_code.getText().clear();
                            }
                        }
                        stop_loading();
                    }
                });
    }

    private void stop_loading() {
        bg_transparent.setVisibility(View.GONE);
        progress_bar.setVisibility(View.GONE);
        tv_loading.setVisibility(View.GONE);
    }
}
