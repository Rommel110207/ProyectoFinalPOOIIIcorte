package ni.edu.uam.modelo;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class RespuestaDetalle {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Hidden
    private Integer id;

    @ManyToOne
    private Evaluacion evaluacion;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @DescriptionsList(descriptionProperties="numeroPregunta")
    private Pregunta pregunta;

    @Column(length=1)
    private String letraSeleccionada;

    @ReadOnly
    private boolean esCorrecta;

}