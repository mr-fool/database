package cpsc471.rateit;

/**
 * Created by Kyle on 6/21/2017.
 */

public class Rating {
    private double _rating;
    private Subject _subject;
    private Item _item;
    private Account _user;

    public Rating() {

    }

    public Rating(double rating, Subject subject, Item item, Account user){
        this._rating = rating;
        this._subject = subject;
        this._item = item;
        this._user = user;
    }

    public void setRating(double rating) {
        this._rating = rating;
    }

    public double getRating() {
        return this._rating;
    }

    public void setSubject(Subject subject) {
        this._subject = subject;
    }

    public Subject getSubject() {
        return this._subject;
    }

    public void setItem(Item item) {
        this._item = item;
    }

    public Item getItem() {
        return this._item;
    }

    public void setUser(Account user) {
        this._user = user;
    }

    public Account getUser() {
        return this._user;
    }
}
