package com.kukuh.studio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainEmployee extends AppCompatActivity {
    ListView list;


    String[] listArr;


    Context context;
//    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employee);

//        btn1 = findViewById(R.id.info_button);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Button"+ btn1.getText().toString(), Toast.LENGTH_LONG).show();
//            }
//        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.activity_list_view, R.id.label, listArr);
//
//        context = this;
//
//        ListView listView = findViewById(R.id.list);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            public void onItemClick(AdapterView<?> parent, View view, int positon, long id){
//                Toast.makeText(context,"Item clicked", Toast.LENGTH_LONG).show();
//
//            }
//        });

        listArr = getResources().getStringArray(R.array.employee);
        ListViewAdapter adapter = new ListViewAdapter(MainEmployee.this, listArr);

        list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainEmployee.this,"Anda Memilih" + listArr[i],Toast.LENGTH_SHORT).show();

                String name = listArr[i];

                Intent intent = new Intent(getApplicationContext(),EmployeeProfile.class);
                intent.putExtra("name", name);
                startActivity(intent);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddEmployee.class);
                startActivity(i);
            }
        });
    }

    //    public void info_Employee(View view){
//        Button btn = (Button) view;
//        Toast.makeText(this, "Button"+ btn.getText().toString(), Toast.LENGTH_SHORT).show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainEmployee.this);
//        builder.setTitle("EMPLOYEE");
//        builder.setCancelable(true);
//        builder.setMessage("Nama Karyawan");
//        builder.setPositiveButton("Profile", null);
//        builder.setNegativeButton("Cancel", null);
//        builder.create();
//
//        Intent intent = new Intent(this,EmployeeProfile.class);
//        startActivity(intent);
//
//
//    }


}




