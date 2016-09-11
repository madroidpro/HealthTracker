package madroid.healthtracker.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import madroid.healthtracker.DatabaseHelper;
import madroid.healthtracker.R;

public class WeightTrackerActivity extends AppCompatActivity {
    public DatabaseHelper dbHelper;
    public String TABLE_NAME="daily_weight";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracker);
        dbHelper=new DatabaseHelper(getApplicationContext());
        getSupportActionBar().setTitle("Weight Tracker");
    }

    private void plotGraph(){
        Log.d("info_d",dbHelper.getRecordCount(TABLE_NAME)+"");
        if(dbHelper.getRecordCount(TABLE_NAME)> 4){
            DataPoint[] DataPointValues = new DataPoint[dbHelper.getRecordCount(TABLE_NAME)];
            Cursor resultSet = dbHelper.getTableData(TABLE_NAME);
            resultSet.moveToFirst();
            for (int i=0;i<dbHelper.getRecordCount(TABLE_NAME);i++){
                String dbDate= resultSet.getString(1);
                Double dbWeight=Double.parseDouble(resultSet.getString(2));
                DateFormat formatter;
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date  date = formatter.parse(dbDate);
                   // Log.d("info_d",date+""+"_"+i);
                    DataPoint dp = new DataPoint(date,dbWeight);
                    DataPointValues[i]=dp;
                }catch (Exception e){
                    Log.d("info_err",e+"");
                    dbHelper.closeDB();
                }
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
                if(weight.getText().toString().trim().length()==0){
                    failFlag = true;
                    weight.setError("This field is required");
                }
                if(failFlag == false){
                    final ContentValues weight_data=new ContentValues();
                    weight_data.put("date",datepicked.getText().toString());
                    weight_data.put("weight",weight.getText().toString());
                    dbHelper.insertTableData(TABLE_NAME,weight_data);
                    datepicked.setText("");
                    weight.setText("");
                    Toast.makeText(getApplicationContext(),"Data Saved",Toast.LENGTH_LONG).show();
                    plotGraph();
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
