package com.example.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Add a New Card");
        setContentView(R.layout.activity_add_card);

        ImageView closeCard = findViewById(R.id.close_icon);
        closeCard.setImageResource(R.drawable.ic_iconmonstr_x_mark_circle_thin);



        // Cancel Button, bottom right anchor
        closeCard.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                finish();
            }
        });

    }
}