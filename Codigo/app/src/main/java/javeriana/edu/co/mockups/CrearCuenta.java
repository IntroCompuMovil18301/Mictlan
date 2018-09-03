package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CrearCuenta extends AppCompatActivity {
    Button crear;
    ImageButton backToMain;
    TextView nacionalidad;
    EditText resnacionalidad;
    RadioButton opAnfitrion;
    RadioButton opHuesped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        crear=(Button)findViewById(R.id.btn_Crear);
        backToMain = (ImageButton)findViewById(R.id.iBtn_Back);
        nacionalidad = (TextView)findViewById(R.id.tV_Nacionalidad);
        resnacionalidad = (EditText)findViewById(R.id.eT_Nacionalidad);
        opAnfitrion = (RadioButton)findViewById(R.id.radioButton_Anfitrion);
        opHuesped = (RadioButton)findViewById(R.id.radioButton_Huesped);

        nacionalidad.setAlpha(0.0f);
        resnacionalidad.setAlpha(0.0f);

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Femenino");
        spinnerArray.add("Masculino");
        spinnerArray.add("Prefiero no responder");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner_Genero);
        sItems.setAdapter(adapter);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Home.class);
                startActivity(intent);
            }
        });

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        opHuesped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nacionalidad.setAlpha(1.0f);
                resnacionalidad.setAlpha(1.0f);
            }
        });

    }
}
