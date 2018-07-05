package com.kukuh.studio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEmployee extends AppCompatActivity {
    private EditText inputName, inputEmail, inputNo, inputPosisi, inputAlamat, inputStatus;
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

                nama = inputName.getText().toString();
                email = inputEmail.getText().toString();
                phone = inputNo.getText().toString();
//                posisi = inputPosisi.getText().toString();
                alamat = inputAlamat.getText().toString();
                status = inputStatus.getText().toString();

                emp = new Employee(nama,"",email,phone,alamat,"");
                database.addEmployee(emp);

                Toast.makeText(AddEmployee.this,"Saved",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(AddEmployee.this, MainEmployee.class);
                startActivity(i);
            }
        });
    }


}
