package com.example.ex6webservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Page3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);
        Button btnchercher = (Button) findViewById(R.id.btnchercher);
        EditText txtnum = (EditText) findViewById(R.id.txtnum);
        EditText txtcode = (EditText) findViewById(R.id.txtcode);
        // Attribuer un écouteur d'évènement au bouton btnajouter
        btnchercher.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userInput = txtnum.getText().toString().trim();
                String userInput2 = txtcode.getText().toString().trim();
                if(!userInput.equals("") && userInput.matches("\\d+") && !userInput2.equals("") && userInput2.matches("\\d+"))
                {
                    // Code à réaliser sur clic du bouton btnajouter
                    String monurl = "https://adrien-fevre.fr/Balladins/administration/webservice/supprimer.php?txtnum=" + txtnum.getText().toString() + "&txtcode=" + txtcode.getText().toString();
                    Page3.ConnectionServeurLycees cnnSrvLyc = new Page3.ConnectionServeurLycees(monurl);
                    cnnSrvLyc.execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Ce n'est pas un nombre".toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private class ConnectionServeurLycees extends AsyncTask<Void, Void, String> {
        private String strurl;
        protected ConnectionServeurLycees(String uneurl)
        {
            super();
            strurl = uneurl;
        }
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),"Réponse reçue", Toast.LENGTH_LONG).show();
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
}