package com.kukuh.studio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
    final Context context = this;
    Database database = new Database();

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
        ImageView btnDelete = findViewById(R.id.delete_profile);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog
                        .setTitle("User ini akan dihapus?")
                        .setMessage("")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder aler = new AlertDialog.Builder(context);
                                database.deleteEmployee(emp.getEmail());
                                aler.setTitle("Berhasil dihapus")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(getApplicationContext(), MainEmployee.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        });
                                aler.show();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                dialog.show();
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                -----------------Pindah Page ------------------
                Employee empEdit = emp;


                Intent i = new Intent(getApplicationContext(), EditEmployee.class);

                i.putExtra("parcelEmp",empEdit);
                startActivity(i);


            }
        });



    }
}
