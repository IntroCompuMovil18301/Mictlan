package javeriana.edu.co.mockups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import javeriana.edu.co.mockups.mAdapterView.CustomAdapter;
import javeriana.edu.co.mockups.mData.ColeccionAlojamientos;

public class AnfitrionAlojamientosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anfitrion_alojamientos);

        ListView lv= (ListView) findViewById(R.id.lv1);
        lv.setAdapter(new CustomAdapter(this, ColeccionAlojamientos.getAlojamiento()));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent lv_intent = new Intent( view.getContext(), HistoricoAlojamientoActivity.class );
                startActivity( lv_intent );
            }
        });
    }
}
