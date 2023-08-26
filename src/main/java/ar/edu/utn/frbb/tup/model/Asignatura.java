package ar.edu.utn.frbb.tup.model;

public class Asignatura {

    private Materia materia;
    private EstadoAsignatura estado;
    private int nota;

    public Asignatura(Materia materia) {
        this.materia = materia;
        this.estado = EstadoAsignatura.NO_CURSADA;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public EstadoAsignatura getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsignatura estado) {
        this.estado = estado;
    }

    public String getNombreAsignatura(){
        return this.materia.getNombre();
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public void cursarAsignatura(){
        this.estado = EstadoAsignatura.CURSADA;
    }

    public void aprobarAsignatura(){
        this.estado = EstadoAsignatura.APROBADA;
    }
}
