package ca.bcit.blood_pressure_1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class PatientAdapter extends ArrayAdapter<Patient> {

    private Activity context;
    private List<Patient> patientList;

    public PatientAdapter(Activity context, List<Patient> patientList) {
        super(context, R.layout.list_layout, patientList);
        this.context = context;
        this.patientList = patientList;
    }

    public PatientAdapter(Activity context, int resource, List<Patient> objects,
                          Activity context1, List<Patient> patientList) {
        super(context, resource, objects);
        this.context = context1;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView tvUserID = listViewItem.findViewById(R.id.tvUserIDValue);
        TextView tvSystolic = listViewItem.findViewById(R.id.tvSystolicReading);
        TextView tvDiastolic = listViewItem.findViewById(R.id.tvDiastolicReading);
        TextView tvDate = listViewItem.findViewById(R.id.tvReadingDateValue);
        TextView tvTime = listViewItem.findViewById(R.id.tvReadingTimeValue);
        TextView tvCondition = listViewItem.findViewById(R.id.tvCondition);

        Patient patient = patientList.get(position);

        tvUserID.setText(patient.getPatientId());
        tvSystolic.setText(String.valueOf(patient.getSystolicReading()));
        tvDiastolic.setText(String.valueOf(patient.getDiastolicReading()));
        tvDate.setText(patient.getReadingDate());
        tvTime.setText(patient.getReadingTime());
        tvCondition.setText(patient.getCondition());

        return listViewItem;
    }
}
