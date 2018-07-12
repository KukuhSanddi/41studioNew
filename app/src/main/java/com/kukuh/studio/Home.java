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
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat jamFormat = new SimpleDateFormat("HH:mm:ss");
        final String jamCheckin = jamFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        final String date = dateFormat.format(calendar.getTime());

        DatabaseReference dRef = database.getReference("visitors").child(date);
        dRef.orderByChild("checkout").equalTo("").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Button btnLogout = findViewById(R.id.logout);
                    btnLogout.setVisibility(View.VISIBLE);
                }else{
                    Button btnLogout = findViewById(R.id.logout);
                    btnLogout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        getWindow().getAttributes().windowAnimations = R.style.Fade;

        startService(new Intent(this,myService.class));

        Button btnVisitor = findViewById(R.id.btn_Visitor);

        btnVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Home.this, MainVisitor.class);
                startActivity(intent);
            }
        });

        Button btnLogout1 = findViewById(R.id.logout);
        btnLogout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Home.this, VisitorLogout.class);
                startActivity(in);
            }
        });
    }

    public void toEmployee (View view){
        Intent intent = new Intent (this, SpeechEmployee.class);
        startActivity(intent);
    }

}
