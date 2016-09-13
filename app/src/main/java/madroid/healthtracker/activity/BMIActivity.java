package madroid.healthtracker.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
