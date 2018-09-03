package javeriana.edu.co.mockups;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import javeriana.edu.co.mockups.mAdapterView.CustomAdapter;
import javeriana.edu.co.mockups.mData.ColeccionAlojamientos;

public class historial_usuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_usuario);

        ListView lv= (ListView) findViewById(R.id.lvHistorial);
        lv.setAdapter(new CustomAdapter(this, ColeccionAlojamientos.getAlojamiento()));

    }
}
