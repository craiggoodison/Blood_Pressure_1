package ca.bcit.blood_pressure_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText editTextPatientId;
    TextView readingDateValue;
    TextView readingTimeValue;
    TextView readingCondition;
    Button buttonAddPatient;
    EditText systolicReading;
    EditText diastolicReading;

    String currTime;
    String currDate;

    SharedPreferences savedValues;

    DatabaseReference databaseBloodPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPatientId = findViewById(R.id.etUserID);
        readingDateValue = findViewById(R.id.tvReadingDateValue);
        readingTimeValue = findViewById(R.id.tvReadingTimeValue);
        readingCondition = findViewById(R.id.tvCondition);
        systolicReading = findViewById(R.id.etSystolicReading);
        diastolicReading = findViewById(R.id.etDiastolicReading);
        buttonAddPatient = findViewById(R.id.btnSubmit);

        currTime = new SimpleDateFormat(
                "HH:mm:ss", Locale.getDefault()).format(new Date());
        currDate = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault()).format(new Date());

        readingTimeValue.setText(currTime);
        readingDateValue.setText(currDate);

        databaseBloodPressure = FirebaseDatabase.getInstance().getReference("BloodPressure");

        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setCondition(v))
                    addPatient();
            }
        });

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

    }

//    /**
////     * Overrides onResume.
////     */
////    @Override
////    public void onResume() {
////        super.onResume();
////
////        // get the instance variables
////        currDate = savedValues.getString("ReadingDate", currDate);
////        currTime = savedValues.getString("ReadingTime", currTime);
////
////        readingDateValue.setText(currDate);
////        readingTimeValue.setText(currTime);
////
////    }

    /**
     * Overrides onPause.
     */
    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("ReadingDate", currDate);
        editor.putString("ReadingTime", currTime);
        editor.commit();

        super.onPause();
    }

    /**
     * Adds patient into Firebase Database.
     */
    private void addPatient() {
        String patientId = editTextPatientId.getText().toString().trim();
        String readingTime = readingTimeValue.getText().toString().trim();
        String readingDate = readingDateValue.getText().toString().trim();
        String condition = readingCondition.getText().toString().trim();
        int systolicReadingValue = Integer.parseInt(systolicReading.getText().toString());
        int diastolicReadingValue = Integer.parseInt(diastolicReading.getText().toString());

        if (TextUtils.isEmpty(patientId)) {
            Toast.makeText(this, "ID field empty.", Toast.LENGTH_LONG).show();
            return;
        }

        String id = databaseBloodPressure.push().getKey();
        Patient patient = new Patient(id, patientId, systolicReadingValue, diastolicReadingValue,
                condition, readingTime, readingDate);

                //new Patient(patientId, systolicReading, diastolicReading, condition, readingTime, readingDate);

        Task setValueTask = databaseBloodPressure.child(id).setValue(patient);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this, "Patient added.", Toast.LENGTH_LONG).show();

                editTextPatientId.setText("");
                readingCondition.setText("");
                systolicReading.setText("");
                diastolicReading.setText("");

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

    /**
     * Determines condition based on systolic and diastolic readings.
     * @param view
     */
    public boolean setCondition(View view) {
        final String normal = "Normal";
        final String elevated = "Elevated";
        final String stage1 = "High Blood Pressure (Stage 1)";
        final String stage2 = "High Blood Pressure (Stage 2)";
        final String hypertensiveCrisis = "Hypertensive Crisis";

        if (systolicReading.getText().toString().equals("") ||
                systolicReading.getText().toString().equals("")) {
            Toast.makeText(this, "Systolic or Diastolic field(s) empty.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

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
                hypertensiveWarning(view);
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
                hypertensiveWarning(view);
            }
        } else if (systolic <= 139) {
            if (diastolic > 120) {
                readingCondition.setText(hypertensiveCrisis);
                hypertensiveWarning(view);
            } else if (diastolic >= 90) {
                readingCondition.setText(stage2);
            } else {
                readingCondition.setText(stage1);
            }
        } else if (systolic <= 180) {
            if (diastolic > 120) {
                readingCondition.setText(hypertensiveCrisis);
                hypertensiveWarning(view);
            } else {
                readingCondition.setText(stage2);
            }
        } else {
            readingCondition.setText(hypertensiveCrisis);
            hypertensiveWarning(view);
        }
        return true;
    }

    /**
     * Long toast when patient is in hypertensive crisis.
     * @param view
     */
    public void hypertensiveWarning(View view) {
        Toast.makeText(this, "CONSULT YOUR DOCTOR IMMEDIATELY", Toast.LENGTH_LONG).show();
    }

    /**
     * Opens history activity containing data stored.
     * @param view
     */
    public void openHistory(View view) {
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
    }

    /**
     * Opens average activity.
     * @param view
     */
    public void openAverage(View view) {
        Intent intent = new Intent(this, Average.class);
        startActivity(intent);
    }


}

