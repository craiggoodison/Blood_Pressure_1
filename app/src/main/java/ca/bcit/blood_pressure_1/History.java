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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class History extends AppCompatActivity {

    DatabaseReference databaseBloodPressure;

    ListView lvPatient;
    List<Patient> patientList;

    int totalSystolic;
    int totalDiastolic;
    String pId;
    String currDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        currDate = new SimpleDateFormat(
                "MM-yyyy", Locale.getDefault()).format(new Date());

        databaseBloodPressure = FirebaseDatabase.getInstance().getReference("BloodPressure");

        lvPatient = findViewById(R.id.lvPatientList);
        patientList = new ArrayList<Patient>();

        lvPatient.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Patient patient = patientList.get(position);
                totalSystolic = 0;
                totalDiastolic = 0;
                int entries = 0;

                for (Patient p : patientList) {
                    if (p.getPatientId().equalsIgnoreCase(patient.getPatientId()) &&
                            p.getReadingDate().contains(currDate)) {
                        entries++;
                        totalSystolic += p.getSystolicReading();
                        totalDiastolic += p.getDiastolicReading();
                    }
                }

                pId = patient.getPatientId();
                totalSystolic /= entries;
                totalDiastolic /= entries;


                showUpdateDialog(patient.getUID(),
                        patient.getPatientId(),
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
     * @param uID
     * @param patientId
     * @param systolicReading
     * @param diastolicReading
     * @param condition
     * @param readingTime
     * @param readingDate
     */
    private void updatePatient(String uID, String patientId, int systolicReading, int diastolicReading,
                               String condition, String readingTime, String readingDate ) {

        DatabaseReference dbRef = databaseBloodPressure.child(uID);

        Patient patient = new Patient(uID, patientId, systolicReading, diastolicReading, condition,
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
    private void showUpdateDialog(final String uID, final String patientId, int systolicReading, int diastolicReading,
                                  String condition, final String readingDate, final String readingTime) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText etUserID = dialogView.findViewById(R.id.etUserIDValue);
        etUserID.setText(patientId);

        final EditText etSystolicReading = dialogView.findViewById(R.id.etSystolicReading);
        etSystolicReading.setText(String.valueOf(systolicReading));

        final EditText etDiastolicReading = dialogView.findViewById(R.id.etDiastolicReading);
        etDiastolicReading.setText(String.valueOf(diastolicReading));

        final TextView tvCondition = dialogView.findViewById(R.id.tvCondition);
        tvCondition.setText(condition);

        final TextView tvReadingDate  = dialogView.findViewById(R.id.tvReadingDateValue);
        tvReadingDate.setText(readingDate);

        final TextView tvReadingTime  = dialogView.findViewById(R.id.tvReadingTimeValue);
        tvReadingTime.setText(readingTime);

        final Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
        dialogBuilder.setTitle("Update Patient " + uID);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientId = etUserID.getText().toString().trim();
                int systolicReading = Integer.parseInt(etSystolicReading.getText().toString().trim());
                int diastolicReading = Integer.parseInt(etDiastolicReading.getText().toString().trim());
                String condition = tvCondition.getText().toString().trim();
                String readingTime = tvReadingTime.getText().toString().trim();
                String readingDate = tvReadingDate.getText().toString().trim();

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

                updatePatient(uID, patientId, systolicReading, diastolicReading, condition,
                        readingTime, readingDate);

                alertDialog.dismiss();
            }
        });

        final Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePatient(uID);

                alertDialog.dismiss();
            }
        });

        final Button btnAverage = dialogView.findViewById(R.id.btnAverage);
        btnAverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAverageView(pId, totalSystolic, totalDiastolic);

                alertDialog.dismiss();
            }
        });
    }

    /**
     * Dialog that shows the average for selected patient.
     * @param systolic
     * @param diastolic
     */
    private void showAverageView(final String pId, final int systolic, final int diastolic) {
        String title = "Month-to-Date Average for " + pId + " on " + currDate;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(History.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.average_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView dialogTitle = dialogView.findViewById(R.id.tvMonthToDateAvg);
        dialogTitle.setText(title);

        final TextView tvSystolic = dialogView.findViewById(R.id.tvSystolicReading);
        tvSystolic.setText(String.valueOf(systolic));

        final TextView tvDiastolic  = dialogView.findViewById(R.id.tvDiastolicReading);
        tvDiastolic.setText(String.valueOf(diastolic));

        final TextView tvAvgCondition = dialogView.findViewById(R.id.tvCondition);
        tvAvgCondition.setText(setCondition(systolic, diastolic));

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Deletes patient data from database.
     * @param id
     */
    private void deletePatient(String id) {
        DatabaseReference dbRef = databaseBloodPressure.child(id);
        Task setRemoveTask = dbRef.removeValue();
        setRemoveTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(History.this,
                        "Patient Deleted.",Toast.LENGTH_LONG).show();
            }
        });

        setRemoveTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(History.this,
                        "Something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Determines condition based on systolic and diastolic readings.
     * @param totalSystolic int
     * @param totalDiastolic int
     * @return string
     */
    public String setCondition(int totalSystolic, int totalDiastolic) {
        final String normal = "Normal";
        final String elevated = "Elevated";
        final String stage1 = "High Blood Pressure (Stage 1)";
        final String stage2 = "High Blood Pressure (Stage 2)";
        final String hypertensiveCrisis = "Hypertensive Crisis";


        if (totalSystolic < 120) {
            if (totalDiastolic < 80) {
                return normal;
            } else if (totalDiastolic <= 89) {
                return stage1;
            } else if (totalDiastolic <= 120) {
                return stage2;
            } else {
                return hypertensiveCrisis;
            }
        } else if (totalSystolic <= 129) {
            if (totalDiastolic < 80) {
                return elevated;
            } else if (totalDiastolic <= 89) {
                return stage1;
            } else if (totalDiastolic <= 120) {
                return stage2;
            } else {
                return hypertensiveCrisis;
            }
        } else if (totalSystolic <= 139) {
            if (totalDiastolic > 120) {
                return hypertensiveCrisis;
            } else if (totalDiastolic >= 90) {
                return stage2;
            } else {
                return stage1;
            }
        } else if (totalSystolic <= 180) {
            if (totalDiastolic > 120) {
                return hypertensiveCrisis;
            } else {
                return stage2;
            }
        } else {
            return hypertensiveCrisis;
        }
    }
}
