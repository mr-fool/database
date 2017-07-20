package cpsc471.rateit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteUsernameActivity extends AppCompatActivity {

    Button submitButton;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_username);

        submitButton = (Button) findViewById(R.id.submit_button);
        username = (TextView) findViewById(R.id.username);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(DeleteUsernameActivity.this, null, null, 1);
                Boolean wasDeleted = dbHelper.deleteAccount(username.getText().toString());

                if(wasDeleted) {
                    Toast.makeText(getApplicationContext(),
                            "User was deleted.",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "User not found.",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            }
        });

    }

}
