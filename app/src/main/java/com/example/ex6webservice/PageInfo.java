package com.example.ex6webservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PageInfo extends Fragment {

    private int indiceImageCourante = 0;
    private ArrayList<String> listeDesChemins;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String monurl2 = "https://adrien-fevre.fr/Balladins/administration/webservice/information.php?txtnum=" + DataStorage.getInstance().getMaDonnee();
        PageInfo.ConnectionServeurLycees cnnSrvLyc = new PageInfo.ConnectionServeurLycees(monurl2);
        cnnSrvLyc.execute();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_info, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void afficherFluxJsonDansListView(String unechainejson) {
        try {
            TextView txtTitle = getView().findViewById(R.id.title);

            TextView description1 = getView().findViewById(R.id.description1);
            TextView description2 = getView().findViewById(R.id.description2);
            Button btn1 = getView().findViewById(R.id.button1);
            Button btn2 = getView().findViewById(R.id.button2);
            listeDesChemins = new ArrayList<>();
            JSONArray tblelements = new JSONArray(unechainejson);
            ImageView monImageView = getView().findViewById(R.id.imgHotel);
            for (int i = 0; i < tblelements.length(); i++) {
                JSONObject unelement = tblelements.getJSONObject(i);
                String cheminImage = "a" + unelement.getString("nomfichier");
                listeDesChemins.add(cheminImage);
                txtTitle.setText(unelement.getString("nom"));
                description1.setText("Adresse: " + unelement.getString("adr1"));
                description2.setText("Ville: " + unelement.getString("ville"));
                description2.setText("Téléphone: " + unelement.getString("tel"));
            }
            if (!listeDesChemins.isEmpty()) {
                afficherImage(listeDesChemins.get(indiceImageCourante), monImageView);
            }
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    afficherImagePrecedente(monImageView);
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    afficherImageSuivante(monImageView);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void afficherImagePrecedente(ImageView monImageView) {
        if (indiceImageCourante > 0) {
            indiceImageCourante--;
            String cheminImage = listeDesChemins.get(indiceImageCourante);
            afficherImage(cheminImage, monImageView);
            Log.d("BoutonPrecedent", "Image précédente affichée " + cheminImage);
        }
    }

    private void afficherImageSuivante(ImageView monImageView) {
        if (indiceImageCourante < listeDesChemins.size() - 1) {
            indiceImageCourante++;
            String cheminImage = listeDesChemins.get(indiceImageCourante);
            afficherImage(cheminImage, monImageView);
            Log.d("BoutonSuivant", "Image suivante affichée " + cheminImage);
        }
    }

    private void afficherImage(String cheminImage, ImageView monImageView) {
        int resID = getResources().getIdentifier(cheminImage.replace(".jpg", ""), "drawable", requireContext().getPackageName());
        monImageView.setImageResource(resID);
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