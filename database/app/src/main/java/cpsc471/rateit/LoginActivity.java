package cpsc471.rateit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.sign_in_button);
        registerButton = (Button) findViewById(R.id.register_button);

        configureButtonListeners();
    }

    private void configureButtonListeners() {
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
    }

    private void attemptLogin(){

        boolean validInput = checkIfInputIsValid();
        if(!validInput)
        {
            return;
        }

        boolean accountExists = checkIfUsernameExists();
        if(!accountExists) {
            Toast.makeText(getApplicationContext(),
                    "That account does not exist, please register one.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        boolean passwordCorrect = checkPassword();
        if(!passwordCorrect) {
            Toast.makeText(getApplicationContext(),
                    "Password incorrect, please try again.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        startMainActiviy();

    }

    private void attemptRegistration()
    {
        boolean validInput = checkIfInputIsValid();
        if(!validInput)
        {
            return;
        }

        boolean accountExists = checkIfUsernameExists();
        if(accountExists) {
            Toast.makeText(getApplicationContext(),
                    "That account exists, please register with a different username.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        Account account = new Account(username.getText().toString(),
                                      password.getText().toString());
        dbHelper.addAccount(account);

        Toast.makeText(getApplicationContext(),
                "Account created successfully.",
                Toast.LENGTH_SHORT).show();

        startMainActiviy();
    }

    private boolean checkIfInputIsValid() {

        boolean accountNameValid = isUsernameValid(username.getText().toString());
        if(!accountNameValid){
            Toast.makeText(getApplicationContext(),
                    "Account names must be alpha-numeric \n(a-z, A-Z, 0-9).",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean passwordValid = isPasswordValid(password.getText().toString());
        if(!passwordValid){
            Toast.makeText(getApplicationContext(),
                    "Password must be longer than 4 characters.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkIfUsernameExists() {
        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        Account account = dbHelper.getAccount(username.getText().toString());
        if (account != null) {
            return true;
        }
        return false;
     }

    private boolean checkPassword()
    {
        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        Account account = dbHelper.getAccount(username.getText().toString());

        if(account != null){
            if (password.getText().toString().equals(account.getPassword())) {
                return true;
            }
        }
        return false;
    }

    private void startMainActiviy() {
        Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
        intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    private boolean isUsernameValid(String user) {
        // a-z, A-Z, 0-9
        boolean hasNonAlpha = user.matches("^.*[^a-zA-Z0-9 ].*$");
        return !hasNonAlpha;
    }

    private boolean isPasswordValid(String pass){
        return (pass.length() >= 4);
    }
}

