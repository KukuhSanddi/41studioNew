package com.kukuh.studio;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kukuh.studio.Visitor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Database {
    Map<String, Visitor> visitors = new HashMap<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    public void updateDatabase(Visitor vis){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String date = dateFormat.format(calendar.getTime());

        DatabaseReference myRef = database.getReference("visitors/"+date);

        String email = vis.getEmail().toString();
        visitors.put(vis.getNama(), new Visitor(vis.getNama(),email,vis.getPhone(),vis.getCheckin(),null,vis.getKeperluan()));
        myRef.push().setValue(visitors);
    }
}
