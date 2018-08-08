package com.kukuh.studio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;

import AlizeSpkRec.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class SpeechEmployee extends AppCompatActivity {

    private static final int VR_REQUEST = 999;
    public static final String TAG = null;

    private final String LOG_TAG = "SpeechRepeatActivity";
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private PulsatorLayout pulsator;
    private boolean mIslistening;
    TextView mText;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Database dbase = new Database();
    private TextToSpeech textToSpeech;

    //Alize Speaker Recognition
    SimpleSpkDetSystem alizeSystem;
    AudioRecord audioRecord = null;
    boolean isRecording = false;
    int buffSize2 = 0;
    short[] shortBuffer2 = new short[1024];
    int sampleRate = 48000;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_employee);

        getWindow().getAttributes().windowAnimations = R.style.Fade;

        //AlizeSR loadBackgroundModel
        try {
            config();
            loadBackgroundmodel();
            InputStream inputKerry = getResources().openRawResource(R.raw.kerry);
            trainWavModel(inputKerry,"Dani");
            alizeSystem.resetAudio();
            InputStream inputCarter = getResources().openRawResource(R.raw.carter);
            trainWavModel(inputCarter,"Via Nisa");
            alizeSystem.resetAudio();
            alizeSystem.resetFeatures();
            InputStream inputRumsfeld = getResources().openRawResource(R.raw.rumsfeld);
            trainWavModel(inputRumsfeld,"Lorem");
            alizeSystem.resetAudio();
            alizeSystem.resetFeatures();
            InputStream inputBush= getResources().openRawResource(R.raw.bush);
            trainWavModel(inputBush,"Davis");
            alizeSystem.resetAudio();
            alizeSystem.resetFeatures();
            InputStream inputChurchill = getResources().openRawResource(R.raw.churchill);
            trainWavModel(inputChurchill,"Ipsum");
            alizeSystem.resetAudio();
            alizeSystem.resetFeatures();
            Log.d("Alize Status", "Speaker Count: "+String.valueOf(alizeSystem.speakerCount()));
            Log.d("Alize Status", "isUBMLoaded  : "+String.valueOf(alizeSystem.isUBMLoaded()));
        } catch (AlizeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] colors = {
                ContextCompat.getColor(this, R.color.colorBlue),
                ContextCompat.getColor(this, R.color.colorRed),
                ContextCompat.getColor(this, R.color.colorGreen),
                ContextCompat.getColor(this, R.color.colorYellow),
                ContextCompat.getColor(this, R.color.colorWhite)
        };

        int[] heights = { 55, 59, 53, 58, 51, 55 };

        Button speechBtn =  findViewById(R.id.btn_record);
//        Button stopBtn = findViewById(R.id.speech_btn_out);
        pulsator = findViewById(R.id.pulsator);
//        ImageView imgV = findViewById(R.id.circleImageView);
//        imgV.bringToFront();
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setLanguage(new Locale("id", "ID"));
            }
        });



        PackageManager packManager = getPackageManager();
        List<ResolveInfo> intActivities = packManager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);


        final RecognitionProgressView recogView = findViewById(R.id.recognition_view);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);

//        recogView.setSpeechRecognizer(mSpeechRecognizer);
//        recogView.setRecognitionListener(new RecognitionListenerAdapter() {
//            @Override
//            public void onResults(Bundle results) {
//                showResults(results);
//            }
//        });

        recogView.setColors(colors);
        recogView.setBarMaxHeightsInDp(heights);
        recogView.setCircleRadiusInDp(8);
        recogView.setSpacingInDp(20);
        recogView.setIdleStateAmplitudeInDp(15);
        recogView.setRotationRadiusInDp(30);
        recogView.play();


        if (ContextCompat.checkSelfPermission(SpeechEmployee.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestRecordAudioPermission();
        } else {
//            pulsator.start();
//            startRecognition();
            recogView.play();
//            Toast.makeText(SpeechEmployee.this, "Start Listening", Toast.LENGTH_SHORT).show();
        }


        speechBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(SpeechEmployee.this,"Start Recording",Toast.LENGTH_SHORT).show();
                        startRecording();
                        return true;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(SpeechEmployee.this,"Recording stopped",Toast.LENGTH_SHORT).show();
                        try {
                            stopRecording();
                        } catch (AlizeException e) {
                            e.printStackTrace();
                        }

                }
                return false;
            }
        });


//        speechBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               if (ContextCompat.checkSelfPermission(SpeechEmployee.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
//                   requestRecordAudioPermission();
//               } else {
//                   pulsator.start();
//                   startRecognition();
//                   Toast.makeText(SpeechEmployee.this,"Start Listening",Toast.LENGTH_SHORT).show();
////                   recogView.postDelayed(new Runnable() {
////                       @Override
////                       public void run() {
////                           startRecognition();
////                       }
////                   }, 50);
//               }
//            }
//        });

//        stopBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pulsator.stop();
//                mSpeechRecognizer.stopListening();
////                recogView.stop();
////                recogView.play();
//            }
//        });

        mText = findViewById(R.id.mText);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }
    }


    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;

            // If the user previously denied this permission then show a message explaining why
            // this permission is needed
            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }

    private void startRecognition(){
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                "id-ID");
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Ucapkan nama anda");
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginingOfSpeech");
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {
            Log.d(TAG, "onEndOfSpeech");
        }

        @Override
        public void onError(int error)
        {
            if(error == 8){
                mSpeechRecognizer.cancel();
                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            }
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

            Log.d(TAG, "error = " + error);

        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results)
        {
            Log.d(TAG, "onResults" + results); //$NON-NLS-1$
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            // matches are the return values of speech recognition engine
            // Use these values for whatever you wish to do
            String str = new String();
            for (int i=0; i<matches.size(); i++){
                Log.d(TAG,"result: "+matches.get(i));
                str += matches.get(i);
            }
            mText.setText(toTitleCase(matches.get(0)));
            searchEmployee(toTitleCase(matches.get(0)));
            startRecognition();
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {
        }
    }


    //Search Employee
    public void searchEmployee(final String nama){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat jamFormat = new SimpleDateFormat("HH:mm:ss");
        final String jamCheckin = jamFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        final String date = dateFormat.format(calendar.getTime());

        final DatabaseReference dRef = database.getReference("employees").child("dataKaryawan");
        final DatabaseReference refAbs = database.getReference("employees").child("absensi").child(date);
        dRef.orderByChild("nama").equalTo(nama).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        Employee empTemp = data.getValue(Employee.class);
                        final Employee emp = new Employee(empTemp.getNama(),empTemp.getEmail(),jamCheckin,"");
                        refAbs.orderByChild("nama").equalTo(nama).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    refAbs.orderByChild("checkout").equalTo("").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                dbase.checkoutEmp(emp.getNama());
                                                textToSpeech.speak("Sampai Jumpa "+mText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                                                Toast.makeText(SpeechEmployee.this,"Anda berhasil checkout",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(SpeechEmployee.this,"Anda sudah melakukan checkout",Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }else{
                                    dbase.checkinEmp(emp);
                                    textToSpeech.speak("Selamat Datang "+mText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                                    Toast.makeText(SpeechEmployee.this,"Anda berhasil checkin",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }else{
                    Toast.makeText(SpeechEmployee.this,"Nama tidak ada",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    //Alize SR

    private void startRecording(){
        buffSize2 = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT );
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize2);
        audioRecord.startRecording();

        isRecording=true;
        audioRecord.read(shortBuffer2,0,shortBuffer2.length);
    }

    private void stopRecording() throws AlizeException {
        audioRecord.stop();
        isRecording = false;
        identifySpeaker(shortBuffer2);


    }

    private void loadBackgroundmodel() throws IOException, AlizeException {
        InputStream backgroundModelAsset = getApplicationContext().getAssets().open("gmm/world.gmm");
        alizeSystem.loadBackgroundModel(backgroundModelAsset);
        backgroundModelAsset.close();
    }

    private void config() throws IOException, AlizeException {
        InputStream configAsset = getApplicationContext().getAssets().open("AlizeConfigurationExample.cfg");
        alizeSystem = new SimpleSpkDetSystem(configAsset,getApplicationContext().getFilesDir().getPath());
        configAsset.close();
    }



    public void trainSpeakerModel(short[] input) throws AlizeException {
//        if(input != null){
//            String nama = mNama.getText().toString();
//            alizeSystem.addAudio(input);
//            alizeSystem.createSpeakerModel(nama);
//            alizeSystem.saveSpeakerModel(nama,"test_"+nama);
//            Toast.makeText(MainActivity.this,"Train Speaker berhasil!",Toast.LENGTH_SHORT).show();
//            Log.d("System Status", "speaker : "+String.valueOf(alizeSystem.speakerCount()));
//        }
    }

    public void trainWavModel(InputStream is,String nama) throws IOException, AlizeException {
        InputStream wavSpeaker = is;
        byte[] speaker = new byte[wavSpeaker.available()];
        alizeSystem.addAudio(speaker);
        wavSpeaker.close();
        alizeSystem.createSpeakerModel(nama);
        alizeSystem.adaptSpeakerModel(nama);
        alizeSystem.saveSpeakerModel(nama,"test_"+nama);
    }

    public void identifySpeaker(short[] data) throws AlizeException {
        try {
            alizeSystem.resetAudio();
            alizeSystem.resetFeatures();
            alizeSystem.addAudio(data);
            SimpleSpkDetSystem.SpkRecResult identificationRes = alizeSystem.identifySpeaker();
            String nama = identificationRes.speakerId;
            mText.setText("Hello "+nama);
            Float score = identificationRes.score;
            Log.d("Score",score.toString());
            searchEmployee(nama);
        } catch (AlizeException e) {
            e.printStackTrace();
        }
    }

}
//    <!--Code By Kukuh Sanddi Razaq & M. Adli Rachman-->