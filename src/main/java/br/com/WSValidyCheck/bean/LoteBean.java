package br.com.WSValidyCheck.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.WSValidyCheck.dao.LoteDAO;
import br.com.WSValidyCheck.dao.ProdutoDAO;
import br.com.WSValidyCheck.domain.Lote;
import br.com.WSValidyCheck.domain.Produto;

@ManagedBean
@SuppressWarnings("serial")
@ViewScoped
public class LoteBean implements Serializable {

	private List<Produto> produtos;
	
	private List<Lote> lotes;
	
	private Produto produto;
	
	private Lote lote;
	
	//usada para definir a data minima na hora de salvar um lote
	private Date dataAtual = new Date();

	public List<Lote> getLotes() {
		return lotes;
	}

	public void setLotes(List<Lote> lotes) {
		this.lotes = lotes;
	}

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}
	
	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	@PostConstruct
	public void listar() {
		try {
			LoteDAO loteDAO = new LoteDAO();
			lotes = loteDAO.listarOrdenado("validade");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar listar as lotes");
			erro.printStackTrace();
		}
	}

	public void novo() {
		try {
			// metodo que gera um novo objeto
			lote = new Lote();

			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtos = produtoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao gerar uma nova lote");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			LoteDAO loteDAO = new LoteDAO();
			lote.setProduto(produto);
			
			loteDAO.salvar(lote);

			// implementação para atraves da list, e cada vez que for salvo sera
			// listado novamente os dados e serao mostrados na tela
			// dessa forma renovo a minha tabela de produtos a cada exclusão ou
			// edição de dados.
			// instancia-se um novo para pode fazer os outros metodos, muda o
			// merge no genericDAO
			lote = new Lote();

			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtos = produtoDAO.listar();

			lotes = loteDAO.listar();

			Messages.addGlobalInfo("Lote salvo com sucesso");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar uma nova lote");
			erro.printStackTrace();
		}
	}

	public void excluir(ActionEvent evento) {
		try {
			lote = (Lote) evento.getComponent().getAttributes()
					.get("loteSelecionada");

			LoteDAO loteDAO = new LoteDAO();
			loteDAO.excluir(lote);

			lotes = loteDAO.listar();

			Messages.addGlobalInfo("Lote excluido com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {

		try {
			lote = (Lote) evento.getComponent().getAttributes()
					.get("loteSelecionada");

			LoteDAO loteDAO = new LoteDAO();
			lotes = loteDAO.listar();
			
			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtos = produtoDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma lote");
			erro.printStackTrace();
		}
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}
}
