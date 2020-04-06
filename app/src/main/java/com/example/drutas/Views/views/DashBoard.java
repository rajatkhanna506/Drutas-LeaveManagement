package com.example.drutas.Views.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drutas.R;
import com.example.drutas.Views.Adapter.TemporaryLeaveAdapter;
import com.example.drutas.Views.Adapter.SavedLeaveAdapter;
import com.example.drutas.Views.Models.NotifyModelData;
import com.example.drutas.Views.Models.SaveModel;
import com.example.drutas.Views.Models.TemporaryModel;
import com.example.drutas.Views.components.NonScrollListView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import static com.example.drutas.Views.util.utility.MyPREFERENCES;

public class DashBoard extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener, SavedLeaveAdapter.EditListener {

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    ImageView ivDrawer;
    RelativeLayout rlStartDatePicker;
    RelativeLayout rlEndDatePicker;
    TextView tvUserEmail;
    TextView tvStartDate;
    TextView tvEndDate;
    Spinner spinner;
    String Start = "";
    String End = "";
    int mYear, mMonth, mDay;
    ArrayList<TemporaryModel> temporaryList;
    ArrayList<TemporaryModel> editTemporaryList;
    ImageView addleave;
    EditText etReason;
    SharedPreferences sharedPreferences;
    RadioGroup rgRequestType;
    RadioButton rbRequestType;
    TextView tvLeaveType;
    LinearLayout llAppliedLeave;
    FrameLayout flSpinnerLayout;
    public String LoginStatus = "loginStatus";
    String LeaveType;
    NotifyModelData notifyModelData;
    NonScrollListView lvTemporaryNonScrollList;
    TemporaryLeaveAdapter temporaryLeaveAdapter;
    NonScrollListView lvSavedNonScrollList;
    SavedLeaveAdapter savedLeaveAdapter;
    ArrayList<NotifyModelData> notifyModelArrayList;
    TemporaryModel temporaryModel;
    int id;
    Date startDate = null;
    Date endDate = null;
    Gson gson;
    Map<String, List<NotifyModelData>> map;
    ArrayList<SaveModel> savedList;
    ArrayList<String> myleaveList;
    TemporaryLeaveAdapter editAdapter;
    String leave = "leave";
    int count = 0;
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        flSpinnerLayout = findViewById(R.id.layout_spinner);
        ivDrawer = findViewById(R.id.ivDrawer);
        lvSavedNonScrollList = findViewById(R.id.lvSavedLeaveList);
        rlStartDatePicker = findViewById(R.id.rlStartDateSelector);
        rlEndDatePicker = findViewById(R.id.rlEndDateSelector);
        tvUserEmail = findViewById(R.id.tvUserEmailid);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        spinner = findViewById(R.id.spinner);
        lvTemporaryNonScrollList = findViewById(R.id.lvTemporaryLeaveList);
        addleave = findViewById(R.id.addLeave);
        etReason = findViewById(R.id.etReason);
        tvLeaveType = findViewById(R.id.tvLeaveType);
        rgRequestType = findViewById(R.id.rgTypeOfRequest);
        llAppliedLeave = findViewById(R.id.llAplliedLeave);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        temporaryList = new ArrayList<>();
        myleaveList = new ArrayList<>();


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.list, R.layout.color_spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        Log.d("TAG", "jump to profile activity");
                        break;
                    case R.id.logout:
                        Log.d("TAG", "logout profile");
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()) ;
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(LoginStatus, "false");
                                editor.commit();
                                Intent i = new Intent(DashBoard.this, LoginActivity.class);
                                startActivity(i);
                            }
                        });
                        break;
                    case R.id.leaves:
                        Log.d("TAG", "jump to leaves    activity");
                        break;
                }
                return false;
            }
        });
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (optionalPendingResult.isDone()) {
            GoogleSignInResult result = optionalPendingResult.get();
            GoogleSignInAccount account = result.getSignInAccount();
            tvUserEmail.setText(account.getEmail());
            Log.d("TAG", "name" + account.getDisplayName());
            Log.d("TAG", "email" + account.getEmail());
            Log.d("TAG", "family" + account.getFamilyName());
        }
        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        rlStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openStartCalendar(v);

            }
        });

        rlEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Start.isEmpty()) {
                    openEndCalendar(v);


                }
            }
        });

        LeaveType = spinner.getSelectedItem().toString();


        rgRequestType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                id = rgRequestType.getCheckedRadioButtonId();
                rbRequestType = findViewById(id);
                if (rbRequestType.getText().toString().equalsIgnoreCase("Leave")) {

                    Log.d("TAG", "out");
                    tvLeaveType.setVisibility(View.VISIBLE);
                    etReason.setHint("Reason for Leave...");
                    spinner.setVisibility(View.VISIBLE);
                    flSpinnerLayout.setVisibility(View.VISIBLE);
                    LeaveType = spinner.getSelectedItem().toString();

                } else if (rbRequestType.getText().toString().equalsIgnoreCase("Work From Home")) {
                    Log.d("TAG", "in");
                    tvLeaveType.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    flSpinnerLayout.setVisibility(View.GONE);
                    etReason.setHint("Reason for WFH");
                    LeaveType = "WFH";

                }
            }
        });


        addleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Start.isEmpty() || End.isEmpty()) {
                    Log.d("TAG", "Please select the dates");
                    Toast.makeText(DashBoard.this, "Please select the dates", Toast.LENGTH_LONG).show();
                } else if (etReason.getText().toString().isEmpty()) {
                    Log.d("TAG", "please fill the reason");
                    Toast.makeText(DashBoard.this, "please fill the reason", Toast.LENGTH_LONG).show();
                } else {

                    saveLeave();
                }


            }
        });


    }


    public void saveLeave() {
        gson = new Gson();
        if (notifyModelArrayList == null) {
            notifyModelArrayList = new ArrayList<>();
        }
      //  if (temporaryList != null) {
//            int savedListSize = notifyModelArrayList.size();
//            for (int i = 0; i < savedListSize; i++) {
//                ArrayList<SaveModel> savedLeavesList = notifyModelArrayList.get(i).getSaveModel();
//                for (int j = 0; j < savedLeavesList.size(); j++) {
//                    String savedLeaves = savedLeavesList.get(j).getLeaveDate();
//                    if ((savedLeaves.equalsIgnoreCase(Start)) || (savedLeaves.equalsIgnoreCase(End))) {
//                        Toast.makeText(getApplicationContext(), "Alreay Leave applied for these Date", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//            }
//            savedList = new ArrayList<>();
//            notifyModelData = new NotifyModelData();
//            Toast.makeText(DashBoard.this, "display list", Toast.LENGTH_LONG).show();
//            notifyModelData.setLeaveType(LeaveType);
//            notifyModelData.setNotifyDate(Start + " To " + End);
//            notifyModelData.setReason(etReason.getText().toString());
//            notifyModelData.setStartDate(Start);
//            notifyModelData.setEndDate(End);
//            for (int i = 0; i < editTemporaryList.size(); i++) {
//                SaveModel saveModel = new SaveModel();
//                TemporaryModel temporaryModel = editTemporaryList.get(i);
//                Log.d("TAG", "msg" + temporaryModel.getLeaveDate());
//                saveModel.setLeaveDate(temporaryModel.getLeaveDate());
//                saveModel.setStartTime(temporaryModel.getStartTime());
//                saveModel.setEndTime(temporaryModel.getEndTime());
//                savedList.add(saveModel);
//            }
//            notifyModelData.setSaveModel(savedList);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            String json = gson.toJson(savedList);
//            editor.putString(leave + "" + pos, json);
//            editor.commit();
//            notifyModelArrayList.add(notifyModelData);
//            if (savedLeaveAdapter == null) {
//                savedLeaveAdapter = new SavedLeaveAdapter(DashBoard.this, notifyModelArrayList);
//                lvSavedNonScrollList.setAdapter(savedLeaveAdapter);
//            } else {
//                savedLeaveAdapter.notifyDataSetChanged();
//            }
//            editTemporaryList.clear();
//            editAdapter.notifyDataSetChanged();
//            editTemporaryList = null;
//            tvStartDate.setText("");
//            tvEndDate.setText("");
//            etReason.setText("");
//            Start = "";
//            End = "";
            //temporaryLeaveAdapter = null;
            //savedLeaveAdapter = null;

   //     } else {
            int savedListSize = notifyModelArrayList.size();
            for (int i = 0; i < savedListSize; i++) {
                ArrayList<SaveModel> savedLeavesList = notifyModelArrayList.get(i).getSaveModel();
                for (int j = 0; j < savedLeavesList.size(); j++) {
                    String savedLeaves = savedLeavesList.get(j).getLeaveDate();
                    if ((savedLeaves.equalsIgnoreCase(Start)) || (savedLeaves.equalsIgnoreCase(End))) {
                        Toast.makeText(getApplicationContext(), "Alreay Leave applied for these Date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


            }

            savedList = new ArrayList<>();
            notifyModelData = new NotifyModelData();
            Toast.makeText(DashBoard.this, "display list", Toast.LENGTH_LONG).show();
            notifyModelData.setLeaveType(LeaveType);
            notifyModelData.setNotifyDate(Start + " To " + End);
            notifyModelData.setReason(etReason.getText().toString());
            notifyModelData.setStartDate(Start);
            notifyModelData.setEndDate(End);
            for (int i = 0; i < temporaryList.size(); i++) {
                SaveModel saveModel = new SaveModel();
                TemporaryModel temporaryModel = temporaryList.get(i);
                Log.d("TAG", "msg" + temporaryModel.getLeaveDate());
                saveModel.setLeaveDate(temporaryModel.getLeaveDate());
                saveModel.setStartTime(temporaryModel.getStartTime());
                saveModel.setEndTime(temporaryModel.getEndTime());
                savedList.add(saveModel);
            }
            notifyModelData.setSaveModel(savedList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String json = gson.toJson(savedList);
            editor.putString(leave + "" + count, json);
            editor.commit();
            notifyModelArrayList.add(notifyModelData);


            if (savedLeaveAdapter == null) {
                savedLeaveAdapter = new SavedLeaveAdapter(DashBoard.this, notifyModelArrayList);
                lvSavedNonScrollList.setAdapter(savedLeaveAdapter);
            } else {
                savedLeaveAdapter.notifyDataSetChanged();
            }


            temporaryList.clear();
            temporaryLeaveAdapter.notifyDataSetChanged();
            tvStartDate.setText("");
            tvEndDate.setText("");
            etReason.setText("");
            Start = "";
            End = "";
            count++;

        }


    //}


    public String openStartCalendar(View v) {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Start = sdf.format(calendar.getTime());

                        try {
                            calendar.setTime(sdf.parse(Start));
                            Start = sdf.format(calendar.getTime());
                            tvStartDate.setText(Start);
                            if (!End.isEmpty()) {
                                setLeaveList();
                            }

                            Log.d("TAG", "manual" + Start);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

        return Start;
    }

    public String openEndCalendar(View v) {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        End = sdf.format(calendar.getTime());

                        try {
                            calendar.setTime(sdf.parse(End));
                            End = sdf.format(calendar.getTime());

                            setLeaveList();
                            Log.d("TAG", "manual" + End);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

        return End;
    }
// temp list method

    public void setLeaveList() {

        if (!temporaryList.isEmpty()) {
            temporaryList.clear();
        }
//        if (savedList == null) {
//            savedList = new ArrayList<>();
//        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            startDate = (Date) formatter.parse(Start);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            endDate = (Date) formatter.parse(End);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (endDate.compareTo(startDate) >= 0) {


            tvEndDate.setText(End);
            long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
            long curTime = startDate.getTime();
            while (curTime <= endTime) {
                temporaryModel = new TemporaryModel();
                Date sdate = new Date(curTime);
                temporaryModel.setLeaveDate(formatter.format(sdate));
                temporaryModel.setStartTime("10:00");
                temporaryModel.setEndTime("18:00");
                temporaryList.add(temporaryModel);


                curTime += interval;
            }
            if (temporaryLeaveAdapter == null) {
                temporaryLeaveAdapter = new TemporaryLeaveAdapter(DashBoard.this, temporaryList);
                lvTemporaryNonScrollList.setAdapter(temporaryLeaveAdapter);

            } else {

                temporaryLeaveAdapter.notifyDataSetChanged();
            }


        } else {

            Toast.makeText(DashBoard.this, "end Date must be after start Date", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LeaveType = parent.getItemAtPosition(position).toString();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void Event(int position, Context context, String reason, String leaveType) {
//        gson = new Gson();
//        this.pos = position;
//        sharedPreferences = getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
//        String list = sharedPreferences.getString(leave + "" + position, "");
//        Log.d("TAG", "savedlist" + list);
//        Type type = new TypeToken<ArrayList<TemporaryModel>>() {
//        }.getType();
//        editTemporaryList = new ArrayList<>();
//        editTemporaryList = gson.fromJson(list, type);
//        Start = editTemporaryList.get(0).getLeaveDate();
//        tvStartDate.setText(Start);
//        End = editTemporaryList.get(editTemporaryList.size() - 1).getLeaveDate();
//        tvEndDate.setText(End);
//        etReason.setText(reason);
//        editAdapter = new TemporaryLeaveAdapter(context, editTemporaryList);
//        lvTemporaryNonScrollList = findViewById(R.id.lvTemporaryLeaveList);
//        lvTemporaryNonScrollList.setAdapter(editAdapter);
//        sharedPreferences.edit().remove("leave" + position).commit();
//        count = pos + 1;
//        temporaryLeaveAdapter = null;
        etReason.setText(notifyModelArrayList.get(position).getReason());
        Start = notifyModelArrayList.get(position).getStartDate();
        End = notifyModelArrayList.get(position).getEndDate();
        tvStartDate.setText(Start);
        tvEndDate.setText(End);
        savedList = notifyModelArrayList.get(position).getSaveModel();
        temporaryList = new ArrayList<>();
        for (int i = 0; i < savedList.size(); i++) {
            TemporaryModel temporaryModel = new TemporaryModel();
            SaveModel saveModel = savedList.get(i);
            //Log.d("TAG", "msg" + saveModel.getLeaveDate());
            temporaryModel.setLeaveDate(saveModel.getLeaveDate());
            temporaryModel.setStartTime(saveModel.getStartTime());
            temporaryModel.setEndTime(saveModel.getEndTime());
            temporaryList.add(temporaryModel);
        }
        lvTemporaryNonScrollList = findViewById(R.id.lvTemporaryLeaveList);
        temporaryLeaveAdapter = new TemporaryLeaveAdapter(context, temporaryList);
        lvTemporaryNonScrollList = findViewById(R.id.lvTemporaryLeaveList);
        lvTemporaryNonScrollList.setAdapter(temporaryLeaveAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences settings = DashBoard.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String flag = settings.getString(LoginStatus, "");
        settings.edit().clear().commit();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LoginStatus, flag);
        editor.commit();
    }
}
