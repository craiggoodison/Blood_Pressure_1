package ca.bcit.blood_pressure_1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class BloodPressureAdapter extends ArrayAdapter<BloodPressure> {

    private Activity context;
    private List<BloodPressure> bpList;

    public BloodPressureAdapter(Activity context, List<BloodPressure> bpList) {
        super(context, R.layout.list_layout, bpList);
        this.context = context;
        this.bpList = bpList;
    }

    public BloodPressureAdapter(Activity context, int resource, List<BloodPressure> objects,
                                Activity context1, List<BloodPressure> bpList) {
        super(context, resource, objects);
        this.context = context1;
        this.bpList = bpList;
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

        BloodPressure bp = bpList.get(position);

        tvUserID.setText(bp.getPatientId());
        tvSystolic.setText(bp.getSystolicReading());
        tvDiastolic.setText(bp.getDiastolicReading());
        tvDate.setText(bp.getReadingDate());
        tvTime.setText(bp.getReadingTime());
        tvCondition.setText(bp.getCondition());

        return listViewItem;
    }
}
