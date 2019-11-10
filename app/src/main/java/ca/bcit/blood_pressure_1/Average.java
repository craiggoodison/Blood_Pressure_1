package ca.bcit.blood_pressure_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Average extends AppCompatActivity {

    DatabaseReference databaseBloodPressure;

    List<Patient> patientList;
    int totalSystolic;
    int totalDiastolic;

    TextView avgSystolic;
    TextView avgDiastolic;
    TextView avgCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average);

        databaseBloodPressure = FirebaseDatabase.getInstance().getReference("BloodPressure");

        patientList = new ArrayList<Patient>();

        avgSystolic = findViewById(R.id.tvSystolicReading);
        avgDiastolic = findViewById(R.id.tvDiastolicReading);
        avgCondition = findViewById(R.id.tvCondition);

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

                for (Patient p : patientList) {
                    totalSystolic += p.getSystolicReading();
                    totalDiastolic += p.getDiastolicReading();
                }

                totalSystolic /= patientList.size();
                totalDiastolic /= patientList.size();

                avgSystolic.setText(String.valueOf(totalSystolic));
                avgDiastolic.setText(String.valueOf(totalDiastolic));
                avgCondition.setText(setCondition());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public String setCondition() {
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
