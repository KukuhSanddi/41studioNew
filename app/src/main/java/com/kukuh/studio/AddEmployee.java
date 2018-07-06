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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddEmployee extends AppCompatActivity {
    private EditText inputName, inputEmail, inputNo, inputPosisi, inputAlamat, inputStatus;
    private TextInputLayout layoutEmail, layoutNo,layoutAddress,layoutStatus;
    private String nama,email,phone,alamat,status;
    private String urlFoto = "";
    Database database = new Database();
    Employee emp;
    ImageView imgBtn;
    final Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        Button saveBtn = findViewById(R.id.save);

        imgBtn = findViewById(R.id.user_profile_photo);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Anda Menekan Image Button");
                alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                alert.show();
            }
        });

        Spinner posisi = findViewById(R.id.spin_posisi);
        Spinner stats = findViewById(R.id.spin_status);

        String[] listArr = getResources().getStringArray(R.array.item_status);

        String[] newArr = new String[listArr.length+1];
        newArr [0] = "Pilih Status Karyawan";
        for (int i = 1; i<newArr.length; i++){
            newArr[i] = listArr[i-1];
        }

        String[] listPos = getResources().getStringArray(R.array.item_posisi);

        String[] posArr = new String[listPos.length+1];
        posArr [0] = "Pilih Posisi Karyawan";
        for (int i = 1; i<posArr.length; i++){
            posArr[i] = listPos[i-1];
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddEmployee.this, R.layout.spinner_item, newArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stats.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(AddEmployee.this, R.layout.spinner_item, posArr);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        posisi.setAdapter(adapter2);


        findViewById(R.id.scroll_employee).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                return true;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputName = findViewById(R.id.input_name_employee);
//                inputPosisi = findViewById(R.id.user_profile_short_bio);
                inputEmail = findViewById(R.id.input_email);
                inputNo = findViewById(R.id.input_phone);
                inputAlamat = findViewById(R.id.input_address);
                inputStatus = findViewById(R.id.input_status);

                layoutEmail = findViewById(R.id.layout_email);
                layoutNo = findViewById(R.id.layout_phone);
                layoutAddress = findViewById(R.id.layout_address);
                layoutStatus = findViewById(R.id.layout_status);

                nama = inputName.getText().toString();
                email = inputEmail.getText().toString();
                phone = inputNo.getText().toString();
//                posisi = inputPosisi.getText().toString();
                alamat = inputAlamat.getText().toString();
                status = inputStatus.getText().toString();

                submitEmployee();
                if ((validateName())&& (validateEmail())
                        && (validatePhone()) && (validateAddress()) && (validateStatus())){

                    emp = new Employee(nama,"",email,phone,alamat,"",status);
                    database.addEmployee(emp);

//                  addArray();

                    Toast.makeText(AddEmployee.this,"Saved",Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(AddEmployee.this, MainEmployee.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }


            }
        });
    }

    private void submitEmployee(){
        if(!validateName()){
            return;
        }

        if (!validateEmail()){
            return;
        }

        if (!validatePhone()){
            return;
        }

        if (!validateAddress()){
            return;
        }

        if (!validateStatus()){

        }
    }

    private boolean validateName(){
        if (inputName.getText().toString().trim().isEmpty()){
            inputName.setError(getString(R.string.err_msg_form));
            requestFocus(inputName);
            return false;
        }
        return true;
    }

    private boolean validateEmail(){
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty()||!isValidEmail(email)){
            inputEmail.setError(getString(R.string.err_msg_email));
            layoutEmail.setError(" ");
            requestFocus(inputEmail);
            return false;
        } else {
            layoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhone(){
        String numb = inputNo.getText().toString().trim();

        if (numb.isEmpty()||!isValidPhone(numb)){
            inputNo.setError((getString(R.string.err_msg_phone)));
            layoutNo.setError(" ");

            requestFocus(inputNo);
            return false;
        } else {
            layoutNo.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateAddress(){
        if (inputAlamat.getText().toString().trim().isEmpty()){
            inputAlamat.setError(getString(R.string.err_msg_form));
            layoutAddress.setError(" ");
            requestFocus(inputAlamat);
            return false;
        } else {
            layoutAddress.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateStatus(){
        if (inputStatus.getText().toString().trim().isEmpty()){
            inputStatus.setError(getString(R.string.err_msg_form));
            layoutStatus.setError(" ");
            requestFocus(inputStatus);
            return false;
        } else {
            layoutAddress.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isValidPhone(String phone){
        return !TextUtils.isEmpty(phone) && android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private void requestFocus(View view){
        if (view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        }
    }


}




