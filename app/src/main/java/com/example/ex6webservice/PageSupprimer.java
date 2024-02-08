package com.example.ex6webservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PageSupprimer extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_supprimer, container, false);
        Button btnchercher = view.findViewById(R.id.btnsupprimer);
        EditText txtnum = view.findViewById(R.id.txtnum);
        EditText txtcode = view.findViewById(R.id.txtcode);
        // Attribuer un écouteur d'évènement au bouton btnajouter
        btnchercher.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userInput = txtnum.getText().toString().trim();
                String userInput2 = txtcode.getText().toString().trim();
                if (!userInput.equals("") && userInput.matches("\\d+") && !userInput2.equals("") && userInput2.matches("\\d+")) {
                    // Code à réaliser sur clic du bouton btnajouter
                    String monurl = "https://adrien-fevre.fr/Balladins/administration/webservice/supprimer.php?txtnum=" + txtnum.getText().toString() + "&txtcode=" + txtcode.getText().toString();
                    PageSupprimer.ConnectionServeurLycees cnnSrvLyc = new PageSupprimer.ConnectionServeurLycees(monurl);
                    cnnSrvLyc.execute();
                    txtnum.setText(null);
                    txtcode.setText(null);
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private class ConnectionServeurLycees extends AsyncTask<Void, Void, String> {
        private final String strurl;

        protected ConnectionServeurLycees(String uneurl) {
            super();
            strurl = uneurl;
        }
        @Override
        protected void onPostExecute(String s) {
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
            TextView txtError = getView().findViewById(R.id.label);
            txtError.setVisibility(View.VISIBLE);
            if (unechainejson.equals(""))
            {
                txtError.setText("Il n'y a pas de réservation !");
            }
            else {
                txtError.setText("La réservation a été annulée !");
            }
    }
}