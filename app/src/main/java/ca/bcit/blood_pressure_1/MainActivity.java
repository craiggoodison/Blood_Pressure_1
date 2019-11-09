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



    private String userId;
    private int systolicReading_;
    private int diastolicReading_;
    private String systolicReadingDate;
    private String diastolicReadingDate;
    private String condition;
    private int seconds;
    private boolean running;
    private boolean wasRunning;

    DatabaseReference databaseBloodPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView time = findViewById(R.id.tvReadingTimeValue);
        TextView date = findViewById(R.id.tvReadingDateValue);

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
                addPatient();
            }
        });

        if (savedInstanceState != null) {
            userId = savedInstanceState.getString("User ID");
            systolicReading_ = savedInstanceState.getInt("Systolic Reading");
            diastolicReading_ = savedInstanceState.getInt("Diastolic Reading");
            systolicReadingDate = savedInstanceState.getString("Systolic Reading Date");
            diastolicReadingDate = savedInstanceState.getString("Diastolic Reading Date");
            condition = savedInstanceState.getString("Condition");
            running = savedInstanceState.getBoolean("Running");
            wasRunning = savedInstanceState.getBoolean("Running");
            seconds = savedInstanceState.getInt("seconds");
        }

    }
    // Start the stopwatch running when the Start button is clicked
    public void onClickStart(View v) {
        running = true;
    }

    // Stop the stopwatch running when the Stop button is clicked
    public void onClickStop(View v) {
        running = false;
    }

    // Reset the stopwatch when the Reset button is clicked
    public void onClickReset(View v) {
        running = false;
        seconds = 0;

    }
//    protected void onStop() {
//        super.onStop();
//        wasRunning = running;
//        running = false;
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (wasRunning) {
//            running = true;
//        }
//    }
    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
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
            Toast.makeText(this, "You must enter the patient's ID.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String id = databaseBloodPressure.push().getKey();
        Patient patient = new Patient(id, patientId, systolicReadingValue, diastolicReadingValue, condition,
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

    /**
     * Determines condition based on systolic and diastolic readings.
     * @param view
     */
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

    }

    /**
     * Long toast when patient is in hypertensive crisis.
     * @param view
     */
    public void hypertensiveWarning(View view) {
        Toast.makeText(this, "CONSULT YOUR DOCTOR IMMEDIATELY", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("User ID", userId);
        savedInstanceState.putInt("Systolic Reading", systolicReading_);
        savedInstanceState.putInt("Diastolic Reading", diastolicReading_);
        savedInstanceState.putString("Systolic Reading Date", systolicReadingDate);
        savedInstanceState.putString("Diastolic Reading Date", diastolicReadingDate);
        savedInstanceState.putString("Condition", condition);
        savedInstanceState.putBoolean("Running", running);
        savedInstanceState.putBoolean("wasRunning", running);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);

    }


    /**
     * Opens history activity containing data stored.
     * @param view
     */
    public void openHistory(View view) {
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
    }


}

