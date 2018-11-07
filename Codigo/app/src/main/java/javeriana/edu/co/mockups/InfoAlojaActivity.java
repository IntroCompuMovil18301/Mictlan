package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoAlojaActivity extends AppCompatActivity {

    private Button biogra_but;
    private Button califi_but;
    private Button reserv_but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_aloja);

        biogra_but = findViewById( R.id.iaa_biogra_anfi );
        califi_but = findViewById( R.id.iaa_califi_aloj );
        reserv_but = findViewById( R.id.iaa_reserv_aloj );

        biogra_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent biogra_intent = new Intent( view.getContext(), BiografiaAnfitrionActivity.class );
                startActivity( biogra_intent );
            }
        });

        califi_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent califi_intent = new Intent(view.getContext(), CalificacionesActivity.class );
                startActivity(califi_intent);
            }
        });

        reserv_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reserv_intent = new Intent( view.getContext(), FechaReservaActivity.class );
                startActivity( reserv_intent );
            }
        });
    }
}
