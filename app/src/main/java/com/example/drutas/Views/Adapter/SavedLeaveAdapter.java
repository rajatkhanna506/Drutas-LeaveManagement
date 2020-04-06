package com.example.drutas.Views.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drutas.R;
import com.example.drutas.Views.Models.NotifyModelData;
import com.example.drutas.Views.Models.SaveModel;
import com.example.drutas.Views.Models.TemporaryModel;
import com.example.drutas.Views.components.NonScrollListView;
import com.example.drutas.Views.views.DashBoard;

import java.util.ArrayList;

import static com.example.drutas.Views.util.utility.MyPREFERENCES;

public class SavedLeaveAdapter extends BaseAdapter {
    ArrayList<NotifyModelData> notifyModelDataArrayList;
    ArrayList<NotifyModelData> notifyModelDataArrayListcopy;
    Context context;
    TextView tvLeaveHeader;
    TextView tvNotiftDate;
    TextView tvReason;
    NonScrollListView lvSavedList;
    NotifyModelData notifyModelData;
    NonScrollAdapter leaveAdapter;
    ImageView ivEdit;
    ImageView ivDelete;
    String Start;
    String End;
    EditListener listener;
    SharedPreferences sharedPreferences;


    public SavedLeaveAdapter(Context context, ArrayList<NotifyModelData> notifyModelArrayList) {
        this.notifyModelDataArrayList = notifyModelArrayList;
        this.notifyModelDataArrayListcopy = notifyModelArrayList;
        this.context = context;
        listener = (EditListener) context;




    }

    public interface EditListener {
        void Event(int position, Context context,String reason,String leaveType);
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.adapter_saved_list, null);
        tvLeaveHeader = convertView.findViewById(R.id.tvLeaveHeader);
        tvNotiftDate = convertView.findViewById(R.id.tvNotifyDate);
        tvReason = convertView.findViewById(R.id.tvReason);
        lvSavedList = convertView.findViewById(R.id.lvNonScroll);
        ivEdit = convertView.findViewById(R.id.edit);
        ivDelete = convertView.findViewById(R.id.delete);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notifyModelDataArrayList.remove(position);
                sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                sharedPreferences.edit().remove("leave"+position).commit();

                notifyDataSetChanged();
            }
        });
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String reason = notifyModelDataArrayList.get(position).getReason();
                String leaveType = notifyModelDataArrayList.get(position).getLeaveType();
                listener.Event(position, context,reason,leaveType);
                notifyModelDataArrayList.remove(position);
                notifyDataSetChanged();
//                sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.remove("leave"+position);
//                editor.commit();





            }
        });

        notifyModelData = notifyModelDataArrayList.get(position);
        tvLeaveHeader.setText(notifyModelData.getLeaveType());
        tvNotiftDate.setText(notifyModelData.getNotifyDate());
        tvReason.setText(notifyModelData.getReason());
        Start = notifyModelData.getStartDate();
        End = notifyModelData.getEndDate();
        leaveAdapter = new NonScrollAdapter(context, notifyModelData.getSaveModel());
        lvSavedList.setAdapter(leaveAdapter);
        return convertView;
    }


}

