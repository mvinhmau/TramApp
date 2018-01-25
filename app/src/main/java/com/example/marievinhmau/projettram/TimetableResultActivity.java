package com.example.marievinhmau.projettram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TimetableResultActivity extends AppCompatActivity {


    private TextView result;
    private TextView result2;
    private String direction;
    private String stationName;
    private String horaire;
    private boolean premiersDerniersPassages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_result);


        result =(TextView) findViewById(R.id.Result1);
        result2 =(TextView) findViewById(R.id.Result2);
        recupererDonneesSelectionnees();
        parseXMLHoraires();



    }

    private void recupererDonneesSelectionnees ()
    {
        Intent intent = getIntent();
        direction = intent.getStringExtra("direction");
        stationName = intent.getStringExtra("station");
        horaire= intent.getStringExtra("horaire");
        premiersDerniersPassages=intent.getBooleanExtra("premiersDeniersPassages", false);

        //enlever les espaces
        direction=direction.trim();
        stationName=stationName.trim();
        horaire=horaire.trim();


        //test = (TextView) findViewById(R.id.Test);
        //test.setText(direction +" - " +stationName+" - "+ horaire);
    }



    private void parseXMLHoraires()  {
        XmlPullParserFactory parserFactory;
        ArrayList<String> list = new ArrayList<>();

        //A changer si on a plusieurs lignes
        String filename="horaires_ligne_1_direction_"+direction;
        //result.setText(filename);



        try {
            parserFactory= XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is= getAssets().open(filename);

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            processParsingResult(parser);




        } catch (XmlPullParserException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private  void processParsingResult(XmlPullParser parser) throws XmlPullParserException, IOException{

        String currentStation = null;
        ArrayList <String> horaires = new ArrayList<>();
        boolean trouve=false;
        int cpt=0;

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT){

            String eltname =null;
            switch (eventType){
                case XmlPullParser.START_TAG:
                    eltname = parser.getName();

                    if ("name".equals(eltname))
                    {
                        String name= parser.nextText();
                        trouve =false;

                        if (name.equals(stationName))
                        {
                            //result.setText("trouve : " +name);
                            trouve=true;

                        }
                    }
                    else if(trouve && "h".equals(eltname))
                    {
                        String horaire = parser.nextText();
                        horaires.add(horaire);
                        result.setText("horaire : "+ horaire);
                    }
                break;
            }

            eventType=parser.next();

        }

        if (premiersDerniersPassages)
        {
            selectionnerHorairesExtremites(horaires);
        }
        else
        {
            selectionnerHoraires(horaires);
            //station.name=stationName;
            //station.horaires=horaires;
            //printStation(station);
        }



    }

    private void selectionnerHoraires(ArrayList<String> horaires)
    {
        ArrayList<String> selection = new ArrayList<>();
        int horaireRef = convertionHoraire(horaire);
        int hInt;
        int cpt=0;
        for (String h : horaires)
        {
            hInt=convertionHoraire(h);
            if (cpt<3 && hInt>horaireRef)
            {
                selection.add(h);
                cpt ++;
            }

        }
        //Test si il y a bien trois horaires séléectionnés cas lorsqu'il n'y a plus de tram
        if (selection.size()<3)
        {
            int manquants= 3 - selection.size();
            for (int i=0; i<manquants; i++)
            {
                selection.add(horaires.get(i));
            }
        }
        horaire= horaire.replaceAll(":","h");
        String msg= "Les trois prochains passages après : "+horaire+" à "+stationName+ " en direction de "
                +direction + " sont :  \n \n";
        print(selection, msg, result);
        selection= new ArrayList<>();
    }

    private void selectionnerHorairesExtremites(ArrayList<String> horaires)
    {
        ArrayList<String> premiers = new ArrayList<>();
        ArrayList<String> derniers = new ArrayList<>();
        int size =horaires.size()-1;
        int i=0;

        for (int cpt=0; cpt<3; cpt ++)
        {
            premiers.add(horaires.get(cpt));

            derniers.add(horaires.get(size-cpt));
        }

        String msg1="Les trois premiers passage à "+stationName +" en direction de "+direction
                + " sont :  \n \n";
        String msg2="Les trois derniers passage à "+stationName +" en direction de "+direction
                + " sont :  \n \n";
        print(premiers, msg1, result);
        print(derniers, msg2, result2);

    }

    private int convertionHoraire (String horaireString)
    {
        horaireString=horaireString.trim();
        horaireString = horaireString.replaceAll(":", "");
        Log.d("convertionHoraire", " "+horaireString);
        return Integer.parseInt(horaireString);
    }


    private void print(ArrayList<String> horaires, String msg, TextView text)
    {
        StringBuilder builder= new StringBuilder();
        builder.append(msg).append("\n");
        for (String h : horaires)
        {
            h=h.trim();
            builder.append(" - ").append(h).append("\n \n");

        }
        text.setText(builder.toString());
    }

}
