package javeriana.edu.co.mockups.mData;

import java.util.ArrayList;

import javeriana.edu.co.mockups.R;

public class ColeccionAlojamientos {
    public static ArrayList<Alojamiento> getAlojamiento() {

        ArrayList<Alojamiento> alojamientos = new ArrayList<>();

        Alojamiento s = new Alojamiento();
        s.setTitulo("Casa bonita en Alderwood Ave");
        s.setTipo("Casa");
        s.setUbicacion("639 Pacific Ave Danbury, CT 06810");
        s.setValorNoche(20);
        alojamientos.add(s);
        s = new Alojamiento();
        s.setTitulo("Cabaña lujosa");
        s.setTipo("Cabaña");
        s.setUbicacion("16 Oak Meadow St Annapolis, MD 21401");
        s.setValorNoche(25);
        alojamientos.add(s);
        return alojamientos;
    }
}
