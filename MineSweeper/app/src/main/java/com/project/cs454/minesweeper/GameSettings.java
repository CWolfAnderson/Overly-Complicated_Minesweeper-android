package com.project.cs454.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class GameSettings extends AppCompatActivity {

    private final String GAME_MODE = "GAME_MODE";
    private final String EASY = "EASY";
    private final String MEDIUM = "MEDIUM";
    private final String HARD = "HARD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button easyButton = (Button) findViewById(R.id.easyButton);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GameSettings.this, Game.class);
                intent.putExtra(GAME_MODE, EASY);
                startActivity(intent);
            }
        });

        Button mediumButton = (Button) findViewById(R.id.mediumButton);
        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameSettings.this, Game.class);
                intent.putExtra(GAME_MODE, MEDIUM);
                startActivity(intent);

            }
        });

        Button hardButton = (Button) findViewById(R.id.hardButton);
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GameSettings.this, Game.class);
                intent.putExtra(GAME_MODE, HARD);
                startActivity(intent);
            }
        });



        Button customButton = (Button) findViewById(R.id.customButton);
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(GameSettings.this, HighScore.class);
                //startActivity(intent);

            }
        });


    }

}
