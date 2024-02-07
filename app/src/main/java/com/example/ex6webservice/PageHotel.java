package com.example.ex6webservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PageHotel extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_hotel, container, false);
        String monurl2 = "https://adrien-fevre.fr/Balladins/administration/webservice/hotel.php";
        PageHotel.ConnectionServeurLycees cnnSrvLyc = new PageHotel.ConnectionServeurLycees(monurl2);
        cnnSrvLyc.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void afficherFluxJsonDansListView(String unechainejson) {
        try {
            ListView monlistview = requireView().findViewById(R.id.lstlycee);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
            JSONArray tblelements = new JSONArray(unechainejson);
            for (int i = 0; i < tblelements.length(); i++) {
                JSONObject unelement = tblelements.getJSONObject(i);
                adapter.add(unelement.getString("nom"));
                adapter2.add(unelement.getString("nohotel"));
            }
            monlistview.setAdapter(adapter);
            monlistview.setOnItemClickListener((parent, v, position, id) -> {

                //Lors de l'évènement, position contient l'index de la ligne cliquée
                DataStorage.getInstance().setMaDonnee(adapter2.getItem(position));
                //Affichage temporaire dans une petite fenêtre
                //Toast.makeText(getContext(),x, Toast.LENGTH_SHORT).show();
                try {
                    tblelements.getJSONObject(position);
                    replaceFragment(new PageInfo(), false);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout1, fragment);
        fragmentTransaction.commit();
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
            //Toast.makeText(getContext(), "Réponse reçue", Toast.LENGTH_SHORT).show();
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