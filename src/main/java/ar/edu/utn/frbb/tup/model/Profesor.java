package ar.edu.utn.frbb.tup.model;

import java.util.ArrayList;

public class Profesor {

    private int profesorId = 12;
    private final String nombre;
    private final String apellido;
    private final String titulo;

    private ArrayList<String> materiasDictadas;

    public Profesor(String nombre, String apellido, String titulo) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.titulo = titulo;

        this.materiasDictadas = new ArrayList<>();
    }

    public void agregarMateriaDictada(String nombreMateria) {
        this.materiasDictadas.add(nombreMateria);
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTitulo() {
        return titulo;
    }

    public ArrayList<String> getMateriasDictadas() {
        return materiasDictadas;
    }

    public int getprofesorId() {
        return profesorId;
    }

    public void setprofesorId(int id) {
        this.profesorId=id;
    }

    public void setMateriasDictadas(ArrayList<String> materiasDictadas) {
        this.materiasDictadas=materiasDictadas;
    }
}
