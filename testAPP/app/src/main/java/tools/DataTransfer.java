package tools;

import com.example.testapp.GlobalVariables;

public class DataTransfer {//auto send data class
    private boolean canSend;
    public long period;
    private DataCache dataCache;
    public Thread thread;
    public SensorModuleName sensorModuleName;

    public DataTransfer(long timeMs, DataCache cache) {
        dataCache = cache;
        period = timeMs;
        thread = new Thread() {
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(period);//ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (dataCache.dataLock) {
                        if(canSend) {
//                            RequestSender.postDataWithParam(ClassToJson.convert(dataCache));
                            RequestSender.postDataWithParam(ClassToJson.convert(dataCache));
                            dataCache.clear();
                        }
                    }
                }
            }
        };
        thread.start();
        canSend = false;
    }
    
    public DataTransfer(long timeMs, DataCache cache, SensorModuleName sensorModuleName) {
        dataCache = cache;
        period = timeMs;
        this.sensorModuleName = sensorModuleName;
        thread = new Thread() {
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(period);//ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (dataCache.dataLock) {
                        if(canSend) {
                            // encryption enbale
                            String jsonStr = ClassToJson.convert(dataCache);
                            RequestSender.postDataWithParam(jsonStr,sensorModuleName);
                            dataCache.clear();
                        }
                    }
                }
            }
        };
        thread.start();
        canSend = false;
    }

    public void pause() {

        canSend = false;
    }

    public void resume() {
        canSend = true;
    }

}
