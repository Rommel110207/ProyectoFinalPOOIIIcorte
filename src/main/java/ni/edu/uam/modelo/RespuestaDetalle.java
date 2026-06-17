package ni.edu.uam.modelo;

import lombok.*;
import org.openxava.annotations.*;
import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @DescriptionsList(descriptionProperties = "id")
    private Evaluacion evaluacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @DescriptionsList(descriptionProperties = "numeroPregunta")
    private Pregunta pregunta;

    @Column(length = 1)
    @Required
    private String letraSeleccionada;

    @ReadOnly
    private boolean esCorrecta;
}