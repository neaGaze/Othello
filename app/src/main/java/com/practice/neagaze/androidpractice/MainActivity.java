package com.practice.neagaze.androidpractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btGameStart;
    EditText etGameSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btGameStart = (Button) findViewById(R.id.btGameStart);
        etGameSize = (EditText) findViewById(R.id.etGameSize);
        btGameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                int gameSize = Integer.parseInt(etGameSize.getText().toString());
                if (gameSize > 3 && gameSize < 10) {
                    intent.putExtra("GAME_SIZE", gameSize);
                    startActivity(intent);
                } else Toast.makeText(MainActivity.this, "INVALID GAME SIZE", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
