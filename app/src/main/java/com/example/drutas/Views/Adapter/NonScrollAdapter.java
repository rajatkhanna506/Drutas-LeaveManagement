package com.example.drutas.Views.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drutas.R;
import com.example.drutas.Views.Models.Timermodel;

import java.util.ArrayList;

public class NonScrollAdapter extends BaseAdapter {
    ArrayList<Timermodel> myLeaveList;
    Context context;
    TextView tvLeaveDate;
    TextView tvStartTime;
    TextView tvEndTime;
    String leaveDate;
    Timermodel timermodel;


    public NonScrollAdapter(Context context, ArrayList<Timermodel> myLeaveList) {
        this.myLeaveList = myLeaveList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return myLeaveList.size();
    }

    @Override
    public Object getItem(int position) {
        return myLeaveList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.timers, null);
        tvLeaveDate = convertView.findViewById(R.id.savedLeaveDate);
        tvStartTime = convertView.findViewById(R.id.savedleaveStartTiming);
        tvEndTime = convertView.findViewById(R.id.savedleaveEndTiming);
        if (timermodel == null) {
            timermodel = new Timermodel();
        }
        timermodel = myLeaveList.get(position);
        tvLeaveDate.setText(timermodel.getLeaveDate());
        Log.e("TAG", "leaved" + timermodel.getLeaveDate());
        tvStartTime.setText(timermodel.getStartTime());
        tvEndTime.setText(timermodel.getEndTime());
        return convertView;
    }
}
