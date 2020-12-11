package tools;

import android.util.Log;

import com.example.testapp.GlobalVariables;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestSender {
    public static void postDataWithParam(String jsonStr){
        if(!GlobalVariables.Parameters.ALLOW_TRANSFER){
            return;
        }
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        jsonStr = "{\"raw_x\":1,\"y\":2,\"z\":2,\"badge_id\":\"888\",\"time_stamp\":1833,\"dataset_id\":\"1\"}";
//        jsonStr="{\"ss\""+":"+jsonStr+"}";
//        GlobalVariables.Variables.deLog.setText(jsonStr);
//        Log.v("sysout",jsonStr);
        RequestBody body = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder().url(GlobalVariables.Parameters.SERVER_URL).post(body).build();
        client.newCall(request).enqueue((new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Log.d("sysout",""+response.code());
                    Log.d("sysout", ""+response.body().string());
                }else{
                    Log.d("sysout",""+response.code());
                    Log.d("sysout", ""+response.body().string());
                }
            }
        }));
    }

    public static void postDataWithParam(String jsonStr, SensorModuleName sensorModuleName){
        if(!GlobalVariables.Parameters.ALLOW_TRANSFER) {
            return;
        }
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        jsonStr = "{\"raw_x\":1,\"y\":2,\"z\":2,\"badge_id\":\"888\",\"time_stamp\":1833,\"dataset_id\":\"1\"}";
//        jsonStr="{\"ss\""+":"+jsonStr+"}";
//        GlobalVariables.Variables.deLog.setText(jsonStr);
//        Log.v("sysout",jsonStr);
        RequestBody body = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder().url(GlobalVariables.Parameters.SERVER_URL).post(body).build();
        Log.d("----------DataSending", sensorModuleName+"-----------");
        client.newCall(request).enqueue((new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Log.d("sysout reponse code",""+response.code());
                    Log.d("sysout success", ""+response.body().string());
                    Log.d("sysout sensorModuleName", sensorModuleName+"");
                }else{
                    Log.d("sysout reponse code",""+response.code());
                    Log.d("sysout success", ""+response.body().string());
                    Log.d("sysout sensorModuleName", sensorModuleName+"");
                }
            }
        }));
    }
}
