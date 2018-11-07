package javeriana.edu.co.mockups.mData;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Alojamiento implements Serializable {

    private String usuario;
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

    public Alojamiento(String usuario, String titulo, String ubicacion, double latitud, double longitud,
                       float valorNoche, String tipo, int personas, int camas, int alcobas, int banos,
                       List<String> images) {
        this.usuario = usuario;
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
}
