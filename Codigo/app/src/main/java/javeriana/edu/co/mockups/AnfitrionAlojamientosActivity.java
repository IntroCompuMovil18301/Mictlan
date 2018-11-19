package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class AnfitrionAlojamientosActivity extends AppCompatActivity {

    private static final String PATH_ALOJ = "alojamientos";
    private static final String LEER_TAG = "LeerActivity";

    ListView lv;

    private FirebaseDatabase database;
    private ArrayList<Alojamiento> aloj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anfitrion_alojamientos);

        setTitle("Mis Alojamientos");

        database = FirebaseDatabase.getInstance();

        lv = (ListView) findViewById(R.id.lv1);

        aloj = new ArrayList<>();

        DatabaseReference alojRef = database.getReference(PATH_ALOJ);
        alojRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                    Alojamiento auxAloj = singleSnap.getValue(Alojamiento.class);

                    if (auxAloj.getUsuario().equals(FirebaseAuth.getInstance().getUid())) {
                        System.out.println("Alojamiento: " + auxAloj.getId());
                        aloj.add(auxAloj);
                        updateListView();
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
                Intent lv_intent = new Intent(view.getContext(), InfoAlojaActivity.class);
                lv_intent.putExtras(alojamiento);
                startActivity(lv_intent);
            }
        });
    }
    private void updateListView() {
        lv.setAdapter(new CustomAdapter(this,aloj));
    }
}
