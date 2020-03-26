package com.example.drutas.Views.Adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import com.example.drutas.R;
import java.util.ArrayList;
import java.util.Calendar;

public class TemporaryLeaveAdapter extends BaseAdapter {
    ArrayList<String> temporaryList;
    Context context;
    TextView tvLeaveDate;
    TextView tvLeaveStartTime;
    TextView tvLeaveEndTime;
    String leaveDate;

    public TemporaryLeaveAdapter(Context context, ArrayList<String> myLeaveList) {
        this.temporaryList = myLeaveList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return temporaryList.size();
    }

    @Override
    public Object getItem(int position) {
        return temporaryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.adapter_leave_date, null);
        tvLeaveDate = convertView.findViewById(R.id.tvLeaveDate);
        tvLeaveStartTime = convertView.findViewById(R.id.leaveStartTiming);
        tvLeaveEndTime = convertView.findViewById(R.id.leaveEndTiming);
        leaveDate = temporaryList.get(position);
        tvLeaveDate.setText(leaveDate);
        tvLeaveStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvLeaveStartTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        tvLeaveEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvLeaveEndTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        return convertView;
    }
}
