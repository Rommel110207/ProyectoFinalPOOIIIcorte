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

    @Column(length = 1)
    @Required
    private String respuestaCorrecta;
}






