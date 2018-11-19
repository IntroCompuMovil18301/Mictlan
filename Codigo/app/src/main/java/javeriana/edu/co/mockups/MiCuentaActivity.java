package javeriana.edu.co.mockups;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javeriana.edu.co.mockups.mData.Usuario;

import static java.lang.Thread.sleep;

public class MiCuentaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;


    private CardView card1;
    private ImageView IOp1;
    private TextView TOp1;

    private CardView card2;
    private ImageView IOp2;
    private TextView TOp2;

    private CardView card3;
    private ImageView IOp3;
    private TextView TOp3;


    private String tipo;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        mAuth = FirebaseAuth.getInstance();


        card1 = (CardView) findViewById(R.id.card1);
        IOp1 = (ImageView) findViewById(R.id.imageOP1);
        TOp1 = (TextView) findViewById(R.id.textOP1);

        card2 = (CardView) findViewById(R.id.card2);
        IOp2 = (ImageView) findViewById(R.id.imageOP2);
        TOp2 = (TextView) findViewById(R.id.textOP2);

        card3 = (CardView) findViewById(R.id.card3);
        IOp3 = (ImageView) findViewById(R.id.imageOP3);
        TOp3 = (TextView) findViewById(R.id.textOP3);


        final FirebaseUser Fuser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("-------", "onDataChange: "+ Fuser.getEmail());
        Query myTopPostsQuery = mDatabase.child("usuarios").child(Fuser.getUid());
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario aux = dataSnapshot.getValue(Usuario.class);
                Log.d("-------", "onDataChange: "+ aux.getTipo());
                tipo = aux.getTipo();

                if (tipo.equals("Huesped")){
                    IOp1.setImageResource(R.drawable.round_seach_w);
                    TOp1.setText("Consultar Alojamiento");

                    IOp2.setImageResource(R.drawable.twotone_map_24);
                    TOp2.setText("Alojamientos en la ruta");

                    IOp3.setImageResource(R.drawable.baseline_event_note_24);
                    TOp3.setText("Reservas");


                } else {
                    IOp1.setImageResource(R.drawable.baseline_create_24);
                    TOp1.setText("Crear Alojamiento");

                    IOp2.setImageResource(R.drawable.baseline_format_list_numbered_24);
                    TOp2.setText("Mis Alojamientos");

                    IOp3.setImageResource(R.drawable.baseline_event_available_24);
                    TOp3.setText("Reservas");
                }
                setUsuario(aux,Fuser.getUid());
                setTipo(tipo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView navUsername = (TextView) navigationView.findViewById(R.id.NombreNavHome);


    }

    public void setTipo(String tipo) {
         final String auxTipo=tipo;
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auxTipo.equals("Huesped")){
                    Intent consultar_intent = new Intent(view.getContext(), BuscarAlojamientoActivity.class);
                    startActivity(consultar_intent);
                }else {
                    Intent intent = new Intent(view.getContext(),CrearAlojamientoActivity.class);
                    startActivity(intent);
                }

            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auxTipo.equals("Huesped")){
                    Intent rutas_intent = new Intent(view.getContext(), RutasUsuarioActivity.class);
                    startActivity(rutas_intent);
                }else {
                    Intent misAloj_intent = new Intent(view.getContext(), AnfitrionAlojamientosActivity.class);
                    startActivity(misAloj_intent);
                }
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auxTipo.equals("Huesped")){
                    Intent reservas_intent = new Intent(view.getContext(), ReservasUsuariosActivity.class);
                    startActivity(reservas_intent);
                }else {
                    Intent alojaReser_intent = new Intent(view.getContext(), ReservasAnfitrionActivity.class);
                    startActivity(alojaReser_intent);
                }
            }
        });
    }

    public void setUsuario(Usuario usuario,String id) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView navUsername = (TextView) navigationView.findViewById(R.id.NombreNavCuenta);
        navUsername.setText(usuario.getNombre());
        if (usuario.getImagen()!= null) {

            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final ImageView navImage = (ImageView) navigationView.findViewById(R.id.iv_Cuenta);
            final File image = new File(getBaseContext().getExternalFilesDir(null),
                    usuario.getImagen());
            if(!image.exists()) {
                Log.d("user-->", "setUsuario: "+id);

                FirebaseStorage.getInstance().getReference("usuarios")
                        .child(id).child(usuario.getImagen()).getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(new FileInputStream(image), null, options);
                            navImage.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(image), null, options);
                    navImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            /*

            navImage.setImageURI(Uri.parse(usuario.getImagen()));
            */
        }
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
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MiCuentaActivity.this, MainActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
