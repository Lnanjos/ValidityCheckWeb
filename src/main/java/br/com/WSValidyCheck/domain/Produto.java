package br.com.WSValidyCheck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class Produto extends GenericDomain{
	
	@Column(length = 100,nullable = false)
	private String nomeProduto;
	
	@Column(length = 44,nullable = false)
	private String codBarraProduto;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Secao secao;

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getCodBarraProduto() {
		return codBarraProduto;
	}

	public void setCodBarraProduto(String codBarraProduto) {
		this.codBarraProduto = codBarraProduto;
	}

	public Secao getSecao() {
		return secao;
	}

	public void setSecao(Secao secao) {
		this.secao = secao;
	}
	
}
