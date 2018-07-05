package com.kukuh.studio;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddEmployee extends AppCompatActivity {
    private EditText inputName, inputEmail, inputNo, inputPosisi, inputAlamat, inputStatus;
    private TextInputLayout layoutEmail, layoutNo,layoutAddress,layoutStatus;
    private String nama,email,posisi,phone,alamat,status;
    private String urlFoto = "";
    Database database = new Database();
    Employee emp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        Button saveBtn = findViewById(R.id.save);



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

                    emp = new Employee(nama,"",email,phone,alamat,"");
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

//    private void addArray(){
//        String[] myArr = getResources().getStringArray(R.array.employee);
//        final int size = myArr.length;
//        final int newSize = size +1;
//
//        String[] tempArray = new String[newSize];
//        for (int i=0; i<size; i++){
//            tempArray[i] = myArr[i];
//        }
//        tempArray[newSize-1] = inputName.getText().toString();
//        myArr = tempArray;
//
//    }

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




