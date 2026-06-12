package ni.edu.uam.modelo;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;

@Entity
@Getter @Setter
public class RespuestaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private long idRespuestaDetalle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Evaluacion evaluacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @DescriptionsList(descriptionProperties = "enunciado")
    private Pregunta pregunta;

    @Column(length = 5)
    private String respuestaSeleccionada;

    @ReadOnly // Calculado por el sistema
    private boolean esCorrecta;

    // Métodos
    public void registrarRespuesta(String respuesta) {
        this.respuestaSeleccionada = respuesta;
    }

    public void marcarComoCorrecta(boolean correcta) {
        this.esCorrecta = correcta;
    }
}