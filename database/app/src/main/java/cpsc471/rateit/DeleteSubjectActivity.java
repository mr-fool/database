package cpsc471.rateit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteSubjectActivity extends AppCompatActivity {

    Button submitButton;
    TextView subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_subject);

        submitButton = (Button) findViewById(R.id.submit_button);
        subject = (TextView) findViewById(R.id.subject);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(DeleteSubjectActivity.this, null, null, 1);
                Boolean wasDeleted = dbHelper.deleteSubject(subject.getText().toString());

                if(wasDeleted) {
                    Toast.makeText(getApplicationContext(),
                            "Subject was deleted.",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Subject not found.",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            }
        });

    }

}
