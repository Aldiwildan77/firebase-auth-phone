package com.zaydhisyam.cobafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class InputNumberActivity extends AppCompatActivity {

    private EditText et_input_number;
    private ImageView bg_transparent;
    private ProgressBar progress_bar;
    private TextView tv_loading;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verifyPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_number);
        set_ui();

        verifyPhoneNumber = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //instant verification, based on providers.
                // cant detect verification without user action
                stop_loading();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                stop_loading();
                Toast.makeText(InputNumberActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                stop_loading();
                Intent toCodeIntent = new Intent(InputNumberActivity.this, InputCodeActivity.class);
                toCodeIntent.putExtra(InputCodeActivity.EXTRA_VERF_ID, verification_id);
                startActivity(toCodeIntent);
            }
        };

        et_input_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //hide keyboard
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //get input text
                    String input_number = et_input_number.getText().toString();
                    if (TextUtils.isEmpty(input_number))
                        et_input_number.setError(getString(R.string.err_empty_field));
                    else
                        input_process(et_input_number.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void set_ui() {
        getSupportActionBar().setTitle(R.string.actionbar_input_number);
        et_input_number = findViewById(R.id.et_input_number);
        bg_transparent = findViewById(R.id.bg_transparent);
        progress_bar = findViewById(R.id.progress_bar);
        tv_loading = findViewById(R.id.tv_loading);
    }

    private void input_process(String number) {
        //visible loading view
        bg_transparent.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.VISIBLE);
        tv_loading.setVisibility(View.VISIBLE);

        //process firebase auth / otp
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                verifyPhoneNumber
        );
    }

    private void stop_loading() {
        bg_transparent.setVisibility(View.GONE);
        progress_bar.setVisibility(View.GONE);
        tv_loading.setVisibility(View.GONE);
    }
}
