package ca.bcit.blood_pressure_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    DatabaseReference databaseBloodPressure;

    ListView lvPatient;
    List<Patient> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        databaseBloodPressure = FirebaseDatabase.getInstance().getReference("BloodPressure");

        lvPatient = findViewById(R.id.lvPatientList);
        patientList = new ArrayList<Patient>();

        lvPatient.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Patient patient = patientList.get(position);

                showUpdateDialog(patient.getPatientId(),
                        patient.getSystolicReading(),
                        patient.getDiastolicReading(),
                        patient.getCondition(),
                        patient.getReadingDate(),
                        patient.getReadingTime());

                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseBloodPressure.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientList.clear();
                for (DataSnapshot patientSnapshot : dataSnapshot.getChildren()) {
                    Patient patient = patientSnapshot.getValue(Patient.class);
                    patientList.add(patient);
                }

                PatientAdapter adapter = new PatientAdapter(History.this, patientList);
                lvPatient.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * Updates patient information.
     * @param patientId
     * @param systolicReading
     * @param diastolicReading
     * @param condition
     * @param readingTime
     * @param readingDate
     */
    private void updatePatient(String patientId, int systolicReading, int diastolicReading,
                               String condition, String readingTime, String readingDate ) {

        DatabaseReference dbRef = databaseBloodPressure.child(patientId);

        Patient patient = new Patient(patientId, systolicReading, diastolicReading, condition,
                readingTime, readingDate);

        Task setValueTask = dbRef.setValue(patient);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(History.this,
                        "Patient Updated.",Toast.LENGTH_LONG).show();
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(History.this,
                        "Something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Update dialog when list item is selected.
     * @param patientId
     * @param systolicReading
     * @param diastolicReading
     * @param condition
     * @param readingDate
     * @param readingTime
     */
    private void showUpdateDialog(final String patientId, int systolicReading, int diastolicReading,
                                  String condition, final String readingDate, final String readingTime) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText etUserID = dialogView.findViewById(R.id.etUserID);
        etUserID.setText(patientId);

        final EditText etSystolicReading = dialogView.findViewById(R.id.etSystolicReading);
        etSystolicReading.setText(systolicReading);

        final EditText etDiastolicReading = dialogView.findViewById(R.id.etSystolicReading);
        etDiastolicReading.setText(diastolicReading);

        final EditText etCondition = dialogView.findViewById(R.id.etCondition);
        etCondition.setText(diastolicReading);

        final EditText etReadingDate  = dialogView.findViewById(R.id.etReadingDate);
        etReadingDate.setText(diastolicReading);

        final EditText etReadingTime  = dialogView.findViewById(R.id.etReadingTime);
        etReadingTime.setText(diastolicReading);

        final Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
        dialogBuilder.setTitle("Update Patient " + patientId + " " + systolicReading + " " + diastolicReading);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientId = etUserID.getText().toString().trim();
                int systolicReading = Integer.parseInt(etSystolicReading.getText().toString().trim());
                int diastolicReading = Integer.parseInt(etDiastolicReading.getText().toString().trim());
                String condition = etCondition.getText().toString().trim();
                String readingTime = etCondition.getText().toString().trim();
                String readingDate = etCondition.getText().toString().trim();

                if (TextUtils.isEmpty(patientId)) {
                    etUserID.setError("ID is required");
                    return;
                } else if (TextUtils.isEmpty(etSystolicReading.getText().toString().trim())) {
                    etSystolicReading.setError("Systolic Reading is required");
                    return;
                } else if (TextUtils.isEmpty(etDiastolicReading.getText().toString().trim())) {
                    etDiastolicReading.setError("Diastolic Reading is required");
                    return;
                }

                updatePatient(patientId, systolicReading, diastolicReading, condition,
                        readingTime, readingDate);

                alertDialog.dismiss();
            }
        });
    }
}
