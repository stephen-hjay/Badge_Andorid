package tools;

import com.example.testapp.GlobalVariables;

import java.util.LinkedList;

public class DataCache {
    public final String type;
    public final String badge_id;
    public final String dataset_id;
    public LinkedList<Long> time_stamp;// yyyy-MM-dd-HH-mm-ss-mss
    public Object dataLock;

    public DataCache(String ty) {
        this.type = ty;
        badge_id = GlobalVariables.Parameters.badgeId;
        dataset_id = GlobalVariables.Parameters.dataSetId;
        time_stamp = new LinkedList<>();
        dataLock = new Object();
    }

    public void addTimeStamp() {
        this.time_stamp.add(System.currentTimeMillis());
    }

    public void clear() {
    }
}
