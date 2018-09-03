package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class RutaActivity extends AppCompatActivity {

    private Button ok_but;
    private Button cancelar_but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        List<String> spinnerArrayAlojamientos =  new ArrayList<String>();
        spinnerArrayAlojamientos.add("Cabaña la alegria");
        spinnerArrayAlojamientos.add("Casita bonita en Alderwood Ave");
        spinnerArrayAlojamientos.add("Cabaña Moderna");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArrayAlojamientos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner_AlojamientosRuta);
        sItems.setAdapter(adapter);

        ok_but = findViewById( R.id.btn_OkRuta );
        cancelar_but = findViewById( R.id.btn_CancelarRuta );

        ok_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ok_intent = new Intent( view.getContext(), Home.class );
                startActivity( ok_intent );
            }
        });

        cancelar_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelar_intent = new Intent( view.getContext(), Home.class );
                startActivity( cancelar_intent );
            }
        });
    }
}
