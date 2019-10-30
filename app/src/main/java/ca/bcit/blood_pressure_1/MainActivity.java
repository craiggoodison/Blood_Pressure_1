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
    TextView readingDate;
    TextView readingDateValue;
//    EditText editTextFirstName;
//    EditText editTextLastName;
    Button buttonAddPatient;
    EditText systolicReading;
    EditText diastolicReading;

    DatabaseReference databaseBloodPressure;

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

        databaseBloodPressure = FirebaseDatabase.getInstance().getReference("students");

        editTextPatientId = findViewById(R.id.etUserID);
//        editTextLastName = findViewById(R.id.editTextLastName);
        buttonAddPatient = findViewById(R.id.btnSubmit);

        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPatient();
            }
        });
        private void addPatient () {
            String patientId = editTextPatientId.getText().toString().trim();
//            String lastName = editTextLastName.getText().toString().trim();
//            String school = spinnerSchool.getSelectedItem().toString().trim();

            if (TextUtils.isEmpty(patientId)) {
                Toast.makeText(this, "You must enter a first name.", Toast.LENGTH_LONG).show();
                return;
            }

//            if (TextUtils.isEmpty(lastName)) {
//                Toast.makeText(this, "You must enter a last name.", Toast.LENGTH_LONG).show();
//                return;
//            }

            String id = databaseBloodPressure.push().getKey();
            Patient patient = new Patient(id, systolicReading, diastolicReading,
                    condition, readingTime, readingDate);

            Task setValueTask = databaseBloodPressure.child(id).setValue(patient);

            setValueTask.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(MainActivity.this, "Student added.", Toast.LENGTH_LONG).show();

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


    }

    public void setCondition() {
        systolicReading = findViewById(R.id.etSystolicReading);
        diastolicReading = findViewById(R.id.etDiastolicReading);
        int systolic = Integer.parseInt(systolicReading.getText().toString());
        int diastolic = Integer.parseInt(diastolicReading.getText().toString());

    }


}
