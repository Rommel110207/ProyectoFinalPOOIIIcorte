package ni.edu.uam.modelo;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.openxava.annotations.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@ToString

public class Candidato {

    @Id @Column(length=20)
    private String cedula;

    @Column(length=50) @Required
    private String nombre;

    @Column(length=50) @Required
    private String apellidos;

    // Getters y Setters
}