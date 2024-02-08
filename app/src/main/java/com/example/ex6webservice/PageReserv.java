package com.example.ex6webservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PageReserv extends Fragment {

    private int indiceImageCourante = 0;
    private ArrayList<String> listeDesChemins;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_reserv, container, false);
        Button btnchercher = view.findViewById(R.id.btnchercher);
        EditText txtnum = view.findViewById(R.id.txtnum);
        EditText txtcode = view.findViewById(R.id.txtcode);
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
            MaterialCardView cardView = getView().findViewById(R.id.card);
            TextView txtTitle = getView().findViewById(R.id.title);
            TextView description1 = getView().findViewById(R.id.description1);
            TextView description2 = getView().findViewById(R.id.description2);
            Button btn1 = getView().findViewById(R.id.button1);
            Button btn2 = getView().findViewById(R.id.button2);
            listeDesChemins = new ArrayList<>();
            JSONArray tblelements = new JSONArray(unechainejson);
            ImageView monImageView = getView().findViewById(R.id.imgHotel);
            TextView txtError = getView().findViewById(R.id.labelError);
            if (tblelements.length() == 0) {
                txtError.setVisibility(View.VISIBLE);
                txtError.setText("La réservation souhaitée n'existe pas !");

                cardView.setVisibility(View.GONE);
            } else {
                txtError.setVisibility(View.GONE);
                // Afficher les éléments s'il y en a
                cardView.setVisibility(View.VISIBLE);
                for (int i = 0; i < tblelements.length(); i++) {
                    JSONObject unelement = tblelements.getJSONObject(i);
                    String cheminImage = "a" + unelement.getString("nomfichier");
                    listeDesChemins.add(cheminImage);
                    txtTitle.setText(unelement.getString("nom"));
                    description1.setText("Téléphone: " + unelement.getString("tel") + "\nDate de début: " + unelement.getString("datedeb") + " \nDate de Fin " + unelement.getString("datefin"));
                    description2.setText("Numéro de réservation: " + unelement.getString("nores") + "\nCode d'accée: " + unelement.getString("codeacces"));
                }
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
            //Toast.makeText(getContext(), "Réponse reçue", Toast.LENGTH_LONG).show();
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