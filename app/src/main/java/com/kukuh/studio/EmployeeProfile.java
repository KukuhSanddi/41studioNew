package com.kukuh.studio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmployeeProfile extends AppCompatActivity {

    Button enable, disable;
    EditText employName, employEmail, employPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enable = findViewById(R.id.btn_edit);
        disable = findViewById(R.id.btn_ok);
        employName = findViewById(R.id.employee_name);
        employEmail = findViewById(R.id.employee_email);
        employPhone = findViewById(R.id.employee_phone);

        employName.setEnabled(false);
        employEmail.setEnabled(false);
        employPhone.setEnabled(false);


        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employName.setEnabled(true);
                employEmail.setEnabled(true);
                employPhone.setEnabled(true);
                Toast.makeText(EmployeeProfile.this, "EditText Enable", Toast.LENGTH_SHORT).show();
            }
        });

        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employName.setEnabled(false);
                employEmail.setEnabled(false);
                employPhone.setEnabled(false);
                Toast.makeText(EmployeeProfile.this, "EditText Disable", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
