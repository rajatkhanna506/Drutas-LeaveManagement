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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TemporaryLeaveAdapter extends BaseAdapter {
    ArrayList<Timermodel> temporaryList;
    Context context;
    TextView tvLeaveDate;
    TextView tvLeaveStartTime;
    TextView tvLeaveEndTime;
    String leaveDate;
    Timermodel timermodel;
    String StartTime;
    String EndTime;
    int size;

    public TemporaryLeaveAdapter(Context context, ArrayList<Timermodel> myLeaveList) {
        this.temporaryList = myLeaveList;
        this.context = context;
        this.size = myLeaveList.size();

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
        timermodel = new Timermodel();
        timermodel = temporaryList.get(position);
        tvLeaveDate.setText(timermodel.getLeaveDate());
        tvLeaveStartTime.setText(timermodel.getStartTime());
        tvLeaveEndTime.setText(timermodel.getEndTime());
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
                        String AM_PM ;
                        if(selectedHour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        String time = selectedHour+":"+selectedMinute;
                        try {
                            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                            final Date dateObj = sdf.parse(time);
                            System.out.println(new SimpleDateFormat("K:mm").format(dateObj));
                            temporaryList.get(position).setStartTime(new SimpleDateFormat("K:mm").format(dateObj)+" "+AM_PM);
                        } catch (final ParseException e) {
                            e.printStackTrace();
                        }
                        //temporaryList.get(position).setStartTime(selectedHour + ":" + selectedMinute+" "+AM_PM);
                        notifyDataSetChanged();
                    }
                }, hour, minute, false);//Yes 24 hour time
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
                        String AM_PM ;
                        if(selectedHour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        String time = selectedHour+":"+selectedMinute;
                        try {
                            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                            final Date dateObj = sdf.parse(time);
                            System.out.println(new SimpleDateFormat("K:mm").format(dateObj));
                            temporaryList.get(position).setEndTime(new SimpleDateFormat("K:mm").format(dateObj)+" "+AM_PM);
                        } catch (final ParseException e) {
                            e.printStackTrace();
                        }
                        //temporaryList.get(position).setEndTime(selectedHour + ":" + selectedMinute+" "+AM_PM);
                        notifyDataSetChanged();
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        return convertView;
    }
}
