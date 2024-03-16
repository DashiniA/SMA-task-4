package uk.ac.wlv.geo_quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageButton;
import android.util.Log;
import android.app.Activity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_OPTION = "index";
    private static final String KEY_QUESTION = "question";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String KEY_CHEATING_STATUS = "status";
    private static final String KEY_BUTTON_ENABLE = "setEnable";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton, mBackButton;
    private TextView mQuestionTextView, success_rate;
    private float rate=0;


    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private ImageButton mImageButton;
    private Context messageResId;
    private ImageButton mbtn_previous;
    private TextView mSuccessRate;

    private int currentQuestion = 0;
    private boolean mIsCheater;
    private boolean[] alreadySelectedAnswers = new boolean[mQuestionBank.length];
    private boolean[] mCheatedQuestionID = new boolean[mQuestionBank.length];


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mCheatedQuestionID[mCurrentIndex] = CheatActivity.wasAnswerShown(data);
        }
    }
    private void updateQuestion(int QuestionNo){
        int quizIndex = mQuestionBank[QuestionNo].getTextResId();
        mQuestionTextView.setText(quizIndex);
        mTrueButton.setEnabled(!alreadySelectedAnswers[QuestionNo]);
        mFalseButton.setEnabled(!alreadySelectedAnswers[QuestionNo]);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (!alreadySelectedAnswers[mCurrentIndex]) {


//            if (mIsCheater&&!mCheatedOnQuestions[mCurrentIndex]) {
//                messageResId = R.string.judgment_toast;
//            } else {
                if (userPressedTrue == answerIsTrue) {
                    if (mCheatedQuestionID[mCurrentIndex]&&!alreadySelectedAnswers[mCurrentIndex]) {
                        messageResId = R.string.judgment_toast;
//                        Toast.makeText(this,"Cheater",Toast.LENGTH_SHORT).show();
                    } else {
                        messageResId = R.string.correct_toast;
                    }
                    rate = rate + ((1.0f / mQuestionBank.length) * 100);
                } else {
                    messageResId = R.string.incorrect_toast;
                }
            }
            alreadySelectedAnswers[mCurrentIndex] = true;
            updateQuestion(mCurrentIndex);
        //}

        Toast.makeText(this,messageResId, Toast.LENGTH_SHORT).show();
        mSuccessRate.setText("Success rate : " + String.format("%.2f%%", rate));

    }

    private void moveToNextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        mIsCheater = false;
        updateQuestion(mCurrentIndex);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_QUESTION, 0);
            rate = savedInstanceState.getFloat(KEY_OPTION,0);
            alreadySelectedAnswers[mCurrentIndex] = savedInstanceState.getBoolean(KEY_BUTTON_ENABLE, false);
            mCheatedQuestionID[mCurrentIndex] = savedInstanceState.getBoolean(KEY_CHEATING_STATUS, false);
            currentQuestion = savedInstanceState.getInt("CURRENT_QUIZ",0);

      }
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mSuccessRate = (TextView) findViewById(R.id.success_rate);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                moveToNextQuestion();
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                moveToNextQuestion();
            }
        });
        mNextButton = (ImageButton) findViewById(R.id.btn_previous);
        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moveToNextQuestion();
            }
        });
        mbtn_previous = (ImageButton) findViewById(R.id.next_button);
        mbtn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex>0){
                    mCurrentIndex = mCurrentIndex - 1;
                    updateQuestion(mCurrentIndex);
                }
            }
        });
        mSuccessRate.setText("Success rate : " + String.format("%.2f%%", rate));
        updateQuestion(mCurrentIndex);
        mCheatedQuestionID[mCurrentIndex] = false;

    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState );
        outState.putInt(KEY_QUESTION, mCurrentIndex);
        outState.putFloat(KEY_OPTION, rate);
        outState.putBoolean(KEY_CHEATING_STATUS, mCheatedQuestionID[mCurrentIndex]);
        outState.putBoolean(KEY_BUTTON_ENABLE, !alreadySelectedAnswers[mCurrentIndex]);
        outState.putInt("CURRENT_QUIZ", currentQuestion);

//        outState.putBooleanArray(KEY_CHEATED_QUESTIONS, mCheatedOnQuestions);
    }

}