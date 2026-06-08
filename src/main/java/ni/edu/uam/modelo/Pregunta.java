package ni.edu.uam.modelo;
import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.Getter;
import lombok.Setter;
    @Entity
    @Getter @Setter
    public class Pregunta {

        @Id
        private Integer numeroPregunta;

        @Stereotype("PHOTO")
        private byte[] imagenLadrillos;

        @Column(length = 1) @Required
        private String respuestaCorrecta;

    }







