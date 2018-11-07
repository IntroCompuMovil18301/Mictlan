package javeriana.edu.co.mockups;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

public class MiCuentaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton crearAloj;
    private ImageButton consultarAloj;
    private ImageButton rutas;
    private ImageButton reservas;
    private ImageButton misAlojamientos;
    private ImageButton alojamientosReser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        crearAloj =(ImageButton)findViewById(R.id.iBtn_CrearAlojamiento);
        consultarAloj = findViewById( R.id.iBtn_Consultar );
        rutas = findViewById( R.id.iBtn_Rutas );
        reservas = findViewById( R.id.iBtn_Reservas);
        misAlojamientos = findViewById( R.id.iBtn_MisAlojamientos );
        alojamientosReser = findViewById( R.id.iBtn_Reservados );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        crearAloj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),CrearAlojamientoActivity.class);
                startActivity(intent);
            }
        });

        consultarAloj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent consultar_intent = new Intent(view.getContext(), BuscarAlojamientoActivity.class);
                startActivity(consultar_intent);
            }
        });

        rutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rutas_intent = new Intent(view.getContext(), RutasUsuarioActivity.class);
                startActivity(rutas_intent);
            }
        });

        reservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reservas_intent = new Intent(view.getContext(), ReservasUsuariosActivity.class);
                startActivity(reservas_intent);
            }
        });

        misAlojamientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent misAloj_intent = new Intent(view.getContext(), AnfitrionAlojamientosActivity.class);
                startActivity(misAloj_intent);
            }
        });

        alojamientosReser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alojaReser_intent = new Intent(view.getContext(), ReservasAnfitrionActivity.class);
                startActivity(alojaReser_intent);
            }
        });
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
        getMenuInflater().inflate(R.menu.mi_cuenta, menu);
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
