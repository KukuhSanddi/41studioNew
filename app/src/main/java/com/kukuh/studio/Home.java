package com.kukuh.studio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Home extends AppCompatActivity {
    //Initate database
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //Initiate context this page
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Call calendar class
        Calendar calendar = Calendar.getInstance();

        //Call jam format
        SimpleDateFormat jamFormat = new SimpleDateFormat("HH:mm:ss");
        final String jamCheckin = jamFormat.format(calendar.getTime());

        //Call date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        final String date = dateFormat.format(calendar.getTime());

        //Load view from XML
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Transition
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        //Call class myService
        startService(new Intent(this,myService.class));

        //initiate btnVisitor
        Button btnVisitor = findViewById(R.id.btn_Visitor);

        //Set btnVisitor listener
        btnVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Home.this, MainVisitor.class);
                startActivity(intent);
            }
        });

        //initiate and set listener of btnLogout
        Button btnLogout1 = findViewById(R.id.logout);
        btnLogout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Home.this, VisitorLogout.class);
                startActivity(in);
            }
        });
    }

    //method onclick button Employee
    public void toEmployee (View view){
        Intent intent = new Intent (this, SpeechEmployee.class);
        startActivity(intent);
    }

}

//    <!--Code By Kukuh Sanddi Razaq & M. Adli Rachman-->
