package com.kukuh.studio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class Home extends AppCompatActivity {

    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        Button btnLogout = findViewById(R.id.logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
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
