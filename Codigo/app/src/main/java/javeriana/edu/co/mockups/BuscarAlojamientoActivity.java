package javeriana.edu.co.mockups;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javeriana.edu.co.mockups.mAdapterView.CustomAdapter;
import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.ColeccionAlojamientos;

public class BuscarAlojamientoActivity extends AppCompatActivity {

    private static final String PATH_ALOJ = "alojamientos/";
    private static final String LEER_TAG = "LeerActivity";

    public final static double RADIUS_OF_EARTH_KM = 6371;

    private Button boton_fecha;
    TextView fechaSel;
    EditText busquedC;

    Calendar cCalendar;
    MyDatePickerDialog dpdInicio;
    MyDatePickerDialog dpdFin;

    String fechaIn = null;
    String fechaFin = null;

    private Geocoder mGeocoder;

    FirebaseDatabase database;

    private ListView alojamientos;

    ArrayList<Alojamiento> aloj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        this.boton_fecha = (Button) findViewById(R.id.boton_de_fecha);
        this.fechaSel = (TextView) findViewById(R.id.mostrar_rango);
        this.busquedC = (EditText) findViewById(R.id.searchViewBuscar);
        alojamientos = findViewById(R.id.lvBuscar);

        mGeocoder = new Geocoder(getBaseContext());

        boton_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cCalendar= Calendar.getInstance();
                int day = cCalendar.get(Calendar.DAY_OF_MONTH);
                int month = cCalendar.get(Calendar.MONTH);
                int year = cCalendar.get(Calendar.YEAR);

                dpdFin = new MyDatePickerDialog(BuscarAlojamientoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        fechaFin = Integer.toString(mDay) + "/" + Integer.toString(mMonth) + "/" + Integer.toString(mYear);
                        fechaSel.setText(fechaIn + " - " + fechaFin);
                    }
                },year,month,day);

                dpdInicio = new MyDatePickerDialog(BuscarAlojamientoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        fechaIn = Integer.toString(mDay) + "/" + Integer.toString(mMonth) + "/" + Integer.toString(mYear);

                        dpdFin.setPermanentTitle("Fecha Fin");
                        dpdFin.show();

                    }
                },year,month,day);
                dpdInicio.setPermanentTitle("Fecha Inicio");
                dpdInicio.show();

            }
        });

        busquedC.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if( i == EditorInfo.IME_ACTION_DONE){
                    doSearch();
                    return true;
                }
                return false;
            }

        });
        database = FirebaseDatabase.getInstance();

        aloj = new ArrayList<Alojamiento>();

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

    }

    private void updateListView() {
        alojamientos.setAdapter(new CustomAdapter(this,aloj));
    }

    private void doSearch(){
        String address = this.busquedC.getText().toString();
        if (!address.isEmpty()){
            try {
                List<Address> addresses = mGeocoder.getFromLocationName(address, 2);

                if (addresses != null && !addresses.isEmpty()) {

                    Address addressResult = addresses.get(0);
                    showMap(addressResult.getLatitude(), addressResult.getLongitude());

                } else {Toast.makeText(BuscarAlojamientoActivity.this, "Dirección no encontrada !", Toast.LENGTH_SHORT).show();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(BuscarAlojamientoActivity.this, "Ingrese una direccion.", Toast.LENGTH_SHORT).show();
        }

    }

    private void showMap(double latitude, double longitude){
        ArrayList<Alojamiento> aux = new ArrayList<Alojamiento>();
        for (Alojamiento alojamiento : aloj) {
            if (distance(latitude, longitude, alojamiento.getLatitud(), alojamiento.getLongitud()) <= 2 ) {
                aux.add(alojamiento);
                Toast.makeText(this,alojamiento.getTitulo(),Toast.LENGTH_LONG).show();
            }
        }
        Intent mapas = new Intent(this, AlojamientoMapsActivity.class);
        Bundle paquete = new Bundle();
        paquete.putSerializable("lista", aux);
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
}
