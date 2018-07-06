package com.kukuh.studio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditEmployee extends AppCompatActivity {
    Button save;
    Employee empEdit;
    Database dbase = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        final EditText EName = findViewById(R.id.input_name_employee);
        final EditText eEmail = findViewById(R.id.input_email);
        final EditText ePhone = findViewById(R.id.input_phone);
        final EditText eAddress = findViewById(R.id.input_address);
        final EditText eStatus = findViewById(R.id.input_status);

        Intent intent = getIntent();
        final Employee emp = intent.getParcelableExtra("parcelEmp");

        EName.setText(emp.getNama());
        eEmail.setText(emp.getEmail());
        ePhone.setText(emp.getPhone());
        eAddress.setText(emp.getAlamat());
        eStatus.setText(emp.getStatus());
        Spinner posisi = findViewById(R.id.spin_posisi);

        String[] listPos = getResources().getStringArray(R.array.item_posisi);

        String[] posArr = new String[listPos.length+1];
        posArr [0] = "Pilih Posisi Karyawan";
        for (int i = 1; i<posArr.length; i++){
            posArr[i] = listPos[i-1];
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(EditEmployee.this, R.layout.spinner_item, posArr);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        posisi.setAdapter(adapter2);

        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                empEdit = new Employee(EName.getText().toString(),"",eEmail.getText().toString(),ePhone.getText().toString(),eAddress.getText().toString(),"",eStatus.getText().toString());
                dbase.editEmployee(empEdit,emp.getEmail());

                Toast.makeText(EditEmployee.this,"Saved",Toast.LENGTH_SHORT).show();


                Intent i = new Intent(getApplicationContext(), EmployeeProfile.class);
                i.putExtra("parcelObject",empEdit);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(i);
            }
        });
    }
}
