package com.kukuh.studio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditEmployee extends AppCompatActivity {
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

//        final EditText EName = findViewById(R.id.input_name_employee);
//        Intent intent = getIntent();
//        final String nameE = intent.getStringExtra("nameE");
//        EName.setText(nameE);

        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String name = nameE;

                Toast.makeText(EditEmployee.this,"Saved",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), EmployeeProfile.class);
//                i.putExtra("nameE", name);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(i);
            }
        });
    }
}
