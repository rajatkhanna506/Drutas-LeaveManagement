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
import com.example.drutas.Views.Models.Timermodel;
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

import static com.example.drutas.Views.util.utility.MyPREFERENCES;

public class DashBoard extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

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
    ArrayList<Timermodel> temporaryList;
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
    Timermodel timermodel;
    int id;
    Date startDate = null;
    Date endDate = null;
    Gson gson;
    Map<String, List<String>> map;
    ArrayList<Timermodel> savedList;
    ArrayList<String> myleaveList;
    Boolean saved = false;


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


        map = new HashMap<String, List<String>>();
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
                gson = new Gson();
                if (Start.isEmpty() || End.isEmpty()) {
                    Log.d("TAG", "Please select the dates");
                    Toast.makeText(DashBoard.this, "Please select the dates", Toast.LENGTH_LONG).show();
                } else if (etReason.getText().toString().isEmpty()) {
                    Log.d("TAG", "please fill the reason");
                    Toast.makeText(DashBoard.this, "please fill the reason", Toast.LENGTH_LONG).show();
                    //saved = false;
                } else {

                    saveLeave();
                }
                // saved = false;

            }
        });
    }

    public void saveLeave() {

        if (notifyModelArrayList == null) {
            notifyModelArrayList = new ArrayList<>();
            notifyModelData = new NotifyModelData();
        }

            Toast.makeText(DashBoard.this, "display list", Toast.LENGTH_LONG).show();
            notifyModelData.setLeaveType(LeaveType);
            notifyModelData.setNotifyDate(Start + " To " + End);
            notifyModelData.setReason(etReason.getText().toString());
            notifyModelData.setStartDate(Start);
            notifyModelData.setEndDate(End);
            notifyModelData.setTimermodel(savedList);
            notifyModelArrayList.add(notifyModelData);
            savedLeaveAdapter = new SavedLeaveAdapter(getApplicationContext(), notifyModelArrayList);
            lvSavedNonScrollList.setAdapter(savedLeaveAdapter);



        temporaryList.clear();
        temporaryLeaveAdapter.notifyDataSetChanged();
        tvStartDate.setText("");
        tvEndDate.setText("");
        etReason.setText("");
        Start = "";
        End = "";



    }


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
        if (savedList == null) {
            savedList = new ArrayList<>();
        }
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
                timermodel = new Timermodel();
                Date sdate = new Date(curTime);
                timermodel.setLeaveDate(formatter.format(sdate));
                timermodel.setStartTime("10:00 AM");
                timermodel.setEndTime("06:00 PM");
                temporaryList.add(timermodel);
                savedList.add(timermodel);

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
}
