package br.com.facilitysoft.helpdesk.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.facilitysoft.helpdesk.domain.Chamado;
import br.com.facilitysoft.helpdesk.services.ChamadoService;
import br.com.facilitysoft.helpdesk.util.dto.ChamadoDTO;

@RestController
@RequestMapping(value = "/chamados")
public class ChamadoResource {
	
	@Autowired
	private ChamadoService chamadoService;
	
	
	@PostMapping
	private ResponseEntity<ChamadoDTO> create(@Valid @RequestBody ChamadoDTO objDTO) {
		Chamado obj = chamadoService.create(objDTO);
		URI uri = ServletUriComponentsBuilder.
				fromCurrentRequestUri().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping(value = "/{id}")
	private ResponseEntity<ChamadoDTO> findById(@PathVariable Long id) {
		Chamado obj = chamadoService.findById(id);
		return ResponseEntity.ok().body(new ChamadoDTO(obj));
	}
	
	@GetMapping
	public ResponseEntity<List<ChamadoDTO>> findAll() {
		List<Chamado> list = chamadoService.findAll();
		List<ChamadoDTO> listDTO = list.stream().map(obj -> new ChamadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ChamadoDTO> update(@PathVariable Long id, @Valid @RequestBody ChamadoDTO objDTO) {
		Chamado updateObj = chamadoService.update(id, objDTO);
		//Retorna um ChamadoDTO passando o Chamado atualizado com parâmetro no costrutor.
		return ResponseEntity.ok().body(new ChamadoDTO(updateObj));
	}
}
