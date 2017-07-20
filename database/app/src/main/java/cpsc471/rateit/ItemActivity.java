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

public class ItemActivity extends AppCompatActivity {

    public RatingBar ratingBar;
    private ArrayAdapter adapter;
    private ListView itemList;
    private Button submitItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.title_item);

        itemList = (ListView) findViewById(R.id.item_list);
        submitItemButton = (Button) findViewById(R.id.button_submit_item);

        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String itemName = bundle.getString("Subject");

        textView.setText(itemName);
        configureSubjectList();
        configureSubmitButton();
    }
    public void rateMe(View view){

        Toast.makeText(getApplicationContext(),
                String.valueOf(ratingBar.getRating()), Toast.LENGTH_LONG).show();
    }

    private void configureSubjectList() {

        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        ArrayList<String> subjects =
            dbHelper.getItemsOfSubject(getIntent().getExtras().getString("Subject"));

        Collections.sort(subjects, new Comparator<String>() {
            public int compare(String s1, String s2) {
                DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
                Item i1 = new Item();
                Item i2 = new Item();
                Subject subject = new Subject();
                subject.setTitle(getIntent().getExtras().getString("Subject"));

                i1.setTitle(s1);
                i2.setTitle(s2);
                i1.setSubject(subject);
                i2.setSubject(subject);

                if (dbHelper.getRatingOfItem(i1) > dbHelper.getRatingOfItem(i2)) return -1;
                if (dbHelper.getRatingOfItem(i1) < dbHelper.getRatingOfItem(i2)) return 1;
                return 0;
            }
        });

        ArrayList<String> displayStrings = new ArrayList<>();
        for (String temp : subjects) {
            displayStrings.add(temp + "\nRating: " +
                    String.format("%.2f",dbHelper.getRatingOfItem(dbHelper.getItem(temp, getIntent().getExtras().getString("Subject")))));
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayStrings);

        itemList.setAdapter(adapter);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent rating = new Intent(getApplicationContext(), ItemRateActivity.class);
                // TODO : Pass in Subject class when it is made
                String item =  parent.getAdapter().getItem(position).toString();
                rating.putExtra("Item", item.substring(0, item.indexOf("\n")));
                rating.putExtra("Subject", getIntent().getExtras().getString("Subject"));
                rating.putExtra("Username", getIntent().getExtras().getString("Username"));
                startActivityForResult(rating,0);
            }
        });
    }

    private void configureSubmitButton() {
        submitItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submit = new Intent(ItemActivity.this, SubmitItemActivity.class);
                submit.putExtra("Subject", getIntent().getExtras().getString("Subject"));
                submit.putExtra("Username", getIntent().getExtras().getString("Username"));
                startActivityForResult(submit,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        updateItemList();
    }

    private void updateItemList() {
        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        ArrayList<String> items = dbHelper.getItemsOfSubject(getIntent().getExtras().getString("Subject"));

        Collections.sort(items, new Comparator<String>() {
            public int compare(String s1, String s2) {
                DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
                Item i1 = new Item();
                Item i2 = new Item();
                Subject subject = new Subject();
                subject.setTitle(getIntent().getExtras().getString("Subject"));

                i1.setTitle(s1);
                i2.setTitle(s2);
                i1.setSubject(subject);
                i2.setSubject(subject);

                if (dbHelper.getRatingOfItem(i1) > dbHelper.getRatingOfItem(i2)) return -1;
                if (dbHelper.getRatingOfItem(i1) < dbHelper.getRatingOfItem(i2)) return 1;
                return 0;
            }
        });

        ArrayList<String> displayStrings = new ArrayList<>();
        for (String temp : items) {
            displayStrings.add(temp + "\nRating: " +
                    String.format("%.2f",dbHelper.getRatingOfItem(dbHelper.getItem(temp, getIntent().getExtras().getString("Subject")))));
        }

        adapter.clear();
        adapter.addAll(displayStrings);
        adapter.notifyDataSetChanged();
    }

}
