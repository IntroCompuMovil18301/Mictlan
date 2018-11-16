package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ComoLlegarActivity extends AppCompatActivity {
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_como_llegar);

        ok = (Button)findViewById(R.id.btn_OkFinalizar);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Home.class);
                startActivity(intent);
            }
        });
    }
}
