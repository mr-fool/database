package cpsc471.rateit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Feedback {
    private String _feedback;
    private Account _submitter;

    public Feedback(){

    }
    public Feedback(String feedback, Account _submitter){
        this._feedback = feedback;
        this._submitter = _submitter;
    }

    public void setFeedback(String feedback) {
        this._feedback = feedback;
    }

    public String getFeedback() {
        return this._feedback;
    }

    public void setSubmitter(Account submitter) {
        this._submitter = submitter;
    }

    public Account getSubmitter() {
        return this._submitter;
    }
}


