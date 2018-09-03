package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InfoReseActivity extends AppCompatActivity {
    Button bioInfoReseAnfitrion;
    Button vercalificacionesInfoRese;
    Button comoLlegar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_rese);

        bioInfoReseAnfitrion = (Button) findViewById(R.id.ira_biogra_anfi);
        vercalificacionesInfoRese = (Button)findViewById(R.id.ira_califi_aloj);
        comoLlegar = (Button)findViewById(R.id.ira_ruta_rese);

        bioInfoReseAnfitrion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),biografia_anfitrion.class);
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
                Intent intent = new Intent(view.getContext(),FinalizarActivity.class);
                startActivity(intent);
            }
        });

    }
}
