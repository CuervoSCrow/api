package med.voll.api.domain.paciente;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarPaciente(
        @NotNull
        Long id,
        String nombre,
        String documento,
        String email,
        String telefono
) {
}
