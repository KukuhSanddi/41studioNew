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

    /**
     * Employee Section
     */

    //Add Employee
    public void addEmployee(Employee emp){
        DatabaseReference dRef = database.getReference("employees").child("dataKaryawan");
        dRef.push().setValue(emp);
    }

    //Edit data Employee
    public void editEmployee(Employee emp,String email){
        final Employee empEdit = emp;
        final DatabaseReference dRef = database.getReference("employees").child("dataKaryawan");
        dRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        String key = data.getKey();
                        dRef.child(key).child("nama").setValue(empEdit.getNama());
                        dRef.child(key).child("posisi").setValue(empEdit.getPosisi());
                        dRef.child(key).child("email").setValue(empEdit.getEmail());
                        dRef.child(key).child("phone").setValue(empEdit.getPhone());
                        dRef.child(key).child("alamat").setValue(empEdit.getAlamat());
                        dRef.child(key).child("status").setValue(empEdit.getStatus());
                    }
                }else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Delete Employee
    public void deleteEmployee(String email){
        final DatabaseReference dRef = database.getReference("employees").child("dataKaryawan");
        dRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        String key = data.getKey();
                        dRef.child(key).removeValue();
                    }

                }else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Search Employee
    public void searchEmployee(String nama){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat jamFormat = new SimpleDateFormat("HH:mm:ss");
        final String jamCheckin = jamFormat.format(calendar.getTime());

        final DatabaseReference dRef = database.getReference("employees").child("dataKaryawan");
        dRef.orderByChild("nama").equalTo(nama).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        String key = data.getKey();
                        Employee emp = new Employee(dRef.child(key).child("nama").toString(),dRef.child(key).child("email").toString(),jamCheckin,"");
                        checkinEmp(emp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
