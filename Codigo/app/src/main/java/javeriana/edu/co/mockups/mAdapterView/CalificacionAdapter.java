package javeriana.edu.co.mockups.mAdapterView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javeriana.edu.co.mockups.R;
import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.mData.Calificacion;

public class CalificacionAdapter {

    private ArrayList<Calificacion> Calificaciones;
    private Context c;

    public CalificacionAdapter(Context c, ArrayList<Calificacion> Calificaciones) {
        this.Calificaciones = Calificaciones;
        this.c = c;
    }




}
