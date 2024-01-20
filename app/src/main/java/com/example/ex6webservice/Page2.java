package com.example.ex6webservice;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class Page2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
        Button btnchercher = (Button) findViewById(R.id.btnchercher);
        // Attribuer un écouteur d'évènement au bouton btnajouter
        btnchercher.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Code à réaliser sur clic du bouton btnajouter
                EditText txtnum = (EditText) findViewById(R.id.txtnum);
                EditText txtcode = (EditText) findViewById(R.id.txtcode);
                String monurl = "https://adrien-fevre.fr/Balladins/administration/webservice/reservation.php?txtnum=" + txtnum.getText().toString() + "&txtcode=" + txtcode.getText().toString();
                ConnectionServeurLycees cnnSrvLyc = new ConnectionServeurLycees(monurl);
                cnnSrvLyc.execute();

            }
        });

    }
    private class ConnectionServeurLycees extends AsyncTask<Void, Void, String> {
        private String strurl;
        //Constructeur
        protected ConnectionServeurLycees(String uneurl)
        {
            super();
            strurl = uneurl;
        }
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),"Réponse reçue", Toast.LENGTH_LONG).show();
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
            monlistview.setAdapter(adapter2);
            monlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    //Lors de l'évènement, position contient l'index de la ligne cliquée
                    String x = adapter2.getItem(position) ;
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
