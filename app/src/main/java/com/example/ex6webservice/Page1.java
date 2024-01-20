package com.example.ex6webservice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.snackbar.Snackbar;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class Page1 extends AppCompatActivity {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        String monurl2 = "https://adrien-fevre.fr/Balladins/administration/webservice/hotel.php";
        ConnectionServeurLycees cnnSrvLyc = new ConnectionServeurLycees(monurl2);
        cnnSrvLyc.execute();
    }
    private class ConnectionServeurLycees extends AsyncTask<Void, Void, String> {
        private String strurl;
        //Constructeur
        protected ConnectionServeurLycees(String uneurl){
            super();
            strurl = uneurl;
        }
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(),"Réponse reçue", Toast.LENGTH_SHORT).show();
            afficherFluxJsonDansListView(s);
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(strurl);
                HttpURLConnection cnn = (HttpURLConnection) url.openConnection();
                StringBuilder leslignes = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cnn.getInputStream()));
                String uneligne;
                uneligne = bufferedReader.readLine();
                while (uneligne != null) {
                    leslignes.append(uneligne + "\n");
                    uneligne = bufferedReader.readLine();
                }
                return leslignes.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    private void afficherFluxJsonDansListView(String unechainejson) {
        try {
            ListView monlistview = (ListView) findViewById(R.id.lstlycee);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            JSONArray tblelements = new JSONArray(unechainejson);
            for (int i = 0; i < tblelements.length(); i++) {
                JSONObject unelement = tblelements.getJSONObject(i);
                adapter.add(unelement.getString("nom"));
                adapter2.add(unelement.getString("nom")+" - "+unelement.getString("adr1")+" - "+unelement.getString("tel"));
            }
            monlistview.setAdapter(adapter);
            monlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    //Lors de l'évènement, position contient l'index de la ligne cliquée
                    String x = adapter2.getItem(position);
                    //Affichage temporaire dans une petite fenêtre
                    Toast.makeText(getApplicationContext(),x, Toast.LENGTH_SHORT).show();
                    JSONObject unelemente = null;
                    try {
                        unelemente = tblelements.getJSONObject(position);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    }