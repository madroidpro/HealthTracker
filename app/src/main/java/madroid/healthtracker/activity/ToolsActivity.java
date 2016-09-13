package madroid.healthtracker.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
        getSupportActionBar().hide();
        InMobiSdk.init(getApplicationContext(), "263e7db887d14ab3b0c83553c24e8dd9"); //'this' is used specify context
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
