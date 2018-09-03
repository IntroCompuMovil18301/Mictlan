package javeriana.edu.co.mockups.mData;

public class Alojamiento {


    private int calif;
    private String titulo;
    private int imagen;
    private String ubicacion;
    private String precio;
    private String tipo;

    public Alojamiento() {
    }

    public int getCalif() {
        return calif;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getImagen() {
        return imagen;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setCalif(int calif) {
        this.calif = calif;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }



    public void setPrecio(String precio) {
        this.precio = precio;
    }

}
