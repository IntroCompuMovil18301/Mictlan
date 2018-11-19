package javeriana.edu.co.mockups;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;

import javeriana.edu.co.mockups.mAdapterView.CustomAdapter;
import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.Usuario;

import static java.lang.Thread.sleep;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String PATH_ALOJ = "alojamientos/";
    private static final String LEER_TAG = "LeerActivity";

    private FloatingActionButton hospedarse;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FirebaseDatabase database;

    private ListView lv;

    private ArrayList<Alojamiento> aloj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        aloj = new ArrayList<Alojamiento>();

        hospedarse = findViewById(R.id.botonHospedarse);
        final FirebaseUser Fuser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("usuarios").child(Fuser.getUid());
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario aux = dataSnapshot.getValue(Usuario.class);
                setUsuario(aux,Fuser.getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference leerRef = database.getReference(PATH_ALOJ);
        leerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                    Alojamiento aux = singleSnap.getValue(Alojamiento.class);
                    System.out.println("Home: " +  aux);
                    aloj.add(aux);
                }
                updateListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(LEER_TAG, "error en la consulta", databaseError.toException());
            }
        });

        lv= (ListView) findViewById(R.id.lvHome);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle alojamiento = new Bundle();
                alojamiento.putSerializable("alojamiento", aloj.get(i));
                Intent lv_intent = new Intent( view.getContext(), InfoAlojaActivity.class );
                lv_intent.putExtras(alojamiento);
                startActivity( lv_intent );
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


        hospedarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),BuscarAlojamientoActivity.class);
                startActivity(intent);
        }
        });

    }

    private void updateListView() {
        lv.setAdapter(new CustomAdapter(this,aloj));
    }

    public void setUsuario(Usuario usuario,String id) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView navUsername = (TextView) navigationView.findViewById(R.id.NombreNavHome);
        navUsername.setText(usuario.getNombre());
        if (usuario.getImagen()!= null) {

            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final ImageView navImage = (ImageView) navigationView.findViewById(R.id.iv_home);
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
        getMenuInflater().inflate(R.menu.home, menu);
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
            startActivity(new Intent(Home.this, MainActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
