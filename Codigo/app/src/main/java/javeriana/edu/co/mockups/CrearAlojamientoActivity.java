package javeriana.edu.co.mockups;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class CrearAlojamientoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_alojamiento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        List<String> spinnerArrayNum =  new ArrayList<String>();
        spinnerArrayNum.add("1");
        spinnerArrayNum.add("2");
        spinnerArrayNum.add("3");
        spinnerArrayNum.add("4");
        spinnerArrayNum.add("5");
        spinnerArrayNum.add("6");
        spinnerArrayNum.add("7");
        spinnerArrayNum.add("8");
        spinnerArrayNum.add("9");
        spinnerArrayNum.add("10");
        spinnerArrayNum.add("+ 10");
        ArrayAdapter<String> adapterNum = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArrayNum);
        adapterNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sPersonas = (Spinner) findViewById(R.id.spinner_Personas);
        Spinner sCamas = (Spinner)findViewById(R.id.spinner_Camas);
        Spinner sHabitaciones = (Spinner)findViewById(R.id.spinner_Alcobas);
        Spinner sBanos = (Spinner)findViewById(R.id.spinner_Banos);

        sPersonas.setAdapter(adapterNum);
        sCamas.setAdapter(adapterNum);
        sHabitaciones.setAdapter(adapterNum);
        sBanos.setAdapter(adapterNum);

        List<String> spinnerArrayTipo = new ArrayList<String>();
        spinnerArrayTipo.add("Casa entera");
        spinnerArrayTipo.add("Habitación privada");
        spinnerArrayTipo.add("Habitación compartida");
        spinnerArrayTipo.add("Apartamento");
        spinnerArrayTipo.add("Finca");

        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, spinnerArrayTipo);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sTipo = (Spinner)findViewById(R.id.spinner_TipoAl);
        sTipo.setAdapter(adapterTipo);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.crear_alojamiento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_opciones) {
            Intent intent = new Intent(this.getBaseContext(),MiCuentaActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Home) {
            Intent intent = new Intent(this.getBaseContext(),Home.class);
            startActivity(intent);

        }  else if (id == R.id.nav_misDatos) {
            Intent intent = new Intent(this.getBaseContext(),MiPerfilActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_Salir) {
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
