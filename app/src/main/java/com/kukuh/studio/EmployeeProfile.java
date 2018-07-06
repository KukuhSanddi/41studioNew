package com.kukuh.studio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EmployeeProfile extends AppCompatActivity {

    Button save;
    EditText employName, employEmail, employPhone, employAddress, employStatus;
    Spinner spinner;
    Employee emp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        employName = findViewById(R.id.user_profile_name);
        employEmail = findViewById(R.id.user_email);
        employPhone = findViewById(R.id.user_phone);
        employAddress = findViewById(R.id.user_address);
        employStatus = findViewById(R.id.user_status);
        save = findViewById(R.id.save);

        employName.setEnabled(false);
        employEmail.setEnabled(false);
        employPhone.setEnabled(false);
        employAddress.setEnabled(false);
        employStatus.setEnabled(false);
        save.setVisibility(View.GONE);


        final TextView txtName = findViewById(R.id.user_profile_name);
        final TextView txtEmail = findViewById(R.id.user_email);
        final TextView txtPhone = findViewById(R.id.user_phone);
        final TextView txtAlamat = findViewById(R.id.user_address);
        final TextView txtStatus = findViewById(R.id.user_status);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final Employee emp = intent.getParcelableExtra("parcelObject");
        txtName.setText(emp.getNama());
        txtEmail.setText(emp.getEmail());
        txtPhone.setText(emp.getPhone());
        txtAlamat.setText(emp.getAlamat());
        txtStatus.setText(emp.getStatus());

        ImageView btnEdit = findViewById(R.id.edit_profile);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                -----------------Pindah Page ------------------
                Employee empEdit = emp;
                String nameE = emp.getNama();
                Intent i = new Intent(getApplicationContext(), EditEmployee.class);
                i.putExtra("name", nameE);
                i.putExtra("parcelEmp",empEdit);
                startActivity(i);


//              ------------------Tanpa Pindah Page-------------

//                employName.setEnabled(true);
//                employEmail.setEnabled(true);
//                employPhone.setEnabled(true);
//                employAddress.setEnabled(true);
//                employStatus.setEnabled(true);
//                save.setVisibility(View.VISIBLE);
            }
        });

//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                employName.setEnabled(false);
//                employEmail.setEnabled(false);
//                employPhone.setEnabled(false);
//                employAddress.setEnabled(false);
//                employStatus.setEnabled(false);
//                save.setVisibility(View.GONE);
//            }
//        });






    }
}
