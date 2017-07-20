package cpsc471.rateit;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import static android.R.attr.rating;
import static android.webkit.WebSettings.PluginState.ON;
import static cpsc471.rateit.R.id.item;
import static cpsc471.rateit.R.id.subject;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 19;
    private static final String DATABASE_NAME = "RateIt.db";

    public static final String COLUMN_ID = "_id";

    private static final String TABLE_ACCOUNTS = "accounts";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_SUBJECTS = "subjects";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SUBMITTER = "submitter";

    private static final String TABLE_ADMINS = "administrators";
    private static final String COLUMN_ADMIN = "admin_username";

    private static final String TABLE_ADVERTISERS = "advertisers";

    private static final String TABLE_ITEMS = "items";
    private static final String COLUMN_SUBJECT = "subject";

    private static final String TABLE_RATINGS = "ratings";
    private static final String COLUMN_RATING = "rating";

    private static final String TABLE_FEEDBACK = "feedbacks";
    private static final String COLUMN_FEEDBACK = "feedback";

    private static final String TABLE_COMMENTS = "comments";
    private static final String COLUMN_COMMENT = "comment";

    private static final String COLUMN_ITEM = "item";

    public DbHelper(Context context,
                    String name,
                    SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE "
                + TABLE_ACCOUNTS
                + "("
                + COLUMN_USERNAME + " TEXT PRIMARY KEY NOT NULL,"
                + COLUMN_PASSWORD + " TEXT"
                + ")";

        String CREATE_SUBJECTS_TABLE = "CREATE TABLE "
                + TABLE_SUBJECTS
                + "("
                + COLUMN_TITLE + " TEXT PRIMARY KEY NOT NULL,"
                + COLUMN_SUBMITTER + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_SUBMITTER + ") REFERENCES " + TABLE_ACCOUNTS + "(" + COLUMN_USERNAME + ")" + " ON DELETE CASCADE"
                + ")";

        String CREATE_ADMINS_TABLE = "CREATE TABLE "
                + TABLE_ADMINS
                + "("
                + COLUMN_ADMIN + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_ADMIN + ") REFERENCES " + TABLE_ACCOUNTS + "(" + COLUMN_USERNAME + ")" + " ON DELETE CASCADE"
                + ")";

        String CREATE_ADVERTISER_TABLE = "CREATE TABLE "
                + TABLE_ADVERTISERS
                + "("
                + COLUMN_USERNAME + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_USERNAME + ") REFERENCES " + TABLE_ACCOUNTS + "(" + COLUMN_USERNAME + ")" + " ON DELETE CASCADE"
                + ")";

        String CREATE_ITEMS_TABLE = "CREATE TABLE "
                + TABLE_ITEMS
                + "("
                + COLUMN_TITLE + " TEXT PRIMARY KEY NOT NULL,"
                + COLUMN_SUBJECT + " TEXT,"
                + COLUMN_SUBMITTER + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_SUBJECT + ") REFERENCES " + TABLE_SUBJECTS + "(" + COLUMN_TITLE + ") ON DELETE CASCADE,"
                + "FOREIGN KEY(" + COLUMN_SUBMITTER + ") REFERENCES " + TABLE_ACCOUNTS + "(" + COLUMN_USERNAME + ") ON DELETE CASCADE"
                + ")";

        String CREATE_RATINGS_TABLE = "CREATE TABLE "
                + TABLE_RATINGS
                + "("
                + COLUMN_RATING + " DOUBLE,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_SUBJECT + " TEXT,"
                + COLUMN_SUBMITTER + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_TITLE + ") REFERENCES " + TABLE_ITEMS+ "(" + COLUMN_TITLE + ") ON DELETE CASCADE,"
                + "FOREIGN KEY(" + COLUMN_SUBJECT + ") REFERENCES " + TABLE_SUBJECTS + "(" + COLUMN_TITLE + ") ON DELETE CASCADE,"
                + "FOREIGN KEY(" + COLUMN_SUBMITTER + ") REFERENCES " + TABLE_ACCOUNTS + "(" + COLUMN_USERNAME + ") ON DELETE CASCADE"
                + ")";

        String CREATE_FEEDBACK_TABLE = "CREATE TABLE "
                + TABLE_FEEDBACK
                + "("
                + COLUMN_FEEDBACK + " TEXT,"
                + COLUMN_SUBMITTER + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_SUBMITTER + ") REFERENCES " + TABLE_ACCOUNTS + "(" + COLUMN_USERNAME + ") ON DELETE CASCADE"
                + ")";

        String CREATE_COMMENT_TABLE = "CREATE TABLE "
                + TABLE_COMMENTS
                + "("
                + COLUMN_COMMENT + " TEXT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_SUBJECT + " TEXT,"
                + COLUMN_SUBMITTER + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_TITLE + ") REFERENCES " + TABLE_ITEMS+ "(" + COLUMN_TITLE + ") ON DELETE CASCADE,"
                + "FOREIGN KEY(" + COLUMN_SUBJECT + ") REFERENCES " + TABLE_SUBJECTS + "(" + COLUMN_TITLE + ") ON DELETE CASCADE,"
                + "FOREIGN KEY(" + COLUMN_SUBMITTER + ") REFERENCES " + TABLE_ACCOUNTS + "(" + COLUMN_USERNAME + ") ON DELETE CASCADE"
                + ")";


        db.execSQL(CREATE_ACCOUNTS_TABLE);
        db.execSQL(CREATE_SUBJECTS_TABLE);
        db.execSQL(CREATE_ADMINS_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_RATINGS_TABLE);
        db.execSQL(CREATE_FEEDBACK_TABLE);
        db.execSQL(CREATE_COMMENT_TABLE);
        db.execSQL(CREATE_ADVERTISER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMINS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVERTISERS);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public void addAccount(Account account) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, account.getUsername());
        values.put(COLUMN_PASSWORD, account.getPassword());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_ACCOUNTS, null, values);

        if(account.getPassword().contains("admin"))
        {
            ContentValues value = new ContentValues();
            value.put(COLUMN_ADMIN, account.getUsername());
            db.insert(TABLE_ADMINS, null, value);
        }

        if(account.getPassword().contains("advertiser"))
        {
            ContentValues value = new ContentValues();
            value.put(COLUMN_USERNAME, account.getUsername());
            db.insert(TABLE_ADVERTISERS, null, value);
        }
        db.close();
    }

    public void addSubject(Subject subject) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, subject.getTitle());
        values.put(COLUMN_SUBMITTER,subject.getSubmitter().getUsername());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_SUBJECTS, null, values);
        db.close();
    }

    public void addItem(Item item) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, item.getTitle());
        values.put(COLUMN_SUBJECT, item.getSubject().getTitle());
        values.put(COLUMN_SUBMITTER,item.getSubmitter().getUsername());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }


    public void addRating(Rating rating) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, rating.getRating());
        values.put(COLUMN_TITLE, rating.getItem().getTitle());
        values.put(COLUMN_SUBJECT, rating.getSubject().getTitle());
        values.put(COLUMN_SUBMITTER, rating.getUser().getUsername());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_RATINGS, null, values);
        db.close();
    }

    public void addFeedback(Feedback feedback){
        ContentValues values = new ContentValues();
        values.put(COLUMN_FEEDBACK, feedback.getFeedback());
        values.put(COLUMN_SUBMITTER, feedback.getSubmitter().getUsername());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_FEEDBACK, null, values);
        db.close();
    }

    public void addComment(Comment comment){
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMENT, comment.getComment());
        values.put(COLUMN_TITLE, comment.getItem().getTitle());
        values.put(COLUMN_SUBJECT, comment.getItem().getSubject().getTitle());
        values.put(COLUMN_SUBMITTER, comment.getSubmitter().getUsername());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_COMMENTS, null, values);
        db.close();
    }

    public Account getAccount(String username) {
        String query = "Select * FROM " + TABLE_ACCOUNTS +
                       " WHERE " + COLUMN_USERNAME +
                       " =  \"" + username + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Account account = new Account();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            account.setUsername(cursor.getString(0));
            account.setPassword(cursor.getString(1));
            cursor.close();
        } else {
            account = null;
        }

        db.close();
        return account;
    }

    public Boolean isAdmin(String username) {
        String query = "SELECT * FROM " + TABLE_ADMINS +
                       " WHERE " + COLUMN_ADMIN +
                       " = \"" + username + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor!=null && cursor.getCount()>0 ) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    public Boolean isAdvertiser(String username) {
        String query = "SELECT * FROM " + TABLE_ADVERTISERS +
                " WHERE " + COLUMN_USERNAME +
                " = \"" + username + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor!=null && cursor.getCount()>0 ) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }


    // TODO : Get the account from the submitter column
    public ArrayList<String> getSubjects() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> subjects = new ArrayList<String>();

        Cursor allRows  = db.rawQuery("SELECT * FROM " + TABLE_SUBJECTS, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    if(name.toString().equals(COLUMN_TITLE))
                        subjects.add(allRows.getString(allRows.getColumnIndex(name)));
                }
            } while (allRows.moveToNext());
        }
        db.close();

        return subjects;
    }

    public ArrayList<String> getItemsOfSubject(String subjectTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> items = new ArrayList<String>();

        Cursor allRows  = db.rawQuery(" SELECT " + COLUMN_TITLE +
                                      " FROM " + TABLE_ITEMS + " AS i" +
                                      " WHERE i.subject = " + "\"" + subjectTitle + "\"", null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    if(name.toString().equals(COLUMN_TITLE))
                        items.add(allRows.getString(allRows.getColumnIndex(name)));
                }
            } while (allRows.moveToNext());
        }
        db.close();

        return items;
    }

    public ArrayList<Feedback> getFeedback() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Feedback> feedback = new ArrayList<>();

        Cursor allRows  = db.rawQuery("SELECT * FROM " + TABLE_FEEDBACK, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                String comment = new String();
                String submitter = new String();
                for (String name: columnNames) {

                    if(name.toString().equals(COLUMN_FEEDBACK))
                        comment = allRows.getString(allRows.getColumnIndex(name));
                    else if(name.toString().equals(COLUMN_SUBMITTER))
                        submitter = allRows.getString(allRows.getColumnIndex(name));
                }

                feedback.add(new Feedback(comment,getAccount(submitter)));
            } while (allRows.moveToNext());
        }
        db.close();

        return feedback;
    }

    public ArrayList<Comment> getCommentsOfItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Comment> comments = new ArrayList<>();

        Cursor allRows  = db.rawQuery(
                " SELECT * " +
                " FROM " + TABLE_COMMENTS + " AS c" +
                " WHERE c.subject = " + "\"" + item.getSubject().getTitle() + "\" AND " +
                         "c.title = " + "\"" + item.getTitle() + "\"", null);

        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                String comment = new String();
                String title = new String();
                String subject = new String();
                String submitter = new String();

                for (String name: columnNames) {

                    if(name.toString().equals(COLUMN_COMMENT))
                        comment = allRows.getString(allRows.getColumnIndex(name));
                    else if(name.toString().equals(COLUMN_TITLE))
                        title = allRows.getString(allRows.getColumnIndex(name));
                    else if(name.toString().equals(COLUMN_SUBJECT))
                        subject = allRows.getString(allRows.getColumnIndex(name));
                    else if(name.toString().equals(COLUMN_SUBMITTER))
                        submitter = allRows.getString(allRows.getColumnIndex(name));
                }

                comments.add(new Comment(comment, getItem(title,subject), getAccount(submitter)));
            } while (allRows.moveToNext());
        }
        db.close();

        return comments;
    }

    public double getRatingOfItem(Item item) {
        double rating = 0.0;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor allRows  = db.rawQuery(" SELECT avg(rating) " +
                " FROM " + TABLE_RATINGS +
                " WHERE title = " + "\"" + item.getTitle() + "\" AND "
                + "subject = " + "\"" + item.getSubject().getTitle() + "\"", null);

        if (allRows.moveToFirst() ){
            rating = allRows.getDouble(0);
        }
        db.close();

        return rating;
    }

    public double getBalanceOfAdvertiser(String username) {
        double rating = 0.0;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor allRows  = db.rawQuery(" SELECT count(rating) " +
                " FROM " + TABLE_RATINGS +
                " WHERE submitter = " + "\"" + username + "\"", null);

        if (allRows.moveToFirst() ){
            rating = allRows.getInt(0);
        }
        db.close();

        return rating;
    }

    public Subject getSubject(String subjectName) {
        String query = "Select * FROM " + TABLE_SUBJECTS +
                " WHERE "
                + COLUMN_TITLE + " =  \"" + subjectName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Subject subject = new Subject();
        String subjectTitle = "";
        String username = "";

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            subjectTitle = cursor.getString(0);
            username = cursor.getString(1);

            subject.setTitle(subjectTitle);
            subject.setSubmitter(getAccount(username));

            cursor.close();
        }
        else {
            subject = null;
        }

        db.close();

        return subject;
    }

    public Item getItem(String itemName, String subject) {

        String query = "Select * FROM " + TABLE_ITEMS +
                " WHERE "
                + COLUMN_TITLE + " =  \"" + itemName + "\" AND "
                +  COLUMN_SUBJECT + " =  \"" + subject + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Item item = new Item();
        String itemTitle = "";
        String subjectTitle = "";
        String username = "";

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            itemTitle = cursor.getString(0);
            subjectTitle = cursor.getString(1);
            username = cursor.getString(2);

            item.setTitle(itemTitle);
            item.setSubject(getSubject(subjectTitle));
            item.setSubmitter(getAccount(username));

            cursor.close();
        }
        else {
            item = null;
        }

        db.close();

        return item;
    }

    public Rating getRating(String itemName, String subjectName, String username) {
        String query = "Select * FROM " + TABLE_RATINGS +
                " WHERE "
                + COLUMN_TITLE + " =  \"" + itemName + "\" AND "
                + COLUMN_SUBJECT + " =  \"" + subjectName + "\" AND "
                + COLUMN_SUBMITTER + " = \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Rating rating = new Rating();
        double valueRated = 0f;
        String itemTitle = "";
        String subjectTitle = "";
        String username_ = "";

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            valueRated = cursor.getDouble(0);
            itemTitle = cursor.getString(1);
            subjectTitle = cursor.getString(2);
            username_ = cursor.getString(3);

            rating.setRating(valueRated);
            rating.setItem(getItem(itemTitle,subjectTitle));
            rating.setSubject(getSubject(subjectTitle));
            rating.setUser(getAccount(username_));

            cursor.close();
        }
        else {
            rating = null;
        }

        if(rating == null)
            Log.d("null", "rating is null");

        db.close();

        return rating;
    }

    public void updateRating(Rating rating) {
        String query = "UPDATE " + TABLE_RATINGS
                + " SET " + COLUMN_RATING + " = \"" + String.valueOf(rating.getRating()) + "\""
                + " WHERE "
                + COLUMN_TITLE + " =  \"" + rating.getItem().getTitle() + "\" AND "
                + COLUMN_SUBJECT + " =  \"" + rating.getSubject().getTitle() + "\" AND "
                + COLUMN_SUBMITTER + " = \"" + rating.getUser().getUsername() + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        db.close();

    }

    public boolean deleteAccount(String username) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_ACCOUNTS + " WHERE " + COLUMN_USERNAME + " =  \"" + username + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Account account = new Account();

        if (cursor.moveToFirst()) {
            account.setUsername(cursor.getString(0));
            db.delete(TABLE_ACCOUNTS, COLUMN_USERNAME + " = ?",
                    new String[] { account.getUsername() });
            cursor.close();
            result = true;
        }

        db.close();
        return result;
    }

    public boolean deleteSubject(String title) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_SUBJECTS + " WHERE " + COLUMN_TITLE + " =  \"" + title + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Subject subject = new Subject();

        if (cursor.moveToFirst()) {
            subject.setTitle(cursor.getString(0));
            db.delete(TABLE_SUBJECTS, COLUMN_TITLE + " = ?",
                    new String[] { subject.getTitle() });
            cursor.close();
            result = true;
        }

        db.close();
        return result;
    }

    public boolean deleteItem(String item, String subject) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_ITEMS +
                       " WHERE " + COLUMN_TITLE + " =  \"" + item + "\" AND " +
                       COLUMN_SUBJECT + " = \"" + subject + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.delete(TABLE_ITEMS, COLUMN_TITLE + " = ? AND " + COLUMN_SUBJECT + " = ?",
                    new String[] {item,subject});
            cursor.close();
            result = true;
        }

        db.close();
        return result;
    }

    public String getTableAsString(String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();
        String tableString = String.format("Table %s:\n", tableName);

        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }

}