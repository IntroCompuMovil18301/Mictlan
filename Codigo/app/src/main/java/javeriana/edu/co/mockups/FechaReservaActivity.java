package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FechaReservaActivity extends AppCompatActivity {

    private Button finali_but;
    private Button crerut_but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha_reserva);

        finali_but = findViewById( R.id.fra_finali_rese );
        crerut_but = findViewById( R.id.fra_crerut_rese );

        finali_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finali_intent = new Intent( view.getContext(), Home.class );
                startActivity( finali_intent );
            }
        });

        crerut_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent crerut_intent = new Intent( view.getContext(), RutaActivity.class );
                startActivity(crerut_intent);
            }
        });
    }
}
