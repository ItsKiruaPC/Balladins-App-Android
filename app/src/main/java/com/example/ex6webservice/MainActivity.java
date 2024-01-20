package com.example.ex6webservice;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnquitter = (Button) findViewById(R.id.btnquitter);
        btnquitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        Button btnpage1 = (Button) findViewById(R.id.btnpage1);
        btnpage1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Page1.class);
                startActivity(myIntent);
            }
        });
        Button btnpage2 = (Button) findViewById(R.id.btnpage2);
        btnpage2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Page2.class);
                startActivity(myIntent);
            }
        });
        Button btnpage3 = (Button) findViewById(R.id.btnpage3);
        btnpage3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Page3.class);
                startActivity(myIntent);
            }
        });
    }
}