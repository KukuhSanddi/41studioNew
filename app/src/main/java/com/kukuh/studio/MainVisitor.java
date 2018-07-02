package com.kukuh.studio;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kukuh.studio.Visitor;
import com.kukuh.studio.Database;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainVisitor extends AppCompatActivity {
//    Spinner spinner = (Spinner) findViewById(R.id.dropdown);
//    ArrayAdapter<CharSequence> adapter = ArrayAdapter
//            .createFromResource(this,R.array.item_dropdown,
//                    android.R.layout.simple_spinner_item);

    // Specify the layout to use when the list of choices appears

//    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    // Apply the adapter to the spinner
//    spinner.setAdapter(adapter);


//    -----------------------------------------------------------

    private EditText inputName, inputEmail, inputNo, inputKep;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutNo, inputLayoutKep;

    Visitor vis;
    Database database = new Database();
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_visitor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        date = dateFormat.format(calendar.getTime());
        SimpleDateFormat jamFormat = new SimpleDateFormat("HH:mm:ss");
        final String jamCheckin = jamFormat.format(calendar.getTime());


        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputNo = findViewById(R.id.input_phone);
        inputKep = findViewById(R.id.input_kep);

        inputLayoutName = findViewById(R.id.input_layout_name);
        inputLayoutEmail = findViewById(R.id.input_layout_email);
        inputLayoutNo = findViewById(R.id.input_layout_phone);
        inputLayoutKep = findViewById(R.id.input_layout_keperluan);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputNo.addTextChangedListener(new MyTextWatcher(inputNo));
        inputKep.addTextChangedListener(new MyTextWatcher(inputKep));

        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String namaVis = inputName.getText().toString();
                final String emailVis = inputEmail.getText().toString();
                final String noVis = inputNo.getText().toString();
                final String kepVis = inputKep.getText().toString();

                submitForm();
                if ((validateName())&& (validateEmail())
                        && (validatePhone()) && (validateKep())){
                    vis = new Visitor(namaVis,emailVis,noVis,jamCheckin,null,kepVis);
                    database.updateDatabase(vis);
//                    sendEmail();
                    finish();
                    Intent intent = new Intent(MainVisitor.this, Home.class);
                    startActivity(intent);
                }

            }
        });

    }


    /**
     * new session
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(MainVisitor.this,MainVisitor.class);
        startActivity(i);
        finish();
    }

    /**
     * Validating Form
     */

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePhone()) {
            return;
        }

        if (!validateKep()){
            return;
        }

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * Name Validation
     */
    private boolean validateName(){
        if (inputName.getText().toString().trim().isEmpty()){
            inputLayoutName.setError(getString(R.string.err_msg_form));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    /**
     *
     * Email Validation
     */
    private boolean validateEmail(){
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty()||!isValidEmail(email)){
            inputLayoutEmail.setError((getString(R.string.err_msg_email)));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    /**
     *
     * Phone Validation
     */
    private boolean validatePhone(){
        String numb = inputNo.getText().toString().trim();

        if (numb.isEmpty()||!isValidPhone(numb)){
            inputLayoutNo.setError((getString(R.string.err_msg_phone)));
            requestFocus(inputNo);
            return false;
        } else {
            inputLayoutNo.setErrorEnabled(false);
        }
        return true;
    }

    /**
     *
     * Validatoon multiple row
     */

    private boolean validateKep(){
        if (inputKep.getText().toString().trim().isEmpty()){
            inputLayoutKep.setError(getString(R.string.err_msg_form));
            requestFocus(inputKep);
            return false;
        } else {
            inputLayoutKep.setErrorEnabled(false);
        }
        return true;
    }

    /**
     *
     * Email format
     */

    private static boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     *
     * Phone Format
     */

    private static boolean isValidPhone(String phone){
        return !TextUtils.isEmpty(phone) && android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private void requestFocus(View view){
        if (view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view){
            this.view= view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
        }

        public void afterTextChanged(Editable editable){
            switch (view.getId()){
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_layout_phone:
                    validatePhone();
                    break;
                case R.id.input_layout_keperluan:
                    validateKep();
                    break;
            }
        }
    }

    //Sending Email
    private void sendEmail() {
        final String namaVis = inputName.getText().toString();
        final String kepVis = inputKep.getText().toString();

        //Getting content for email
        String email = "adli.rahman23@gmail.com";
        String subject = "41Studio Visitor";
        String message = namaVis+" sedang menunggu dibawah dan ingin bertemu dengan anda, dengan keperluan "+kepVis+".";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }
}
