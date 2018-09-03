package javeriana.edu.co.mockups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import javeriana.edu.co.mockups.mAdapterView.CustomAdapter;
import javeriana.edu.co.mockups.mData.ColeccionAlojamientos;

public class buscar_actividad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        ListView lv= (ListView) findViewById(R.id.lvBuscar);
        lv.setAdapter(new CustomAdapter(this, ColeccionAlojamientos.getAlojamiento()));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent lv_intent = new Intent( view.getContext(), InfoAlojaActivity.class );
                startActivity( lv_intent );
            }
        });

    }
}
