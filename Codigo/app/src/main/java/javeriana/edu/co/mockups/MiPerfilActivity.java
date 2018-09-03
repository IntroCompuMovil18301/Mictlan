package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MiPerfilActivity extends AppCompatActivity {
    ImageButton backToHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        backToHome = (ImageButton)findViewById(R.id.iBtn_BackPerfil);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Home.class);
                startActivity(intent);
            }
        });
    }
}
