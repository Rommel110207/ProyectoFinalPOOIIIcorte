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
@View(members = "candidato;" + "idEvaluacion, fechaInicio, estado, notaFinal;" + "respuestas") //Organiza como se ve en la pantalla
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ReadOnly
    private long idEvaluacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @DescriptionsList //Muestra un combo box con los candidatos
    private Candidato candidato;

    @DefaultValueCalculator(org.openxava.calculators.CurrentLocalDateCalculator.class)
    private LocalDate fechaInicio;

    @Column(length = 20)
    private String estado = "Pendiente"; //Estado inicial

    @ReadOnly //Se calcula mediante la accion, el usuario no la edita a mano
    private double notaFinal;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL)
    private Collection<RespuestaDetalle> respuestas;

    //Metodos definidos en el enterprise architect
    public void iniciarEvaluacion(){
        this.estado = "En progreso...";
    }

    public void finalizarEvaluacion(){
        this.estado = "Finalizado";
    }
}
