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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        Button saveBtn = findViewById(R.id.save);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddEmployee.this,"Saved",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(AddEmployee.this, MainEmployee.class);
                startActivity(i);
            }
        });
    }
}
