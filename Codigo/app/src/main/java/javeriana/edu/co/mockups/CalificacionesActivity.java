package javeriana.edu.co.mockups;

import android.support.annotation.NonNull;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import javeriana.edu.co.mockups.mAdapterView.CalificacionAdapter;
import javeriana.edu.co.mockups.mAdapterView.CustomAdapter;
import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.Calificacion;
import javeriana.edu.co.mockups.mData.Reserva;

public class CalificacionesActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;

    private static final String PATH_CALIF = "calificaciones";
    private static final String PATH_USERS = "usuarios/";

    private DatabaseReference mDatabase;
    private ListView list_cal;
    private ArrayList<Calificacion> calificaciones;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificaciones);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        final Alojamiento alojamiento = (Alojamiento) getIntent().getExtras().getSerializable("alojamiento");
        calificaciones = new ArrayList<>();

        list_cal = (ListView) findViewById(R.id.list_cal);

        DatabaseReference calificacionRef = database.getReference(PATH_CALIF);
        calificacionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                    Calificacion aux = singleSnap.getValue(Calificacion.class);
                    Log.d("---->", "onDataChange: "+aux.getComentario());
                    if (aux.getAlojamientoId().equals(alojamiento.getId())) {
                        calificaciones.add(aux);
                    }
                }
                updateListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("--->", "error en la consulta", databaseError.toException());
            }
        });

    }
    private void updateListView() {
        list_cal.setAdapter(new CalificacionAdapter(this,calificaciones));
    }
}
