package ca.bcit.blood_pressure_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    TextView patientId;
    EditText editTextPatientId;
    TextView etreadingTime;
    TextView readingDate;
    TextView readingDateValue;
    TextView readingTimeValue;
    TextView readingCondition;
    Button buttonAddPatient;
    EditText systolicReading;
    EditText diastolicReading;

    DatabaseReference databaseBloodPressure;
//added
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

        databaseBloodPressure = FirebaseDatabase.getInstance().getReference("BloodPressure");

        editTextPatientId = findViewById(R.id.etUserID);
        readingDateValue = findViewById(R.id.tvReadingDateValue);
        readingTimeValue = findViewById(R.id.tvReadingTimeValue);
        readingCondition = findViewById(R.id.tvCondition);
        systolicReading = findViewById(R.id.etSystolicReading);
        diastolicReading = findViewById(R.id.etDiastolicReading);
        buttonAddPatient = findViewById(R.id.btnSubmit);

        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCondition(v);
            }
        });

    }
    private void addPatient() {
        String patientId = editTextPatientId.getText().toString().trim();
        String readingTime = readingTimeValue.getText().toString().trim();
        String readingDate = readingDateValue.getText().toString().trim();
        String condition = readingCondition.getText().toString().trim();
        int systolicReadingValue = Integer.parseInt(systolicReading.getText().toString());
        int diastolicReadingValue = Integer.parseInt(diastolicReading.getText().toString());

//            String lastName = editTextLastName.getText().toString().trim();
//            String school = spinnerSchool.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(patientId)) {
            Toast.makeText(this, "You must enter the patient's ID.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(condition)) {
            Toast.makeText(this, "You must enter a condition.", Toast.LENGTH_LONG).show();
            return;
        }

        String id = databaseBloodPressure.push().getKey();
        Patient patient = new Patient(patientId, systolicReadingValue, diastolicReadingValue, condition,
                readingTime, readingDate);

                //new Patient(patientId, systolicReading, diastolicReading, condition, readingTime, readingDate);

        Task setValueTask = databaseBloodPressure.child(id).setValue(patient);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this, "Patient added.", Toast.LENGTH_LONG).show();

                editTextPatientId.setText("");
//                    editTextLastName.setText("");

            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,
                        "something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void setCondition(View view) {
        final String normal = "Normal";
        final String elevated = "Elevated";
        final String stage1 = "High Blood Pressure (Stage 1)";
        final String stage2 = "High Blood Pressure (Stage 2)";
        final String hypertensiveCrisis = "Hypertensive Crisis";
        int systolic = Integer.parseInt(systolicReading.getText().toString());
        int diastolic = Integer.parseInt(diastolicReading.getText().toString());

        if (systolic < 120) {
            if (diastolic < 80) {
                readingCondition.setText(normal);
            } else if (diastolic <= 89) {
                readingCondition.setText(stage1);
            } else if (diastolic <= 120) {
                readingCondition.setText(stage2);
            } else {
                readingCondition.setText(hypertensiveCrisis);
            }
        } else if (systolic <= 129) {
            if (diastolic < 80) {
                readingCondition.setText(elevated);
            } else if (diastolic <= 89) {
                readingCondition.setText(stage1);
            } else if (diastolic <= 120) {
                readingCondition.setText(stage2);
            } else {
                readingCondition.setText(hypertensiveCrisis);
            }
        } else if (systolic <= 139) {
            if (diastolic > 120) {
                readingCondition.setText(hypertensiveCrisis);
            } else if (diastolic >= 90) {
                readingCondition.setText(stage2);
            } else {
                readingCondition.setText(stage1);
            }
        } else if (systolic <= 180) {
            if (diastolic > 120) {
                readingCondition.setText(hypertensiveCrisis);
            } else {
                readingCondition.setText(stage2);
            }
        } else {
            readingCondition.setText(hypertensiveCrisis);
        }
    }

}
