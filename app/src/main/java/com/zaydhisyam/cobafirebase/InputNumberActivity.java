package com.zaydhisyam.cobafirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class InputNumberActivity extends AppCompatActivity {

    private EditText et_input_number;
    private ImageView bg_transparent;
    private ProgressBar progress_bar;
    private TextView tv_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_number);
        set_ui();

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
    }
}
