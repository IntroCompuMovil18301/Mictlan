package javeriana.edu.co.mockups;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.Reserva;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;

public class FechaReservaActivity extends AppCompatActivity {

    private static final String PATH_ALOJ = "alojamientos/";
    private static final String PATH_RESE = "reservas/";

    private TextView calTit;
    private ExpCalendarView calend;
    private TextView fecIni;
    private TextView fecFin;
    private Button finali_but;
    private Button crerut_but;
    private TextView totalRese;
    private short ini;

    private Alojamiento alojamiento;
    private Calendar fechaActual;
    private Calendar fechaIni;
    private String fechaIniS;
    private Calendar fechaFin;
    private String fechaFinS;
    private float precio;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha_reserva);

        alojamiento = (Alojamiento) getIntent().getSerializableExtra("alojamiento");

        calTit = findViewById(R.id.fra_caltit);
        calend = findViewById(R.id.fra_calend);
        fecIni = findViewById(R.id.fra_fecini);
        fecFin = findViewById(R.id.fra_fecfin);
        finali_but = findViewById(R.id.fra_finali_rese);
        crerut_but = findViewById(R.id.fra_crerut_rese);
        totalRese = findViewById(R.id.fra_total_rese);
        ini = 0;

        database = FirebaseDatabase.getInstance();

        calend.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                String str;
                if (month == 12) {
                    str = new DateFormatSymbols().getMonths()[0];
                    String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                    calTit.setText(String.format(Locale.getDefault(), "%d %s", year + 1, cap));
                } else {
                    str = new DateFormatSymbols().getMonths()[month];
                    String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                    calTit.setText(String.format(Locale.getDefault(), "%d %s", year, cap));
                }
            }
        });
        fechaActual = new GregorianCalendar();
        Date currentTime = new Date();
        fechaActual.setTime(currentTime);
        calend.travelTo(new DateData(fechaActual.get(Calendar.YEAR), fechaActual.get(Calendar.MONTH),
                fechaActual.get(Calendar.DAY_OF_MONTH)));
        if (alojamiento.getReservas() != null) {
            for (final String reserva : alojamiento.getReservas()) {
                DatabaseReference reservaRef = database.getReference(PATH_RESE).child(reserva);
                reservaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Reserva aux = dataSnapshot.getValue(Reserva.class);
                        try {
                            Date auxIniDate = new SimpleDateFormat("dd/MM/yyyy").parse(aux.getFechaInicio());
                            Calendar auxIni = new GregorianCalendar();
                            auxIni.setTime(auxIniDate);

                            Date auxFinDate = new SimpleDateFormat("dd/MM/yyyy").parse(aux.getFechaFinal());
                            Calendar auxFin = new GregorianCalendar();
                            auxFin.setTime(auxFinDate);

                            calend.markDate(new DateData(auxIni.get(Calendar.YEAR),
                                    auxIni.get(Calendar.MONTH), auxIni.get(Calendar.DAY_OF_MONTH))
                                    .setMarkStyle(MarkStyle.BACKGROUND, Color.RED));

                            auxIni.add(Calendar.DAY_OF_MONTH, 1);
                            while (auxIni.before(auxFin)) {
                                calend.markDate(new DateData(auxIni.get(Calendar.YEAR),
                                        auxIni.get(Calendar.MONTH), auxIni.get(Calendar.DAY_OF_MONTH))
                                        .setMarkStyle(MarkStyle.BACKGROUND, Color.RED));
                                auxIni.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            calend.markDate(new DateData(auxFin.get(Calendar.YEAR),
                                    auxFin.get(Calendar.MONTH), auxFin.get(Calendar.DAY_OF_MONTH))
                                    .setMarkStyle(MarkStyle.BACKGROUND, Color.RED));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        calend.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                if (ini == 0 && fechaValida(date)) {
                    if (date.getMonth() == 12)
                        fechaIni = new GregorianCalendar(date.getYear() + 1, 0, date.getDay());
                    else
                        fechaIni = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDay());
                    fechaIniS = String.format(Locale.getDefault(), "%d/%d/%d",
                            fechaIni.get(Calendar.DAY_OF_MONTH), fechaIni.get(Calendar.MONTH) + 1,
                            fechaIni.get(Calendar.YEAR));
                    fecIni.setText(String.format("%s\n%s", "Fecha Inicio", fechaIniS));
                    precio = alojamiento.getValorNoche();
                    totalRese.setText(String.format(Locale.getDefault(), "Total: $%.2f", precio));
                    ini = 1;
                    calend.markDate(new DateData(fechaIni.get(Calendar.YEAR),
                            fechaIni.get(Calendar.MONTH), fechaIni.get(Calendar.DAY_OF_MONTH))
                            .setMarkStyle(MarkStyle.BACKGROUND, Color.parseColor("#539ed3")));
                } else if (ini == 1 && fechaValida(date)) {
                    if (date.getMonth() == 12)
                        fechaFin = new GregorianCalendar(date.getYear() + 1, 0, date.getDay());
                    else
                        fechaFin = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDay());
                    if (fechaIni.before(fechaFin) && fechaFinValida()) {
                        fechaFinS = String.format(Locale.getDefault(), "%d/%d/%d",
                                fechaFin.get(Calendar.DAY_OF_MONTH),
                                fechaFin.get(Calendar.MONTH) + 1, fechaFin.get(Calendar.YEAR));
                        fecFin.setText(String.format("%s\n%s", "Fecha Fin", fechaFinS));

                        Calendar aux = (Calendar) fechaIni.clone();
                        aux.add(Calendar.DAY_OF_MONTH, 1);
                        while (aux.before(fechaFin)) {
                            calend.markDate(new DateData(aux.get(Calendar.YEAR),
                                    aux.get(Calendar.MONTH), aux.get(Calendar.DAY_OF_MONTH))
                                    .setMarkStyle(MarkStyle.BACKGROUND, Color.parseColor("#539ed3")));
                            aux.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        calend.markDate(new DateData(fechaFin.get(Calendar.YEAR),
                                fechaFin.get(Calendar.MONTH), fechaFin.get(Calendar.DAY_OF_MONTH))
                                .setMarkStyle(MarkStyle.BACKGROUND, Color.parseColor("#539ed3")));
                        precio = alojamiento.getValorNoche() *
                                (fechaFin.get(Calendar.DAY_OF_YEAR) - fechaIni.get(Calendar.DAY_OF_YEAR));
                        totalRese.setText(String.format(Locale.getDefault(), "Total: $%.2f", precio));
                        ini = 2;
                    } else {
                        fechaFin = null;
                        Toast.makeText(FechaReservaActivity.this,
                                "Hay reservas en el intervalo del inicio hasta el final", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        fecIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ini = 0;
                fecIni.setText("Fecha Inicio");
                fecFin.setText("Fecha Fin");
                if (fechaIni != null)
                    calend.unMarkDate(new DateData(fechaIni.get(Calendar.YEAR),
                            fechaIni.get(Calendar.MONTH), fechaIni.get(Calendar.DAY_OF_MONTH)));
                if (fechaIni != null && fechaFin != null) {
                    Calendar aux = (Calendar) fechaIni.clone();
                    aux.add(Calendar.DAY_OF_MONTH, 1);
                    while (aux.before(fechaFin)) {
                        calend.unMarkDate(new DateData(aux.get(Calendar.YEAR),
                                aux.get(Calendar.MONTH), aux.get(Calendar.DAY_OF_MONTH)));
                        aux.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    calend.unMarkDate(new DateData(fechaFin.get(Calendar.YEAR),
                            fechaFin.get(Calendar.MONTH), fechaFin.get(Calendar.DAY_OF_MONTH)));
                }
                Toast.makeText(FechaReservaActivity.this,
                        "Seleccione la fecha de inicio", Toast.LENGTH_SHORT).show();
            }
        });

        fecFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ini != 0) {
                    ini = 1;
                    fecFin.setText("Fecha Fin");
                    if (fechaFin != null) {
                        Calendar aux = (Calendar) fechaIni.clone();
                        aux.add(Calendar.DAY_OF_MONTH, 1);
                        while (aux.before(fechaFin)) {
                            calend.unMarkDate(new DateData(aux.get(Calendar.YEAR),
                                    aux.get(Calendar.MONTH), aux.get(Calendar.DAY_OF_MONTH)));
                            aux.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        calend.unMarkDate(new DateData(fechaFin.get(Calendar.YEAR),
                                fechaFin.get(Calendar.MONTH), fechaFin.get(Calendar.DAY_OF_MONTH)));
                    }
                    Toast.makeText(FechaReservaActivity.this,
                            "Seleccione la fecha final", Toast.LENGTH_SHORT).show();
                }
            }
        });

        finali_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (0 < ini) {
                    DatabaseReference crearReseRef = database.getReference(PATH_RESE);
                    final DatabaseReference actualizarAloja = database.getReference(PATH_ALOJ);
                    String usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final Reserva reserva;
                    if (fechaFin == null)
                        reserva = new Reserva(crearReseRef.push().getKey(), usuario,
                                alojamiento.getId(), fechaIniS, precio);
                    else
                        reserva = new Reserva(crearReseRef.push().getKey(), usuario,
                                alojamiento.getId(), fechaIniS, fechaFinS, precio);
                    crearReseRef.child(reserva.getId()).setValue(reserva,
                            new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError,
                                                       @NonNull DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Toast.makeText(FechaReservaActivity.this,
                                                "No se pudo crear la reserva", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(FechaReservaActivity.this,
                                                "La reserva ha sido creada", Toast.LENGTH_SHORT).show();
                                        alojamiento.addReserva(reserva.getId());
                                        actualizarAloja.child(alojamiento.getId()).updateChildren(alojamiento.toMap());
                                    }

                                }
                            });
                    Intent finali_intent = new Intent(view.getContext(), Home.class);
                    startActivity(finali_intent);
                } else {
                    Toast.makeText(FechaReservaActivity.this,
                            "No se ha seleccionado una fecha para la reserva", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        crerut_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent crerut_intent = new Intent(view.getContext(), RutaActivity.class);
                startActivity(crerut_intent);
            }
        });
    }

    private boolean fechaValida(DateData date) {
        Calendar aux = date.getMonth() == 12 ? new GregorianCalendar(date.getYear() + 1,
                0, date.getDay()) : new GregorianCalendar(date.getYear(), date.getMonth(), date.getDay());
        return calend.getMarkedDates().getAll().indexOf(date) == -1 && fechaActual.before(aux);
    }

    private boolean fechaFinValida() {
        boolean valido = true;
        Calendar aux = (Calendar) fechaIni.clone();

        aux.add(Calendar.DAY_OF_MONTH, 1);
        while (aux.before(fechaFin)) {
            DateData fecha = new DateData(aux.get(Calendar.YEAR), aux.get(Calendar.MONTH),
                    aux.get(Calendar.DAY_OF_MONTH));
            if (calend.getMarkedDates().getAll().indexOf(fecha) != -1)
                valido = false;
            aux.add(Calendar.DAY_OF_MONTH, 1);
        }

        return valido;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ArrayList<DateData> fechas = (ArrayList<DateData>) calend.getMarkedDates().getAll().clone();
        for (DateData date : fechas)
            calend.unMarkDate(date);
    }
}
