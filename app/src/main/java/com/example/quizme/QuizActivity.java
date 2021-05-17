package com.example.quizme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizme.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {
    ActivityQuizBinding binding;
    ArrayList<Question> questions;
    Question question;
    int index = 0;
    CountDownTimer timer;
    FirebaseFirestore database;
    ProgressDialog dialog;
    int correctAnswer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        questions = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Calculating Your Score......");
        database = FirebaseFirestore.getInstance();
        final String catId = getIntent().getStringExtra("catId");
        Random random = new Random();
        final int rand = random.nextInt(20);
        database.collection("categories")
                .document(catId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index", rand)
                .orderBy("index")
                .limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() < 5) {
                    database.collection("categories")
                            .document(catId)
                            .collection("questions")
                            .whereLessThanOrEqualTo("index", rand)
                            .orderBy("index")
                            .limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Question question = snapshot.toObject(Question.class);
                                questions.add(question);
                            }
                            setNextQuestion();
                        }
                    });
                } else {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Question question = snapshot.toObject(Question.class);
                        questions.add(question);
                    }
                    setNextQuestion();
                }
            }
        });

        resetTimer();

    }

    void resetTimer() {
        timer = new CountDownTimer(40000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                setNextQuestion();
                finish();
            }
        };
    }

    void showAnswer() {
        if (question.getAnswer().equals(binding.Option1.getText().toString()))
            binding.Option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (question.getAnswer().equals(binding.Option2.getText().toString()))
            binding.Option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (question.getAnswer().equals(binding.Option3.getText().toString()))
            binding.Option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (question.getAnswer().equals(binding.Option4.getText().toString()))
            binding.Option4.setBackground(getResources().getDrawable(R.drawable.option_right));
    }

    void setNextQuestion() {
        if (timer != null) {
            timer.cancel();
        }
        timer.start();
        if (index < questions.size()) {
            binding.QuestionCounter.setText(String.format("%d/%d", (index + 1), (questions.size())));
            question = questions.get(index);
            binding.Questions.setText(question.getQuestion());
            binding.Option1.setText(question.getOption1());
            binding.Option2.setText(question.getOption2());
            binding.Option3.setText(question.getOption3());
            binding.Option4.setText(question.getOption4());

        }
    }

    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        if (selectedAnswer.equals(question.getAnswer())) {
            correctAnswer++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }

    void reset() {
        binding.Option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.Option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.Option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.Option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.Option1:
            case R.id.Option2:
            case R.id.Option3:
            case R.id.Option4:
                if (timer != null)
                    timer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);
//                Toast.makeText(this, "Option Clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.next:
                reset();
                if (index <= questions.size()) {
                    index++;
                    setNextQuestion();

                }
                else {
                    dialog.show();
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswer);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    finish();
                }

//                    Toast.makeText(this, "Quiz is Finished.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}