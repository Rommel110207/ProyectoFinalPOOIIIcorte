package ni.edu.uam.modelo;

import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentLocalDateCalculator;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import org.openxava.validators.ValidationException;

@Entity
@Getter @Setter
@NoArgsConstructor  // Asegura constructor vacío para JPA
@AllArgsConstructor // Constructor full campos
@View(members = "fecha, candidato; puntajeTotal; respuestas")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer id;

    @DefaultValueCalculator(CurrentLocalDateCalculator.class)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @DescriptionsList
    private Candidato candidato;

    @ReadOnly
    private int puntajeTotal;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL)
    @ListProperties("pregunta.numeroPregunta, letraSeleccionada, esCorrecta")
    private Collection<RespuestaDetalle> respuestas;

    //Validacion de reglas de negocio (Fecha de Aplicacion)
    @PrePersist
    @PreUpdate
    private void validarFechaAplicacion() {
        // Verificamos que la propiedad fecha no sea nula para evitar un NullPointerException
        if (this.fecha != null) {
            LocalDate hoy = LocalDate.now(); // Captura de forma exacta el día de hoy en el servidor

            // Si la fecha configurada en pantalla es estrictamente posterior a hoy
            if (this.fecha.isAfter(hoy)) {
                // Lanzamos la excepción que OpenXava transformará en una alerta visual roja
                throw new ValidationException("No se permite registrar evaluaciones con una fecha de aplicación futura.");
            }
        }
    }
}
