package uk.ac.wlv.geo_quiz;

//import static uk.ac.wlv.geo_quiz.MainActivity.KEY_CHEATING_STATUS;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE = "uk.ac.wlv.geo_quiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "uk.ac.wlv.geo_quiz.answer_shown";
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private boolean mIsAnswerShown;


    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    private void displayAnswer() {
        if (mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
        setAnswerShownResult(true);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswer = (Button) findViewById(R.id.show_answer_button);

        if(savedInstanceState != null){
            mIsAnswerShown = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN, false); // Restore cheating status
//            if(mIsAnswerShown) {
//                setAnswerShownResult(true);
//                showAnswer();
//            }
            displayAnswer();
        }
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            displayAnswer();
            }
        });
    }

//    private void showAnswer() {
//    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_ANSWER_SHOWN, mIsAnswerShown);
    }
}