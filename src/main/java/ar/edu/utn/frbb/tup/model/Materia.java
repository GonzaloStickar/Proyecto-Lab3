package ar.edu.utn.frbb.tup.model;

import java.util.ArrayList;
import java.util.List;

public class Materia {

    private int materiaId;
    private String nombre;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    private int anio;
    private int cuatrimestre;
    private Profesor profesor;

    private List<String> correlatividades;

    public Materia(){}


    public Materia(String nombre, int anio, int cuatrimestre, Profesor profesor) {
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.nombre = nombre;
        this.profesor = profesor;

        this.correlatividades = new ArrayList<>();
    }

    public void agregarCorrelatividad(String m){
        this.correlatividades.add(m);
    }

    public List<String> getCorrelatividades(){
        return this.correlatividades;
    }

    public void setCorrelatividades(List<String> correlatividades){
        this.correlatividades=correlatividades;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(int materiaId) {
        this.materiaId = materiaId;
    }
}
