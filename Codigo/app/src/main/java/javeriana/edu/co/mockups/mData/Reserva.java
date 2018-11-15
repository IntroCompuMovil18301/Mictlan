package javeriana.edu.co.mockups.mData;

import java.io.Serializable;

public class Reserva implements Serializable {
    private String id;
    private String usuarioId;
    private String alojamientoId;
    private String fechaInicio;
    private String fechaFinal;
    private double costo;

    public Reserva(String id, String usuarioId, String alojamientoId, String fechaInicio,
                   String fechaFinal, double costo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.alojamientoId = alojamientoId;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.costo = costo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getAlojamientoId() {
        return alojamientoId;
    }

    public void setAlojamientoId(String alojamientoId) {
        this.alojamientoId = alojamientoId;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}
