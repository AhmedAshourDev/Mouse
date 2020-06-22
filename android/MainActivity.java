package com.example.gomagic;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onConnect(View v) {
        EditText editText = findViewById(R.id.edit_f);
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("addr", editText.getText().toString());
        startActivity(intent);
    }
}
