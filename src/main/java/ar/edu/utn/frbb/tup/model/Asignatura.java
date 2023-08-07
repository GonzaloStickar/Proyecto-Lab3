package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class Asignatura {

    private Materia materia;
    private EstadoAsignatura estado;
    private Integer nota;

    public Asignatura(Materia materia) {
        this.materia = materia;
        this.estado = EstadoAsignatura.NO_CURSADA;
    }

    public Optional<Integer> getNota() {
        return Optional.ofNullable(nota);
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

    public void aprobarAsignatura(int nota) throws EstadoIncorrectoException {
        if (this.estado.equals(EstadoAsignatura.CURSADA)) {
            if (nota >= 6) {
                aprobarAsignatura();
                setNota(nota);
            }
        } else if (this.estado.equals(EstadoAsignatura.NO_CURSADA)) {
            if (nota >= 6) {
                aprobarAsignatura();
                setNota(nota);
            } else {
                cursarAsignatura();
                setNota(nota);
            }
        } else {
            throw new EstadoIncorrectoException("La materia ya está aprobada", HttpStatus.CONFLICT);
        }
    }

}
