package med.voll.api.domain.paciente;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Paciente")
@Table(name="pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    private String nombre;
    private String email;
    private String documento;
    private String telefono;
    private Boolean activo;

    @Embedded
    private Direccion direccion;

    public Paciente(DatosRegistroPaciente datos){
        this.activo=true;
        this.nombre=datos.nombre();
        this.email=datos.email();
        this.telefono=datos.telefono();
        this.documento=datos.documento();
        this.direccion=new Direccion(datos.direccion());
    }
    public void actualizarDatos(@RequestBody @Valid DatosActualizarPaciente datosActualizarPacientes) {
        if (datosActualizarPacientes.nombre() != null) {
            this.nombre = datosActualizarPacientes.nombre();
        }
        if (datosActualizarPacientes.documento() != null) {
            this.documento = datosActualizarPacientes.documento();
        }
        if (datosActualizarPacientes.email() != null) {
            this.email = datosActualizarPacientes.email();
        }
        if (datosActualizarPacientes.telefono() != null) {
            this.telefono = datosActualizarPacientes.telefono();
        }
    }

    public void inactivar(){
        this.activo=false;
    }

}
