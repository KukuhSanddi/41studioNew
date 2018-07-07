package com.kukuh.studio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Home extends AppCompatActivity {

    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this,myService.class));

        Button btnVisitor = findViewById(R.id.btn_Visitor);

        btnVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Home.this, MainVisitor.class);
                startActivity(intent);
//                AlertDialog.Builder dialogBox = new AlertDialog.Builder(context);
//
//                dialogBox.setTitle("Selamat Datang di 41 studio");
//
//                dialogBox
//                        .setMessage(" ")
//                        .setCancelable(true)
//                        .setPositiveButton("Masuk", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent(Home.this, MainVisitor.class);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent(Home.this, VisitorLogout.class);
//                                startActivity(intent);
//                            }
//                        });
//                AlertDialog alertDialog = dialogBox.create();
//
//                alertDialog.show();
            }
        });

        ImageButton btnLogout = findViewById(R.id.logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Home.this, VisitorLogout.class);
                startActivity(in);
            }
        });
    }
//    public void toVisitor (View view){
//        Intent intent = new Intent (this, MainVisitor.class);
//        startActivity(intent);
//    }

    public void toEmployee (View view){
        Intent intent = new Intent (this, SpeechEmployee.class);
        startActivity(intent);
    }

}
