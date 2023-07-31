package ar.edu.utn.frbb.tup.model;

import java.util.ArrayList;
import java.util.List;

public class Carrera {

    private String nombre;
    private int cantidadCuatrimestres;
    private List<Materia> materiasList;

    private int codigoCarrera;
    private int departamentoInt;

    public int getCodigoCarrera() {
        return codigoCarrera;
    }

    public void setCodigoCarrera(int codigoCarrera) {
        this.codigoCarrera = codigoCarrera;
    }

    public int getDepartamentoInt() {
        return departamentoInt;
    }

    public void setDepartamentoInt(int departamentoInt) {
        this.departamentoInt = departamentoInt;
    }

    public Carrera() {}

    public Carrera(String nombre, int cantidadAnios) {
        this.nombre = nombre;
        this.cantidadCuatrimestres = cantidadAnios;
        this.materiasList = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadCuatrimestres() {
        return cantidadCuatrimestres;
    }

    public void setCantidadCuatrimestres(int cantidadAnios) {
        this.cantidadCuatrimestres = cantidadAnios;
    }

    public List<Materia> getMateriasList() {
        return materiasList;
    }

    public void setMateriasList(List<Materia> materiasList) {
        this.materiasList = materiasList;
    }

    public void agregarMateria(Materia materia) {
    }
}
