package javeriana.edu.co.mockups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.Reserva;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;

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
    private Calendar fechaActual;
    private ExpCalendarView calend;
    private TextView irames;
    private SimpleRatingBar estrellas;

    private CardView biogra_but;
    private TextView nombre_anfi;
    private ImageView foto_anfi;
    Button vercalificacionesInfoRese;
    Button comoLlegar;
    Button calificar;
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
        calend = findViewById(R.id.ira_calend);
        irames = findViewById(R.id.mesesira);
        estrellas = (SimpleRatingBar) findViewById(R.id.iaa_rating_reser);

        biogra_but = findViewById(R.id.ira_biogra_anfi);
        nombre_anfi = findViewById( R.id.ira_nombre_anfi );
        foto_anfi = findViewById( R.id.ira_foto_anfi );

        vercalificacionesInfoRese = (Button)findViewById(R.id.ira_califi_aloj);
        comoLlegar = (Button)findViewById(R.id.ira_ruta_rese);
        calificar = (Button) findViewById(R.id.ira_calificar_aloj);
        calificar.setEnabled(false);
        calificar.setAlpha(0.0f);

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

        fechaActual = new GregorianCalendar();
        Date currentTime = new Date();
        fechaActual.setTime(currentTime);
        calend.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                String str;
                if (month == 12) {
                    str = new DateFormatSymbols().getMonths()[0];
                    String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                    irames.setText(String.format(Locale.getDefault(), "%d %s", year + 1, cap));
                } else {
                    str = new DateFormatSymbols().getMonths()[month];
                    String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                    irames.setText(String.format(Locale.getDefault(), "%d %s", year, cap));
                }
            }
        });
        calend.travelTo(new DateData(fechaActual.get(Calendar.YEAR), fechaActual.get(Calendar.MONTH),
                fechaActual.get(Calendar.DAY_OF_MONTH)));
        try {
            Date auxIniDate = new SimpleDateFormat("dd/MM/yyyy").parse(reserva.getFechaInicio());
            Calendar auxIni = new GregorianCalendar();
            auxIni.setTime(auxIniDate);

            Date auxFinDate = new SimpleDateFormat("dd/MM/yyyy").parse(reserva.getFechaFinal());
            Calendar auxFin = new GregorianCalendar();
            auxFin.setTime(auxFinDate);

            calend.markDate(new DateData(auxIni.get(Calendar.YEAR),
                    auxIni.get(Calendar.MONTH), auxIni.get(Calendar.DAY_OF_MONTH))
                    .setMarkStyle(MarkStyle.BACKGROUND, Color.rgb(213, 35, 54)));

            auxIni.add(Calendar.DAY_OF_MONTH, 1);
            while (auxIni.before(auxFin)) {
                calend.markDate(new DateData(auxIni.get(Calendar.YEAR),
                        auxIni.get(Calendar.MONTH), auxIni.get(Calendar.DAY_OF_MONTH))
                        .setMarkStyle(MarkStyle.BACKGROUND, Color.rgb(213, 35, 54)));
                auxIni.add(Calendar.DAY_OF_MONTH, 1);
            }


            calend.markDate(new DateData(auxFin.get(Calendar.YEAR),
                    auxFin.get(Calendar.MONTH), auxFin.get(Calendar.DAY_OF_MONTH))
                    .setMarkStyle(MarkStyle.BACKGROUND, Color.rgb(213, 35, 54)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        estrellas.setRating(alojamiento.getCalificacion());

        Date date2= null;
        try {
            date2 = new SimpleDateFormat("dd/MM/yyyy").parse(reserva.getFechaFinal());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date c = Calendar.getInstance().getTime();
        if (c.compareTo(date2) < 0)
        {
            calificar.setEnabled(true);
            calificar.setAlpha(1.0f);
        }

        calificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),CalificarActivity.class);
                Bundle paquete = new Bundle();
                paquete.putSerializable("alojamiento", alojamiento);
                paquete.putSerializable("reserva",reserva);
                intent.putExtras(paquete);
                startActivity(intent);
            }


        });

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
                Bundle paquete = new Bundle();
                paquete.putSerializable("alojamiento", alojamiento);
                intent.putExtras(paquete);
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
