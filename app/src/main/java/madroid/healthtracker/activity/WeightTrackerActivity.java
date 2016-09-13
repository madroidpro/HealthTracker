package madroid.healthtracker.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import madroid.healthtracker.AlarmManagerBroadcastReceiver;
import madroid.healthtracker.DatabaseHelper;
import madroid.healthtracker.R;

public class WeightTrackerActivity extends AppCompatActivity {
    public DatabaseHelper dbHelper;
    public String TABLE_NAME="daily_weight";
    private int reminder_state=0;
    private SharedPreferences sharedPreferences=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracker);
        dbHelper=new DatabaseHelper(getApplicationContext());
        getSupportActionBar().setTitle("Weight Tracker");
        sharedPreferences = getSharedPreferences("HealthTrackerPreferences",MODE_PRIVATE);
        reminder_state=sharedPreferences.getInt("reminder_state",0);
        final CheckBox reminder = (CheckBox) findViewById(R.id.reminder);
        if(reminder_state == 0){
            sharedPreferences.edit().putInt("reminder_state",3).apply();
            reminderToggle(reminder);
        }
        if(reminder_state == 1){
            reminder.setChecked(false);
        }
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminderToggle(reminder);
            }
        });
    }

    private void reminderToggle(CheckBox reminder){
        if(reminder.isChecked()){
            sharedPreferences.edit().putInt("reminder_state",2).apply();
            Intent intent = new Intent(this, AlarmManagerBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this.getApplicationContext(),0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            scheduleAlarms(alarmManager, pendingIntent, getApplicationContext());
        }else{
            sharedPreferences.edit().putInt("reminder_state",1).apply();
            Intent intent = new Intent(this, AlarmManagerBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this.getApplicationContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }

    }

    public void scheduleAlarms(AlarmManager mgr, PendingIntent pi, Context context)
    {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, 7);
        calSet.set(Calendar.MINUTE, 15);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if(calSet.compareTo(calNow) <= 0){
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1);
        }
        Log.d("info_date_open",calNow.getTime()+"");
        Log.d("info_date_set",calSet.getTime()+"");
        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), calSet.getTimeInMillis(), pi);
    }

    private void plotGraph(){
        Log.d("info_d",dbHelper.getRecordCount(TABLE_NAME)+"");
        if(dbHelper.getRecordCount(TABLE_NAME)> 4){
            DataPoint[] DataPointValues = new DataPoint[dbHelper.getRecordCount(TABLE_NAME)];
            Cursor resultSet = dbHelper.getTableData(TABLE_NAME);
            resultSet.moveToFirst();
            for (int i=0;i<dbHelper.getRecordCount(TABLE_NAME);i++){
                long dbDate= Long.parseLong(resultSet.getString(1));
                Double dbWeight=Double.parseDouble(resultSet.getString(2));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dbDate);
                Date  date = calendar.getTime();
                  Log.d("info_d",date+""+"_"+i);
                    DataPoint dp = new DataPoint(date,dbWeight);
                    DataPointValues[i]=dp;
                resultSet.moveToNext();
            }
            dbHelper.closeDB();
            GraphView graph = (GraphView) findViewById(R.id.graph);
            GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
            gridLabelRenderer.setGridColor(getResources().getColor(R.color.colorPrimaryDark));
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(DataPointValues);
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(),new SimpleDateFormat("dd-MM")));
            graph.addSeries(series);
            series.setThickness(2);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(6);
            series.setColor(getResources().getColor(R.color.colorPrimary));
            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);
        }else {
            TextView graphHead = (TextView) findViewById(R.id.graphHead);
            graphHead.setText("Your graph will appear after saving 5 weight logs");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       final EditText datepicked = (EditText) findViewById(R.id.datePicked);
        final EditText weight= (EditText) findViewById(R.id.weight);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean failFlag = false;
                if(datepicked.getText().toString().trim().length()==0){
                    failFlag = true;
                    datepicked.setError("This field is required");
                }
                else if(weight.getText().toString().trim().length()==0){
                    failFlag = true;
                    weight.setError("This field is required");
                }
                if(failFlag == false){
                    final ContentValues weight_data=new ContentValues();
                    //String text = "2011-10-02 18:48:05.123";
                    DateFormat formatter;
                    formatter = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date  date = formatter.parse(datepicked.getText().toString());
                        System.out.println(date.getTime());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(date.getTime());
                        System.out.println(calendar.getTime());
                        weight_data.put("date",date.getTime());
                        weight_data.put("weight",weight.getText().toString());
                        dbHelper.insertTableData(TABLE_NAME,weight_data);
                        datepicked.setText("");
                        weight.setText("");
                        Toast.makeText(getApplicationContext(),"Data Saved",Toast.LENGTH_LONG).show();
                        plotGraph();
                    }catch (Exception e){
                        Log.d("info_err",e+"");
                    }
                }
            }
        });
        plotGraph();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            EditText editText = (EditText) getActivity().findViewById(R.id.datePicked);
            editText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(day).append("/").append(month + 1).append("/").append(year).append(" "));

        }
    }
}
