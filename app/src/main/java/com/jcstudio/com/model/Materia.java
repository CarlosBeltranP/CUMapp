package com.jcstudio.com.model;


public class Materia {
    private long mId;
    private String materia_nombre;
    private double materia_uv;
    private double nota_obtenida;
    private double cum_calculado;
    private long ciclo_id;

    public Materia() {

    }

    public Materia(String materia_nombre, double materia_uv, double nota_obtenida) {
        this.materia_nombre = materia_nombre;
        this.materia_uv = materia_uv;
        this.nota_obtenida = nota_obtenida;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getMateria_nombre() {
        return materia_nombre;
    }

    public void setMateria_nombre(String materia_nombre) {
        this.materia_nombre = materia_nombre;
    }

    public double getMateria_uv() {
        return materia_uv;
    }

    public void setMateria_uv(double materia_uv) {
        this.materia_uv = materia_uv;
    }

    public double getNota_obtenida() {
        return nota_obtenida;
    }

    public void setNota_obtenida(double nota_obtenida) {
        this.nota_obtenida = nota_obtenida;
    }

    public long getCiclo_id() {
        return ciclo_id;
    }

    public void setCiclo_id(long ciclo_id) {
        this.ciclo_id = ciclo_id;
    }

    public double getCum_calculado() {
        return cum_calculado;
    }

    public void setCum_calculado(double cum_calculado) {
        this.cum_calculado = cum_calculado;
    }
}

