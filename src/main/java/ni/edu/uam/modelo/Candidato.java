package ni.edu.uam.modelo;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;
import java.util.Collection;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private long idCandidato;

    @Column(length = 20, nullable = false)
    @Required
    private String identificacion;

    @Column(length = 100, nullable = false)
    @Required
    private String nombre;

    // ? AQUÍ SE CORRIGIÓ: Cambiamos 'idEvaluacion' por 'id' para que coincida con la clase de tu compañera
    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL)
    @ListProperties("id, fecha, puntajeTotal")
    private Collection<Evaluacion> evaluaciones;

}
