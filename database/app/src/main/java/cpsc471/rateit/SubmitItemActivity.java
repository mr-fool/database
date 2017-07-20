package cpsc471.rateit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubmitItemActivity extends AppCompatActivity {


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
                submitItem();
            }
        });
    }

    private void submitItem() {
        DbHelper dbHelper = new DbHelper(this, null, null, 1);

        Item item = new Item();
        item.setTitle(title.getText().toString());
        item.setSubject(new Subject(getIntent().getExtras().getString("Subject"), new Account()));
        item.setSubmitter(new Account(getIntent().getExtras().getString("Username"),""));

        if(itemExists(item)){
            Toast.makeText(getApplicationContext(),
                    "Item " + item.getTitle() + " already exists in subject " + item.getSubject().getTitle(),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            dbHelper.addItem(item);
            Toast.makeText(getApplicationContext(),
                    "Item submitted successfully.",
                    Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent();
        setResult(RESULT_OK,intent );
        finish();
    }

    private Boolean itemExists(Item item) {
        DbHelper dbHelper = new DbHelper(this, null, null, 1);
        Item doesThisExist = dbHelper.getItem(item.getTitle(), item.getSubject().getTitle());

        if(doesThisExist != null){
            return true;
        }

        return false;
    }
}
