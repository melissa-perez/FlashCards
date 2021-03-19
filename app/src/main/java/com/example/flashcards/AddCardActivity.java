package com.example.flashcards;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
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
        String wrongAnswerToEdit1 = getIntent().getStringExtra("wrong1");
        String wrongAnswerToEdit2 = getIntent().getStringExtra("wrong2");

        EditText editQuestion = findViewById(R.id.editQuestionField);
        editQuestion.setText(questionToEdit);

        EditText editAnswer = findViewById(R.id.editAnswerField);
        editAnswer.setText(answerToEdit);

        EditText editWrong1 = findViewById(R.id.editIncorrectField1);
        editWrong1.setText(wrongAnswerToEdit1);

        EditText editWrong2 = findViewById(R.id.editIncorrectField2);
        editWrong2.setText(wrongAnswerToEdit2);

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
                String wrongAnswerToReturn1 = ((EditText) findViewById(R.id.editIncorrectField1))
                        .getText().toString();
                String wrongAnswerToReturn2 = ((EditText) findViewById(R.id.editIncorrectField2))
                        .getText().toString();

                if(!questionToReturn.isEmpty() &&
                        !answerToReturn.isEmpty() &&
                        !wrongAnswerToReturn1.isEmpty() &&
                        !wrongAnswerToReturn2.isEmpty()){
                    Intent data = new Intent(); // put our data
                    data.putExtra("question", questionToReturn); // the key as 'string1'
                    data.putExtra("answer", answerToReturn); // the key as 'string2
                    data.putExtra("wrong1", wrongAnswerToReturn1); // the key as 'string1'
                    data.putExtra("wrong2", wrongAnswerToReturn2); // the key as 'string2
                    setResult(RESULT_OK, data); // set result code and bundle data for response
                    finish();
                }
                else{
                    displayToast("Must enter both question and answers.");
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