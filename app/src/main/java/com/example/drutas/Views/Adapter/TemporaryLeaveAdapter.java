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
import android.widget.Toast;

import com.example.drutas.R;
import com.example.drutas.Views.Models.Timermodel;

import java.util.ArrayList;
import java.util.Calendar;

public class TemporaryLeaveAdapter extends BaseAdapter {
    ArrayList<String> temporaryList;
    Context context;
    TextView tvLeaveDate;
    TextView tvLeaveStartTime;
    TextView tvLeaveEndTime;
    String leaveDate;
    int sizeOfList;
    Timermodel timermodel;
    String StartTime;
    String EndTime;

    public TemporaryLeaveAdapter(Context context, ArrayList<String> myLeaveList) {
        this.temporaryList = myLeaveList;
        this.context = context;
        sizeOfList = temporaryList.size();
        timermodel = new Timermodel();
        StartTime = timermodel.getStartTime();
        EndTime = timermodel.getEndTime();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.adapter_leave_date, null);
        tvLeaveDate = convertView.findViewById(R.id.tvLeaveDate);
        tvLeaveStartTime = convertView.findViewById(R.id.leaveStartTiming);
        tvLeaveEndTime = convertView.findViewById(R.id.leaveEndTiming);
        leaveDate = temporaryList.get(position);
        tvLeaveDate.setText(leaveDate);
//        tvLeaveStartTime.setText(StartTime);
//        tvLeaveEndTime.setText(EndTime);

//        for (int i = 0; i < sizeOfList; i++) {
//            if (i == position) {


        tvLeaveStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "pos" + position, Toast.LENGTH_SHORT).show();
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvLeaveStartTime.setText(selectedHour + ":" + selectedMinute);
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
                        tvLeaveEndTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
//            }
//
//        }
        return convertView;
    }
}
