package madroid.healthtracker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;

import madroid.healthtracker.R;

/**
 * Created by madroid on 11-09-2016.
 */
public class BMIMetricsFragment extends Fragment {

    protected View view;
    public BMIMetricsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.bmi_metric_fragment,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        final EditText bmi_weight_edit = (EditText) view.findViewById(R.id.bmi_weight);
        final EditText bmi_height_edit = (EditText) view.findViewById(R.id.bmi_height);

        final Button calculate= (Button) view.findViewById(R.id.bmi_calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View cview) {

                boolean failFlag = false;
                if(bmi_weight_edit.getText().toString().trim().length()==0){
                    failFlag = true;
                    bmi_weight_edit.setError("This field is required");
                }
                if(bmi_height_edit.getText().toString().trim().length()==0){
                    failFlag = true;
                    bmi_height_edit.setError("This field is required");
                }
                if(failFlag == false){
                    double bmi_weight= Double.parseDouble(bmi_weight_edit.getText().toString());
                    double bmi_height=  Double.parseDouble(bmi_height_edit.getText().toString());
                    calculateBMI(bmi_weight,bmi_height);
                    getActivity().getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
                }
            }
        });
    }

    private void calculateBMI(double bmi_weight, double bmi_height){
        // BMI formula ( weight / height * height )
        double result = (bmi_weight/(bmi_height*bmi_height))*10000;
        TextView bmi_info= (TextView) view.findViewById(R.id.bmi_info);
        TextView bmi_value= (TextView) view.findViewById(R.id.bmi_value);
        ImageView bmi_img = (ImageView) view.findViewById(R.id.bmi_img);
        if(result < 18.5){
            //under weight
            bmi_img.setImageDrawable(getResources().getDrawable(R.drawable.below));
            bmi_info.setText(getResources().getString(R.string.underweight));
            bmi_value.setText(Math.round(result)+"");
        }else if(result > 18.5 && result < 24.9){
            //Normal weight
            bmi_img.setImageDrawable(getResources().getDrawable(R.drawable.smile));
            bmi_info.setText(getResources().getString(R.string.normal));
            bmi_value.setText(Math.round(result)+"");
        }else if(result > 25 && result < 29.9 ){
            //Overweight
            bmi_img.setImageDrawable(getResources().getDrawable(R.drawable.sad));
            bmi_info.setText(getResources().getString(R.string.overweight));
            bmi_value.setText(Math.round(result)+"");
        }else{
            //obese
            bmi_img.setImageDrawable(getResources().getDrawable(R.drawable.sad));
            bmi_info.setText(getResources().getString(R.string.obese));
            bmi_value.setText(Math.round(result)+"");
        }
        Log.d("info_result",result+"");
    }
}
