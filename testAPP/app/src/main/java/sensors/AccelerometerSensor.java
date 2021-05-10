package sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import com.example.testapp.GlobalVariables;

import java.util.LinkedList;

import interfaces.SensorFunction;
import interfaces.StateMachineRunnable;
import tools.DataCache;
import tools.DataTransfer;
import tools.SensorModuleName;

public class AccelerometerSensor implements SensorEventListener, SensorFunction {
    private SensorManager sensorManager;
    private StateMachineRunnable stateMachine;
    private TextView[] displayData;
    private boolean displayOn;
    private long sampleCnt;
    private long sampleDiv;  // sample rate about 60,000 microsecond     0.06s
    private Data dataCache;
    private DataTransfer dataTransfer;
    private Object fixLock;
    private float[] fixAccu;
    private int fixAccuCnt;
    private float[] fixParam;
    public AccelerometerSensor(){}
    public AccelerometerSensor(SensorManager manager, StateMachineRunnable machine){
        sensorManager=manager;
        stateMachine=machine;
        displayOn=false;
        sampleCnt=0;
        dataCache=new Data("Movement");//accelerometer  set up data cache
        dataTransfer=new DataTransfer(GlobalVariables.Parameters.ACC_TRANSFER_PERIOD,dataCache, SensorModuleName.ACCELEROMETERS);//start sending
        sampleDiv= GlobalVariables.Parameters.ACC_SAMPLE_DIV;
        startSensor();
        dataTransfer.resume();
        if(GlobalVariables.Parameters.ACC_FIX){
            fixLock=new Object();
            fixAccu=new float[3];
            fixAccuCnt=0;
            fixParam=new float[3];

            new Thread(){
                @Override
                public void run() {
                    try{
                        // 2000 ms 一次
                        Thread.sleep(GlobalVariables.Parameters.ACC_FIX_PERIOD);
                    }catch(Exception e){}
                    fix();
                }
            }.start();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(displayOn) {
            // display to test the accelerometer values change
            displayData[0].setText(String.format("%.5f", event.values[0]));
//            displayData[1].setText(String.format("%.5f", event.values[1]));
//            displayData[2].setText(String.format("%.5f", event.values[2]));
//            Log.v("sysout","herer");
        }

        sampleCnt++;
        if(sampleCnt>=sampleDiv){
            sampleCnt=0;
            // accelerometer fix
            if(GlobalVariables.Parameters.ACC_FIX){
                synchronized (fixLock){
                    for(int i=0;i<3;i++){
                        fixAccu[i]+=event.values[i];
                    }
                    fixAccuCnt++;
                }
                //correct acc values
            }
            dataCache.addData(event.values);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public int startSensor(){
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                sensorManager.SENSOR_DELAY_UI);// register to manager to start sensor
        dataTransfer.resume();
        return 0;
    }

    public int stopSensor(){
        sensorManager.unregisterListener(this);
        dataTransfer.pause();
        return 0;
    }

    //fix acceleration data
    private void fix(){
        synchronized (fixLock){
            if(fixAccuCnt>0){
                //fix module and get result
                fixAccu[0]/=fixAccuCnt;
                fixAccu[1]/=fixAccuCnt;
                fixAccu[2]/=fixAccuCnt;


                fixAccuCnt=0;
                fixAccu[0]=0;
                fixAccu[1]=0;
                fixAccu[2]=0;
            }
        }
    }

    @Override
    public int setTransferPeriod(long period) {
        if(period>0) {
            dataTransfer.period = period;
            dataTransfer.resume();
        }else{
            dataTransfer.pause();
        }
        return 0;
    }

    @Override
    public int setSamplePeriod(long period) {
        if(period>0){
            sampleDiv=period;
        }
        return 0;
    }

    @Override
    public int enableDisplay(TextView[] textViews) {
        displayData=textViews;
        displayOn=true;
        return 0;
    }

    private class Data extends DataCache{
        LinkedList<Float> x;
        LinkedList<Float> y;
        LinkedList<Float> z;
        public Data(String ty){
            super(ty);
            x=new LinkedList<>();
            y=new LinkedList<>();
            z=new LinkedList<>();
        }

        @Override
        public void clear(){
//            synchronized (dataLock) {// sending need synchronized, no need another here
                time_stamp = new LinkedList<>();
                x = new LinkedList<>();
                y = new LinkedList<>();
                z = new LinkedList<>();
//            }
        }

        void addData(float[] data){
            synchronized (dataLock){
                x.add(data[0]);
                y.add(data[1]);
                z.add(data[2]);
                this.addTimeStamp();
            }
        }



    }
}
