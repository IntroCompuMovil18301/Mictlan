package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import javeriana.edu.co.mockups.mAdapterView.CustomAdapter;
import javeriana.edu.co.mockups.mData.ColeccionAlojamientos;

public class rutas_usuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas_usuario);

        ListView lv= (ListView) findViewById(R.id.lv6);
        lv.setAdapter(new CustomAdapter(this, ColeccionAlojamientos.getAlojamiento()));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent lv_intent = new Intent( view.getContext(), FinalizarActivity.class );
                startActivity( lv_intent );
            }
        });
    }
}
