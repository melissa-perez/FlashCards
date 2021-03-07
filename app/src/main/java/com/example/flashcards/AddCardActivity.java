package com.example.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Add/Edit a New Card ");
        setContentView(R.layout.activity_add_card);

        // after clicking edit, we can get the question that we are editing
        String questionToEdit = getIntent().getStringExtra("question");
        String answerToEdit = getIntent().getStringExtra("answer");

        EditText editQuestion = findViewById(R.id.editQuestionField);
        editQuestion.setText(questionToEdit);
        EditText editAnswer = findViewById(R.id.editAnswerField);
        editAnswer.setText(answerToEdit);


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

                if(questionToReturn.isEmpty())
                    displayToast("Must enter both question and answer.");
                else if(answerToReturn.isEmpty())
                    displayToast("Must enter both question and answer.");
                else{
                    Intent data = new Intent(); // put our data
                    data.putExtra("question", questionToReturn); // the key as 'string1'
                    data.putExtra("answer", answerToReturn); // the key as 'string2
                    setResult(RESULT_OK, data); // set result code and bundle data for response
                    finish();
                }

            }
        });

        // Cancel Button, bottom right anchor
        closeCard.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                finish();
            }
        });
    }

    private void displayToast(String message) {
        // Inflate toast XML layout
        View layout = getLayoutInflater().inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        // Fill in the message into the textview
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);
        // Construct the toast, set the view and display
        Toast toast = new Toast(getApplicationContext());
        toast.setView(layout);
        toast.show();
    }
}