package javeriana.edu.co.mockups.mAdapterView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

import javeriana.edu.co.mockups.R;
import javeriana.edu.co.mockups.mData.Alojamiento;
import javeriana.edu.co.mockups.InfoAlojaActivity;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<Alojamiento> alojamientos;
    private Context c;
    public CustomAdapter(Context c,ArrayList<Alojamiento> alojamientos) {
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
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.model,viewGroup,false);
        }

        TextView tituloTxt= (TextView) view.findViewById(R.id.dato1);
        TextView tipoTxt= (TextView) view.findViewById(R.id.dato2);
        TextView ubicacionTxt= (TextView) view.findViewById(R.id.dat03);
        TextView precioTxt= (TextView) view.findViewById(R.id.dato4);
        ImageView img= (ImageView) view.findViewById(R.id.spacecraftImage);
        SimpleRatingBar ratingBar= (SimpleRatingBar) view.findViewById(R.id.ratingBarID);

        final Alojamiento s= (Alojamiento) this.getItem(pos);

        tituloTxt.setText(s.getTitulo());
        tipoTxt.setText(s.getTipo());
        ubicacionTxt.setText(s.getUbicacion());
        precioTxt.setText( s.getPrecio());
        ratingBar.setRating(s.getCalif());
        img.setImageResource(s.getImagen());

        return view;
    }



}
