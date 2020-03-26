package com.example.drutas.Views.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.drutas.R;
import com.example.drutas.Views.Models.NotifyModelData;
import com.example.drutas.Views.components.NonScrollListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SavedLeaveAdapter extends BaseAdapter {
    ArrayList<NotifyModelData> notifyModelDataArrayList;
    Context context;
    TextView tvLeaveHeader;
    TextView tvNotiftDate;
    TextView tvReason;
    NonScrollListView lvSavedList;
    ArrayList<String> myLeaveList;
    NonScrollAdapter leaveAdapter;
    String Start;
    String End;
    Date startDate = null;
    Date endDate = null;
    SharedPreferences sharedPreferences;

    public SavedLeaveAdapter(Context applicationContext, ArrayList<NotifyModelData> notifyModelArrayList) {
        this.notifyModelDataArrayList = notifyModelArrayList;
        this.context = applicationContext;
    }

    public void acceptArrayList(ArrayList<String> myLeaveList) {
        this.myLeaveList = myLeaveList;
    }


    @Override
    public int getCount() {
        return notifyModelDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return notifyModelDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.adapter_saved_list, null);
        tvLeaveHeader = convertView.findViewById(R.id.tvLeaveHeader);
        tvNotiftDate = convertView.findViewById(R.id.tvNotifyDate);
        tvReason = convertView.findViewById(R.id.tvReason);
        lvSavedList = convertView.findViewById(R.id.lvNonScroll);
        NotifyModelData notifyModelData = notifyModelDataArrayList.get(position);
        tvLeaveHeader.setText(notifyModelData.getLeaveType());
        tvNotiftDate.setText(notifyModelData.getNotifyDate());
        tvReason.setText(notifyModelData.getReason());
//        Start = notifyModelData.getStartDate();
//        End = notifyModelData.getEndDate();

//        if (leaveAdapter == null) {
//            leaveAdapter = new NonScrollAdapter(context, myLeaveList);
//            lvSavedList.setAdapter(leaveAdapter);
//        } else {
//            leaveAdapter.notifyDataSetChanged();
//        }
//
        return convertView;
    }


}

