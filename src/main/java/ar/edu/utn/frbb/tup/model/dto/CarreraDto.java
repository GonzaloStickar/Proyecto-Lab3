package ar.edu.utn.frbb.tup.model.dto;

public class CarreraDto {
    private String nombre;
    private int codigoCarrera;
    private int departamentoInt;
    private int cantidadAnios;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

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

    public int getCantidadAnios() {
        return cantidadAnios;
    }

    public void setCantidadAnios(int cantidadAnios) {
        this.cantidadAnios = cantidadAnios;
    }
}
