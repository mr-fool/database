package cpsc471.rateit;

import java.sql.Date;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by Kyle on 6/13/2017.
 */

public class Subject {
    private String _title;
    private Account _submitter;

    public Subject() {

    }

    public Subject(String title, Account submitter){
        this._title = title;
        this._submitter = submitter;
    }

    public Subject(String title) {
        this._title = title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public String getTitle() {
        return this._title;
    }

    public void setSubmitter(Account submitter) {
        this._submitter = submitter;
    }

    public Account getSubmitter() {
        return this._submitter;
    }
}
