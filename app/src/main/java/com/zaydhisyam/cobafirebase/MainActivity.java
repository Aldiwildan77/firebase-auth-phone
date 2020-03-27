package com.zaydhisyam.cobafirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private TextView tv_hello;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        tv_hello = findViewById(R.id.tv_hello);
        btn_logout = findViewById(R.id.btn_logout);

        //check logged in or not
        if (firebaseAuth.getCurrentUser() == null)
            startActivity(new Intent(MainActivity.this, InputNumberActivity.class));
        else
            tv_hello.setText("Welcome On Board, " + firebaseAuth.getCurrentUser().getPhoneNumber());

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Toast.makeText(MainActivity.this, R.string.toast_logout, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

}
