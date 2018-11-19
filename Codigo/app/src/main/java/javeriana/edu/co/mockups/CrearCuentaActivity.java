package javeriana.edu.co.mockups;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javeriana.edu.co.mockups.mData.Usuario;

public class CrearCuentaActivity extends AppCompatActivity {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 392;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 758;
    private static final int REQUEST_CAMERA = 485;
    private static final int REQUEST_IMAGE_CAPTURE = 615;
    private static final int IMAGE_PICKER_REQUEST = 31;
    private static final String TAG = "CREAR_CUENTA_ACTIVITY";

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private ImageView imagenP;
    Button crear;
    ImageButton backToMain;
    TextView nacionalidad;
    Spinner resnacionalidad;
    RadioButton opAnfitrion;
    RadioButton opHuesped;
    EditText correo;
    EditText contraseña;
    ImageView tomarFoto;
    EditText nombreCuenta;
    EditText edad;
    RadioGroup tipoGroup;
    RadioButton tipo;
    Spinner sItems;
    TextView genero;

    ImageView agImagen;
    private String imageName;
    private Bitmap imagen;

    List<String> nationalities =  new ArrayList<String>();


    private boolean huesped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        crear=(Button)findViewById(R.id.btn_Crear);
        backToMain = (ImageButton)findViewById(R.id.iBtn_Back);
        nacionalidad = (TextView)findViewById(R.id.tV_Nacionalidad);
        resnacionalidad = (Spinner) findViewById(R.id.spinner_nationality);
        opAnfitrion = (RadioButton)findViewById(R.id.radioButton_Anfitrion);
        opHuesped = (RadioButton)findViewById(R.id.radioButton_Huesped);
        correo = (EditText) findViewById(R.id.eT_Correo);
        contraseña = (EditText) findViewById(R.id.acc_contra);
        nombreCuenta = (EditText) findViewById(R.id.eT_NombreCuenta);
        edad = (EditText) findViewById(R.id.eT_Edad);
        tipoGroup = (RadioGroup) findViewById(R.id.radioGroup);
        genero = (TextView) findViewById(R.id.tV_Genero);
        agImagen = (ImageView) findViewById(R.id.iV_FotoCuenta);
        imagenP = (ImageView) findViewById(R.id.tomarfot);




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

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Femenino");
        spinnerArray.add("Masculino");
        spinnerArray.add("otro");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner) findViewById(R.id.spinner_Genero);
        sItems.setAdapter(adapter);
        nacionalidad.setAlpha(0.0f);
        resnacionalidad.setAlpha(0.0f);
        sItems.setAlpha(0.0f);
        genero.setAlpha(0.0f);
        huesped = false;

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    Toast.makeText(CrearCuentaActivity.this, "Validando información...", Toast.LENGTH_LONG).show();

                    mAuth.createUserWithEmailAndPassword(correo.getText().toString(), contraseña.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        int selectedId = tipoGroup.getCheckedRadioButtonId();
                                        tipo = (RadioButton) findViewById(selectedId);
                                        Usuario usuario;
                                        sendRegistrationLink();
                                        if (tipo.getText().toString().equals("Huesped")) {
                                            Log.i("-------", "onComplete: "+resnacionalidad.getSelectedItem().toString());
                                            Log.i("-------", "onComplete: "+sItems.getSelectedItem().toString());
                                            usuario = new Usuario(nombreCuenta.getText().toString(), Integer.parseInt(edad.getText().toString()),resnacionalidad.getSelectedItem().toString(),sItems.getSelectedItem().toString(),imageName,tipo.getText().toString());
                                        }
                                        else {
                                            usuario = new Usuario(nombreCuenta.getText().toString(), Integer.parseInt(edad.getText().toString()),null,null,imageName,tipo.getText().toString());
                                        }
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("usuarios");
                                        myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(CrearCuentaActivity.this, "Usuario creado", Toast.LENGTH_LONG).show();


                                                    StorageReference crearImagenes = storage.getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        Bitmap image = imagen;
                                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                        byte[] data = baos.toByteArray();

                                                        UploadTask uploadTask = crearImagenes.child(imageName).putBytes(data);
                                                        startActivity(new Intent(CrearCuentaActivity.this, MainActivity.class));
                                                        mAuth.signOut();

                                                } else {
                                                    Toast.makeText(CrearCuentaActivity.this,"Error creando usuario",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });


                }
            }
        });

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        opHuesped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nacionalidad.setAlpha(1.0f);
                resnacionalidad.setAlpha(1.0f);
                sItems.setAlpha(1.0f);
                genero.setAlpha(1.0f);
                huesped = true;
            }
        });

        opAnfitrion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nacionalidad.setAlpha(0.0f);
                resnacionalidad.setAlpha(0.0f);
                sItems.setAlpha(0.0f);
                genero.setAlpha(0.0f);
                huesped = false;
            }
        });



        nationalities.add("Seleccione nacionalidad");
        RESTcountries();
        ArrayAdapter<String> adapterNat = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, nationalities);
        resnacionalidad.setAdapter(adapterNat);

    }

    private void RESTcountries () {
        RequestQueue queue = Volley.newRequestQueue(this);
        String query = "https://restcountries.eu/rest/v2/all?fields=name";
        StringRequest req = new StringRequest(Request.Method.GET, query, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String data = (String) response;
                try {
                    JSONArray nacionalities = new JSONArray(data);
                    for (int i = 0; i < nacionalities.length(); i++) {
                        JSONObject jo = (JSONObject) nacionalities.get(i);
                        String n = (String) jo.get("name");
                        nationalities.add(n);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG", "Error handling rest invocation"+error.getCause());
                    }
                });
        queue.add(req);

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
                            imageName = aux.substring(aux.lastIndexOf("/") + 1);
                            agImagen.setImageURI(image_uri);
                            final InputStream imagenStream =
                                    getContentResolver().openInputStream(image_uri);
                            imagen = BitmapFactory.decodeStream(imagenStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }break;
            case REQUEST_IMAGE_CAPTURE: {
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        imagen =(Bitmap) extras.get("data");
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + ".jpg";
                        imageName=imageFileName;
                        agImagen.setImageBitmap(imagen);
                    }
                }
            }
            break;

        }
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
    private void updateCargarImagen() {
        agImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cargarImagen_intent = new Intent(Intent.ACTION_PICK);
                cargarImagen_intent.setType("image/*");
                startActivityForResult(cargarImagen_intent, IMAGE_PICKER_REQUEST);


            }
        });
    }


    private void updateTomarFoto() {
        imagenP.setOnClickListener(new View.OnClickListener() {
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

        String correoAux = correo.getText().toString();
        String ontraseñaAux = contraseña.getText().toString();
        String nombreAux = nombreCuenta.getText().toString();
        String edadAux = edad.getText().toString();
        String generoAux = sItems.getSelectedItem().toString();
        String nacionalidad = resnacionalidad.getSelectedItem().toString();

        if (TextUtils.isEmpty(correoAux)) {
            correo.setError("Required.");
            valid =false;
        } else {
            correo.setError(null);
        }

        if (TextUtils.isEmpty(ontraseñaAux)) {
            contraseña.setError("Required.");
            valid = false;
        } else {
            contraseña.setError(null);
        }

        if (TextUtils.isEmpty(nombreAux)) {
            nombreCuenta.setError("Required.");
            valid = false;
        } else {
            nombreCuenta.setError(null);
        }

        if (TextUtils.isEmpty(edadAux)) {
            edad.setError("Required.");
            valid = false;
        } else {
            edad.setError(null);
        }
        if(imageName==null){
            Toast.makeText(CrearCuentaActivity.this,"Por favor, agregue una imagen...", Toast.LENGTH_LONG).show();
        }

        if (huesped) {
            if (TextUtils.isEmpty(nacionalidad)) {
               // resnacionalidad.("Required.");
                valid = false;
            } else {
                //resnacionalidad.setError(null);
            }
        }


        return valid;
    }

    private void sendRegistrationLink() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(CrearCuentaActivity.this, new OnCompleteListener() {

                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(CrearCuentaActivity.this,"Verification email sent to " + user.getEmail(),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CrearCuentaActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(CrearCuentaActivity.this,"Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());


                        }
                    }
                });
    }


}

