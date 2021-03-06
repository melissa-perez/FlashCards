package com.example.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Add a New Card");
        setContentView(R.layout.activity_add_card);

        ImageView closeCard = findViewById(R.id.close_icon);
        ImageView saveCard = findViewById(R.id.save_icon);
        closeCard.setImageResource(R.drawable.ic_iconmonstr_x_mark_circle_thin);


        // Save Button, bottom left anchor
        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                String questionToReturn = ((EditText) findViewById(R.id.editQuestionField))
                        .getText().toString();
                String answerToReturn = ((EditText) findViewById(R.id.editAnswerField))
                        .getText().toString();
                Intent data = new Intent(); // put our data
                 data.putExtra("question", questionToReturn); // the key as 'string1'
                 data.putExtra("answer", answerToReturn); // the key as 'string2
                  setResult(RESULT_OK, data); // set result code and bundle data for response
                finish();
            }
        });

        // Cancel Button, bottom right anchor
        closeCard.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                finish();
            }
        });

    }
}