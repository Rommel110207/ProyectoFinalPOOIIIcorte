package ni.edu.uam.modelo;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;

@Entity
@Getter @Setter
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private long idPregunta;

    @Column(length = 255, required = true)
    private String enunciado;

    @Column(required = true)
    private double puntajeBase;

    // Campo inferido necesario para la lógica del método del UML
    @Column(length = 5)
    @Required
    private String respuestaCorrecta;

    // Método
    public boolean verificarRespuestaCorrecta(String respuestaSeleccionada) {
        if (this.respuestaCorrecta == null || respuestaSeleccionada == null) return false;
        return this.respuestaCorrecta.equalsIgnoreCase(respuestaSeleccionada);
    }
}