package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;

import med.voll.api.domain.medico.*;
import med.voll.api.domain.medico.*;
import med.voll.api.domain.direccion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico ,
                                                                UriComponentsBuilder uriComponentBuilder){

        Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(
                medico.getId(),
                medico.getNombre(),
                medico.getEmail(),
                medico.getTelefono(),
                medico.getDocumento(),
                medico.getEspecialidad().toString(),
                new DatosDireccion(
                        medico.getDireccion().getCalle(),
                        medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento()
                )
        );
        //URI url = "htpp://localhost:8080/medicos"+medico.getId();
        URI url = uriComponentBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaMedico);
        //Return 201 Created
        //URL donde encontrar al medico
        //GET http://localhost:8080/medicos/xxx

    }
    @GetMapping
    public ResponseEntity<Page<DatosListadoMedico>> listadoMedicos(
            @PageableDefault(size= 10 ,page=0) Pageable paginacion){
        //Regresa todos los registros
       // return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new) );
    }
    @PutMapping
    @Transactional
    public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizarMedicos datosActualizarMedicos){
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedicos.id());
        medico.actualizarDatos(datosActualizarMedicos);
        return ResponseEntity.ok(new DatosRespuestaMedico(
                medico.getId(),
                medico.getNombre(),
                medico.getEmail(),
                medico.getTelefono(),
                medico.getDocumento(),
                medico.getEspecialidad().toString(),
                new DatosDireccion(
                        medico.getDireccion().getCalle(),
                        medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento()
                )
        ));
    }

    //  DELETE LOGICO
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }
  /*    DELETE EN LA BASE DE DATOS
    public void eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medicoRepository.delete(medico);


    }
*/
  @GetMapping("/{id}")
  @Transactional
  public ResponseEntity<DatosRespuestaMedico> retornaDatosMedico(@PathVariable Long id){
      Medico medico = medicoRepository.getReferenceById(id);
      var datosMedicos = new DatosRespuestaMedico(
              medico.getId(),
              medico.getNombre(),
              medico.getEmail(),
              medico.getTelefono(),
              medico.getDocumento(),
              medico.getEspecialidad().toString(),
              new DatosDireccion(
                      medico.getDireccion().getCalle(),
                      medico.getDireccion().getDistrito(),
                      medico.getDireccion().getCiudad(),
                      medico.getDireccion().getNumero(),
                      medico.getDireccion().getComplemento()
              ));
      return ResponseEntity.ok(datosMedicos);
  }
}
