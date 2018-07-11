package com.kukuh.studio;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SpeechEmployee extends AppCompatActivity {

    private static final int VR_REQUEST = 999;
    public static final String TAG = null;

    private final String LOG_TAG = "SpeechRepeatActivity";
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private boolean mIslistening;
    TextView mText;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Database dbase = new Database();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_employee);

        getWindow().getAttributes().windowAnimations = R.style.Fade;

        int[] colors = {
                ContextCompat.getColor(this, R.color.colorBlue),
                ContextCompat.getColor(this, R.color.colorRed),
                ContextCompat.getColor(this, R.color.colorGreen),
                ContextCompat.getColor(this, R.color.colorYellow),
                ContextCompat.getColor(this, R.color.colorWhite)
        };

        int[] heights = { 55, 59, 53, 58, 51, 55, };

        ImageView speechBtn =  findViewById(R.id.speech_btn);

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> intActivities = packManager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);


        final RecognitionProgressView recogView = findViewById(R.id.recognition_view);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recogView.setSpeechRecognizer(mSpeechRecognizer);
        recogView.setRecognitionListener(new RecognitionListenerAdapter() {
            @Override
            public void onResults(Bundle results) {
                showResults(results);
            }
        });

        recogView.setColors(colors);
        recogView.setBarMaxHeightsInDp(heights);
        recogView.setCircleRadiusInDp(8);
        recogView.setSpacingInDp(20);
        recogView.setIdleStateAmplitudeInDp(15);
        recogView.setRotationRadiusInDp(30);
        recogView.play();

        speechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (ContextCompat.checkSelfPermission(SpeechEmployee.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                   requestRecordAudioPermission();
               } else {
                   startRecognition();
                   Toast.makeText(SpeechEmployee.this,"Start Listening",Toast.LENGTH_SHORT).show();
                   recogView.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           startRecognition();
                       }
                   }, 50);
               }
            }
        });



        mText = (TextView) findViewById(R.id.mText);

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

    private void showResults(Bundle results) {
        Log.d(TAG, "onResults" + results); //$NON-NLS-1$
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        // matches are the return values of speech recognition engine
        // Use these values for whatever you wish to do
        String str = new String();
        for (int i=0; i<matches.size(); i++){
            Log.d(TAG,"result: "+matches.get(i));
            str += matches.get(i);
        }
        mText.setText(matches.get(0));
        searchEmployee(matches.get(0));
    }

    //Search Employee
    public void searchEmployee(final String nama){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat jamFormat = new SimpleDateFormat("HH:mm:ss");
        final String jamCheckin = jamFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
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
                                    dbase.checkoutEmp(nama);
                                }else{
                                    dbase.checkinEmp(emp);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }else{
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
