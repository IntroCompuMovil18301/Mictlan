package javeriana.edu.co.mockups;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import javeriana.edu.co.mockups.mData.Alojamiento;

public class AlojamientoMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alojamiento_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        LatLng bogota = new LatLng(4.6486259, -74.2478962);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));

        ArrayList<Alojamiento> lista = (ArrayList<Alojamiento>) getIntent().getSerializableExtra("lista");
        for (Alojamiento alojamiento : lista) {
            LatLng position = new LatLng(alojamiento.getLatitud(), alojamiento.getLongitud());
            if (mMap != null) {
                //Agregar Marcador al mapa
                createMarker(position, alojamiento.getTitulo(), alojamiento.getUbicacion());
            }

        }
    }

    protected Marker createMarker(LatLng position, String titulo, String ubicacion) {

        return mMap.addMarker(new MarkerOptions()
                .position(position)
                .anchor(0.5f, 0.5f)
                .title(titulo)
                .snippet(ubicacion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel)));
    }

}


