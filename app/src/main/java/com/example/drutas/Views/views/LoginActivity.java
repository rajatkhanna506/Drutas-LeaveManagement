package com.example.drutas.Views.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.drutas.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.example.drutas.Views.util.utility.MyPREFERENCES;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    SignInButton signInButton;
    ProgressDialog progressDialog;
    private GoogleApiClient googleApiClient;
    private static final int SIGN_IN = 1;
    SharedPreferences sharedPreferences;
    public String LoginStatus = "loginStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton = findViewById(R.id.signIn);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading");
                progressDialog.show();
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN);
            }
        });
        String check = sharedPreferences.getString(LoginStatus,"false");
        Log.d("TAG","check"+check);
        if(check.equalsIgnoreCase("true"))
        {
            Intent i = new Intent(this,DashBoard.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                progressDialog.dismiss();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LoginStatus, "true");
                editor.commit();
                startActivity(new Intent(LoginActivity.this, DashBoard.class));
                finish();
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
