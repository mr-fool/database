package cpsc471.rateit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class FeedbackActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private ListView itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemList = (ListView) findViewById(R.id.feedback_list);

        configureFeedbackList();
    }

    private void configureFeedbackList() {
        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        ArrayList<Feedback> feedback = dbHelper.getFeedback();

        ArrayList<String> items = new ArrayList<>();
        for (Feedback temp : feedback) {
                items.add("User " + temp.getSubmitter().getUsername() + " submitted:\n" + temp.getFeedback());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        itemList.setAdapter(adapter);
    }

//    private void updateItemList() {
//        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
//        ArrayList<String> items = dbHelper.getItemsOfSubject(getIntent().getExtras().getString("Subject"));
//
//        adapter.clear();
//        adapter.addAll(items);
//        adapter.notifyDataSetChanged();
//    }

}
