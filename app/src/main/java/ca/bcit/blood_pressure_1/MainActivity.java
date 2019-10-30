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

    EditText systolicReading;
    EditText diastolicReading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView time = findViewById(R.id.tvReadingTimeValue);
        TextView date = findViewById(R.id.tvReadingDateValue);

//        Date currTime = Calendar.getInstance().getTime();
        String currTime = new SimpleDateFormat(
                "HH:mm:ss", Locale.getDefault()).format(new Date());
        String currDate = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault()).format(new Date());

        time.setText(currTime);
        date.setText(currDate);

    }

    public void setCondition() {
        systolicReading = findViewById(R.id.etSystolicReading);
        diastolicReading = findViewById(R.id.etDiastolicReading);
        int systolic = Integer.parseInt(systolicReading.getText().toString());
        int diastolic = Integer.parseInt(diastolicReading.getText().toString());


    }

    // test push

}
