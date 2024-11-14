package com.example.appchat.model;

import java.util.List;

public class Post {
    private int id_post;
    private String titulo;
    private String descripcion;
    private int duracion;
    private String categoria;
    private double presupuesto;
    private List<String> imagenes;
    private String id_user;

    public Post() {}

    public Post(int id_post, String titulo, String descripcion, int duracion, String categoria, double presupuesto, List<String> imagenes, String id_user) {
        this.id_post = id_post;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.categoria = categoria;
        this.presupuesto = presupuesto;
        this.imagenes = imagenes;
        this.id_user = id_user;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public Post(String titulo, String descripcion, int duracion, String categoria, double presupuesto, List<String> imagenes, String id_user) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.categoria = categoria;
        this.presupuesto = presupuesto;
        this.imagenes = imagenes;
        this.id_user = id_user;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }


    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public double getPresupuesto() { return presupuesto; }
    public void setPresupuesto(double presupuesto) { this.presupuesto = presupuesto; }

    public List<String> getImagenes() { return imagenes; }
    public void setImagenes(List<String> imagenes) { this.imagenes = imagenes; }
}


