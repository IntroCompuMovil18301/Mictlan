package javeriana.edu.co.mockups;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javeriana.edu.co.mockups.mData.Alojamiento;

public class AlojamientosEnCamino extends FragmentActivity implements OnMapReadyCallback {

    private static final String PATH_ALOJ = "alojamientos/";
    private static final String LEER_TAG = "ALojamientosEnCamino";

    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    final static int REQUEST_CHECK_SETTINGS = 2;
    final static int RADIUS_OF_EARTH_KM =  6371;

    public static final double lowerLeftLatitude = 4.497712;
    public static final double lowerLeftLongitude = -74.242971;
    public static final double upperRightLatitude = 4.763589;
    public static final double upperRigthLongitude = -74.003313;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Geocoder mGeocoder;
    private FirebaseDatabase database;
    private Location location;
    private MarkerOptions actualMarkerOptions;
    private Marker actualMarker;
    private ArrayList<LatLng> points;
    private Polyline path;
    private PolylineOptions lineOptions;
    public Set<Alojamiento> alojamientos;
    private LatLng positionOrigin;
    private LatLng positionDest;
    private FloatingActionButton back;
    Alojamiento alojamiento;

    ArrayList<Alojamiento> aloj = new ArrayList<Alojamiento>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alojamientos_en_camino);

        alojamientos =  new HashSet<Alojamiento>();
        back = findViewById(R.id.f_btn_back_Alojamiento_rutas_b);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION,
                "Se necesita acceder a los ubicacion", MY_PERMISSIONS_REQUEST_LOCATION);

        database = FirebaseDatabase.getInstance();

        mLocationRequest = createLocationRequest();

        mGeocoder = new Geocoder(getBaseContext());

        DatabaseReference leerRef = database.getReference(PATH_ALOJ);
        leerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                    Alojamiento aux = singleSnap.getValue(Alojamiento.class);
                    aloj.add(aux);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(LEER_TAG, "error en la consulta", databaseError.toException());
            }
        });
        
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        actualMarkerOptions = new MarkerOptions();

        actualMarkerOptions
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.person))
                .title("Actual");

        alojamiento = (Alojamiento) getIntent().getSerializableExtra("alojamiento");

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                location = locationResult.getLastLocation();
                //Log.i(“LOCATION", "Location update in the callback: " + location);
                if (location != null && mMap != null) {
                    LatLng actualPosition = new LatLng(location.getLatitude(),location.getLongitude());
                    if (actualMarker != null){
                        actualMarker.setPosition(actualPosition);
                    }
                    else{
                        actualMarkerOptions.position(actualPosition);
                        actualMarker = mMap.addMarker(actualMarkerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(actualPosition));
                    }
                    /*if (positionDest != null) {
                        drawPath(actualPosition, positionDest);
                    }*/

                }
            }
        };

        turnLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        loadLocation();

        if (mMap != null) {
            //Agregar Marcador al mapa
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(alojamiento.getLatitud(),alojamiento.getLongitud()))
                    .anchor(0.5f, 0.5f)
                    .title(alojamiento.getTitulo())
                    .snippet(alojamiento.getId())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel)));
        }
        if (mMap != null && location != null) {
            Toast.makeText(this, "Dibujar Mapa", Toast.LENGTH_LONG).show();
            positionDest = new LatLng(alojamiento.getLatitud(),alojamiento.getLongitud());
            positionOrigin = new LatLng(location.getLatitude(), location.getLongitude());
            markAlojamientosInWay(positionOrigin, positionDest);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String alojId = marker.getSnippet();
                if(alojId != null){
                    DatabaseReference alojaRef= FirebaseDatabase.getInstance().getReference(PATH_ALOJ).child(alojId);
                    alojaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            alojamiento = dataSnapshot.getValue(Alojamiento.class);
                            Intent intent = new Intent(AlojamientosEnCamino.this, InfoAlojaActivity.class);
                            Bundle aloj = new Bundle();
                            aloj.putSerializable("alojamiento",alojamiento);
                            intent.putExtras(aloj);
                            startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                    }
                    );

                }
                return false;
            }
        });


    }

    /**
     * Solicita el permiso para acceder a la ubucacion
     *
     * @param context
     * @param permission
     * @param explanation
     * @param requestId
     */
    private void requestPermission(Activity context, String permission, String explanation, int requestId ){
        if (ContextCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,permission)) {
                Toast.makeText(context, explanation, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestId);
        }
    }

    /** Crea una peticion de localizacion para x tiempo
     *
     * @return
     */
    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); //tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval(5000); //máxima tasa de refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    /**
     *
     *  Establece los parametros para las tareas de actualizacion de localizacion
     */
    private void turnLocation(){
        LocationSettingsRequest.Builder builder = new
                LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        //loadLocation();

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates(); //Todas las condiciones para recibir localizaciones
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {// Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(AlojamientosEnCamino.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        } break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. No way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    /**
     * Iniciar servicios de actualizacion de localizacion
     */
    private void startLocationUpdates(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, null);
        }
    }
    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION : {
                loadLocation();
                break;
            }
        }
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    startLocationUpdates(); //Se encendió la localización!!!
                } else {
                    Toast.makeText(this,
                            "Sin acceso a localización, hardware deshabilitado!",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    /**
     * Parar servicios de actualizacion
     */
    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    /**
     * Encuentra los alojamientos más cercanos en un radio de n km
     * @param latitud
     * @param longitud
     * @param radious
     */
    public void findAlojamientoInRadius(double latitud, double longitud, double radious){

        LatLng latLngA = new LatLng(latitud,longitud);
        LatLng latLngB;
        for (Alojamiento alojamientoInd:aloj) {
            if (alojamientoInd != null) {
                latLngB = new LatLng(alojamientoInd.getLatitud(), alojamientoInd.getLongitud());
                if (isInradious(latLngA, latLngB, radious)) {
                    alojamientos.add(alojamientoInd);
                }
            }
        }
        if(alojamientos.isEmpty()){
            Toast.makeText(AlojamientosEnCamino.this," No Existen alojamientos entre tu origen y destino.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(AlojamientosEnCamino.this,"Existen "+alojamientos.size()+" alojamientos entre tu origen y destino. \n" +
                    "Si deseas descansar presiona el marcador y haz una reserva!",Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * Crea un markador en una posicion especifica
     *
     * @param latitud
     * @param longitud
     * @param titulo
     * @param ubicacion
     * @return
     */
    protected Marker createMarker(double latitud, double longitud, String id, String titulo, String ubicacion) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitud,longitud))
                .anchor(0.5f, 0.5f)
                .title(titulo)
                .snippet(id)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_marker)));
    }

    /**
     * Retorna si esta en el radio especificado
     *
     * @param latLngA
     * @param latLngB
     * @param radious
     * @return
     */
    private boolean isInradious(LatLng latLngA, LatLng latLngB, double radious){

        Location locationA = new Location("punto A");
        locationA.setLatitude(latLngA.latitude);
        locationA.setLongitude(latLngA.longitude);

        Location locationB = new Location("punto B");
        locationB.setLatitude(latLngB.latitude);
        locationB.setLongitude(latLngB.longitude);

        double distance = locationA.distanceTo(locationB);

        if (distance < radious){
            return true;
        }
        return false;
    }

    /**
     *
     * @param origin
     * @param destination
     */
    private void markAlojamientosInWay(LatLng origin, LatLng destination){
        if(path != null){
            path.remove();
        }
        // Getting URL to the Google Directions API
        String url = getUrl(origin, destination);
        AlojamientosEnCamino.FetchUrl FetchUrl = new AlojamientosEnCamino.FetchUrl();

        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    /**
     *
     */
    private void loadLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new
                    OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null && mMap != null) {
                                LatLng actualPosition = new LatLng(location.getLatitude(),location.getLongitude());
                                actualMarkerOptions.position(actualPosition);
                                actualMarker = mMap.addMarker(actualMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(actualPosition));
                                positionDest = new LatLng(alojamiento.getLatitud(),alojamiento.getLongitud());
                                positionOrigin = new LatLng(location.getLatitude(), location.getLongitude());
                                markAlojamientosInWay(positionOrigin, positionDest);
                            }
                        }});
        }
        else {

        }
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        //String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        //String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters + "&key=" + R.string.google_directions_key;
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters + "&key=" + "AIzaSyBnelNcSt3akle6dFOuEgMtkzhtCG0Mxf0";

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    public class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }

    }

    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    findAlojamientoInRadius(lat,lng,2000.0);
                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.parseColor("#0a70a2"));
                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null  && positionDest!=null && mMap != null) {
                for(Alojamiento alojamientoEnc: alojamientos){
                    if(alojamiento.getLatitud() != alojamientoEnc.getLatitud() && alojamiento.getLongitud() != alojamientoEnc.getLongitud()){
                        createMarker(alojamientoEnc.getLatitud(),alojamientoEnc.getLongitud(),alojamientoEnc.getId(),alojamientoEnc.getTitulo(),alojamientoEnc.getUbicacion());
                    }
                }
                path = mMap.addPolyline(lineOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionDest, 15));
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }

}
