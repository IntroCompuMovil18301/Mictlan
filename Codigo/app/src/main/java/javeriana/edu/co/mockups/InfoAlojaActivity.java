package javeriana.edu.co.mockups;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javeriana.edu.co.mockups.mAdapterView.ImageModel;
import javeriana.edu.co.mockups.mAdapterView.SlidingImage_Adapter;
import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.Usuario;

public class InfoAlojaActivity extends AppCompatActivity {


    private TextView tipo_aloj;
    private TextView ubicac_aloj;
    private TextView numper_aloj;
    private TextView numcam_aloj;
    private TextView numban_aloj;
    private TextView numalc_aloj;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
   // private int[] myImageList = new int[]{R.drawable.descarga, R.drawable.descarga1,R.drawable.descarga2,R.drawable.descarga3};
    private Bitmap[] myBitList;

    private CardView biogra_but;
    private TextView nombre_anfi;
    private ImageView foto_anfi;
    private SimpleRatingBar estrellas;

    private Button califi_but;
    private Button reserv_but;
    private FirebaseAuth mAuth;

    String idAloj;
    private String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_aloja);

        final Alojamiento alojamiento = (Alojamiento) getIntent().getExtras().getSerializable("alojamiento");
        System.out.println("InfoAlojaActivity: " + alojamiento);


        setTitle(alojamiento.getTitulo());

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList(alojamiento);


        init();


        tipo_aloj = findViewById( R.id.iaa_tipo_aloj );
        ubicac_aloj = findViewById( R.id.iaa_ubicac_aloj );
        numper_aloj = findViewById( R.id.iaa_numper_aloj );
        numcam_aloj = findViewById( R.id.iaa_numcam_aloj );
        numban_aloj = findViewById( R.id.iaa_numban_aloj );
        numalc_aloj = findViewById( R.id.iaa_numalc_aloj );

        biogra_but = findViewById( R.id.iaa_biogra_anfi );
        nombre_anfi = findViewById( R.id.iaa_nombre_anfi );
        foto_anfi = findViewById( R.id.iaa_foto_anfi );

        califi_but = findViewById( R.id.iaa_califi_aloj );
        reserv_but = findViewById( R.id.iaa_reserv_aloj );
        estrellas = (SimpleRatingBar) findViewById(R.id.iaa_rating_reser);

      /*  final File image = new File(getBaseContext().getExternalFilesDir(null),
                alojamiento.getImages().get(0) + "jpg");
        if(!image.exists()) {
            FirebaseStorage.getInstance().getReference("alojamientos")
                    .child(alojamiento.getId()).child(alojamiento.getImages().get(0)).getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(new FileInputStream(image), null, options);
                        foto_aloj.setImageBitmap(bitmap);
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
                foto_aloj.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }*/

        tipo_aloj.setText(tipo_aloj.getText().toString() + alojamiento.getTipo());
        ubicac_aloj.setText(ubicac_aloj.getText().toString() + alojamiento.getUbicacion());
        numper_aloj.setText(Integer.toString(alojamiento.getPersonas()) + numper_aloj.getText().toString());
        numcam_aloj.setText(Integer.toString(alojamiento.getCamas()) + numcam_aloj.getText().toString());
        numalc_aloj.setText(Integer.toString(alojamiento.getAlcobas()) + numalc_aloj.getText().toString());
        numban_aloj.setText(Integer.toString(alojamiento.getBanos()) + numban_aloj.getText().toString());
        estrellas.setRating(alojamiento.getCalificacion());

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final Query anfitrion = mDatabase.child("usuarios").child(alojamiento.getUsuario());
        anfitrion.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario aux = dataSnapshot.getValue(Usuario.class);
                nombre_anfi.setText(aux.getNombre());
                tipo = aux.getTipo();
                if ( aux.getImagen() != null )
                    foto_anfi.setImageURI(Uri.parse(aux.getImagen()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        biogra_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent biogra_intent = new Intent( view.getContext(), BiografiaAnfitrionActivity.class );
                startActivity( biogra_intent );
            }
        });

        califi_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),CalificacionesActivity.class);
                Bundle paquete = new Bundle();
                paquete.putSerializable("alojamiento", alojamiento);
                intent.putExtras(paquete);
                startActivity(intent);
            }
        });

        reserv_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reserv_intent = new Intent( view.getContext(), FechaReservaActivity.class );
                reserv_intent.putExtra("alojamiento", alojamiento);
                startActivity( reserv_intent );
            }
        });
    }
    private ArrayList<ImageModel> populateList(Alojamiento alojamiento) {
        //TODO llenar la lista con imagenes
        final ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            final File image = new File(getBaseContext().getExternalFilesDir(null),
                    alojamiento.getImages().get(i));
            Log.d("imagen----->", "populateList: "+ image);
            if(!image.exists()) {
                FirebaseStorage.getInstance().getReference("alojamientos")
                        .child(alojamiento.getId()).child(alojamiento.getImages().get(i)).getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(new FileInputStream(image), null, options);
                            ImageModel imageModel = new ImageModel();
                            imageModel.setImage_bitmap(bitmap);
                            list.add(imageModel);
                            //foto_aloj.setImageBitmap(bitmap);
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
                   // foto_aloj.setImageBitmap(bitmap);
                    ImageModel imageModel = new ImageModel();
                    imageModel.setImage_bitmap(bitmap);
                    list.add(imageModel);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;

    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager_aloj);
        mPager.setAdapter(new SlidingImage_Adapter(InfoAlojaActivity.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator_aloj);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
}
