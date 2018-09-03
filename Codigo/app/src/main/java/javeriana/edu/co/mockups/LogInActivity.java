package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class LogInActivity extends AppCompatActivity {

    private Button inicio;
    private Button ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        inicio = findViewById( R.id.lia_inicio_but );
        ingresar = findViewById( R.id.lia_ingresar_but );

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inicio_intent = new Intent( view.getContext(), MainActivity.class );
                startActivity( inicio_intent );
            }
        });

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ingresar_intent = new Intent( view.getContext(), Home.class );
                startActivity( ingresar_intent );
            }
        });
    }
}
