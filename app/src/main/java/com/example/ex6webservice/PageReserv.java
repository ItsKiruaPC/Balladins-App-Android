package com.example.ex6webservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PageReserv extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_reserv, container, false);
        EditText txtnum = view.findViewById(R.id.txtnum);
        EditText txtcode = view.findViewById(R.id.txtcode);
        Button btnchercher = view.findViewById(R.id.btnchercher);
        // Attribuer un écouteur d'évènement au bouton btnajouter
        btnchercher.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code à réaliser sur clic du bouton btnajouter

                String monurl = "https://adrien-fevre.fr/Balladins/administration/webservice/reservation.php?txtnum=" + txtnum.getText().toString() + "&txtcode=" + txtcode.getText().toString();
                PageReserv.ConnectionServeurLycees cnnSrvLyc = new PageReserv.ConnectionServeurLycees(monurl);
                cnnSrvLyc.execute();

            }

        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void afficherFluxJsonDansListView(String unechainejson) {
        try {
            ListView monlistview = getView().findViewById(R.id.lstlycee);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
            JSONArray tblelements = new JSONArray(unechainejson);
            for (int i = 0; i < tblelements.length(); i++) {
                JSONObject unelement = tblelements.getJSONObject(i);
                adapter.add(unelement.getString("nom"));
                adapter2.add(unelement.getString("nom") + " - " + unelement.getString("adr1") + " - " + unelement.getString("tel"));
            }
            monlistview.setAdapter(adapter2);
            monlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    //Lors de l'évènement, position contient l'index de la ligne cliquée
                    String x = adapter2.getItem(position);
                    //Affichage temporaire dans une petite fenêtre
                    Toast.makeText(getContext(), x, Toast.LENGTH_SHORT).show();
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

    private class ConnectionServeurLycees extends AsyncTask<Void, Void, String> {
        private final String strurl;

        //Constructeur
        protected ConnectionServeurLycees(String uneurl) {
            super();
            strurl = uneurl;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getContext(), "Réponse reçue", Toast.LENGTH_LONG).show();
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
}