package br.com.WSValidityCheck.domain;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

// A classe pode virar tabela, todas as anotações servem para fazer essa transformação 
@SuppressWarnings("serial")
@MappedSuperclass
// Serialização é o numero de identificação de cada classe
public class GenericDomain implements Serializable {

	// Cria o atributo como chave primaria
	@Id
	// Equivale o auto incremento, sera inserido no banco a cada novo registro,
	// dentro da estrategia tem o tipo de geração,
	//no caso automaticamente usando o auto ou sequencialmente com o sequence.
	@GeneratedValue(strategy = GenerationType.AUTO)
	// Ao criar o codigo com o tipo Long ele aceitara numeros maiores
	private Long codigo;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericDomain other = (GenericDomain) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Override
	public String toString() {
		return String.format("%s[codigo=%d]", getClass().getSimpleName(), getCodigo());
	}

}
