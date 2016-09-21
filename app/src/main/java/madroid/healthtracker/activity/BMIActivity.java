package madroid.healthtracker.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.inmobi.ads.InMobiBanner;

import madroid.healthtracker.R;
import madroid.healthtracker.adapter.ViewPagerAdapter;
import madroid.healthtracker.fragment.BMIImperialFragment;
import madroid.healthtracker.fragment.BMIMetricsFragment;

public class BMIActivity extends AppCompatActivity {

    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        getSupportActionBar().setTitle("BMI Calculator");

        InMobiBanner banner = (InMobiBanner)findViewById(R.id.banner);
        banner.load();
    }

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
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BMIMetricsFragment(),"Metrics");
        adapter.addFragment(new BMIImperialFragment(),"US/Imperial");
        viewPager.setAdapter(adapter);
    }


}
