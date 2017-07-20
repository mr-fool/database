package cpsc471.rateit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Comment {
    private String _comment;
    private Item _item;
    private Account _submitter;

    public Comment(){

    }
    public Comment(String comment, Item item, Account _submitter){
        this._comment = comment;
        this._item = item;
        this._submitter = _submitter;
    }

    public void setComment(String comment) {
        this._comment = comment;
    }

    public String getComment() {
        return this._comment;
    }

    public void setSubmitter(Account _submitter) {
        this._submitter = _submitter;
    }

    public Account getSubmitter() {
        return this._submitter;
    }

    public void setItem(Item item) {
        this._item = item;
    }

    public Item getItem() {
        return this._item;
    }
}