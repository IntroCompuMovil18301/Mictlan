package javeriana.edu.co.mockups.mAdapterView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javeriana.edu.co.mockups.R;
import javeriana.edu.co.mockups.mData.Alojamiento;

public class CustomAdapter extends BaseAdapter {

    private ArrayList<Alojamiento> alojamientos;
    private Context c;

    public CustomAdapter(Context c, ArrayList<Alojamiento> alojamientos) {
        this.alojamientos = alojamientos;
        this.c = c;
    }

    @Override
    public int getCount() {
        return alojamientos.size();
    }

    @Override
    public Object getItem(int i) {
        return alojamientos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int pos, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.model, viewGroup, false);
        }
        TextView tituloTxt = (TextView) view.findViewById(R.id.dato1);
        TextView tipoTxt = (TextView) view.findViewById(R.id.dato2);
        TextView ubicacionTxt = (TextView) view.findViewById(R.id.dat03);
        TextView precioTxt = (TextView) view.findViewById(R.id.dato4);
        final ImageView img = (ImageView) view.findViewById(R.id.spacecraftImage);
        TextView ratingBar = (TextView) view.findViewById(R.id.ratingBarID);

        final Alojamiento s = (Alojamiento) this.getItem(pos);

        tituloTxt.setText(s.getTitulo());
        tipoTxt.setText("Tipo: " + s.getTipo());
        ubicacionTxt.setText("Ubicacion: " + s.getUbicacion());
        precioTxt.setText("Precio: " + s.getValorNoche());
        ratingBar.setText(Float.toString(s.getCalificacion()));


        final File image = new File(viewGroup.getContext().getExternalFilesDir(null), s.getImages().get(0) + "jpg");
        if (!image.exists()) {
            FirebaseStorage.getInstance().getReference("alojamientos")
                    .child(s.getId()).child(s.getImages().get(0)).getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(new FileInputStream(image), null, options);
                        img.setImageBitmap(bitmap);
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
                img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        System.out.println( "CustomAdapter: " + s);

        return view;
    }
}
