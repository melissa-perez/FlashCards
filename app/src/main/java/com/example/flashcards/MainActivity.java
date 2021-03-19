package com.example.flashcards;

import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    boolean isShowingAnswers = false;

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Flash Cards");

        TextView flashcardQuestion = findViewById(R.id.flashcard_question);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer);
        ImageView eyeToggle = findViewById(R.id.unhide_icon);
        ImageView editCard = findViewById(R.id.edit_icon);
        ImageView addCard = findViewById(R.id.add_icon);
        TextView wrongAnswer1 = findViewById(R.id.choice1);
        TextView wrongAnswer2 = findViewById(R.id.choice2);
        TextView correctAnswer = findViewById(R.id.choice3);
        ImageView nextCard = findViewById(R.id.cycle_icon);


        flashcardDatabase = new FlashcardDatabase(getApplicationContext());

        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.flashcard_question)).
                    setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.flashcard_answer)).
                    setText(allFlashcards.get(0).getAnswer());
        }

        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashcardAnswer.setVisibility(View.VISIBLE);
            }
        });

        flashcardAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardQuestion.setVisibility(View.VISIBLE);
                flashcardAnswer.setVisibility(View.INVISIBLE);
            }
        });

        wrongAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wrongAnswer1.setBackgroundColor(getResources().getColor(R.color.incorrect_red,
                        null));
            }
        });

        wrongAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wrongAnswer2.setBackgroundColor(getResources().getColor(R.color.incorrect_red,
                        null));
            }
        });

        correctAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctAnswer.setBackgroundColor(getResources().getColor(R.color.correct_green,
                        null));

            }
        });

        eyeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowingAnswers) {
                    isShowingAnswers = true;
                    eyeToggle.setImageResource(R.drawable.ic_iconmonstr_eye_off_thin);
                    correctAnswer.setVisibility(View.VISIBLE);
                    wrongAnswer1.setVisibility(View.VISIBLE);
                    wrongAnswer2.setVisibility(View.VISIBLE);
                } else {
                    eyeToggle.setImageResource(R.drawable.ic_iconmonstr_eye_thin);
                    correctAnswer.setVisibility(View.INVISIBLE);
                    wrongAnswer1.setVisibility(View.INVISIBLE);
                    wrongAnswer2.setVisibility(View.INVISIBLE);
                    isShowingAnswers = false;
                }
            }
        });

        // Edit Button, bottom left anchor
        editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionToEdit = ((TextView) flashcardQuestion).getText().toString();
                String answerToEdit = ((TextView) flashcardAnswer).getText().toString();
                String wrongOptionToEdit1 = ((TextView) wrongAnswer1).getText().toString();
                String wrongOptionToEdit2 = ((TextView) wrongAnswer2).getText().toString();

                Intent editCardIntent = new Intent(MainActivity.this,
                        AddCardActivity.class);

                editCardIntent.putExtra("question", questionToEdit);
                editCardIntent.putExtra("answer", answerToEdit);
                editCardIntent.putExtra("wrong1", wrongOptionToEdit1);
                editCardIntent.putExtra("wrong2", wrongOptionToEdit2);
                MainActivity.this.startActivityForResult(editCardIntent, 100);
            }
        });

        // Add Button, bottom right anchor
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCardIntent = new Intent(MainActivity.this,
                        AddCardActivity.class);
                MainActivity.this.startActivityForResult(addCardIntent, 100);
            }

        });

        // Next card button, on top of eye icon
        nextCard.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                if (allFlashcards != null && allFlashcards.size() > 0) {
                    currentCardDisplayedIndex += 1;
                    if(currentCardDisplayedIndex < allFlashcards.size()){
                        ((TextView) findViewById(R.id.flashcard_question)).
                                setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                        ((TextView) findViewById(R.id.flashcard_answer)).
                                setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView flashcardQuestion = findViewById(R.id.flashcard_question);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer);
        TextView wrongAnswer1 = findViewById(R.id.choice1);
        TextView wrongAnswer2 = findViewById(R.id.choice2);
        TextView correctAnswer = findViewById(R.id.choice3);

        if (requestCode == 100 && resultCode == RESULT_OK) { // this 100 needs to match
            String newQuestion = data.getExtras().getString("question");
            String newAnswer = data.getExtras().getString("answer");
           // String newWrongOption1 = data.getExtras().getString("wrong1");
           // String newWrongOption2 = data.getExtras().getString("wrong2");
            flashcardQuestion.setText(newQuestion);
            flashcardAnswer.setText(newAnswer);
            //wrongAnswer1.setText(newWrongOption1);
           // wrongAnswer2.setText(newWrongOption2);
           // correctAnswer.setText(newAnswer);

            flashcardDatabase.insertCard(new Flashcard(newQuestion, newAnswer));
            allFlashcards = flashcardDatabase.getAllCards();

            Snackbar.make(flashcardQuestion,
                    "Created/Edited card successfully.",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}

