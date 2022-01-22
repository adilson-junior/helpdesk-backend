package br.com.facilitysoft.helpdesk;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.facilitysoft.helpdesk.domain.Chamado;
import br.com.facilitysoft.helpdesk.domain.Cliente;
import br.com.facilitysoft.helpdesk.domain.Tecnico;
import br.com.facilitysoft.helpdesk.domain.enums.Perfil;
import br.com.facilitysoft.helpdesk.domain.enums.Prioridade;
import br.com.facilitysoft.helpdesk.domain.enums.Status;
import br.com.facilitysoft.helpdesk.repositories.ChamadoRepository;
import br.com.facilitysoft.helpdesk.repositories.ClienteRepository;
import br.com.facilitysoft.helpdesk.repositories.TecnicoRepository;



@SpringBootApplication
public class HelpdeskApplication /* implements CommandLineRunner */{
	//Usando a injeção de dependencias do Spring boot		
	/*
	 * @Autowired private TecnicoRepository tecnicoRepository;
	 * 
	 * @Autowired private ClienteRepository clienteRepository;
	 * 
	 * @Autowired private ChamadoRepository chamadoRepository;
	 * 
	 * @Autowired private BCryptPasswordEncoder encoder;
	 */
	//Fim da injeção de dependencias.

	public static void main(String[] args) {
		SpringApplication.run(HelpdeskApplication.class, args);
	}
	/*
	@Override
	public void run(String... args) throws Exception {		
		  //Iniciando uma carga inicial no banco de dados. //Add um Tecnico 			  
			  Tecnico tecnico1 = new Tecnico(null,  "Adilson", "09402050760", "adilsonvajr@gmail.com", encoder.encode("123456")); 
			  tecnico1.addPerfil(Perfil.ADMIN);			 
		  //Add um  Cliente 			
			  Cliente cliente1 = new Cliente(null, "Vasconcelos Studio De Beleza", "11663254745", "vaneza.mylena@hotmail.com", encoder.encode("123456"));  			 
		  //Add um Chamado 			
			  Chamado chamado1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 0", "Ajuda com site", cliente1, tecnico1);			 
		  //Usando o saveAll eu posso passar um ou uma lista de objetos			
			  tecnicoRepository.saveAll(Arrays.asList(tecnico1));			  
			  clienteRepository.saveAll(Arrays.asList(cliente1));			  
			  chamadoRepository.saveAll(Arrays.asList(chamado1));			  
	}		*/

}
