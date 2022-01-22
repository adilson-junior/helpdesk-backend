package br.com.facilitysoft.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.facilitysoft.helpdesk.domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long>{

}
