package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {

    @Autowired
    AsignaturaDao dao;

    @Override
    public Asignatura getAsignatura(int materiaId) {
        return null;
    }

    @Override
    public void actualizarAsignatura(Asignatura a) {

    }


    public List<Asignatura> getSomeAsignaturasRandomFromAsignaturasDao() throws AsignaturaNotFoundException {
        List<Asignatura> listaAsignaturas = dao.getAllAsignaturas();
        if (listaAsignaturas.isEmpty()) {
            throw new AsignaturaNotFoundException("No hay Asignaturas"); //Lo cual no va a suceder, ya que las Asignaturas estan "Hardcodeadas".
        }
        ArrayList<Integer> listaNumerosRandom = new ArrayList<>();
        List<Asignatura> listaAsignaturasRandom = new ArrayList<>();
        while (listaNumerosRandom.size()<3) { //Cantidad de Asignaturas a asignar al Alumno (yo le asigno 3 solamente)
            int i = crearNumeroEntreRangoRandom(0,(listaAsignaturas.size())-1);
            if (!listaNumerosRandom.contains(i)) {
                listaNumerosRandom.add(i);
            }
        }
        for (Integer numero : listaNumerosRandom) {
            listaAsignaturasRandom.add(listaAsignaturas.get(numero));
        }
        return listaAsignaturasRandom;
    }

    public static int crearNumeroEntreRangoRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
