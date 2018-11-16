package javeriana.edu.co.mockups;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javeriana.edu.co.mockups.mAdapterView.ImageAddAdapter;
import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.Usuario;

public class CrearAlojamientoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 392;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 758;
    private static final int REQUEST_CAMERA = 485;
    private static final int REQUEST_IMAGE_CAPTURE = 615;
    private static final int IMAGE_PICKER_REQUEST = 31;

    private static final String PATH_ALOJ = "alojamientos/";

    public static final double lowerLeftLatitude = 4.475113;
    public static final double lowerLeftLongitude = -74.216308;
    public static final double upperRightLatitude = 4.815938;
    public static final double upperRigthLongitude = -73.997955;

    private FirebaseDatabase database;
    private FirebaseStorage storage;

    Geocoder mGeocoder;

    private EditText nombreAlojamiento;
    private Spinner tipoAl;
    private EditText ubicacion;
    private EditText precio;
    private EditText personas;
    private EditText alcobas;
    private EditText camas;
    private EditText banos;
    private Button cargarImagen;
    private Button tomarFoto;
    private Button crear;
    private ListView lv_images;

    private List<String> imagesName;
    private List<Bitmap> images;
    private ImageAddAdapter adapter;

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

        List<String> spinnerArrayTipo = new ArrayList<String>();
        spinnerArrayTipo.add("Casa entera");
        spinnerArrayTipo.add("Habitación privada");
        spinnerArrayTipo.add("Habitación compartida");
        spinnerArrayTipo.add("Apartamento");
        spinnerArrayTipo.add("Finca");

        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, spinnerArrayTipo);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoAl = (Spinner)findViewById(R.id.spinner_TipoAl);
        tipoAl.setAdapter(adapterTipo);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        nombreAlojamiento = findViewById(R.id.eT_NombreAlojamiento);
        tipoAl = findViewById(R.id.spinner_TipoAl);
        ubicacion = findViewById(R.id.eT_Ubicacion);
        precio = findViewById(R.id.eT_Precio);
        personas = findViewById(R.id.spinner_Personas);
        alcobas = findViewById(R.id.spinner_Alcobas);
        camas = findViewById(R.id.spinner_Camas);
        banos = findViewById(R.id.spinner_Banos);

        cargarImagen = findViewById(R.id.cca_cargar_img);
        tomarFoto = findViewById(R.id.cca_tomar_foto);
        crear = findViewById(R.id.btn_CrearAloj);

        imagesName = new ArrayList<String>();
        images = new ArrayList<>();

        lv_images = findViewById(R.id.lv_images);

        final FirebaseUser Fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query myTopPostsQuery = database.getReference().child("usuarios").child(Fuser.getUid());
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario aux = dataSnapshot.getValue(Usuario.class);
                setUsuario(aux);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                    "Para seleccionar una imagen",
                    REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            updateCargarImagen();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermission(this, Manifest.permission.CAMERA,
                    "Para tomar una foto", REQUEST_CAMERA);
            requestPermission(this, Manifest.permission.CAMERA,
                    "Para tomar una foto", REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            updateTomarFoto();
        }

        mGeocoder = new Geocoder(getBaseContext());

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    String usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String titulo = nombreAlojamiento.getText().toString();
                    String ubi = ubicacion.getText().toString();
                    float valorNoche = Float.parseFloat(precio.getText().toString());
                    String tipo = tipoAl.getSelectedItem().toString();
                    int per = Integer.parseInt(personas.getText().toString());
                    int cam = Integer.parseInt(camas.getText().toString());
                    int alc = Integer.parseInt(alcobas.getText().toString());
                    int bno = Integer.parseInt(banos.getText().toString());

                    try {
                        List<Address> addresses = mGeocoder.getFromLocationName(
                                ubi, 2,
                                lowerLeftLatitude,
                                lowerLeftLongitude,
                                upperRightLatitude,
                                upperRigthLongitude);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address addressResult = addresses.get(0);
                            DatabaseReference crearAlojRef = database.getReference(PATH_ALOJ);
                            final Alojamiento aloja = new Alojamiento(usuario, crearAlojRef.push().getKey(), titulo, ubi, addressResult.getLatitude(), addressResult.getLongitude(), valorNoche, tipo, per,
                                    cam, alc, bno, imagesName);
                            crearAlojRef.child(aloja.getId()).setValue(aloja, new
                                    DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                Toast.makeText(CrearAlojamientoActivity.this,
                                                        "No se pudo crear el alojamiento", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(CrearAlojamientoActivity.this,
                                                        "El alojamiento ha sido creada", Toast.LENGTH_SHORT).show();
                                                StorageReference crearImagenes = storage.getReference(PATH_ALOJ).child(aloja.getId());
                                                for (int i = 0; i < images.size(); i++) {
                                                    Bitmap image = images.get(i);
                                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                    byte[] data = baos.toByteArray();

                                                    UploadTask uploadTask = crearImagenes.child(imagesName.get(i)).putBytes(data);
                                                }
                                            }
                                        }
                                    });
                            Intent crear_intent = new Intent(v.getContext(), Home.class);
                            startActivity(crear_intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setUsuario(Usuario usuario) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView navUsername = (TextView) navigationView.findViewById(R.id.NombreNavCrear);
        navUsername.setText(usuario.getNombre());
        if (usuario.getImagen()!= null) {
            ImageView navImage = (ImageView) navigationView.findViewById(R.id.iv_Cuenta);
            navImage.setImageURI(Uri.parse(usuario.getImagen()));
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
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(CrearAlojamientoActivity.this, MainActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void requestPermission(Activity context, String permission, String explanation, int requestId ){
        if (ContextCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,permission)) {
                Toast.makeText(context, explanation, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestId);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateCargarImagen();
                }
                break;
            }

            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateTomarFoto();
                }
                break;
            }

            case REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateTomarFoto();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_PICKER_REQUEST: {
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri image_uri = data.getData();
                        if (image_uri != null) {
                            String aux = image_uri.toString();
                            imagesName.add(aux.substring(aux.lastIndexOf("/") + 1));
                            final InputStream imagenStream =
                                    getContentResolver().openInputStream(image_uri);
                            images.add(BitmapFactory.decodeStream(imagenStream));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case REQUEST_IMAGE_CAPTURE: {
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        images.add((Bitmap) extras.get("data"));
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + ".jpg";
                        imagesName.add(imageFileName);
                    }
                }
                break;
            }
        }
        adapter = new ImageAddAdapter(imagesName, this);
        lv_images.setAdapter(adapter);
    }

    private void updateCargarImagen() {
        cargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cargarImagen_intent = new Intent(Intent.ACTION_PICK);
                cargarImagen_intent.setType("image/*");
                startActivityForResult(cargarImagen_intent, IMAGE_PICKER_REQUEST);
            }
        });
    }

    private void updateTomarFoto() {
        tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tomarFoto_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (tomarFoto_intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(tomarFoto_intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String titulo = nombreAlojamiento.getText().toString();
        String ubi = ubicacion.getText().toString();
        String valorNoche = precio.getText().toString();
        String per = personas.getText().toString();
        String alc = alcobas.getText().toString();
        String cam = camas.getText().toString();
        String bno = banos.getText().toString();

        if (TextUtils.isEmpty(titulo)) {
            nombreAlojamiento.setError("Required.");
            valid =false;
        } else {
            nombreAlojamiento.setError(null);
        }

        if (TextUtils.isEmpty(ubi)) {
            ubicacion.setError("Required.");
            valid = false;
        } else {
            ubicacion.setError(null);
        }

        if (TextUtils.isEmpty(valorNoche)) {
            precio.setError("Required.");
            valid = false;
        } else {
            precio.setError(null);
        }

        if (TextUtils.isEmpty(per)) {
            personas.setError("Required.");
            valid = false;
        } else {
            personas.setError(null);
        }

        if (TextUtils.isEmpty(cam)) {
            camas.setError("Required.");
            valid = false;
        } else {
            camas.setError(null);
        }

        if (TextUtils.isEmpty(alc)) {
            alcobas.setError("Required.");
            valid = false;
        } else {
            alcobas.setError(null);
        }

        if (TextUtils.isEmpty(bno)) {
            banos.setError("Required.");
            valid = false;
        } else {
            banos.setError(null);
        }

        if (imagesName.size() < 4) {
            Toast.makeText(this,"Necesitas " + (4 - imagesName.size())+ " imagenes",
                    Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }
}
