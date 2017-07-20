package cpsc471.rateit;

/**
 * Created by Kyle on 6/21/2017.
 */

public class Item {
    private String _title;
    private Subject _subject;
    private Account _submitter;

    public Item() {

    }

    public Item(String title, Subject subject, Account submitter){
        this._title = title;
        this._subject = subject;
        this._submitter = submitter;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public String getTitle() {
        return this._title;
    }

    public void setSubject(Subject subject) {
        this._subject = subject;
    }

    public Subject getSubject() {
        return this._subject;
    }


    public void setSubmitter(Account submitter) {
        this._submitter = submitter;
    }

    public Account getSubmitter() {
        return this._submitter;
    }
}
