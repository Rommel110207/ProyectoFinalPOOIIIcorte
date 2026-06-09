package ni.edu.uam.modelo;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentLocalDateCalculator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Getter
@Setter
@View(members = "fecha,candidato;puntajeTotal;respuestas")
public class Evaluacion {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Hidden
    private Integer id;

    @DefaultValueCalculator(CurrentLocalDateCalculator.class)
    private LocalDate fecha;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @DescriptionsList
    private Candidato candidato;

    @ReadOnly
    private int puntajeTotal;

    @OneToMany(mappedBy="evaluacion", cascade=CascadeType.ALL)
    @ListProperties("pregunta.numeroPregunta, letraSeleccionada, esCorrecta")
    private Collection<RespuestaDetalle> respuestas;
}
