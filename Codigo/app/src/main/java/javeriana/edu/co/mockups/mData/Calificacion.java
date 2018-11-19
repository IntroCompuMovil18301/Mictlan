package javeriana.edu.co.mockups.mData;

public class Calificacion {
    private String id;
    private String alojamientoId;
    private String reservaId;
    private float estrellas;
    private String comentario;
    private String fecha;

    public Calificacion() {
    }

    public Calificacion(String id, String alojamientoId, String reservaId, float estrellas, String comentario, String fecha) {
        this.id = id;
        this.alojamientoId = alojamientoId;
        this.reservaId = reservaId;
        this.estrellas = estrellas;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public String getAlojamientoId() {
        return alojamientoId;
    }

    public void setAlojamientoId(String alojamientoId) {
        this.alojamientoId = alojamientoId;
    }

    public String getReservaId() {
        return reservaId;
    }

    public void setReservaId(String reservaId) {
        this.reservaId = reservaId;
    }

    public float getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(float estrellas) {
        this.estrellas = estrellas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
