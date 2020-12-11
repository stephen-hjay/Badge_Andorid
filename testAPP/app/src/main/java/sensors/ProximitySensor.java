package sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import java.util.LinkedList;

import interfaces.SensorFunction;
import interfaces.StateMachineRunnable;
import tools.DataCache;
import tools.DataTransfer;


/*In Android, the proximity sensor is primarily used to detect when the user’s
face is close to the screen. This is how the phone screen seems to know to switch
 off when you hold it up to your ear during phone calls, preventing any errant button presses. */
public class ProximitySensor implements SensorEventListener, SensorFunction {
    private SensorManager sensorManager;
    private StateMachineRunnable stateMachine;
    private TextView[] displayData;
    private boolean displayOn;
    private long transferPeriod;
    private long periodCnt;
    private long timeStamp;
    private Data dataCache;
    private DataTransfer dataTransfer;
    public ProximitySensor(){}
    public ProximitySensor(SensorManager manager, StateMachineRunnable machine){
        sensorManager=manager;
        stateMachine=machine;
        displayOn=false;
        periodCnt=0;
        transferPeriod=10;
        timeStamp=0;
        dataCache=new ProximitySensor.Data("prox");//Proximity  set up data cache
//        dataTransfer=new DataTransfer(500,dataCache);//start sending
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(displayOn) {
            displayData[3].setText(String.format("%.5f", event.values[0]));
        }

        // auto sending data
//        if(transferPeriod>0){
//            periodCnt++;
//            if(periodCnt>=transferPeriod){
//                periodCnt=0;
//                timeStamp++;
//                RequestSender.postDataWithParam(buildJson(event.values[0],event.values[1],event.values[2],timeStamp));
//            }
//        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public int startSensor(){
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                sensorManager.SENSOR_DELAY_UI);// register to manager to start sensor
        return 0;
    }

    public int stopSensor(){
        sensorManager.unregisterListener(this);
        return 0;
    }



    @Override
    public int setTransferPeriod(long period) {
        transferPeriod=period;
        return 0;
    }

    @Override
    public int setSamplePeriod(long period) {
        return 0;
    }

    @Override
    public int enableDisplay(TextView[] textViews) {
        displayData=textViews;
        displayOn=true;
        return 0;
    }

    private class Data extends DataCache {
        LinkedList<Float> a;
        public Data(String ty){
            super(ty);
            a=new LinkedList<>();
        }

        @Override
        public void clear(){
//            synchronized (dataLock) {// sending need synchronized, no need another here
            this.time_stamp = new LinkedList<>();
            a = new LinkedList<>();
//            }
        }

        void addData(float[] data){
            synchronized (dataLock){
                a.add(data[0]);
                addTimeStamp();
            }
        }



    }
}
