package com.example.drutas.Views.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.drutas.R;
import com.example.drutas.Views.Models.SaveModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NonScrollAdapter extends BaseAdapter {
    ArrayList<SaveModel> SaveLeaveList;
    Context context;
    TextView tvLeaveDate;
    TextView tvStartTime;
    TextView tvEndTime;
    SaveModel saveModel;
    Date dateObj1;
    Date dateObj2;


    public NonScrollAdapter(Context context, ArrayList<SaveModel> myLeaveList) {
        this.SaveLeaveList = myLeaveList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return SaveLeaveList.size();
    }

    @Override
    public Object getItem(int position) {
        return SaveLeaveList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.adapter_save_leave, null);
        tvLeaveDate = convertView.findViewById(R.id.savedLeaveDate);
        tvStartTime = convertView.findViewById(R.id.savedleaveStartTiming);
        tvEndTime = convertView.findViewById(R.id.savedleaveEndTiming);
        saveModel = new SaveModel();
        saveModel = SaveLeaveList.get(position);
        tvLeaveDate.setText(saveModel.getLeaveDate());
        Log.e("TAG", "leaved" + saveModel.getLeaveDate());
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
        try {

            dateObj1 = sdf1.parse(saveModel.getStartTime());
            tvStartTime.setText(sdf2.format(dateObj1) + " ");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {

            dateObj2 = sdf1.parse(saveModel.getEndTime());
            tvEndTime.setText(sdf2.format(dateObj2) + " ");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
