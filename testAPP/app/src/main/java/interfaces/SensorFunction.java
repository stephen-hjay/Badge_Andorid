package interfaces;

import android.widget.TextView;

public interface SensorFunction {
    public int startSensor();
    public int stopSensor();
    public int setTransferPeriod(long period);//ms 0 means stop
    public int setSamplePeriod(long period);//ms   0 means stop
    public int enableDisplay(TextView[] textViews);
}
