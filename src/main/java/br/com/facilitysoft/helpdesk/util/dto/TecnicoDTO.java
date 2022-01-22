package br.com.facilitysoft.helpdesk.util.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.facilitysoft.helpdesk.domain.Tecnico;
import br.com.facilitysoft.helpdesk.domain.enums.Perfil;

public class TecnicoDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected Long id;
	//Essa @NotNull anotação valida se o campo esta vindo null do front-end
	@NotNull(message = "O campo NOME é obrigatório")
	protected String nome;
	@NotNull(message = "O campo CPF é obrigatório")
	protected String cpf;
	@NotNull(message = "O campo E-MAIL é obrigatório")
	protected String email;
	@NotNull(message = "O campo SENHA é obrigatório")
	protected String senha;
	protected Set<Integer> perfis = new HashSet<>();	
	@JsonFormat(pattern = "dd/MM/yyyy")
	protected LocalDate dataCriacao = LocalDate.now();
	
	public TecnicoDTO() {
		super();
		addPerfil(Perfil.CLIENTE);
	}

	public TecnicoDTO(Tecnico obj) {
		super();
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.cpf = obj.getCpf();
		this.email = obj.getEmail();
		this.senha = obj.getSenha();
		//Converte a lista de perfis para o tipo set
		this.perfis = obj.getPerfis().stream().map(x -> x.getCodigo()).collect(Collectors.toSet());
		this.dataCriacao = obj.getDataCriacao();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}

	public void addPerfil(Perfil perfil) {
		this.perfis.add(perfil.getCodigo());
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

}
