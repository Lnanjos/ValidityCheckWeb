package br.com.WSValidyCheck.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.omnifaces.util.Messages;
import br.com.WSValidyCheck.dao.ProdutoDAO;
import br.com.WSValidyCheck.domain.Produto;

@ManagedBean
@ViewScoped
public class ProdutoBean {
	
	private List<Produto> produtos;
	
	private Produto produto;

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	@PostConstruct
	public void listar() {
		try {
			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtos = produtoDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro");
			erro.printStackTrace();

		}
	}

	public void novo() {
		produto = new Produto();
	}

	public void salvar() {
		try {
			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtoDAO.salvar(produto);
			produto = new Produto();
			produtos = produtoDAO.listar();

			Messages.addGlobalInfo("Produto salvo com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {
		produto = (Produto) evento.getComponent().getAttributes()
				.get("produtoSelecionado");
	}

	public void excluir(ActionEvent evento) {
		try {
			produto = (Produto) evento.getComponent().getAttributes()
					.get("produtoSelecionado");

			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtoDAO.excluir(produto);

			produtos = produtoDAO.listar();
			Messages.addGlobalInfo("Produto excluido com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar");
			erro.printStackTrace();
		}
	}

}
