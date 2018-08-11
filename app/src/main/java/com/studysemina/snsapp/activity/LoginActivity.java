package com.studysemina.snsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.studysemina.snsapp.R;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
