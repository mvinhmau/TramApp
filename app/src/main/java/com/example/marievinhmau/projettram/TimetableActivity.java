package com.example.marievinhmau.projettram;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;


public class TimetableActivity extends AppCompatActivity  {

    private Spinner spinnerStations;
    private Spinner spinnerDirections;
    private Spinner spinnerMinutes;
    private Spinner spinnerHeures;
    private Button btnRechercher;
    private RadioButton radioPremiersDerniersPassages, radioProchainsPassages;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

       //Creation des Listes Déroulantes
        creationSpinner();

        //ecouteur d'evenement
        addListener();
    }


    //Récupérer les directions
    private ArrayList <String> parseXMLDirection(String filename)  {
        XmlPullParserFactory parserFactory;
        ArrayList<String> list = new ArrayList<>();
        try {
            parserFactory= XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is= getAssets().open(filename);

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            list= processParsingDirection(parser);



        } catch (XmlPullParserException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private ArrayList<String> processParsingDirection(XmlPullParser parser) throws XmlPullParserException, IOException{
        ArrayList <String> directions = new ArrayList<>();
        String direction = "";

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT){

            String eltname =null;
            switch (eventType){
                case XmlPullParser.START_TAG:
                    eltname = parser.getName();

                    //Log.i("ele", "processParsing: "+eltname);
                    if ("direction".equals(eltname))
                        {
                            direction=parser.nextText();
                            directions.add(direction);

                            //test.setText("name: " +currentStation.name);
                        }

                    break;
            }

            eventType=parser.next();
        }

        return directions;
    }




    //Récupérer les noms de stations
    private ArrayList <String> parseXMLStation(String filename)  {
        XmlPullParserFactory parserFactory;
        ArrayList<String> list = new ArrayList<>();
        try {
            parserFactory= XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is= getAssets().open(filename);

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            list= processParsingStation(parser);



        } catch (XmlPullParserException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private ArrayList<String> processParsingStation(XmlPullParser parser) throws XmlPullParserException, IOException{
        ArrayList <String> stations = new ArrayList<>();
        String currentStation = null;

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT){

            String eltname =null;
            switch (eventType){
                case XmlPullParser.START_TAG:
                    eltname = parser.getName();

                    //Log.i("ele", "processParsing: "+eltname);

                    if ("name".equals(eltname))
                    {
                            currentStation=parser.nextText();
                            stations.add(currentStation);
                    }

                    break;
            }

            eventType=parser.next();
        }

        return stations;
    }



    //Fonctions Liste Heures
    private ArrayList<Integer> listeHeures ()
    {
        ArrayList<Integer> heures = new ArrayList<>();

        for (int cpt=0; cpt<25; cpt ++)
        {
            heures.add(cpt);
        }
        return heures;
    }


    //Fonctions Liste Minutes
    private ArrayList<Integer> listeMinutes ()
    {
        ArrayList<Integer> minutes = new ArrayList<>();
        minutes.add(00);

        for (int cpt=15; cpt<60; cpt+=10)
        {
            minutes.add(cpt);
        }
        return minutes;
    }


    //Ecouteur d'evenement
    public void addListener() {
        //ItinaryActivity
        btnRechercher = (Button) findViewById(R.id.searchBtn);
        radioPremiersDerniersPassages = (RadioButton) findViewById(R.id.PremiersDeniersBox);
        radioProchainsPassages = (RadioButton) findViewById(R.id.ProchainsPassagesBox);
        btnRechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimetableActivity.this, TimetableResultActivity.class);
                //Récupérer la direction, la station et l'horaire
                String direction =spinnerDirections.getSelectedItem().toString();
                String station =spinnerStations.getSelectedItem().toString();
                String minutes, heures;
                if (parseInt(spinnerMinutes.getSelectedItem().toString())<10)
                {
                    minutes="00";
                }
                else
                {
                    minutes=spinnerMinutes.getSelectedItem().toString();
                }
                String horaire =spinnerHeures.getSelectedItem().toString()+":"+minutes;
                boolean premiersDerniersPassages =false;
                if (radioPremiersDerniersPassages.isChecked())
                {
                    premiersDerniersPassages =true;
                }
                if (radioPremiersDerniersPassages.isChecked())
                {
                    Date date = new Date();
                    heures=""+date.getHours();
                    minutes=""+date.getMinutes();
                    if (parseInt(heures.trim())<10)
                    {
                        heures="0"+heures;
                    }
                    if (parseInt(minutes.trim())<10)
                    {
                        minutes="0"+minutes;
                    }
                    horaire=heures.trim()+":"+minutes.trim();
                    Log.d("date ", " "+horaire);
                }
                intent.putExtra("direction", direction);
                intent.putExtra("station", station);
                intent.putExtra("horaire", horaire);
                intent.putExtra("premiersDeniersPassages", premiersDerniersPassages);

                startActivity(intent);
            }
        });
    }

    //Creation des différentes Listes Déroulantes
    private void creationSpinner(){
        String filename ="stations_ligne_1.xml";
        //Liste déroulante des directions
        spinnerDirections = (Spinner) findViewById(R.id.listedirection);
        ArrayList <String> directions = parseXMLDirection(filename);
        ArrayAdapter adapterDirection = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                directions
        );
        adapterDirection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDirections.setAdapter(adapterDirection);

        //Liste déroulante des différentes stations
        spinnerStations = (Spinner) findViewById(R.id.listeStations);
        ArrayList <String> stations = parseXMLStation(filename);
        ArrayAdapter adapterStations = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                stations
        );
        adapterStations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStations.setAdapter(adapterStations);

        //Liste déroulante des heures
        spinnerHeures = (Spinner) findViewById(R.id.listeHeures);
        ArrayList <Integer> heures = listeHeures();
        ArrayAdapter adapterHeures = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                heures
        );
        adapterHeures.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHeures.setAdapter(adapterHeures);

        //Liste déroulante des minutes
        spinnerMinutes = (Spinner) findViewById(R.id.listeMinutes);
        ArrayList <Integer> minutes = listeMinutes();
        ArrayAdapter adapterMinutes = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                minutes
        );
        adapterHeures.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMinutes.setAdapter(adapterMinutes);
    }











}



