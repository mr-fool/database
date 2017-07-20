package cpsc471.rateit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ItemRateActivity extends AppCompatActivity {

    RatingBar submitRatingBar;
    RatingBar viewRatingBar;
    Button commentButton;
    String itemName;
    String subjectName;
    String loggedInUsername;
    private ArrayAdapter adapter;
    private ListView itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_rate);

        submitRatingBar = (RatingBar) findViewById(R.id.rating_bar_submit);
        viewRatingBar = (RatingBar) findViewById(R.id.rating_bar_view);
        TextView textView = (TextView) findViewById(R.id.title_item);
        commentButton = (Button) findViewById(R.id.button_comment);
        itemList = (ListView) findViewById(R.id.comment_list);

        Bundle bundle = getIntent().getExtras();
        itemName = bundle.getString("Item");
        subjectName = bundle.getString("Subject");
        loggedInUsername = bundle.getString("Username");

        textView.setText(itemName);

        configureCommentButton();
        configureCommentList();
        updateItemRating();

    }

    public void rateMe(View view){

        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);

        Rating rating = new Rating();
        rating.setRating(submitRatingBar.getRating());
        rating.setSubject(dbHelper.getSubject(subjectName));
        rating.setUser(dbHelper.getAccount(loggedInUsername));
        rating.setItem(dbHelper.getItem(itemName, subjectName));

        if(ratingExists(rating) && !dbHelper.isAdvertiser(loggedInUsername)){
            Toast.makeText(getApplicationContext(),
                    "Rating has been updated.",
                    Toast.LENGTH_SHORT).show();
            dbHelper.updateRating(rating);
        }
        else {
            dbHelper.addRating(rating);

            Toast.makeText(getApplicationContext(),
                    "Rating submitted successfully.",
                    Toast.LENGTH_SHORT).show();
        }
        updateItemRating();
        Intent intent = new Intent();
        setResult(RESULT_OK,intent );
        finish();
    }

    private void updateItemRating() {
        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        viewRatingBar.setRating((float) dbHelper.getRatingOfItem(dbHelper.getItem(itemName,subjectName)));
    }

    private void configureCommentButton() {
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submit = new Intent(ItemRateActivity.this, SubmitCommentActivity.class);
                submit.putExtra("Item", getIntent().getExtras().getString("Item"));
                submit.putExtra("Subject", getIntent().getExtras().getString("Subject"));
                submit.putExtra("Username", getIntent().getExtras().getString("Username"));
                startActivityForResult(submit,0);
            }
        });
    }

    private Boolean ratingExists(Rating rating) {
        DbHelper dbHelper = new DbHelper(this, null, null, 1);
        Rating doesThisExist = dbHelper.getRating(rating.getItem().getTitle(),
                                                  rating.getSubject().getTitle(),
                                                  rating.getUser().getUsername());


        if(doesThisExist != null){
            return true;
        }
        return false;
    }

    private void configureCommentList() {

        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        ArrayList<Comment> comments =
            dbHelper.getCommentsOfItem(dbHelper.getItem(getIntent().getExtras().getString("Item"),
                                                        getIntent().getExtras().getString("Subject")));

        Log.d("comments", dbHelper.getTableAsString("comments"));

        ArrayList<String> displayStrings = new ArrayList<>();
        for (Comment temp : comments) {
            displayStrings.add("User: " +
                               temp.getSubmitter().getUsername() + "\n" +
                               temp.getComment());

        }


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayStrings);
        itemList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        updateCommentList();
    }

    private void updateCommentList() {
        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        ArrayList<Comment> comments =
                dbHelper.getCommentsOfItem(dbHelper.getItem(getIntent().getExtras().getString("Item"),
                        getIntent().getExtras().getString("Subject")));

        ArrayList<String> displayStrings = new ArrayList<>();
        for (Comment temp : comments) {
            displayStrings.add("User: " +
                    temp.getSubmitter().getUsername() + "\n" +
                    temp.getComment());
        }
        adapter.clear();
        adapter.addAll(displayStrings);
        adapter.notifyDataSetChanged();
    }
}
