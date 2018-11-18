package javeriana.edu.co.mockups.mData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Alojamiento implements Serializable {


    private String usuario;
    private String id;
    private String titulo;
    private String ubicacion;
    private double latitud;
    private double longitud;
    private float valorNoche;
    private String tipo;
    private int personas;
    private int camas;
    private int alcobas;
    private int banos;
    private List<String> images;
    private List<String> reservas;

    public Alojamiento() {

    }

    public Alojamiento(String usuario, String id, String titulo, String ubicacion, double latitud,
                       double longitud, float valorNoche, String tipo, int personas, int camas,
                       int alcobas, int banos, List<String> images) {
        this.usuario = usuario;
        this.id = id;
        this.titulo = titulo;
        this.ubicacion = ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.valorNoche = valorNoche;
        this.tipo = tipo;
        this.personas = personas;
        this.camas = camas;
        this.alcobas = alcobas;
        this.banos = banos;
        this.images = images;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public float getValorNoche() {
        return valorNoche;
    }

    public void setValorNoche(float valorNoche) {
        this.valorNoche = valorNoche;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPersonas() {
        return personas;
    }

    public void setPersonas(int personas) {
        this.personas = personas;
    }

    public int getCamas() {
        return camas;
    }

    public void setCamas(int camas) {
        this.camas = camas;
    }

    public int getAlcobas() {
        return alcobas;
    }

    public void setAlcobas(int alcobas) {
        this.alcobas = alcobas;
    }

    public int getBanos() {
        return banos;
    }

    public void setBanos(int banos) {
        this.banos = banos;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getReservas() {
        return reservas;
    }

    public void setReservas(List<String> reservas) {
        this.reservas = reservas;
    }

    public void addImage(String image) {
        this.images.add(image);
    }

    public void addReserva(String reserva) {
        if (this.reservas == null) {
            this.reservas = new ArrayList<>();
        }
        this.reservas.add(reserva);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("usuario", usuario);
        result.put("id", id);
        result.put("titulo", titulo);
        result.put("ubicacion", ubicacion);
        result.put("latitud", latitud);
        result.put("longitud", longitud);
        result.put("valorNoche", valorNoche);
        result.put("tipo", tipo);
        result.put("personas", personas);
        result.put("camas", camas);
        result.put("alcobas", alcobas);
        result.put("banos", banos);
        result.put("images", images);
        result.put("reservas", reservas);

        return result;
    }

    @Override
    public String toString() {
        return "Alojamiento{" +
                "usuario='" + usuario + '\'' +
                ", id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", valorNoche=" + valorNoche +
                ", tipo='" + tipo + '\'' +
                ", personas=" + personas +
                ", camas=" + camas +
                ", alcobas=" + alcobas +
                ", banos=" + banos +
                ", images=" + images +
                ", reservas=" + reservas +
                '}';
    }
}
