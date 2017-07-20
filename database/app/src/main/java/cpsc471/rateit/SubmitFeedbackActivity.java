package cpsc471.rateit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static cpsc471.rateit.R.id.subject;

public class SubmitFeedbackActivity extends AppCompatActivity {


    private Button submitButton;
    private EditText comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_feedback);

        submitButton = (Button) findViewById(R.id.submit);
        comment = (EditText) findViewById(R.id.comment);
        configureSubmitButton();
    }

    private void configureSubmitButton() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        DbHelper dbHelper = new DbHelper(this, null, null, 1);
        Feedback feedback = new Feedback();

        feedback.setFeedback(comment.getText().toString());
        feedback.setSubmitter(dbHelper.getAccount(getIntent().getExtras().getString("Username")));

        dbHelper.addFeedback(feedback);
        Log.d("test", dbHelper.getTableAsString("feedbacks"));

        Toast.makeText(getApplicationContext(),
                    "Feedback submitted.",
                    Toast.LENGTH_SHORT).show();

        finish();
    }

}
