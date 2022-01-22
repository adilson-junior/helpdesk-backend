package br.com.facilitysoft.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.facilitysoft.helpdesk.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Long>{

}
