package br.com.facilitysoft.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.facilitysoft.helpdesk.domain.Pessoa;
import br.com.facilitysoft.helpdesk.domain.Tecnico;
import br.com.facilitysoft.helpdesk.repositories.PessoaRepository;
import br.com.facilitysoft.helpdesk.repositories.TecnicoRepository;
import br.com.facilitysoft.helpdesk.util.dto.TecnicoDTO;
import br.com.facilitysoft.helpdesk.util.exception.DataIntegrityViolationException;
import br.com.facilitysoft.helpdesk.util.exception.ObjectNotFoundException;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public Tecnico findById(Long id) {
		Optional<Tecnico> obj = tecnicoRepository.findById(id);
		//Trantando uma exception caso o tecnico não for encontrado.
		return obj.orElseThrow(() -> new ObjectNotFoundException("Tecnico com o id: "+id+" não encotrado."));
	}

	public List<Tecnico> findAll() {
		return tecnicoRepository.findAll();
	}

	public Tecnico create(TecnicoDTO objDTO) {
		//Por garantia o id é setado como null, para evitar erros
		objDTO.setId(null);
		//Teste para validar se o cpf já existe
		//Realiza o encoder da senha
		objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		validaPorCpfEEmail(objDTO);
		//converte o objDTO para uma instância de Tecnico
		Tecnico newObj = new Tecnico(objDTO);
		return tecnicoRepository.save(newObj);
	}
	
	public Tecnico update(Long id, @Valid TecnicoDTO objDTO) {
		//Para garantir, o objDTO recebe o id que vem junto com a requisição
		objDTO.setId(id);
		//Verifica se o objeto existe no banco
		Tecnico updateObj = findById(id);
		//Realiza o encoder da senha
		if(!objDTO.getSenha().equals(updateObj.getSenha())) {
			objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		}		
		//Valida se o CPF já existe e se é o mesmo CPF da requisição
		validaPorCpfEEmail(objDTO);
		//Se tudo der certo um new objeto é instâciado recebendo o objDTO como parâmetros
		updateObj = new Tecnico(objDTO);
		return tecnicoRepository.save(updateObj);
	}
	
    public void delete(Long id) {
    	//Verifica se o objeto existe no banco
		Tecnico obj = findById(id);
		//Teste para verificar se o tecnico tem algum chamado vinculado a ele
		if(obj.getChamados().size() > 0) {
			//Uma exceção será lancado 
			throw new DataIntegrityViolationException("O Técnico possui ordens de serviços e não pode ser deletado!");
		}
		//Se tudo deu certo ele é deletado do banco.
		tecnicoRepository.deleteById(id);		
	}

    private void validaPorCpfEEmail(TecnicoDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}

		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
		}
	}

}
