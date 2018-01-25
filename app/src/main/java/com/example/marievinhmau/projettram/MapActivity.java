package com.example.marievinhmau.projettram;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.google.maps.android.PolyUtil;

import org.joda.time.DateTime;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener {

    private static final int overview = 0;
    private GoogleMap mMap;
    private ArrayList<Location> LocationStations = new ArrayList();
    private FusedLocationProviderClient mFusedLocationClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupGoogleMapScreenSettings(mMap);
        //ouvre le plan sur Montpellier
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(43.610769, 3.8767159999999876)));
        mMap.setMinZoomPreference(15.0f);

        getLocationStations();
        createStationsMarks();
        enableMyLocation();
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);


    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            Toast.makeText(this, "Activer la géocalisation", Toast.LENGTH_LONG).show();
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);


        }
    }

    private void getLocationStations() {

        LocationStations = new ArrayList<>();
        Location location = new Location("");
        boolean currentStation = false;


        XmlPullParserFactory parserFactory;

        try {
            Log.d("create marks", "OK ");
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("stations_ligne_1.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String eltname = null;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        eltname = parser.getName();

                        if ("name".equals(eltname)) {
                            location = new Location(parser.nextText());
                            currentStation = true;

                        } else {
                            if ((currentStation) && "latitude".equals(eltname)) {
                                location.setLatitude(Double.parseDouble(parser.nextText()));
                            }
                            if ((currentStation) && "longitude".equals(eltname)) {
                                location.setLongitude(Double.parseDouble(parser.nextText()));
                                //ajout des coord à l'arrayList
                                LocationStations.add(location);
                                currentStation = false;
                            }

                        }
                        break;
                }

                eventType = parser.next();
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void createStationsMarks() {
        for (Location location : LocationStations) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(location.getProvider())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            );
        }

    }


    @Override
    public boolean onMyLocationButtonClick() {
        Log.d("Button " , "etape1");
        //currentLocation = mMap.getMyLocation();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("Button " , "etape1 bis");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d("Button " , "etape2");
                            if (location != null) {
                                Log.d("Button " , "etape3");
                                Location currentLocation= new Location("current");
                                currentLocation.setLatitude(location.getLatitude());
                                currentLocation.setLongitude(location.getLongitude());
                                Log.d("Position : ", "" + currentLocation.getLatitude() + " :" + currentLocation.getLongitude());
                                double distMin = 1000000000;
                                double dist = 0;
                                Location locationStationPlusProche = new Location("");
                                for (Location locationStation : LocationStations) {


                                    dist =currentLocation.distanceTo(locationStation);
                                    if (dist < distMin) {
                                        distMin = dist;
                                        locationStationPlusProche = locationStation;
                                    }
                                }
                                Log.d("dist min : ", "" + distMin);
                                mMap.setMinZoomPreference(16.0f);

                                DirectionsResult results = getDirectionsDetails(currentLocation.getLatitude()+","+currentLocation.getLongitude()
                                        ,locationStationPlusProche.getLatitude()+","+ locationStationPlusProche.getLongitude()
                                        ,TravelMode.WALKING);
                                if (results != null) {
                                    Log.d("Direction", "OK");
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                            .title(location.getProvider())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                    );
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(locationStationPlusProche.getLatitude(), locationStationPlusProche.getLongitude()))
                                            .title(location.getProvider())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                    );
                                    addPolyline(results, mMap);
                                    positionCamera(results.routes[overview], mMap);
                                    String msg2= "La station la plus proche  est " + locationStationPlusProche.getProvider() + "\n";
                                    msg2+= getInformationTimeDistance(results);
                                    createToast(msg2);

                                }
                                else {
                                    Log.d("Direction", "erreur resultat null");
                                }

                            }
                        }
                    });
        }
        return false;


    }
    private void createToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void positionCamera(DirectionsRoute route, GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(route.legs[overview].startLocation.lat, route.legs[overview].startLocation.lng), 12));
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[overview].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath).width(5)
                .color(Color.BLUE));
    }

    private String getInformationTimeDistance(DirectionsResult results){
        return  "Temps :"+ results.routes[overview].legs[overview].duration.humanReadable + "\n Distance :" + results.routes[overview].legs[overview].distance.humanReadable;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
        Log.d("Position : ", "" + location.getLatitude());
    }



    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext
                .setQueryRateLimit(3)
                .setApiKey("AIzaSyA7CLAOlhDPOPV-QD32VE9oE_FXXlF4nDY")
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    private DirectionsResult getDirectionsDetails(String origin,String destination,TravelMode mode) {
        DateTime now = new DateTime();
        try {
            Log.d("Direction Result", "etape 1");
            DirectionsResult dir= DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .await();
            Log.d("Direction Result", "etape 2");
            return dir;

        } catch (ApiException e) {
            e.printStackTrace();
            Log.d("API EXECEPTION","");
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("INTERRUPTED EXECEPTION","");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("IO EXECEPTION","");
            return null;
        }
    }


    private void setupGoogleMapScreenSettings(GoogleMap mMap) {
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setTrafficEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
    }




}