package sensors;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.testapp.GlobalVariables;
import com.example.testapp.recorder.VoiceRecorder;
import com.konovalov.vad.Vad;
import com.konovalov.vad.VadConfig;

import java.io.IOException;
import java.util.LinkedList;

import interfaces.SensorFunction;
import tools.DataCache;
import tools.DataTransfer;
import tools.SensorModuleName;

public class MicrophoneSensor implements SensorFunction {
    private static boolean localStorage = false;
    TextView[] textViews;
    private boolean isRecording = false;
    private Thread thread;

    /**
     * MediaRecorder for saving audio in local storage
     **/
    private MediaRecorder mediaRecorder;

    private String filePath = null;

    // Audio Source
    private static final int AudioSource = MediaRecorder.AudioSource.MIC;
    // Standard Sample Rate
    private static final int SampleRateInHz = 44100;
    // Channel ?
    private static final int ChannelConfig = AudioFormat.CHANNEL_IN_DEFAULT;
    private static final int AudioFormat_ = AudioFormat.ENCODING_PCM_16BIT;
    private int BufferSize = AudioRecord.getMinBufferSize(SampleRateInHz, ChannelConfig, AudioFormat_);
    private AudioRecord audiorecord;
    private boolean enableDisplay;
    private Data dataCache;
    private long divCnt;
    private long divLimit;
    private DataTransfer dataTransfer;


    // the voice activity detection module
//    private ArrayAdapter sampleRateAdapter;
//    private ArrayAdapter frameAdapter;
//    private ArrayAdapter modeAdapter;
//    private VoiceRecorder recorder;
    private VadConfig config;
    private Vad vad;


    public MicrophoneSensor() {
    }

    public MicrophoneSensor(boolean local) {
        this.localStorage = local;
        enableDisplay = false;
        dataCache=new Data("Voice");
        divLimit= GlobalVariables.Parameters.MIC_SAMPLE_DIV;
        divCnt=0;
        dataTransfer=new DataTransfer(GlobalVariables.Parameters.MIC_TRANSFER_PERIOD,dataCache, SensorModuleName.MICROPHONE);
    }

    @Override
    public int startSensor() {
        if(!isRecording) {
            if (localStorage) {
                // filePath can be changed into the location where users want to store the audio
                filePath = Environment.getDownloadCacheDirectory().getAbsolutePath() + "/AudiioRecordFile";
                MediaRecorderReady();
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                if (AudioRecord.ERROR_BAD_VALUE == BufferSize || AudioRecord.ERROR == BufferSize) {
                    throw new RuntimeException("Unable to getMinBufferSize");
                }

                audiorecord = new AudioRecord(AudioSource, SampleRateInHz, ChannelConfig, AudioFormat_, BufferSize);
                if (audiorecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
                    throw new RuntimeException("The AudioRecord is not uninitialized");
                }
                config = VadConfig.newBuilder()
                        .setSampleRate(VadConfig.SampleRate.SAMPLE_RATE_16K)
                        .setFrameSize(VadConfig.FrameSize.FRAME_SIZE_480)
                        .setMode(VadConfig.Mode.VERY_AGGRESSIVE)
                        .build();

                // modified without callback
//                recorder = new VoiceRecorder(config);

                vad =  new Vad(config);

                destroyThread();
                isRecording = true;
                //  if(thread == null){
                thread = new Thread() {
                    @Override
                    public void run() {
                        startRecording();
                    }
                };
                thread.start();
                vad.start();
                //}
            }
            if(!GlobalVariables.Parameters.VOICE_DETECT) {
                dataTransfer.resume();
            }
        }
        return 0;
    }

    @Override
    public int stopSensor() {
        if(isRecording) {
            dataTransfer.pause();
            if (localStorage) {
                if (mediaRecorder == null)
                    return 0;
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
            } else {
                if (audiorecord != null) {
                    if (audiorecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                        audiorecord.stop();
                    }
                    if (audiorecord.getState() == AudioRecord.STATE_INITIALIZED) {
                        audiorecord.release();
                    }
                }
            }
            destroyThread();
            isRecording = false;
            dataTransfer.pause();
        }
        return 0;
    }

    @Override
    public int setTransferPeriod(long period) {
        return 0;
    }

    @Override
    public int setSamplePeriod(long period) {
        return 0;
    }

    @Override
    public int enableDisplay(TextView[] textViews) {
        enableDisplay = true;
        this.textViews = textViews;
        return 0;
    }

    private void destroyThread() {
        try {
            isRecording = false;
            if (null != thread && Thread.State.RUNNABLE == thread.getState()) {
                try {
                    Thread.sleep(500);
                    thread.interrupt();
                } catch (Exception e) {
                    thread = null;
                }
            }
            thread = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            thread = null;
        }
    }

    private void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setAudioSamplingRate(SampleRateInHz);
        mediaRecorder.setOutputFile(filePath);
    }

    private void startRecording() {
        short[] buffer = new short[BufferSize];
        // start voice activity detection

        audiorecord.startRecording();
        while (!thread.isInterrupted()&&isRecording && audiorecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {


            int ReadResult = audiorecord.read(buffer, 0, BufferSize);
            // 直接在这里复用

            boolean isSpeech = vad.isSpeech(buffer);


            double decible = 0.0;
            int rate = 0;
            long energy = 0;

            for (int i = 0; i < ReadResult; i++) {
                if (i > 0 && buffer[i] * buffer[i - 1] < 0) {
                    rate++;
                }
                if (Math.abs(buffer[i]) > decible) {
                    decible = (double) Math.abs(buffer[i]);
                }
                energy += Math.pow(buffer[i], 2);
            }


            /////////////////Voice detection
            if(GlobalVariables.Parameters.VOICE_DETECT) {
                if(GlobalVariables.Parameters.VOICE_ACTIVITY_DETECT){

                    if (GlobalVariables.Parameters.VOICE_ALWAYS_SEND){
                        // modify this state to control
                        if (isSpeech) {
                            GlobalVariables.Variables.deLog.setText("Voice Detected");
                            dataTransfer.resume();
                            if (enableDisplay) {
                                if (decible > 1) {
                                    decible = 20 * Math.log10(decible);
                                }
                                textViews[0].setText(String.format("%.2f", decible));
                            }

                            divCnt++;
                            if(divCnt>=divLimit){
                                divCnt=0;
                                dataCache.addData(20 * Math.log10(decible),1.0);
                            }
                        } else {
                            GlobalVariables.Variables.deLog.setText("No Voice");
                            dataTransfer.resume();
                            if (enableDisplay) {
                                if (decible > 1) {
                                    decible = 20 * Math.log10(decible);
                                }
                                textViews[0].setText(String.format("%.2f", decible));
                            }

                            divCnt++;
                            if(divCnt>=divLimit){
                                divCnt=0;
                                dataCache.addData(20 * Math.log10(decible),0.0);
                            }
                        }
                    }else{
                        // modify this state to control
                        if (isSpeech) {
                            GlobalVariables.Variables.deLog.setText("Voice Detected");
                            dataTransfer.resume();
                            if (enableDisplay) {
                                if (decible > 1) {
                                    decible = 20 * Math.log10(decible);
                                }
                                textViews[0].setText(String.format("%.2f", decible));
                            }

                            divCnt++;
                            if(divCnt>=divLimit){
                                divCnt=0;
                                dataCache.addData(20 * Math.log10(decible),1.0);
                            }
                        } else {
                            GlobalVariables.Variables.deLog.setText("No Voice");
                            dataTransfer.pause();
                            if (enableDisplay) {
                                if (decible > 1) {
                                    decible = 20 * Math.log10(decible);
                                }
                                textViews[0].setText(String.format("%.2f", decible));
                            }

                            divCnt++;
                            if(divCnt>=divLimit){
                                divCnt=0;
                                dataCache.addData(20 * Math.log10(decible),0.0);
                            }
                        }
                    }
                }else {
                    if (true) {
                        GlobalVariables.Variables.deLog.setText("Voice Detected (without voice detection)");
                        dataTransfer.resume();
                    } else {
                        GlobalVariables.Variables.deLog.setText("No Voice");
                        dataTransfer.pause();
                    }
                    if (enableDisplay) {
                        if (decible > 1) {
                            decible = 20 * Math.log10(decible);
                        }
                        textViews[0].setText(String.format("%.2f", decible));
                    }

                    divCnt++;
                    if(divCnt>=divLimit){
                        divCnt=0;
                        dataCache.addData(20 * Math.log10(decible),0.0);
                    }
                }
            }
            ///////////////////////////////////////////

    //       textViews[0].setText("" + rate);
    //       textViews[1].setText("" + energy);



        }
    }

    class Data extends DataCache{

        public LinkedList<Double> decibel;
        public LinkedList<Double> frequency;


        public Data(String type){
            super(type);
            decibel=new LinkedList<>();
            frequency=new LinkedList<>();
        }

        @Override
        public void clear() {
            decibel=new LinkedList<>();
            frequency=new LinkedList<>();
            time_stamp=new LinkedList<>();
        }

        public void addData(Double dec, Double freq){
            synchronized (this.dataLock) {
                decibel.add(dec);// dec -> decimal
                frequency.add(freq);// freq -> frequency
                addTimeStamp();
            }
        }
    }
}
