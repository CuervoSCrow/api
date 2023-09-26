package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.*;
import med.voll.api.domain.paciente.*;
import med.voll.api.domain.direccion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaPaciente> registrarPaciente(@RequestBody @Valid
                                                                    DatosRegistroPaciente datosRegistroPaciente, UriComponentsBuilder uriComponentsBuilder)
    {
        Paciente paciente = repository.save(new Paciente(datosRegistroPaciente));
        DatosRespuestaPaciente datosRespuestaPaciente = new DatosRespuestaPaciente(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getEmail(),
                paciente.getDocumento(),
                paciente.getTelefono(),
                new DatosDireccion(
                       paciente.getDireccion().getCalle(),
                       paciente.getDireccion().getDistrito(),
                       paciente.getDireccion().getCiudad(),
                       paciente.getDireccion().getNumero(),
                       paciente.getDireccion().getComplemento()
                )
        );
        URI url=uriComponentsBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaPaciente);
    }
    @GetMapping
    public ResponseEntity<Page<DatosListadoPaciente>> listadoPaciente(
            @PageableDefault(size=4,page=0) Pageable paginacion
    ){
        //return repository.findAll(paginacion).map(DatosListadoPaciente::new);
        return ResponseEntity.ok(repository.findByActivoTrue(paginacion).map(DatosListadoPaciente::new));
    }
    @PutMapping
    @Transactional
    public ResponseEntity actualizarPaciente(@RequestBody @Valid DatosActualizarPaciente datosActualizarPaciente){
        Paciente paciente = repository.getReferenceById(datosActualizarPaciente.id());
        paciente.actualizarDatos(datosActualizarPaciente);
        return ResponseEntity.ok(new DatosRespuestaPaciente(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getEmail(),
                paciente.getDocumento(),
                paciente.getTelefono(),
                new DatosDireccion(
                        paciente.getDireccion().getCalle(),
                        paciente.getDireccion().getDistrito(),
                        paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getNumero(),
                        paciente.getDireccion().getComplemento()
                )
        ));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity removerPaciente(@PathVariable Long id){
        Paciente paciente = repository.getReferenceById(id);
        paciente.inactivar();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaPaciente>retornaDatosPaciente(@PathVariable Long id){
        Paciente paciente = repository.getReferenceById(id);
        DatosRespuestaPaciente datosPacientes = new DatosRespuestaPaciente(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getEmail(),
                paciente.getDocumento(),
                paciente.getTelefono(),
                new DatosDireccion(
                        paciente.getDireccion().getCalle(),
                        paciente.getDireccion().getDistrito(),
                        paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getNumero(),
                        paciente.getDireccion().getComplemento()
                )
        );

        return ResponseEntity.ok(datosPacientes);
    }
}
