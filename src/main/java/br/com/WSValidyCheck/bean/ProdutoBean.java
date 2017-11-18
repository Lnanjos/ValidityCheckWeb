package br.com.WSValidyCheck.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;

import br.com.WSValidyCheck.dao.ProdutoDAO;
import br.com.WSValidyCheck.dao.SecaoDAO;
import br.com.WSValidyCheck.domain.Produto;
import br.com.WSValidyCheck.domain.Secao;
import br.com.WSValidyCheck.util.HibernateUtil;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ProdutoBean implements Serializable {
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

	public List<Secao> getSecoes() {
		return secoes;
	}

	public void setSecoes(List<Secao> secoes) {
		this.secoes = secoes;
	}

	@PostConstruct
	public void listar() {
		try {
			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtos = produtoDAO.listarOrdenado("nomeProduto");
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

			Messages.addGlobalInfo("Produto salvo com sucesso");
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

			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtos = produtoDAO.listar();
			
			SecaoDAO secaoDAO = new SecaoDAO();
			secoes = secaoDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma produto");
			erro.printStackTrace();
		}
	}
	
	public void imprimir(){
		try {
			DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
			Map<String, Object> filtros = tabela.getFilters();

			String filterCodBarra = "%"+(String) filtros.get("codBarraProduto")+"%";
			String filterNomeProduto = "%"+(String) filtros.get("nomeProduto")+"%";
			String filterSecao = "%"+(String) filtros.get("secao.codigo} #{produto.secao.nomeSecao")+"%";
			
			System.out.println(filtros);
			
			
			// caminho de acesso ao relatório
			String caminho = Faces.getRealPath("/reports/produtos.jasper");

			/*
			 * Método para usar a imagem no relatório String logo =
			 * Faces.getRealPath("/reports/logo2.png");
			 * System.out.println(logo); parametros.put("reportlogo", logo);
			 */

			Map<String, Object> parametros = new HashMap<>();
			if(!filtros.isEmpty()){
				if (!filterCodBarra.equals("%null%")) {
					parametros.put("COD_BARRA", filterCodBarra);
				}
				if (!filterNomeProduto.equals("%null%")){
					parametros.put("NOME_PRODUTO", filterNomeProduto);
				}
				if(!filterSecao.equals("%null%")){
					parametros.put("NOME_SECAO", filterSecao);
				}
			}
			
			//cria a conexão
			Connection conexao = HibernateUtil.getConexao();
			
			//Criação do relatório
			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros,conexao);
			JasperPrintManager.printReport(relatorio, false);
					
		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Ocorreu um erro ao gerar relatório");
		}
	}
}