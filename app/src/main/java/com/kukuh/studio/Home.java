package com.kukuh.studio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this,myService.class));
    }
    public void toVisitor (View view){
        Intent intent = new Intent (this, MainVisitor.class);
        startActivity(intent);
    }

    public void toEmployee (View view){
        Intent intent = new Intent (this, MainEmployee.class);
        startActivity(intent);
    }
}
