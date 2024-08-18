package com.example.entrevista;

import java.sql.Blob;
import java.util.Date;

public class Entrevista {
    private int idOrden;
    private String descripcion;
    private String periodista;
    private Date fecha;
    private Blob imagenUrl;  // Blob para almacenar la imagen del entrevistado
    private Blob audioUrl;   // Blob para almacenar el audio de la entrevista

    public Entrevista() {
        // Constructor vacío necesario para Firebase
    }

    public Entrevista(int idOrden, String descripcion, String periodista, Date fecha, Blob imagenUrl, Blob audioUrl) {
        this.idOrden = idOrden;
        this.descripcion = descripcion;
        this.periodista = periodista;
        this.fecha = fecha;
        this.imagenUrl = imagenUrl;
        this.audioUrl = audioUrl;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPeriodista() {
        return periodista;
    }

    public void setPeriodista(String periodista) {
        this.periodista = periodista;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Blob getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(Blob imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Blob getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(Blob audioUrl) {
        this.audioUrl = audioUrl;
    }
}
