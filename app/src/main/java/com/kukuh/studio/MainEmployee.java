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
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MainEmployee extends AppCompatActivity {
    ListView list;


    String[] listArr;
    Database dbase = new Database();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    ArrayList<Employee> listNama;
//    = new ArrayList<>();

    Context context;
//    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employee);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getNamaEmployee();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddEmployee.class);
                startActivity(i);
            }
        });
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    public void getNamaEmployee(){
        final ArrayList<Employee> listNama = new ArrayList<>();
        final String[] namaArr;
        final DatabaseReference dRef = database.getReference("employees").child("dataKaryawan");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listNama.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Employee emp = data.getValue(Employee.class);
                    listNama.add(emp);
                    Log.e("adloy",emp.getNama());
                }
                listArr = new String[listNama.size()];
                for (int i=0; i<listNama.size();i++){
                    listArr[i]=listNama.get(i).getNama();
                    Log.e("ewe ",listArr[i].toString());
                }

                ListViewAdapter adapter = new ListViewAdapter(MainEmployee.this, listArr);

                list = findViewById(R.id.list);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(MainEmployee.this,"Anda Memilih " + listArr[i],Toast.LENGTH_SHORT).show();

                        String name = listArr[i];

                        Intent intent = new Intent(getApplicationContext(),EmployeeProfile.class);
                        intent.putExtra("name", name);
                        startActivity(intent);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        namaArr = new String[listNama.size()];
//        for (int i=0; i<listNama.size();i++){
//            namaArr[i]=listNama.get(i).getNama();
//            Log.e("adloy ",namaArr[i].toString());
//        }

    }


}




