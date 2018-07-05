package com.kukuh.studio;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kukuh.studio.Visitor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Database {

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    /**
     * Visitor Section
     */

    //Visitor Checkin
    public void checkinVis(Visitor vis){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String date = dateFormat.format(calendar.getTime());

        DatabaseReference myRef = database.getReference("visitors/"+date);

        String email = vis.getEmail().toString();
        myRef.push().setValue(vis);
    }

    //Visitor Checkout
    public void checkoutVis(final String email){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        final String date = dateFormat.format(calendar.getTime());
        SimpleDateFormat jamFormat = new SimpleDateFormat("HH:mm:ss");
        final String jamCheckout = jamFormat.format(calendar.getTime());

        final DatabaseReference ref = database.getReference("visitors").child(date);
        ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        String key = data.getKey();
                        ref.child(key).child("checkout").setValue(jamCheckout);
                    }
                }else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getNama(String email){
        final String[] nama = new String[1];
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        final String date = dateFormat.format(calendar.getTime());
        final DatabaseReference ref = database.getReference("visitors").child(date);

        ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String key = dataSnapshot.getKey();
                    nama[0] = dataSnapshot.child("visitors").child(key).child("nama").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return nama[0];
    }

    /**
     * Employee Section
     */

    //Add Employee
    public void addEmployee(Employee emp){
        DatabaseReference dRef = database.getReference("employees").child("dataKaryawan");
        dRef.push().setValue(emp);
    }

    public String[] getNamaEmployee(){
        final ArrayList<Employee> listNama = new ArrayList<>();
        final String[] namaArr;
        final DatabaseReference dRef = database.getReference("employees").child("dataKaryawan");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Employee emp = data.getValue(Employee.class);
                    listNama.add(emp);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        namaArr = new String[listNama.size()];
        for (int i=0; i<listNama.size();i++){
            namaArr[i]=listNama.get(i).getNama();
            Log.e("adloy ",namaArr[i].toString());
        }

        return namaArr;
    }

    //Checkin Employee
    public void checkinEmp(Employee emp){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        final String date = dateFormat.format(calendar.getTime());

        DatabaseReference dRef = database.getReference("employees").child("absensi").child(date);
        dRef.push().setValue(emp);
    }
}
