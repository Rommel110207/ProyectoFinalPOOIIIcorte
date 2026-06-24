package ni.edu.uam.modelo;

import lombok.*;
import org.openxava.annotations.*;
import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pregunta {

    @Id
    private Integer numeroPregunta;

    @Stereotype("PHOTO")
    private byte[] imagenLadrillos;

    @Required
    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private  IncisoCorrecto respuestaCorrecta;

    public enum IncisoCorrecto {
        A, B, C, D, E
    }
}






