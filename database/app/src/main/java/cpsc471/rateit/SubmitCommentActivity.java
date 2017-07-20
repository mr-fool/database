package cpsc471.rateit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubmitCommentActivity extends AppCompatActivity {


    private Button submitButton;
    private EditText commentEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_comment);

        submitButton = (Button) findViewById(R.id.submit);
        commentEntry = (EditText) findViewById(R.id.comment);
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
        Comment comment = new Comment();

        comment.setComment(commentEntry.getText().toString());
        comment.setSubmitter(dbHelper.getAccount(getIntent().getExtras().getString("Username")));
        comment.setItem(dbHelper.getItem(getIntent().getExtras().getString("Item"),
                                         getIntent().getExtras().getString("Subject")));

        dbHelper.addComment(comment);

        Toast.makeText(getApplicationContext(),
                    "Comment submitted.",
                    Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        setResult(RESULT_OK,intent );
        finish();
    }

}
