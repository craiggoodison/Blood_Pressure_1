package ca.bcit.blood_pressure_1;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String NORMAL = "Normal";
    private final String ELEVATED = "Elevated";
    private final String HIGH1 = "High Blood Pressure (Stage 1)";
    private final String HIGH2 = "High Blood Pressure (Stage 2)";
    private final String HYPERTENSIVECRISIS = "Hypertensive Crisis";

    EditText systolicReading;
    EditText diastolicReading;
    TextView conditionValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView time = findViewById(R.id.tvReadingTimeValue);
        TextView date = findViewById(R.id.tvReadingDateValue);

//        Date currTime = Calendar.getInstance().getTime();
        String currTime = new SimpleDateFormat(
                "HH:mm", Locale.getDefault()).format(new Date());
        String currDate = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault()).format(new Date());

        time.setText(currTime);
        date.setText(currDate);

    }

    public void setCondition(View view) {
        conditionValue = findViewById(R.id.tvCondition);
        systolicReading = findViewById(R.id.etSystolicReading);
        diastolicReading = findViewById(R.id.etDiastolicReading);
        int systolic = Integer.parseInt(systolicReading.getText().toString());
        int diastolic = Integer.parseInt(diastolicReading.getText().toString());

        // logic not fully working...
        if (systolic < 120 && diastolic < 80) {
            conditionValue.setText(NORMAL);
        } else if ((systolic >= 120 && systolic <= 129) && diastolic < 80) {
            conditionValue.setText(ELEVATED);
        } else if ((systolic >= 130 && systolic <= 139) || (diastolic >= 80 && diastolic <= 89)) {
            conditionValue.setText(HIGH1);
        } else if ((systolic > 140 && systolic < 180) || (diastolic > 90 && diastolic < 120)) {
            conditionValue.setText(HIGH2);
        } else {
            conditionValue.setText(HYPERTENSIVECRISIS);
        }

    }

    // test push

}
