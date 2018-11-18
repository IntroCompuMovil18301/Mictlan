package javeriana.edu.co.mockups;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.Calificacion;
import javeriana.edu.co.mockups.mData.Reserva;
import javeriana.edu.co.mockups.mData.Usuario;

public class CalificarActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;

    private static final String PATH_CALIF = "calificaciones/";

    private DatabaseReference mDatabase;
    private TextView aloj;
    private TextView anfitrion;
    private SimpleRatingBar estrellas;
    private TextInputEditText reseña;
    private Button ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);

        final Alojamiento alojamiento = (Alojamiento) Objects.requireNonNull(getIntent().getExtras()).getSerializable("alojamiento");
        final Reserva reserva = (Reserva) getIntent().getExtras().getSerializable("reserva");

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        aloj = (TextView) findViewById(R.id.cal_nomb_aloj);
        anfitrion = (TextView) findViewById(R.id.cal_nomb_hues) ;
        estrellas = (SimpleRatingBar) findViewById(R.id.cal_rating_aloj);
        reseña =  (TextInputEditText) findViewById(R.id.cal_res_aloj);
        ok = (Button) findViewById(R.id.cal_env_aloj);


        aloj.setText(alojamiento.getTitulo());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser Fuser = mAuth.getCurrentUser();
        Query myTopPostsQuery = mDatabase.child("usuarios").child(alojamiento.getUsuario());
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario aux = dataSnapshot.getValue(Usuario.class);
                anfitrion.setText(aux.getNombre());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final float ret = estrellas.getRating();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    DatabaseReference crearCalifRef = database.getReference(PATH_CALIF);

                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    Date today = Calendar.getInstance().getTime();

                    String reportDate = df.format(today);

                    final Calificacion calif = new Calificacion(Fuser.getUid(),alojamiento.getId(),reserva.getId(),estrellas.getRating(),reseña.getText().toString(),reportDate);

                    crearCalifRef.child(calif.getId()).setValue(calif, new
                            DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Toast.makeText(CalificarActivity.this,
                                                "No se pudo crear el alojamiento", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(CalificarActivity.this,
                                                "El alojamiento ha sido creada", Toast.LENGTH_SHORT).show();

                                    }
                                }
                    });

                }
            }
        });

    }

    private boolean validateForm() {
        return true;
    }

}
