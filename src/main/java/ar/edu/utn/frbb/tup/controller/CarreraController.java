package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CarreraController {

    @PostMapping("/carrera") //POST: /carrera
    public ResponseEntity<?> crearCarrera(@RequestBody CarreraDto carreraDto) {

        return ResponseEntity.status(HttpStatus.OK).body(carreraDto);
    }
}
