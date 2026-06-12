package ni.edu.uam.modelo;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;
import java.util.Collection;

@Entity
@Getter @Setter
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden // Oculto en la vista del usuario
    private long idCandidato;

    @Column(length = 20, required = true)
    private String identificacion;

    @Column(length = 100, required = true)
    private String nombre;

    @OneToMany(mappedBy = "candidato")
    @ListProperties("idEvaluacion, fechaInicio, estado, notaFinal")
    private Collection<Evaluacion> evaluaciones;

    // Método definido en tu UML
    public boolean validarIdentificacion(String identificacion) {
        // Lógica de validación (ej. formato de cédula)
        return this.identificacion != null && !this.identificacion.trim().isEmpty();
    }
}