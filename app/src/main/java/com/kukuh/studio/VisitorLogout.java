package com.kukuh.studio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VisitorLogout extends AppCompatActivity {

    final Context context = this;
    Button btnLogout;
    Database database = new Database();
    EditText email;
    TextInputLayout inputLayoutEmail;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_logout);

        inputLayoutEmail = findViewById(R.id.input_layout_email_log);
        txt = findViewById(R.id.text_email);
        btnLogout = findViewById(R.id.btn_logout);
        email = findViewById(R.id.email_log);

        final String emailVis = email.getText().toString();

        txt.setText(database.getNama(emailVis));



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateEmail()){
                    return;
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
                                database.checkoutVis(emailVis);

                                box2.setTitle("Anda Berhasil Keluar");
                                box2
                                        .setCancelable(true)
                                        .setMessage("")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(VisitorLogout.this, Home.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);                                            }
                                        });
                                AlertDialog alertDia = box2.create();

                                alertDia.show();
                            }
                        });
                AlertDialog alertDialog = dialogBox.create();

                alertDialog.show();
            }
        });

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

    private static boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
