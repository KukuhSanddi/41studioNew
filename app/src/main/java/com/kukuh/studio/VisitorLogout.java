package com.kukuh.studio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class VisitorLogout extends AppCompatActivity {
    //Call class Context of this page
    final Context context = this;
    Button btnLogout;
    //initiate database
    Database dBase = new Database();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    EditText email;
    TextInputLayout inputLayoutEmail;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Call layout on this page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_logout);

        //Transition
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        //Set color of toast
        Toasty.Config.getInstance()
                .setErrorColor(Color.RED)
                .setSuccessColor(Color.GREEN);

        //Initiate variable from layout
        inputLayoutEmail = findViewById(R.id.input_layout_email_log);
        txt = findViewById(R.id.text_email);
        btnLogout = findViewById(R.id.btn_logout);
        email = findViewById(R.id.email_log);
        ImageView logo = findViewById(R.id.logo_out);


        final String[] namaVis = new String[1];
        //Set listener button on logo
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Set listener edittext email form
        ((EditText)findViewById(R.id.email_log)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                final String emailVis = email.getText().toString();
                if(i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent !=null && keyEvent.getAction() == KeyEvent.ACTION_DOWN
                                && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    if(keyEvent == null || !keyEvent.isShiftPressed()){
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
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

                                }
                                else {
                                    Toasty.error(VisitorLogout.this,"Email anda tidak ada",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
                        return true;
                    }
                }
                return false;
            }
        });

        //Set btnLogout listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateEmail();

                if (email.getText().toString().equals("")){
                    AlertDialog.Builder dialogBox = new AlertDialog.Builder(context);

                    dialogBox.setTitle("Anda belum melakukan checkin")
                            .setCancelable(true)
                            .setIcon(R.drawable.ic_close_24dp)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    AlertDialog alertDialog = dialogBox.create();
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                    alertDialog.show();

                } else {
                    AlertDialog.Builder dialogBox = new AlertDialog.Builder(context);

                    dialogBox.setTitle("Confirm logout?");

                    dialogBox.setMessage("");
                    dialogBox.setCancelable(false);
                    dialogBox.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // kembali ke halaman visitor logout
                        }
                    });
                    dialogBox.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final AlertDialog.Builder box2 = new AlertDialog.Builder(context);
                            final String emailVis = email.getText().toString();
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
                            final String date = dateFormat.format(calendar.getTime());
                            SimpleDateFormat jamFormat = new SimpleDateFormat("HH:mm:ss");
                            final String jamCheckout = jamFormat.format(calendar.getTime());

                            final DatabaseReference ref = database.getReference("visitors").child(date);
                            ref.orderByChild("email").equalTo(emailVis).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        for(DataSnapshot data :dataSnapshot.getChildren()){
                                            if (data.hasChild("checkout")){
//                                                Toast.makeText(VisitorLogout.this,"Anda sudah logout",Toast.LENGTH_SHORT).show();
                                                box2.setTitle("Anda sudah logout");
                                                box2
                                                        .setCancelable(true)
                                                        .setIcon(R.drawable.ic_close_24dp)
                                                        .setMessage(" Silahkan check-in terlebih dahulu")
                                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                                                            }
                                                        });
                                                AlertDialog alertDia = box2.create();
                                                alertDia.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

                                                alertDia.show();
                                                final Button negativeButton = alertDia.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
                                                LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
                                                negativeButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                                negativeButton.setLayoutParams(negativeButtonLL);
                                            } else if(!data.hasChild("checkout")){
                                                dBase.checkoutVis(emailVis);
//                                                Toast.makeText(VisitorLogout.this,"Logout berhasil",Toast.LENGTH_SHORT).show();
                                                box2.setTitle("Anda Berhasil Keluar");
                                                box2
                                                        .setCancelable(true)
                                                        .setMessage("Terimakasih ")
                                                        .setIcon(R.drawable.ic_check_24dp)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                Toasty.success(VisitorLogout.this,"Anda berhasil checkout",Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(VisitorLogout.this, Home.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent);                                            }
                                                        });
                                                AlertDialog alertDia = box2.create();
                                                alertDia.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

                                                alertDia.show();

                                                final Button negativeButton = alertDia.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
                                                LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
                                                negativeButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                                negativeButton.setLayoutParams(negativeButtonLL);
                                            }
                                        }
                                    }else{
                                        Toasty.error(VisitorLogout.this,"Email anda tidak ada",Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    AlertDialog alertDialog = dialogBox.create();
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                    alertDialog.show();
                    final Button negativeButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
                    final Button positiveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
                    layoutParams.weight = 10;
                    negativeButton.setLayoutParams(layoutParams);
                    positiveButton.setLayoutParams(layoutParams);
                }
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

//    <!--Code By Kukuh Sanddi Razaq & M. Adli Rachman-->