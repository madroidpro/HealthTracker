package madroid.healthtracker.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.inmobi.ads.InMobiBanner;
import com.inmobi.sdk.InMobiSdk;

import java.util.ArrayList;
import java.util.List;

import madroid.healthtracker.R;
import madroid.healthtracker.adapter.ToolsListAdapter;

public class ToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        InMobiSdk.init(getApplicationContext(), "263e7db887d14ab3b0c83553c24e8dd9"); //'this' is used specify context
    }

    // Initiating Menu XML file (menu.xml)

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.menu_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi,Download HealthTracker Android App from https://play.google.com/store/apps/details?id="+getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            case R.id.menu_rate:
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName()));
                startActivity(rateIntent);
                //Toast.makeText(MainActivity.this, "Rate is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_history:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"madroid.pro@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback HealthTracker");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, " ");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
                //Toast.makeText(MainActivity.this, "Rate is Selected", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<ListTitles> list= new ArrayList<>();

        ListTitles lt = new ListTitles("BMI Calculator",1);
        list.add(lt);

        lt = new ListTitles("Weight Tracker",2);
        list.add(lt);

        ToolsListAdapter toolsListAdapter = new ToolsListAdapter(list,getApplicationContext());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.toolsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(toolsListAdapter);

        InMobiBanner banner = (InMobiBanner)findViewById(R.id.banner);
        banner.load();
    }

    public class ListTitles{
        public String title;
        public int titleId;

        public ListTitles(String title, int titleId) {
            this.title = title;
            this.titleId = titleId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTitleId() {
            return titleId;
        }

        public void setTitleId(int titleId) {
            this.titleId = titleId;
        }
    }
}
