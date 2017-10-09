package br.com.WSValidyCheck.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.omnifaces.util.Messages;
import br.com.WSValidyCheck.dao.ProdutoDAO;
import br.com.WSValidyCheck.dao.SecaoDAO;
import br.com.WSValidyCheck.domain.Produto;
import br.com.WSValidyCheck.domain.Secao;

@ManagedBean
@ViewScoped
public class ProdutoBean {
	private Produto produto;

	private List<Produto> produtos;
	private List<Secao> secoes;

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<Secao> getSecaos() {
		return secoes;
	}

	public void setSecaos(List<Secao> secoes) {
		this.secoes = secoes;
	}

	@PostConstruct
	public void listar() {
		try {
			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtos = produtoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar listar as produtos");
			erro.printStackTrace();
		}
	}

	public void novo() {
		try {
			// metodo que gera um novo objeto
			produto = new Produto();

			SecaoDAO secaoDAO = new SecaoDAO();
			secoes = secaoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao gerar uma nova produto");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtoDAO.salvar(produto);

			// implementação para atraves da list, e cada vez que for salvo sera
			// listado novamente os dados e serao mostrados na tela
			// dessa forma renovo a minha tabela de secoes a cada exclusão ou
			// edição de dados.
			// instancia-se um novo para pode fazer os outros metodos, muda o
			// merge no genericDAO
			produto = new Produto();

			SecaoDAO secaoDAO = new SecaoDAO();
			secoes = secaoDAO.listar();

			produtos = produtoDAO.listar();

			Messages.addGlobalInfo("Produto salva com sucesso");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar uma nova produto");
			erro.printStackTrace();
		}
	}

	public void excluir(ActionEvent evento) {
		try {
			produto = (Produto) evento.getComponent().getAttributes()
					.get("produtoSelecionada");

			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtoDAO.excluir(produto);

			produtos = produtoDAO.listar();

			Messages.addGlobalInfo("Produto excluido com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {

		try {
			produto = (Produto) evento.getComponent().getAttributes()
					.get("produtoSelecionada");

			SecaoDAO secaoDAO = new SecaoDAO();
			secoes = secaoDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma produto");
			erro.printStackTrace();

		}
		// tabela hash: tabela de endereçamento, separa os codigos e
		// endereçamento

	}
}