package cpsc471.rateit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubmitSubjectActivity extends AppCompatActivity {


    private Button submitSubjectButton;
    private EditText title;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        submitSubjectButton = (Button) findViewById(R.id.submit_subject_button);
        title = (EditText) findViewById(R.id.title);

        configureSubmitButton();
    }

    private void configureSubmitButton() {
        submitSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitSubject();
            }
        });
    }

    private void submitSubject() {
        DbHelper dbHelper = new DbHelper(this, null, null, 1);
        Subject subject = new Subject(title.getText().toString());
        subject.setSubmitter(new Account(getAccountDetails(),""));

        if(subjectExists(subject)){
            Toast.makeText(getApplicationContext(),
                    "Subject " + subject.getTitle() + " already exists.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            dbHelper.addSubject(subject);
            Toast.makeText(getApplicationContext(),
                    "Subject submitted successfully.",
                    Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent();
        setResult(RESULT_OK,intent );
        finish();
    }

    private String getAccountDetails() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString("username");
    }

    private Boolean subjectExists(Subject subject) {
        DbHelper dbHelper = new DbHelper(this, null, null, 1);
        Subject doesThisExist = dbHelper.getSubject(subject.getTitle().toString());

        if(doesThisExist != null){
            return true;
        }

        return false;
    }

}
