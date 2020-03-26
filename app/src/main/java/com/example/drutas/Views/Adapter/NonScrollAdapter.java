package com.example.drutas.Views.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.drutas.R;

import java.util.ArrayList;

public class NonScrollAdapter extends BaseAdapter {
    ArrayList<String> myLeaveList;
    Context context;
    TextView tvLeaveDate;
    String leaveDate;


    public NonScrollAdapter(Context context, ArrayList<String> myLeaveList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.adapter_leave_date, null);
        tvLeaveDate = convertView.findViewById(R.id.tvLeaveDate);
        leaveDate = myLeaveList.get(position);
        tvLeaveDate.setText(leaveDate);
        return convertView;
    }
}
