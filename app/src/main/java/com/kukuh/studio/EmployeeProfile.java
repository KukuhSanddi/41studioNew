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

    Button enable, disable;
    EditText employName, employEmail, employPhone;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView txtName = findViewById(R.id.user_profile_name);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        txtName.setText(name);

        ImageView btnEdit = findViewById(R.id.edit_profile);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameE = txtName.getText().toString();
                Intent i = new Intent(getApplicationContext(), EditEmployee.class);
                i.putExtra("name", nameE);
                startActivity(i);
            }
        });






    }
}
