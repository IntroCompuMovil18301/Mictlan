package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HistoricoAlojamientoActivity extends AppCompatActivity {
    Button bioAnfitrion;
    Button verCalificacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_alojamiento);

        bioAnfitrion = (Button)findViewById(R.id.haa_biogra_anfi);
        verCalificacion = (Button)findViewById(R.id.haa_califi_aloj);

        bioAnfitrion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),biografia_anfitrion.class);
                startActivity(intent);
            }
        });

        verCalificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),CalificacionesActivity.class);
                startActivity(intent);
            }
        });
    }
}
