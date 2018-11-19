package javeriana.edu.co.mockups.mAdapterView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javeriana.edu.co.mockups.R;
import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.Calificacion;
import javeriana.edu.co.mockups.mData.Reserva;
import javeriana.edu.co.mockups.mData.Usuario;

public class CalificacionAdapter extends BaseAdapter {

    private static final String PATH_USERS = "usuarios";


    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private ArrayList<Calificacion> calificaciones;
    private Context c;

    public CalificacionAdapter(Context c, ArrayList<Calificacion> Calificaciones) {
        this.calificaciones = Calificaciones;
        this.c = c;
    }

    @Override
    public int getCount() {
        return calificaciones.size();
    }

    @Override
    public Object getItem(int i) {
        return calificaciones.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int pos, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.calimodel, viewGroup, false);
        }
        database = FirebaseDatabase.getInstance();
        final TextView usuario = (TextView) view.findViewById(R.id.calif_us_nam);
        TextView fecha = (TextView) view.findViewById(R.id.calif_fecha);
        TextView reseña = (TextView) view.findViewById(R.id.calif_res);
        SimpleRatingBar estrellas = (SimpleRatingBar) view.findViewById(R.id.calif_rat);

        final Calificacion s = (Calificacion) this.getItem(pos);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("usuarios").child(s.getId());
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                javeriana.edu.co.mockups.mData.Usuario aux = dataSnapshot.getValue(Usuario.class);
                usuario.setText("Usuario: " + aux.getNombre());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        fecha.setText(s.getFecha());
        reseña.setText(s.getComentario());
        estrellas.setRating(s.getEstrellas());


        return view;
    }


}
