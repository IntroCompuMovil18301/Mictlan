package javeriana.edu.co.mockups.mData;

import java.util.ArrayList;

import javeriana.edu.co.mockups.R;

public class ColeccionAlojamientos {
    public static ArrayList<Alojamiento> getAlojamiento() {

        ArrayList<Alojamiento> alojamientos = new ArrayList<>();

        Alojamiento s = new Alojamiento();
        s.setCalif(4);
        s.setTitulo("Casa bonita en Alderwood Ave");
        s.setImagen(R.drawable.descarga);
        s.setTipo("Casa");
        s.setUbicacion("639 Pacific Ave Danbury, CT 06810");
        s.setPrecio("20");
        alojamientos.add(s);
        s = new Alojamiento();
        s.setCalif(5);
        s.setTitulo("Cabaña lujosa");
        s.setImagen(R.drawable.descarga3);
        s.setTipo("Cabaña");
        s.setUbicacion("16 Oak Meadow St Annapolis, MD 21401");
        s.setPrecio("25");
        alojamientos.add(s);
        return alojamientos;
    }
}
