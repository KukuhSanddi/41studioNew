package com.kukuh.studio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class VisitorLogout extends AppCompatActivity {

    final Context context = this;
    Button btnLogout;
    Database dBase = new Database();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    EditText email;
    TextInputLayout inputLayoutEmail;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_logout);

        getWindow().getAttributes().windowAnimations = R.style.Fade;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputLayoutEmail = findViewById(R.id.input_layout_email_log);
        txt = findViewById(R.id.text_email);
        btnLogout = findViewById(R.id.btn_logout);
        email = findViewById(R.id.email_log);
        ImageView logo = findViewById(R.id.logo_out);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailVis = email.getText().toString();
                if (!validateEmail()) {
                    return;
                }
                final String[] namaVis = new String[1];
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                final String date = dateFormat.format(calendar.getTime());
                final DatabaseReference ref = database.getReference("visitors").child(date);
                ref.orderByChild("email").equalTo(emailVis).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Visitor vis = data.getValue(Visitor.class);
                                txt.setText(vis.getNama());
                                namaVis[0] = vis.getNama();
                            }
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
                                            dBase.checkoutVis(emailVis);

                                            box2.setTitle("Anda Berhasil Keluar");
                                            box2
                                                    .setCancelable(true)
                                                    .setMessage("Terimakasih "+ namaVis[0])
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent intent = new Intent(VisitorLogout.this, Home.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);                                            }
                                                    });
                                            AlertDialog alertDia = box2.create();

                                            alertDia.show();
                                        }
                                    });
                            AlertDialog alertDialog = dialogBox.create();

                            alertDialog.show();
                        }
                        else {
                            Toast.makeText(VisitorLogout.this,"Email anda tidak ada",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(getApplicationContext(), Home.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();

    }

    private boolean validateEmail(){
        String inputEmail = email.getText().toString().trim();

        if (inputEmail.isEmpty()||!isValidEmail(inputEmail)){
            email.setError(getString(R.string.err_msg_email));
            inputLayoutEmail.setError(" ");
            requestFocus(email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view){
        if (view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    /**
     *
     * Email format
     */

    private boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
