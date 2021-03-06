package javeriana.edu.co.mockups;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javeriana.edu.co.mockups.mAdapterView.CustomAdapter;
import javeriana.edu.co.mockups.mAdapterView.ImageAddAdapter;
import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.Reserva;

public class BuscarAlojamientoActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 758;
    private static final int PLACE_PICKER_REQUEST = 997;

    private static final String PATH_ALOJ = "alojamientos/";
    private static final String PATH_RESERVA = "reservas/";
    public static final String LEER_TAG = "BuscarAlojamiento";

    private TextView boton_inicio  ;
    private TextView boton_final  ;
    private FloatingActionButton getLocation;

    public int diain;
    public int mesin;
    public int anoin;

    public final static double RADIUS_OF_EARTH_KM = 6371;
    Reserva reservaVal;

    TextView fechaSel;
    EditText busquedC;

    boolean selectionDate = false;

    Calendar cCalendar;
    MyDatePickerDialog dpdInicio;
    MyDatePickerDialog dpdFin;

    String fechaIn = null;
    String fechaFin = null;

    private Geocoder mGeocoder;

    FirebaseDatabase database;

    boolean date_flag = false;
    private ListView alojamientos;

    ArrayList<Alojamiento> aloj;
    ArrayList<Reserva> resv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        this.boton_inicio = findViewById(R.id.boton_de_fecha_inicio);
        this.boton_final = findViewById(R.id.boton_de_fecha_fin);
        getLocation = findViewById(R.id.ba_fab_location);

        this.busquedC = (EditText) findViewById(R.id.searchViewBuscar);
        alojamientos = findViewById(R.id.lvBuscar);

        mGeocoder = new Geocoder(getBaseContext());

        this.boton_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cCalendar = Calendar.getInstance();
                int day = cCalendar.get(Calendar.DAY_OF_MONTH);
                int month = cCalendar.get(Calendar.MONTH);
                int year = cCalendar.get(Calendar.YEAR);

                dpdInicio = new MyDatePickerDialog(BuscarAlojamientoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        fechaIn = Integer.toString(mDay) + "/" + Integer.toString(mMonth+1) + "/" + Integer.toString(mYear);
                        selectionDate = true;
                        boton_inicio.setText(String.format("%s\n%s", "Fecha Inicio", fechaIn));
                        diain = mDay;
                        mesin = mMonth;
                        anoin = mYear;
                    }
                }, year, month, day);

                dpdInicio.setPermanentTitle("Fecha Inicio");
                dpdInicio.show();
            }
        });

        this.boton_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cCalendar = Calendar.getInstance();
                int day = cCalendar.get(Calendar.DAY_OF_MONTH);
                int month = cCalendar.get(Calendar.MONTH);
                int year = cCalendar.get(Calendar.YEAR);

                if(selectionDate){
                    day = diain ;
                    month = mesin;
                    year = anoin;
                }



                dpdFin = new MyDatePickerDialog(BuscarAlojamientoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        fechaFin = Integer.toString(mDay) + "/" + Integer.toString(mMonth+1) + "/" + Integer.toString(mYear);
                        selectionDate = false;
                        boton_final.setText(String.format("%s\n%s", "Fecha Final", fechaFin));

                    }
                }, year, month, day);

                dpdFin.setPermanentTitle("Fecha Inicio");
                dpdFin.show();
            }
        });

        busquedC.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    try {
                        doSearch();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }

        });
        database = FirebaseDatabase.getInstance();

        aloj = new ArrayList<Alojamiento>();
        resv = new ArrayList<Reserva>();

        DatabaseReference leerRef = database.getReference(PATH_ALOJ);
        leerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                    Alojamiento aux = singleSnap.getValue(Alojamiento.class);
                    aloj.add(aux);
                }
                updateListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(LEER_TAG, "error en la consulta", databaseError.toException());
            }
        });

        leerRef = database.getReference(PATH_RESERVA);
        leerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                    Reserva aux = singleSnap.getValue(Reserva.class);
                    if(aux != null){
                        resv.add(aux);
                        Log.w("resv",aux.getId());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION,
                    "Para seleccionar una localizacion desde el mapa",
                    REQUEST_LOCATION);
        } else {
            updateGetLocation();
        }

        alojamientos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle alojamiento = new Bundle();
                alojamiento.putSerializable("alojamiento", aloj.get(i));
                Intent lv_intent = new Intent( view.getContext(), InfoAlojaActivity.class );
                lv_intent.putExtras(alojamiento);
                startActivity( lv_intent );
            }
        });
    }


    /**
     * Retorna si un alojamiento esta en el radio especificado
     *
     * @param latLngA
     * @param latLngB
     * @param radious
     * @return
     */
    private boolean isInradious(LatLng latLngA, LatLng latLngB, double radious) {

        Location locationA = new Location("punto A");
        locationA.setLatitude(latLngA.latitude);
        locationA.setLongitude(latLngA.longitude);

        Location locationB = new Location("punto B");
        locationB.setLatitude(latLngB.latitude);
        locationB.setLongitude(latLngB.longitude);

        double distance = locationA.distanceTo(locationB);

        if (distance < radious) {
            return true;
        }
        return false;
    }

    private void updateListView() {
        alojamientos.setAdapter(new CustomAdapter(this, aloj));
    }

    private void doSearch() throws ParseException {
        String address = this.busquedC.getText().toString();
        if (!address.isEmpty()) {
            try {
                List<Address> addresses = mGeocoder.getFromLocationName(address, 2);

                if (addresses != null && !addresses.isEmpty()) {

                    Address addressResult = addresses.get(0);
                    if (!selectionDate) {
                        showMap(addressResult.getLatitude(), addressResult.getLongitude());
                    } else {
                        showMapWithDateFilter(addressResult.getLatitude(), addressResult.getLongitude());
                    }

                } else {
                    Toast.makeText(BuscarAlojamientoActivity.this, "Dirección no encontrada !", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(BuscarAlojamientoActivity.this, "Ingrese una direccion.", Toast.LENGTH_SHORT).show();
        }

    }

    private void showMap(double latitude, double longitude) {
        ArrayList<Alojamiento> aux = new ArrayList<Alojamiento>();
        for (Alojamiento alojamientoSel : aloj) {
            if (distance(latitude, longitude, alojamientoSel.getLatitud(), alojamientoSel.getLongitud()) <= 2) {
                aux.add(alojamientoSel);
                Toast.makeText(this, "Exiten "+ aux.size()+ "alojamientos en la zona que buscaste. \n Para mas información toca el marcador del alojamiento", Toast.LENGTH_LONG).show();
            }
        }
        Intent mapas = new Intent(this, AlojamientoMapsActivity.class);
        Bundle paquete = new Bundle();
        paquete.putSerializable("lista", aux);
        paquete.putDouble("latitud", latitude);
        paquete.putDouble("longitud", longitude);
        mapas.putExtras(paquete);
        startActivity(mapas);
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result * 100.0) / 100.0;
    }

    /**
     * Saber si el alojamiento esta reservado entre las fechas
     *
     * @param alojamiento
     * @return
     */
    private boolean isReservedInDate(Alojamiento alojamiento) throws ParseException {
        boolean flag = false;
        boolean flag2 = false;
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dateInf;
        Date dateSup;
        Date selectedDateInf = format.parse(fechaIn);
        Date selectedDateSup = format.parse(fechaFin);
        Reserva reservaaux = null;
        for (String reserva : alojamiento.getReservas()) {
            if (reserva != null) {
                flag2 = false;
                for(Reserva rev : resv){
                    if (rev.getId().equals(reserva)){
                        reservaaux = rev;
                        flag2 = true;
                        break;
                    }
                }

                if(reservaaux != null){
                    dateInf = format.parse(reservaaux.getFechaInicio());
                    dateSup = format.parse(reservaaux.getFechaFinal());
                    Log.w("FECHA",dateInf.toString());
                    Log.w("FECHA_2",dateSup.toString());
                    Log.w("FECHA_3",selectedDateInf.toString());
                    Log.w("FECHA_4",selectedDateSup.toString());
                    Log.w("RESU", String.valueOf(matchDates(dateInf, dateSup, selectedDateInf, selectedDateSup)));
                    if (matchDates(dateInf, dateSup, selectedDateInf, selectedDateSup)) {
                        flag = true;
                    }
                    reservaaux = null;
                }
            }
        }
        return flag;
    }

    /**
     * Find if a range is available
     *
     * @param cotaInf
     * @param CotaSup
     * @param FechaSearchInf
     * @param FechaSearchSup
     * @return
     */
    private boolean matchDates(Date cotaInf, Date CotaSup, Date FechaSearchInf, Date FechaSearchSup) {
        boolean flag = false;
        if (cotaInf.after(FechaSearchInf) && cotaInf.before(FechaSearchSup)) {
            flag = true;
        }

        if(cotaInf.equals(FechaSearchInf) && CotaSup.after(FechaSearchSup)){
            flag = true;
        }

        if(CotaSup.equals(FechaSearchSup) && cotaInf.before(FechaSearchInf)){
            flag = true;
        }

        if (CotaSup.after(FechaSearchInf) && CotaSup.before(FechaSearchSup)) {
            flag = true;
        }

        if (cotaInf.before(FechaSearchInf) && CotaSup.after(FechaSearchSup)) {
            flag = true;
        }

        if (cotaInf.equals(FechaSearchInf) && CotaSup.equals(FechaSearchSup)) {
            flag = true;
        }

        return flag;
    }

    private void showMapWithDateFilter(double latitude, double longitude) throws ParseException {
        ArrayList<Alojamiento> aux = new ArrayList<Alojamiento>();
        for (Alojamiento alojamiento : aloj) {
            if (distance(latitude, longitude, alojamiento.getLatitud(), alojamiento.getLongitud()) <= 2) {
                if (!isReservedInDate(alojamiento)) {
                    aux.add(alojamiento);
                    Toast.makeText(this, alojamiento.getTitulo(), Toast.LENGTH_LONG).show();
                }
            }
        }
        Intent mapas = new Intent(this, AlojamientoMapsActivity.class);
        Bundle paquete = new Bundle();
        paquete.putSerializable("lista", aux);
        paquete.putDouble("latitud", latitude);
        paquete.putDouble("longitud", longitude);
        mapas.putExtras(paquete);
        startActivity(mapas);
    }

    private void requestPermission(Activity context, String permission, String explanation,
                                   int requestId) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                Toast.makeText(context, explanation, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestId);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGetLocation();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PLACE_PICKER_REQUEST: {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    busquedC.setText(place.getAddress());
                    //latLng = place.getLatLng();
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private void updateGetLocation() {
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(BuscarAlojamientoActivity.this), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    Log.e(LEER_TAG, e.getStackTrace().toString());
                }
            }
        });
    }
}