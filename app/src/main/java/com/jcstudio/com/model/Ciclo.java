package com.jcstudio.com.model;

public class Ciclo {

    private long mId;
    private String ciclo_nombre;
    private double total_nota;
    private int total_materia;
    private double total_uv;


    public Ciclo() {

    }
    public Ciclo(long mId, String ciclo_nombre, double total_nota, int total_materia, double total_uv) {
        this.mId = mId;
        this.ciclo_nombre = ciclo_nombre;
        this.total_nota = total_nota;
        this.total_materia = total_materia;
        this.total_uv = total_uv;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getCiclo_nombre() {
        return ciclo_nombre;
    }

    public void setCiclo_nombre(String ciclo_nombre) {
        this.ciclo_nombre = ciclo_nombre;
    }

    public double getTotal_nota() {
        return total_nota;
    }

    public void setTotal_nota(double total_nota) {
        this.total_nota = total_nota;
    }

    public int getTotal_materia() {
        return total_materia;
    }

    public void setTotal_materia(int total_materia) {
        this.total_materia = total_materia;
    }

    public double getTotal_uv() {
        return total_uv;
    }

    public void setTotal_uv(double total_uv) {
        this.total_uv = total_uv;
    }
}
