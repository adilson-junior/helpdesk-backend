package br.com.facilitysoft.helpdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.facilitysoft.helpdesk.domain.Chamado;
import br.com.facilitysoft.helpdesk.domain.Cliente;
import br.com.facilitysoft.helpdesk.domain.Tecnico;
import br.com.facilitysoft.helpdesk.domain.enums.Prioridade;
import br.com.facilitysoft.helpdesk.domain.enums.Status;
import br.com.facilitysoft.helpdesk.repositories.ChamadoRepository;
import br.com.facilitysoft.helpdesk.util.dto.ChamadoDTO;
import br.com.facilitysoft.helpdesk.util.exception.ObjectNotFoundException;

@Service
public class ChamadoService {
	
	@Autowired
	private ChamadoRepository chamadoRepository;
	@Autowired
	private TecnicoService tecnicoService;
	@Autowired
	private ClienteService clienteService;
	
	public Chamado findById(Long id) {
		//Pesquisa o objeto no banco.
		Optional<Chamado> obj = chamadoRepository.findById(id);
		//Retorna o objeto caso não encontre ele retorna uma exceção
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: "+id));
	}

	public List<Chamado> findAll() {
		return chamadoRepository.findAll();
	}

	public Chamado create(@Valid ChamadoDTO objDTO) {
		//Chamda o metodo save e passa o metodo newChamado que retorna um objeto chamado.
		return chamadoRepository.save(newChamado(objDTO));
	}
	
	public Chamado update(Long id, @Valid ChamadoDTO objDTO) {
		//Seta o id no objDTO da requisição para garantir que seja o mesmo e evite erros.
		objDTO.setId(id);
		//Teste para verificar se realmente o objeto existe, caso não exista o esse metodo já lança uma exceção.
		Chamado updateObj = findById(id);
		//Dando tudo certo todos os dados são atualizados com o objetoDTO
		updateObj = newChamado(objDTO);
		//Chama o metodo save passando o objeto atualizado		
		return chamadoRepository.save(updateObj);
	}
	
	public Chamado newChamado(ChamadoDTO objDTO) {
		//Verifica se o Técnico existe no banco.
		Tecnico tecnico = tecnicoService.findById(objDTO.getTecnico());
		//Verifica se o Cliente existe no banco.
		Cliente cliente = clienteService.findById(objDTO.getCliente());		
		//Cria um obj chamado
		Chamado chamado = new Chamado();
		//Testa para saber se o objeto é o mesmo para caso seja um update
		if(objDTO.getId() != null) {
			chamado.setId(objDTO.getId());
		}
		//Teste para saber se o Chamado foi finalizado
		if(objDTO.getStatus().equals(2)) {
			chamado.setDataFechamento(LocalDate.now());
		}
		//Seta todos os campos do objeto
		chamado.setTecnico(tecnico);
		chamado.setCliente(cliente);
		chamado.setPrioridade(Prioridade.toEnum(objDTO.getPrioridade()));
		chamado.setStatus(Status.toEnum(objDTO.getStatus()));
		chamado.setTitulo(objDTO.getTitulo());
		chamado.setObservacoes(objDTO.getObservacoes());
		return chamado;		
	}

}
