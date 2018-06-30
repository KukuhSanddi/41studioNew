package com.kukuh.studio;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kukuh.studio.Visitor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    Map<String, Visitor> visitors = new HashMap<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();



    public void updateDatabase(Visitor vis,String date){
        DatabaseReference myRef = database.getReference("visitors/"+date);
        String email = vis.getEmail().toString();
        visitors.put(vis.getNama(), new Visitor(vis.getNama(),email,vis.getPhone(),vis.getCheckin(),null,vis.getKeperluan()));
        myRef.setValue(visitors);
    }



}
