package br.com.facilitysoft.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.facilitysoft.helpdesk.domain.Cliente;
import br.com.facilitysoft.helpdesk.domain.Pessoa;
import br.com.facilitysoft.helpdesk.repositories.ClienteRepository;
import br.com.facilitysoft.helpdesk.repositories.PessoaRepository;
import br.com.facilitysoft.helpdesk.util.dto.ClienteDTO;
import br.com.facilitysoft.helpdesk.util.exception.DataIntegrityViolationException;
import br.com.facilitysoft.helpdesk.util.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository tecnicoRepository;
	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public Cliente findById(Long id) {
		Optional<Cliente> obj = tecnicoRepository.findById(id);
		//Trantando uma exception caso o tecnico não for encontrado.
		return obj.orElseThrow(() -> new ObjectNotFoundException("Cliente com o id: "+id+" não encotrado."));
	}

	public List<Cliente> findAll() {
		return tecnicoRepository.findAll();
	}

	public Cliente create(ClienteDTO objDTO) {
		//Pora garantia o id é setado como null, para evitar erros
		objDTO.setId(null);
		//Realiza o encoder da senha
		objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		//Teste para validar se o cpf já existe
		validaPorCpfEEmail(objDTO);
		//converte o objDTO para uma instância de Cliente
		Cliente newObj = new Cliente(objDTO);
		return tecnicoRepository.save(newObj);
	}
	
	public Cliente update(Long id, @Valid ClienteDTO objDTO) {
		//Para garantir, o objDTO recebe o id que vem junto com a requisição
		objDTO.setId(id);
		//Verifica se o objeto existe no banco
		Cliente updateObj = findById(id);
		//Realiza o encoder da senha
		if(!objDTO.getSenha().equals(updateObj.getSenha())) {
			objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		}		
		//Valida se o CPF já existe e se é o mesmo CPF da requisição
		validaPorCpfEEmail(objDTO);
		//Se tudo der certo um new objeto é instâciado recebendo o objDTO como parâmetros
		updateObj = new Cliente(objDTO);
		return tecnicoRepository.save(updateObj);
	}
	
    public void delete(Long id) {
    	//Verifica se o objeto existe no banco
		Cliente obj = findById(id);
		//Teste para verificar se o tecnico tem algum chamado vinculado a ele
		if(obj.getChamados().size() > 0) {
			//Uma exceção será lancado 
			throw new DataIntegrityViolationException("O Técnico possui ordens de serviços e não pode ser deletado!");
		}
		//Se tudo deu certo ele é deletado do banco.
		tecnicoRepository.deleteById(id);		
	}

	private void validaPorCpfEEmail(ClienteDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		//valida se ele existe e se ele existir verifica se o cpf é igual ao DTO para o caso de update
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("Uma pessoa com cpf:"+objDTO.getCpf()+ "já foi cadstrado no sistema. ");
		}
		//valida se ele existe e se ele existir verifica se o E-mail é igual ao DTO para o caso de update
		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("Uma pessoa com E-mail:"+objDTO.getEmail()+ "já foi cadstrado no sistema. ");
		}
	}

}
