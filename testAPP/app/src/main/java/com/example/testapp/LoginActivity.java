package com.example.testapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import tools.ClassToJson;
import tools.DataCache;
import tools.ResponseSuccessEntity;

public class LoginActivity extends AppCompatActivity {

    private  EditText usernameEditText;
    private EditText passwordEditText;
    private EditText userIDEditTxt;
    private EditText dataSetIDTxt;
    private String uName;
    private String pwd;
    private String uID;
    private String dataID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        userIDEditTxt=findViewById(R.id.txUserID);
        dataSetIDTxt=findViewById(R.id.txDataSetID);
        final Button loginButton = findViewById(R.id.login);
        checkPermissions();
        getInfoFromSharedPreference();
        userIDEditTxt.setText(uName, TextView.BufferType.NORMAL);
        passwordEditText.setText(pwd,TextView.BufferType.NORMAL);
        userIDEditTxt.setText(uID,TextView.BufferType.NORMAL);
        dataSetIDTxt.setText(dataID,TextView.BufferType.NORMAL);

            //login action
            loginButton.setOnClickListener((x) -> {
                if(GlobalVariables.Parameters.LOGIN) {
                    // four fields
                    uName = usernameEditText.getText().toString();
                    pwd = passwordEditText.getText().toString();
                    uID = userIDEditTxt.getText().toString();
                    dataID = dataSetIDTxt.getText().toString();
                    //  showToast("please fill all blanks");
                    /*
                    String a;//mac addr
                    String b;//user name
                    String c;//user id
                    String d;//pwd
                     */

                    if (uName == null || uName.length() == 0 || pwd == null || pwd.length() == 0||
                    uID==null||uID.length()==0||dataID==null||dataID.length()==0){
                        showToast("please fill all blanks");
                        return;
                    } else {
                        GlobalVariables.Parameters.dataSetId=dataID;

                        //check if correct
                        if(GlobalVariables.Parameters.SERVER_LOGIN) {
                            showToast("logging in, please wait");
                            int timing = 0;
                            boolean flag = true;
                            try {
                                checkToServer(uName,pwd,uID);
                                while(flag) {
                                    try {
                                        Thread.sleep(200);
                                    } catch (Exception e) {
                                    }
                                    timing++;
                                    if (GlobalVariables.Variables.loginCode != 0) {
                                        //login success
                                        if(GlobalVariables.Variables.loginCode==200){
                                            if(GlobalVariables.Variables.loginResponseBody.equals("true")) {
                                                showToast("Login Success!");
                                                //   showToast(GlobalVariables.Variables.loginCode+"");

                                                // autofill
                                                setInfoFromSharedPreference();
                                                flag = false;
                                                loginGo();
                                            }else{//wrong uname or pwd
                                                showToast("incorrect username or password, code: "+GlobalVariables.Variables.loginResponseBody);
                                                GlobalVariables.Variables.loginCode=0;
                                                flag=false;
                                            }
                                        }else if(timing>25){//timeout
                                            GlobalVariables.Variables.loginCode=0;
                                            showToast("network or server error");
                                            flag=false;
                                        }

                                    }
                                }
                            }catch(Exception e){
                                showToast(e.toString());
                            }
                        }else{
                            //dummy check
                            if (uName.equals("abc") && pwd.equals("123")) {
                                loginGo();
                            } else {
                                showToast("incorrect username or password");
                            }
                        }
                    }
                }else {
                    loginGo();
                }
            });
    }

    private void loginGo(){
        usernameEditText.setText("");
        passwordEditText.setText("");
        Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(MainIntent);
    }

    //permission self check
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};

    private void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                break;
        }

        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        //
        i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
        startActivity(i);
    }

    void checkToServer(String uname,String pwd,String uID){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(ClassToJson.convert(new Data(uname,pwd,uID)), JSON);
        Request request = new Request.Builder().url(GlobalVariables.Parameters.SERVER_URL).post(body).build();
        //  String TAG = "logintest";
        // asynchronous request with callback.
        client.newCall(request).enqueue((new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody body = response.body();
                if (body != null) {
                    Gson gson=new Gson();
                    GlobalVariables.Variables.loginCode=response.code();
                    String responseJSON = response.body().string();
                    ResponseSuccessEntity responseSuccessEntity = gson.fromJson(responseJSON,ResponseSuccessEntity.class);
                    GlobalVariables.Variables.loginResponseBody = responseSuccessEntity.getSuccess();
                    Log.d("reponse result",responseSuccessEntity.getSuccess());
                    body.close();
//                    Log.d("reponseCode",GlobalVariables.Variables.loginCode+"");

                }
            }
        }));
    }

    class Data extends DataCache{
        String a;//mac addr
        String b;//user name
        String c;//user id
        String d;//pwd
        public Data(String uname,String pwd,String uID){
            super("BadgeMetadata");// user and device ID
            // 888 fot test -> BT_MAC_ID for device ID
            a = GlobalVariables.Parameters.MY_BT_MAC_ID;
            b=uname;// "lai"
            c=uID;// "lai8"
            d=pwd;//"kkk"
            this.addTimeStamp();
        }
    }

    private void showToast(String Str) {
        Toast.makeText(this, Str, Toast.LENGTH_SHORT).show();
    }

    // read the last preference from local file
    private void getInfoFromSharedPreference(){
        SharedPreferences preferences=getSharedPreferences("loginData",MODE_PRIVATE);
        uName = preferences.getString("usrName","");// default : lai
        pwd = preferences.getString("pwd","");// 888
        uID = preferences.getString("uID","");// lai8
        dataID = preferences.getString("dataSetIP","");//xxx
        Log.d("saveInfo","recover info from local");
    }
    private void setInfoFromSharedPreference(){
        SharedPreferences.Editor editor=getSharedPreferences("loginData",MODE_PRIVATE).edit();
        editor.putString("usrName",uName);
        editor.putString("pwd",pwd);
        editor.putString("usrID",uID);
        editor.putString("dataSetIP",dataID);
        Log.d("saveInfo","success");
        editor.apply();
    }
}


