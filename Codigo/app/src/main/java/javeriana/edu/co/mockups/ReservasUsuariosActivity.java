package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javeriana.edu.co.mockups.mAdapterView.CustomAdapter;
import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.ColeccionAlojamientos;
import javeriana.edu.co.mockups.mData.Reserva;

public class ReservasUsuariosActivity extends AppCompatActivity {

    private static final String PATH_ALOJ = "alojamientos";
    private static final String PATH_RESE = "reservas";
    private static final String LEER_TAG = "LeerActivity";

    ListView lv;

    private FirebaseDatabase database;

    private ArrayList<Alojamiento> aloj;
    private ArrayList<Reserva> reservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas_usuarios);

        setTitle("Reservas Realizadas");

        database = FirebaseDatabase.getInstance();

        lv = (ListView) findViewById(R.id.lv5);

        aloj = new ArrayList<>();
        reservas = new ArrayList<>();

        DatabaseReference reservaRef = database.getReference(PATH_RESE);
        reservaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                    Reserva auxRese = singleSnap.getValue(Reserva.class);

                    if (auxRese.getUsuarioId().equals(FirebaseAuth.getInstance().getUid())) {
                        System.out.println("Alojamiento: " + auxRese.getAlojamientoId());
                        reservas.add(auxRese);
                        DatabaseReference leerAlojRef = database.getReference(PATH_ALOJ)
                                .child(auxRese.getAlojamientoId());
                        leerAlojRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Alojamiento auxAloja = dataSnapshot.getValue(Alojamiento.class);
                                    aloj.add(auxAloja);
                                    updateListView();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    System.out.println("Vacio: " + aloj.isEmpty());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle alojamiento = new Bundle();
                alojamiento.putSerializable("alojamiento", aloj.get(i));
                alojamiento.putSerializable("reserva", reservas.get(i));
                Intent lv_intent = new Intent(view.getContext(), InfoReseActivity.class);
                lv_intent.putExtras(alojamiento);
                startActivity( lv_intent );
            }
        });

    }

    private void updateListView() {
        lv.setAdapter(new CustomAdapter(this,aloj));
    }
}
