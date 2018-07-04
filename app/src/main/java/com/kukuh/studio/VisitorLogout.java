package com.kukuh.studio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VisitorLogout extends AppCompatActivity {

    final Context context = this;
    Button btnLogout;
    Database database = new Database();
    EditText email;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_logout);

        txt = findViewById(R.id.text_email);
        btnLogout = findViewById(R.id.btn_logout);
        email = findViewById(R.id.email_log);

        final String emailVis = email.getText().toString();

        txt.setText(database.getNama(emailVis));


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBox = new AlertDialog.Builder(context);



                dialogBox.setTitle("Confirm logout?");

                dialogBox
                        .setMessage("")
                        .setCancelable(false)
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // kembali ke halaman visitor logout
                            }
                        })
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder box2 = new AlertDialog.Builder(context);
                                final String emailVis = email.getText().toString();
                                database.checkoutVis(emailVis);

                                box2.setTitle("Anda Berhasil Keluar");
                                box2
                                        .setCancelable(true)
                                        .setMessage("")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //
                                            }
                                        });
                                AlertDialog alertDia = box2.create();

                                alertDia.show();
                            }
                        });
                AlertDialog alertDialog = dialogBox.create();

                alertDialog.show();

                Intent intent = new Intent(VisitorLogout.this, Home.class);
                startActivity(intent);

            }
        });

    }
}
