package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class AlumnoControllerTest {

    @InjectMocks
    AlumnoController alumnoController;

    @Mock
    AlumnoService alumnoService;

    MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(alumnoController).build();
    }

    @Test
    void crearAlumno() throws Exception {
        Mockito.when(alumnoService.crearAlumno(any(AlumnoDto.class))).thenReturn(new Alumno());
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("pepito");
        alumnoDto.setApellido("master");
        mockMvc.perform(MockMvcRequestBuilders.post("/alumno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(alumnoDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void putAlumnoById() throws Exception {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("pepito");
        alumnoDto.setApellido("master");

        Alumno alumno = new Alumno("pepe 1","gonzalez",1);
        alumno.setNombre(alumnoDto.getNombre());
        alumno.setApellido(alumnoDto.getApellido());
        alumno.setId(123);

        Mockito.when(alumnoService.putAlumnoById(123, alumnoDto)).thenReturn(alumno);

        mockMvc.perform(MockMvcRequestBuilders.put("/alumno/{idAlumno}", 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(alumnoDto)))
                .andExpect(status().isOk());
    }

    @Test
    void delAlumnoById() throws Exception {
        Alumno alumno = new Alumno("pepe 1","gonzalez",1);
        alumno.setId(123);
        Mockito.when(alumnoService.delAlumnoById(123)).thenReturn(alumno);

        mockMvc.perform(MockMvcRequestBuilders.delete("/alumno/{idAlumno}", 123))
                .andExpect(status().isOk());
    }

    @Test
    void getAlumnoById() throws Exception {
        Alumno alumno = new Alumno("pepe 1","gonzalez",1);
        alumno.setId(123);
        Mockito.when(alumnoService.getAlumnoById(123)).thenReturn(alumno);
        mockMvc.perform(MockMvcRequestBuilders.get("/alumno/{idAlumno}", 123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}