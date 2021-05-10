package com.example.testapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import java.util.LinkedList;

import interfaces.StateMachineRunnable;
import interfaces.StopScan;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import tools.ClassToJson;
import tools.DataCache;

public class SimpleScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler  {
    private ZXingScannerView mScannerView;
    private Button btStop;
    private Button btScanOk;
    private StateMachineRunnable stateMachine;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.empty);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//force portrait
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        btStop=findViewById(R.id.stopCam);
        btScanOk=findViewById(R.id.scanOK);

        if(GlobalVariables.Parameters.SHOW_NOTHING){
            btStop.setVisibility(View.GONE);
            btScanOk.setVisibility(View.GONE);
        }else{
            // test mode
            btScanOk.setOnClickListener((x)->{
                scanOk("button pressed");
            });
            btStop.setOnClickListener((x)->{
                stopScan();
            });

        }
        GlobalVariables.Variables.stopScan=new ExternalStopScan();
        GlobalVariables.Variables.isScanning=true; // why?
        if(GlobalVariables.Parameters.SCREEN_ON) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        GlobalVariables.Variables.isScanning=true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    private void stopScan(){
        GlobalVariables.Variables.isScanning=false;
        GlobalVariables.Variables.stopScan=null;
        mScannerView.stopCamera();
        finish();
    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        scanOk(rawResult.getText());
        //send badge 2 id to server
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
            }
        }, 2000);

    }

    class ExternalStopScan implements StopScan {

        @Override
        public void stop() {
            stopScan();
        }
    }

    private void scanOk(String code){
        GlobalVariables.Variables.haveQR=true;
        // used to updata global QRCode variable;
        new Data("QRcode",code);
        new Thread(){
            @Override
            public void run(){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    e.printStackTrace();
                }
                GlobalVariables.Variables.stateMachine.run();
            }
        }.start();

        stopScan();
    }

    class Data extends DataCache {
        LinkedList<String> result;
        public Data(String type, String code){
            super(type);
            result=new LinkedList<>();
            result.add(code);
            this.addTimeStamp();

            GlobalVariables.Variables.qrCode=(ClassToJson.convert(this));//only one element, directly send
        }
    }
}