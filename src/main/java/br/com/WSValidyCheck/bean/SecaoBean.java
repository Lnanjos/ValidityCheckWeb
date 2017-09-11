package br.com.WSValidyCheck.bean;


import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.omnifaces.util.Messages;

import br.com.WSValidyCheck.dao.SecaoDAO;
import br.com.WSValidyCheck.domain.Secao;

@SuppressWarnings("serial")
// A tela se comunica com a parte logica
@ManagedBean
// faz com que quando abre a tela entende que ela esta ligada com a visao que ja
// foi criada anteriormente
@ViewScoped
public class SecaoBean implements Serializable {

	private List<Secao> secoes;

	private Secao secao;

	public Secao getSecao() {
		return secao;
	}

	public void setSecao(Secao secao) {
		this.secao = secao;
	}

	public List<Secao> getSecoes() {
		return secoes;
	}

	public void setSecoes(List<Secao> secoes) {
		this.secoes = secoes;
	}

	@PostConstruct
	public void listar() {
		try {
			SecaoDAO secaoDAO = new SecaoDAO();
			secoes = secaoDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro");
			erro.printStackTrace();

		}
	}

	public void novo() {
		secao = new Secao();
		// metodo que gera um novo objeto
	}

	public void salvar() {
		try {
			SecaoDAO secaoDAO = new SecaoDAO();
			secaoDAO.salvar(secao);

			// implementação para atraves da list, e cada vez que for salvo sera
			// listado novamente os dados e serao mostrados na tela
			// dessa forma renovo a minha tabela de estados a cada exclusão ou
			// edição de dados.
			// instancia-se um novo para pode fazer os outros metodos, muda o
			// merge no genericDAO
			secao = new Secao();
			secoes = secaoDAO.listar();

			Messages.addGlobalInfo("Secão salvo com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar");
			erro.printStackTrace();
		}
	}

	// criar o método editar, usando o actionevent que gera um evento na pagina,
	// vai editar um estado,
	// meu getcomponent é a linha adicionada pela caneta, ai pega os atributos
	// do componente, e o estado selecionado
	// pega o elemento atraves do get e usa coo atributo.para isso modifica o
	// metodo salvar
	public void editar(ActionEvent evento) {
		secao = (Secao) evento.getComponent().getAttributes()
				.get("secaoSelecionado");
	}

	// o metodo excluir funciona como o editar, a cada atualização a lista é
	// gerada novamente com os dados que ainda estao salvos
	public void excluir(ActionEvent evento) {
		try {
			secao = (Secao) evento.getComponent().getAttributes()
					.get("secaoSelecionado");

			SecaoDAO secaoDAO = new SecaoDAO();
			secaoDAO.excluir(secao);

			secoes = secaoDAO.listar();
se
			Messages.addGlobalInfo("Secão excluido com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar");
			erro.printStackTrace();
		}
	}

}