package ni.edu.uam.modelo;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;
import java.util.Collection;

@Entity
@Getter @Setter
@NoArgsConstructor // Constructor vacío obligatorio para JPA
@AllArgsConstructor // Constructor con todos los campos
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden // Oculto en la vista del usuario en el panel
    private long idCandidato;

    // Se cambió 'required = true' por 'nullable = false' para corregir el error de compilación
    @Column(length = 20, nullable = false)
    @Required // Esto ya maneja la validación obligatoria en la web de OpenXava
    private String identificacion;

    // Se corrigió también aquí para evitar el mismo error en el campo nombre
    @Column(length = 100, nullable = false)
    @Required
    private String nombre;

    // Relación bidireccional con las evaluaciones que realice este candidato
    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL)
    @ListProperties("id, fecha, puntajeTotal") // Ajustado a las variables reales de tus compañeros
    private Collection<Evaluacion> evaluaciones;

}
