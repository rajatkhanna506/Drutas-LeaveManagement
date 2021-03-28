package com.example.drutas.Views.Adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.drutas.R;
import com.example.drutas.Views.Models.TemporaryModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TemporaryLeaveAdapter extends BaseAdapter {
    ArrayList<TemporaryModel> temporaryList;
    Context context;
    TextView tvLeaveDate;
    TextView tvLeaveStartTime;
    TextView tvLeaveEndTime;
    String leaveDate;
    TemporaryModel timermodel;
    Date startTime;
    Date endTime;
    String strt;
    String end;
    int size;
    Date dateObj1;
    Date dateObj2;

    public TemporaryLeaveAdapter(Context context, ArrayList<TemporaryModel> myLeaveList) {
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
        convertView = layoutInflater.inflate(R.layout.adapter_temporary_leave_, null);
        tvLeaveDate = convertView.findViewById(R.id.tvLeaveDate);
        tvLeaveStartTime = convertView.findViewById(R.id.leaveStartTiming);
        tvLeaveEndTime = convertView.findViewById(R.id.leaveEndTiming);
        timermodel = new TemporaryModel();
        timermodel = temporaryList.get(position);
        tvLeaveDate.setText(timermodel.getLeaveDate());
        final SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");

        try {
            dateObj1 = sdf1.parse(timermodel.getStartTime());
            dateObj2 = sdf1.parse(timermodel.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvLeaveStartTime.setText(sdf2.format(dateObj1) + "");
        tvLeaveEndTime.setText(sdf2.format(dateObj2) + "");
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
                        String time = selectedHour + ":" + selectedMinute;
                        try {
                            strt = time;
                            startTime = sdf1.parse(strt);
                            if (startTime == null) {
                                startTime = sdf1.parse(timermodel.getStartTime());
                            }
                            if (endTime == null) {
                                endTime = sdf1.parse(timermodel.getEndTime());
                            }
                            if (startTime.after(endTime)) {
                                Toast.makeText(context, "msg wrong", Toast.LENGTH_SHORT).show();
                                startTime = null;
                                endTime = null;
                            } else {
                                Toast.makeText(context, "msg right", Toast.LENGTH_SHORT).show();
                                temporaryList.get(position).setStartTime(sdf1.format(startTime));
                                notifyDataSetChanged();
                            }

                        } catch (final ParseException e) {
                            e.printStackTrace();
                        }


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
//                        String AM_PM;
//                        if (selectedHour < 12) {
//                            AM_PM = "AM";
//                        } else {
//                            AM_PM = "PM";
//                        }
                        String time = selectedHour + ":" + selectedMinute;
                        try {

                          //  SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");


                            end = time;
                            endTime = sdf1.parse(end);
                            if (startTime == null) {
                                startTime = sdf1.parse(timermodel.getStartTime());
                            }
                            if (endTime == null) {
                                endTime = sdf1.parse(timermodel.getEndTime());
                            }
                            if (endTime.before(startTime)) {
                                Toast.makeText(context, "msg wrong", Toast.LENGTH_SHORT).show();
                                endTime = null;
                                startTime = null;

                            } else {
                                Toast.makeText(context, "msg right", Toast.LENGTH_SHORT).show();
                                temporaryList.get(position).setEndTime(sdf1.format(endTime));
                                notifyDataSetChanged();
                            }

                        } catch (final ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        return convertView;
    }
}
