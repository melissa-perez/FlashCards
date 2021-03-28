package com.example.flashcards;

import android.animation.Animator;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import  java.util.Random;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    Random rand = new Random();
    CountDownTimer countDownTimer;

    boolean isShowingAnswers = false;
    boolean isShowingBack = false;
    int currentCardDisplayedIndex = 0;

    Flashcard cardToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Flash Cards");
        findViewById(R.id.timer).bringToFront();


        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        // UI display ID's
        TextView flashcardQuestion = findViewById(R.id.flashcard_question);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer);
        ImageView eyeToggle = findViewById(R.id.unhide_icon);
        ImageView editCard = findViewById(R.id.edit_icon);
        ImageView addCard = findViewById(R.id.add_icon);
        TextView wrongAnswer1 = findViewById(R.id.choice1);
        TextView wrongAnswer2 = findViewById(R.id.choice2);
        TextView correctAnswer = findViewById(R.id.choice3);
        ImageView nextCard = findViewById(R.id.cycle_icon);
        ImageView deleteCard = findViewById(R.id.trash_icon);
        ImageView emptyState = findViewById(R.id.empty_state);
        TextView emptyStateText = findViewById(R.id.empty_text);
        final Animation leftOutAnim = AnimationUtils.loadAnimation(flashcardQuestion.getContext(),
                R.anim.left_out);
        final Animation rightInAnim = AnimationUtils.loadAnimation(flashcardQuestion.getContext(),
                R.anim.right_in);

        countDownTimer = new CountDownTimer(16000, 1000) {
            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.timer)).setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
            }
        };

        // determine what to show on entry
        if (allFlashcards != null && allFlashcards.size() > 0) {

            currentCardDisplayedIndex = rand.nextInt(allFlashcards.size());
            cardToEdit = allFlashcards.get(currentCardDisplayedIndex);
            startTimer();

            flashcardQuestion.setVisibility(View.VISIBLE);

            emptyState.setVisibility(View.INVISIBLE);
            emptyStateText.setVisibility(View.INVISIBLE);

            flashcardQuestion.setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
            flashcardAnswer.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
            wrongAnswer1.setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1());
            wrongAnswer2.setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
            correctAnswer.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
        }
        else{
            flashcardQuestion.
                    setText("");
            flashcardAnswer.
                    setText("");
            correctAnswer.
                    setText("");
            wrongAnswer1.
                    setText("");
            wrongAnswer2.
                    setText("");
            flashcardQuestion.setVisibility(View.INVISIBLE);
            flashcardAnswer.setVisibility(View.INVISIBLE);
            wrongAnswer1.setVisibility(View.INVISIBLE);
            wrongAnswer2.setVisibility(View.INVISIBLE);
            correctAnswer.setVisibility(View.INVISIBLE);

            emptyState.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.VISIBLE);
        }

        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowingBack = true;

                // get the center for the clipping circle
                int cx = flashcardAnswer.getWidth() / 2;
                int cy = flashcardAnswer.getHeight() / 2;

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

                // create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(flashcardAnswer, cx,
                        cy, 0f, finalRadius);

                // hide the question and show the answer to prepare for playing the animation!
                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashcardAnswer.setVisibility(View.VISIBLE);

                anim.setDuration(2000);
                anim.start();
            }
        });

        flashcardAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowingBack = false;
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
                if (!isShowingAnswers && allFlashcards != null && allFlashcards.size() > 0 ) {
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

        // Add Button, bottom right anchor
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent addCardIntent = new Intent(MainActivity.this,
                        AddCardActivity.class);

                MainActivity.this.startActivityForResult(addCardIntent, 100);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        // Edit Button, bottom left anchor
        editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allFlashcards != null && allFlashcards.size() > 0) {
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
                    MainActivity.this.startActivityForResult(editCardIntent, 200);
                }
            }
        });

        // Next card button, on top of eye icon
        nextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allFlashcards != null && allFlashcards.size() > 1) {

                    // display a random card
                    currentCardDisplayedIndex = rand.nextInt(allFlashcards.size());

                    // update cardToEdit in case of edit request
                    cardToEdit = allFlashcards.get(currentCardDisplayedIndex);

                    // display the next random Card

                    flashcardQuestion.
                            setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                    flashcardAnswer.
                            setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
                    correctAnswer.
                            setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
                    wrongAnswer1.
                            setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1());
                    wrongAnswer2.
                            setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());


                    if (isShowingBack) {
                        flashcardAnswer.setVisibility(View.INVISIBLE);
                        flashcardAnswer.setVisibility(View.VISIBLE);
                        findViewById(R.id.flashcard_answer).startAnimation(rightInAnim);
                    }
                    else{
                        startTimer();
                        findViewById(R.id.flashcard_question).startAnimation(rightInAnim);
                    }
                }
            }
        });

        leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                findViewById(R.id.flashcard_question).startAnimation(leftOutAnim);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.flashcard_question).startAnimation(rightInAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // we don't need to worry about this method
            }
        });

        // Next card button, on top of eye icon
        deleteCard.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {

                int indexToDelete = currentCardDisplayedIndex;

                if (allFlashcards != null && allFlashcards.size() > 0) {

                    // remove card from Database
                    flashcardDatabase.deleteCard(cardToEdit.getQuestion());

                    // update our database
                    allFlashcards = flashcardDatabase.getAllCards();

                    if(allFlashcards.size() > 0){

                        if (indexToDelete >= 1) {
                            indexToDelete -= 1;

                            Flashcard prevCard = allFlashcards.get(indexToDelete);

                            // show the previous cards information
                            flashcardQuestion.
                                    setText(prevCard.getQuestion());
                            flashcardAnswer.
                                    setText(prevCard.getAnswer());
                            correctAnswer.
                                    setText(prevCard.getAnswer());
                            wrongAnswer1.
                                    setText(prevCard.getWrongAnswer1());
                            wrongAnswer2.
                                    setText(prevCard.getWrongAnswer2());

                            cardToEdit = prevCard;
                            currentCardDisplayedIndex = indexToDelete;
                        }
                        else if (indexToDelete == 0) {

                            Flashcard prevCard = allFlashcards.get(indexToDelete);

                            // show the previous cards information
                            flashcardQuestion.
                                    setText(prevCard.getQuestion());
                            flashcardAnswer.
                                    setText(prevCard.getAnswer());
                            correctAnswer.
                                    setText(prevCard.getAnswer());
                            wrongAnswer1.
                                    setText(prevCard.getWrongAnswer1());
                            wrongAnswer2.
                                    setText(prevCard.getWrongAnswer2());

                            cardToEdit = prevCard;
                            currentCardDisplayedIndex = indexToDelete;
                        }
                    }

                    if(allFlashcards.size() == 0)
                    {
                        flashcardQuestion.
                                setText("");
                        flashcardAnswer.
                                setText("");
                        correctAnswer.
                                setText("");
                        wrongAnswer1.
                                setText("");
                        wrongAnswer2.
                                setText("");
                        flashcardQuestion.setVisibility(View.INVISIBLE);
                        flashcardAnswer.setVisibility(View.INVISIBLE);
                        wrongAnswer1.setVisibility(View.INVISIBLE);
                        wrongAnswer2.setVisibility(View.INVISIBLE);
                        correctAnswer.setVisibility(View.INVISIBLE);

                        emptyState.setVisibility(View.VISIBLE);
                        emptyStateText.setVisibility(View.VISIBLE);
                        eyeToggle.setImageResource(R.drawable.ic_iconmonstr_eye_thin);
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
        Flashcard flashcard = null;
        ImageView emptyState = findViewById(R.id.empty_state);
        TextView emptyStateText = findViewById(R.id.empty_text);

        if (requestCode == 100 && resultCode == RESULT_OK) { // this 100 needs to match
            // return new information
            String newQuestion = data.getExtras().getString("question");
            String newAnswer = data.getExtras().getString("answer");
            String newWrongOption1 = data.getExtras().getString("wrong1");
            String newWrongOption2 = data.getExtras().getString("wrong2");

            //add the new card to database
            flashcard = new Flashcard(newQuestion, newAnswer,
                    newWrongOption1, newWrongOption2);

            // store current card index being added in case of edit request

            if (allFlashcards.size() == 0) {
                emptyState.setVisibility(View.INVISIBLE);
                emptyStateText.setVisibility(View.INVISIBLE);

                flashcardQuestion.setVisibility(View.VISIBLE);
            }

            // update databases
            flashcardDatabase.insertCard(flashcard);
            allFlashcards = flashcardDatabase.getAllCards();

            // store card just created and index and the current displaying information
            cardToEdit = flashcard;
            currentCardDisplayedIndex = allFlashcards.size() - 1;

            flashcardQuestion.setText(cardToEdit.getQuestion());
            flashcardAnswer.setText(cardToEdit.getAnswer());
            wrongAnswer1.setText(cardToEdit.getWrongAnswer1());
            wrongAnswer2.setText(cardToEdit.getWrongAnswer2());
            correctAnswer.setText(cardToEdit.getAnswer());

            Snackbar.make(flashcardQuestion,
                    "Created card successfully.",
                    Snackbar.LENGTH_SHORT)
                    .show();
        } else if (requestCode == 200 && resultCode == RESULT_OK) { // this 200 needs to match

            // other functions have store the card index and object, not best programming,
            // but no loops


            // return new information
            String newQuestion = data.getExtras().getString("question");
            String newAnswer = data.getExtras().getString("answer");
            String newWrongOption1 = data.getExtras().getString("wrong1");
            String newWrongOption2 = data.getExtras().getString("wrong2");

            //update the cardToEdit
            cardToEdit.setQuestion(newQuestion);
            cardToEdit.setAnswer(newAnswer);
            cardToEdit.setWrongAnswer1(newWrongOption1);
            cardToEdit.setWrongAnswer2(newWrongOption2);

            //update edited card in database
            flashcardDatabase.updateCard(cardToEdit);

            // display the new information on the Activity
            flashcardQuestion.setText(newQuestion);
            flashcardAnswer.setText(newAnswer);
            wrongAnswer1.setText(newWrongOption1);
            wrongAnswer2.setText(newWrongOption2);
            correctAnswer.setText(newAnswer);
            Snackbar.make(flashcardQuestion,
                    "Edited card successfully.",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void startTimer() {
        countDownTimer.cancel();
        countDownTimer.start();
    }
}