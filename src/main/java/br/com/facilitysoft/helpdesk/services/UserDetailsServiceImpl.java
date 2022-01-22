package br.com.facilitysoft.helpdesk.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.facilitysoft.helpdesk.config.security.UserSS;
import br.com.facilitysoft.helpdesk.domain.Pessoa;
import br.com.facilitysoft.helpdesk.repositories.PessoaRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Pessoa> user = pessoaRepository.findByEmail(email);
		//Teste para saber se esse usuário existe
		if(user.isPresent()) {
			return new UserSS(user.get().getId(), user.get().getEmail(), user.get().getSenha(), user.get().getPerfis());
		}
		//Caso não for encontrado um usuário com esse email lança essa exceção.
		throw new UsernameNotFoundException(email);
	}
		
}
