package br.com.WSValidyCheck.domain;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class Lote extends GenericDomain {

	@ManyToOne
	@JoinColumn(nullable = false)
	private Produto produto;
	
	@Column(nullable = false)
	private Date validade;

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Date getValidade() {
		return validade;
	}

	public void setValidade(Date validade) {
		this.validade = validade;
	}
	
	public String getValidadeToString(){
		Locale brasil = new Locale("pt", "BR");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, brasil);
        return dateFormat.format(validade);
	}
	
}
