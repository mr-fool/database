package cpsc471.rateit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import static android.R.id.list;
import static cpsc471.rateit.R.id.username;

public class SubjectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static int VISIBLE = 1;
    private static int NOT_VISIBLE = 0;
    private Button submitButton;
    private DrawerLayout drawer;
    private ArrayAdapter adapter;
    private ListView listView;
    private TextView usernameLabel;
    private TextView accessLevelLabel;
    private Toolbar toolbar;
    private View header;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.list_subject);
        submitButton = (Button) findViewById(R.id.submit_button);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        usernameLabel = (TextView) header.findViewById(R.id.profile_username);
        accessLevelLabel = (TextView) header.findViewById(R.id.profile_accesslevel);

        setSupportActionBar(toolbar);
        configureSubmitButton();
        configureProfile();
        configureMenuItems();
        configureActionBar();
        configureSubjectList();
    }

    private void configureActionBar() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureSubmitButton() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submit = new Intent(SubjectActivity.this, SubmitSubjectActivity.class);
                Bundle bundle = getIntent().getExtras();
                String userInfo = bundle.getString("username");
                submit.putExtra("username", userInfo);

                startActivityForResult(submit,0);
            }
        });
    }

    private void configureProfile()
    {
        String username = getIntent().getExtras().getString("username");
        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);

        usernameLabel.setText("Username: " + username );

        if(dbHelper.isAdmin(username) == true) {
            accessLevelLabel.setText("Access Level: Administrator");
        }
        else if(dbHelper.isAdvertiser(username) == true) {
            accessLevelLabel.setText("Access Level: Advertiser\nBalance: $" + String.valueOf(dbHelper.getBalanceOfAdvertiser(username)));
        }
        else {
            accessLevelLabel.setText("Access Level: User");
        }

    }

    private void configureMenuItems() {
        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        Boolean isAdmin = dbHelper.isAdmin(getIntent().getExtras().getString("username"));
        Menu nav_Menu = navigationView.getMenu();

        if(isAdmin) {
            nav_Menu.findItem(R.id.nav_delete_subject).setVisible(true);
            nav_Menu.findItem(R.id.nav_delete_item).setVisible(true);
            nav_Menu.findItem(R.id.nav_delete_account).setVisible(true);
            nav_Menu.findItem(R.id.nav_view_feedback).setVisible(true);
            nav_Menu.findItem(R.id.nav_submit_feedback).setVisible(false);
        }
        else {
            nav_Menu.findItem(R.id.nav_delete_subject).setVisible(false);
            nav_Menu.findItem(R.id.nav_delete_item).setVisible(false);
            nav_Menu.findItem(R.id.nav_delete_account).setVisible(false);
            nav_Menu.findItem(R.id.nav_view_feedback).setVisible(false);
            nav_Menu.findItem(R.id.nav_submit_feedback).setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_delete_subject) {
            Intent intent = new Intent(this, DeleteSubjectActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (id == R.id.nav_delete_item) {
            Intent intent = new Intent(this, DeleteItemActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (id == R.id.nav_delete_account) {
            Intent intent = new Intent(this, DeleteUsernameActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (id == R.id.nav_view_feedback) {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.nav_submit_feedback) {
            Intent intent = new Intent(this, SubmitFeedbackActivity.class);
            intent.putExtra("Username", getIntent().getExtras().getString("username"));
            startActivityForResult(intent, 0);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void configureSubjectList() {

        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        ArrayList<String> subjects = dbHelper.getSubjects();
        Collections.sort(subjects,String.CASE_INSENSITIVE_ORDER);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjects);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent rating = new Intent(getApplicationContext(), ItemActivity.class);
                // TODO : Pass in Subject class when it is made
                String subject =  parent.getAdapter().getItem(position).toString();
                rating.putExtra("Subject", subject);
                rating.putExtra("Username",  getIntent().getExtras().getString("username"));
                startActivity(rating);
            }
        });
    }

    private void updateListContents() {
        DbHelper dbHelper = new DbHelper(getApplicationContext(), null, null, 1);
        ArrayList<String> subjects = dbHelper.getSubjects();
        Collections.sort(subjects,String.CASE_INSENSITIVE_ORDER);
        adapter.clear();
        adapter.addAll(subjects);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        updateListContents();
    }
}
