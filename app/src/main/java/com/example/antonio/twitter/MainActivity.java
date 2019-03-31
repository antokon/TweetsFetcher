package com.example.antonio.twitter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TwitterI.AsyncTaskResponse {


    //TextView tv;
    EditText usernameField;
    Button getButton;
    ListView listv;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        usernameField = findViewById(R.id.usernameField);

        getButton = findViewById(R.id.getButton);

        listv = findViewById(R.id.lv);
        listv.setVisibility(View.GONE);
        getButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                username = String.valueOf(usernameField.getText());

                if(!checkInternetConnectivity(MainActivity.this))
                    Toast.makeText(MainActivity.this, "Check internet access!", Toast.LENGTH_SHORT).show();

                else if(username.matches("")) {
                    Toast.makeText(MainActivity.this, "A username is needed!", Toast.LENGTH_SHORT).show();
                }
                else {

                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET)== PackageManager.PERMISSION_GRANTED ){
                       fetchTweetData();

                    }else{
                        requestPermissions(new String[]{Manifest.permission.INTERNET},0);
                    }

                   }
            }});
    }
 private  void fetchTweetData(){
     TwitterI twitterTimeline = new TwitterI(username);

     twitterTimeline.delegateResp = MainActivity.this;

     twitterTimeline.execute();

     listv.setVisibility(View.INVISIBLE);
 }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            fetchTweetData();

    }

    @Override
    public void processResponse(List<String> response) {

        Log.d("Timeline", response + "");
        if (response.isEmpty()) { Toast.makeText(MainActivity.this, "Nothing to show!", Toast.LENGTH_SHORT).show();
        }
        else if(response.get(0).contains("statusCode=400")) { Toast.makeText(MainActivity.this, "Problem. Try again!", Toast.LENGTH_SHORT).show();
            response.remove(0);
        }
        else { Intent intent = new Intent();

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, response);
            listv.setAdapter(arrayAdapter);
            listv.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkInternetConnectivity(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}


