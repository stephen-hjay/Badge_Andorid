package com.example.testapp;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.testapp.recorder.VoiceRecorder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.konovalov.vad.VadConfig;

import interfaces.StateMachineRunnable;
import interfaces.StopScan;

public class GlobalVariables {
    private GlobalVariables() {
    }

    public static class Variables {
        public static int deviceCnt = 0;
        public static boolean haveQR = false;
        public static String qrCode = "";
        public static StopScan stopScan = null;
        public static boolean isScanning = false;
        public static StateMachineRunnable stateMachine = null;
        public static int reallyNear = 0;// 0 no really near, 1 have really near, 2 really near changed
        public static TextView deLog=null;
        public static int loginCode = 0;// code from login server response
        public static String loginResponseBody = "";
    }

    public static class Parameters {
        //badge basic info
        public static final String badgeId = "device-0513-1";
        public static String dataSetId = "8-hour-2";
        // public static final String SERVER_URL = "https://6zowfrzywc.execute-api.us-west-2.amazonaws.com/dev/api/";
        // public static final String SERVER_URL="http://192.168.0.4:8080/badge/";
        public static final String SERVER_URL="https://voy9lqy01l.execute-api.us-west-2.amazonaws.com/dev/api";

        // it needs to be customized for every machine
        // public static final String MY_BT_MAC_ID="A8:3E:0E:B7:7C:34";
        public static final String MY_BT_MAC_ID="888";

        //global settings
        public static final boolean ALLOW_TRANSFER=true;
        public static final boolean START_BLUE=true;
        public static final boolean START_ACC=true;
        public static final boolean ACC_FIX=false;
        public static final boolean START_MIC=true;
        public static final boolean VOICE_DETECT=false;
        public static final boolean VOICE_ACTIVITY_DETECT=true;
        public static final boolean SCREEN_ON = true;
        public static final boolean SHOW_NOTHING=false;

        public static final boolean LOGIN = false;
        public static final boolean SERVER_LOGIN=false;

        public static final boolean START_PROXI=false;


        //Bluetooth
        public static final long BLUE_SAMPLE_PERIOD = 3000;// bluetoothe scan period (ms)
        public static final long BLUE_SCAN_TIME_LIMIT = 5;//scan times for one output
        public static final double BLUE_NEAR_THRESHOLD = 10.0;//threshold for near device (meter)
        public static final double BLUE_REALLY_NEAR_THRESHOLD = 3.0;//threshold for really near device (meter)
        public static String[] blueToothMacs = new String[]{"2C:41:A1:F5:B9:F6", "5A:BF:46:B0:A9:CE",
                "A8:3E:0E:B7:74:F6", "A8:3E:0E:B7:7C:34","70:3C:69:48:0E:B0","A8:3E:0E:B7:78:EA","48:01:C5:0C:31:93","A8:3E:0E:B7:7A:EC"};

        //accelerometer
        public static final long ACC_SAMPLE_DIV = 2;// sample rate about 60,000 us=0.06s        need: 8/s
        public static final long ACC_TRANSFER_PERIOD = 6000;//acc data transfer period ms    need: 1 per 6s
        public static final long ACC_FIX_PERIOD = 2000;//acc data fix accumulation period ms

        //microphone
        public static final long MIC_SAMPLE_DIV = 5;// sample rate about  ???
        public static final long MIC_TRANSFER_PERIOD = 6000;//mic data transfer period ms
    }







}
