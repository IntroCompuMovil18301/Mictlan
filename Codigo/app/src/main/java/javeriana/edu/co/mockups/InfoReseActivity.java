package javeriana.edu.co.mockups;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.Reserva;

public class InfoReseActivity extends AppCompatActivity {

    private ImageView foto_aloj;
    private TextView tipo_aloj;
    private TextView ubicac_aloj;
    private TextView numper_aloj;
    private TextView numcam_aloj;
    private TextView numban_aloj;
    private TextView numalc_aloj;
    private TextView fechaini_rese;
    private TextView fechafin_rese;
    private TextView total_rese;

    private CardView biogra_but;
    private TextView nombre_anfi;
    private ImageView foto_anfi;

    Button vercalificacionesInfoRese;
    Button comoLlegar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_rese);

        final Alojamiento alojamiento = (Alojamiento) getIntent().getExtras().getSerializable("alojamiento");
        final Reserva reserva = (Reserva) getIntent().getExtras().getSerializable("reserva");

        setTitle(alojamiento.getTitulo());

        foto_aloj = findViewById( R.id.ira_foto_aloj );
        tipo_aloj = findViewById( R.id.ira_tipo_aloj );
        ubicac_aloj = findViewById( R.id.ira_ubicac_aloj );
        numper_aloj = findViewById( R.id.ira_numper_aloj );
        numcam_aloj = findViewById( R.id.ira_numcam_aloj );
        numban_aloj = findViewById( R.id.ira_numban_aloj );
        numalc_aloj = findViewById( R.id.ira_numalc_aloj );
        fechaini_rese = findViewById(R.id.ira_fecini_rese);
        fechafin_rese = findViewById(R.id.ira_fecfin_rese);
        total_rese = findViewById(R.id.ira_total_rese);

        biogra_but = findViewById(R.id.ira_biogra_anfi);
        nombre_anfi = findViewById( R.id.ira_nombre_anfi );
        foto_anfi = findViewById( R.id.ira_foto_anfi );

        vercalificacionesInfoRese = (Button)findViewById(R.id.ira_califi_aloj);
        comoLlegar = (Button)findViewById(R.id.ira_ruta_rese);

        final File image = new File(getBaseContext().getExternalFilesDir(null),
                alojamiento.getImages().get(0) + "jpg");
        if(!image.exists()) {
            FirebaseStorage.getInstance().getReference("alojamientos")
                    .child(alojamiento.getId()).child(alojamiento.getImages().get(0)).getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(new FileInputStream(image), null, options);
                        foto_aloj.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(image), null, options);
                foto_aloj.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        tipo_aloj.setText(tipo_aloj.getText().toString() + alojamiento.getTipo());
        ubicac_aloj.setText(ubicac_aloj.getText().toString() + alojamiento.getUbicacion());
        numper_aloj.setText(Integer.toString(alojamiento.getPersonas()) + " " + numper_aloj.getText().toString());
        numcam_aloj.setText(Integer.toString(alojamiento.getCamas()) + " " + numcam_aloj.getText().toString());
        numalc_aloj.setText(Integer.toString(alojamiento.getAlcobas()) + " " + numalc_aloj.getText().toString());
        numban_aloj.setText(Integer.toString(alojamiento.getBanos()) + " " + numban_aloj.getText().toString());
        fechaini_rese.setText(fechaini_rese.getText().toString() + " " + reserva.getFechaInicio());
        fechafin_rese.setText("FechaFinal: " + reserva.getFechaFinal());
        total_rese.setText(total_rese.getText().toString() + " " + Double.toString(reserva.getCosto()));

        biogra_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),BiografiaAnfitrionActivity.class);
                startActivity(intent);
            }
        });

        vercalificacionesInfoRese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),CalificacionesActivity.class);
                startActivity(intent);
            }
        });

        comoLlegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ComoLlegarActivity.class);
                Bundle paquete = new Bundle();
                paquete.putSerializable("alojamiento", alojamiento);
                intent.putExtras(paquete);
                startActivity(intent);
            }
        });

    }
}
