package br.com.WSValidityCheck.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
public class Secao extends GenericDomain {

	@Column(length=50,nullable=false)
	@Basic(optional=false)
	private String nomeSecao;

	public String getNomeSecao() {
		return nomeSecao;
	}

	public void setNomeSecao(String nomeSecao) {
		this.nomeSecao = nomeSecao;
	}
}


