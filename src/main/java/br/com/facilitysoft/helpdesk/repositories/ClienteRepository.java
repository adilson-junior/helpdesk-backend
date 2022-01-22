package br.com.facilitysoft.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.facilitysoft.helpdesk.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
