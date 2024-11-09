package com.example.appchat.model;

import java.util.List;

public class Post {
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private String categoria;
    private String duracion;
    private double presupuesto;
    private List<String> imagenes;

    // Constructor vacío requerido para Firebase
    public Post() {}

    public Post(String titulo, String descripcion, String ubicacion, String categoria, String duracion, double presupuesto, List<String> imagenes) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.categoria = categoria;
        this.duracion = duracion;
        this.presupuesto = presupuesto;
        this.imagenes = imagenes;
    }

    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public double getPresupuesto() { return presupuesto; }
    public void setPresupuesto(double presupuesto) { this.presupuesto = presupuesto; }

    public List<String> getImagenes() { return imagenes; }
    public void setImagenes(List<String> imagenes) { this.imagenes = imagenes; }
}


