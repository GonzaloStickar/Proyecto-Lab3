package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CarreraController {

    @Autowired
    private CarreraService carreraService;

    @PostMapping("/carrera") //POST: /carrera
    public ResponseEntity<?> crearCarrera(@RequestBody CarreraDto carreraDto) {
        carreraService.crearCarrera(carreraDto);
        return ResponseEntity.status(HttpStatus.OK).body(carreraService.getAllCarreras());
    }

    @PutMapping("/carrera/{idCarrera}") //PUT: /carrera/{idCarrera}
    public ResponseEntity<?> putCarreraById(@PathVariable Integer idCarrera,
                                  @RequestBody CarreraDto carreraDto) throws CarreraNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(carreraService.putCarreraById(idCarrera,carreraDto));
    }

    @DeleteMapping("/carrera/{idCarrera}") //DELETE: /carrera/{idCarrera}
    public ResponseEntity<?> delaCarreraById(@PathVariable Integer idCarrera) throws CarreraNotFoundException {
        for (Carrera carrera : carreraService.getAllCarreras()) {
            if (carrera.getCodigoCarrera() == idCarrera) {
                carreraService.delCarreraById(idCarrera);
                return ResponseEntity.status(HttpStatus.OK).body("Carrera eliminada.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrera no encontrada con id: " + idCarrera);
    }
}
