package br.com.WSValidityCheck.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
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

import br.com.WSValidityCheck.dao.LoteDAO;
import br.com.WSValidityCheck.dao.ProdutoDAO;
import br.com.WSValidityCheck.domain.Lote;
import br.com.WSValidityCheck.domain.Produto;
import br.com.WSValidityCheck.util.HibernateUtil;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

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

	private int lotesVencendo = lotesVencendo();
	
	private boolean temLotesVencendo = temLotesVencendo();
	
	private int lotesVencidos = lotesVencidos();
	
	private boolean temLotesVencidos = temLotesVencidos();
	
	private int lotesVencendoUmMes = lotesVencendoUmMes();
	
	private boolean temLotesVencendoUmMes = temLotesVencendoUmMes();
	
	public int getLotesVencendo() {
		return lotesVencendo;
	}

	public void setLotesVencendo(int lotesVencendo) {
		this.lotesVencendo = lotesVencendo;
	}

	public boolean isTemLotesVencendo() {
		return temLotesVencendo;
	}

	public void setTemLotesVencendo(boolean temLotesVencendo) {
		this.temLotesVencendo = temLotesVencendo;
	}

	public int getLotesVencidos() {
		return lotesVencidos;
	}

	public void setLotesVencidos(int lotesVencidos) {
		this.lotesVencidos = lotesVencidos;
	}

	public boolean isTemLotesVencidos() {
		return temLotesVencidos;
	}

	public void setTemLotesVencidos(boolean temLotesVencidos) {
		this.temLotesVencidos = temLotesVencidos;
	}

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
	
	public void imprimir(){
		try {
			DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
			Map<String, Object> filtros = tabela.getFilters();
			
			System.out.println(filtros);

			String filterCodBarra = "%"+(String) filtros.get("produto.codBarraProduto")+"%";
			String filterNomeProduto = "%"+(String) filtros.get("produto.nomeProduto")+"%";
			String filterNomeSecao = "%"+(String) filtros.get("produto.secao.codigo} #{produto.secao.nomeSecao")+"%";
			String filterData = "%"+(String) filtros.get("dataValidade")+"%";

			// caminho de acesso ao relatório
			String caminho = Faces.getRealPath("/reports/lotes.jasper");

			/*
			 * Método para usar a imagem no relatório String logo =
			 * Faces.getRealPath("/reports/logo2.png");
			 * System.out.println(logo); parametros.put("reportlogo", logo);
			 */

			Map<String, Object> parametros = new HashMap<>();
			if(!filtros.isEmpty()){
				if (!filterCodBarra.equals("%null%")) {
					parametros.put("CODIGO", filterCodBarra);
				}
				if (!filterNomeProduto.equals("%null%")){
					parametros.put("NOME_PRODUTO", filterNomeProduto);
				}
				if (!filterNomeSecao.equals("%null%")){
					parametros.put("NOME_SECAO", filterNomeSecao);
				}
				if(!filterData.equals("%null%")){
					parametros.put("DATA_VALIDADE", filterData);
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
	
	public int lotesVencendo(){
		LoteDAO loteDAO = new LoteDAO();
		return loteDAO.contarLotesVencendo();
	}
	
	private boolean temLotesVencendo() {
		if(lotesVencendo>0){
			return true;	
		}else{
			return false;
		}	
	}
	
	public int lotesVencidos(){
		LoteDAO loteDAO = new LoteDAO();
		return loteDAO.contarLotesVencidos();
	}
	
	private boolean temLotesVencidos() {
		if(lotesVencidos>0){
			return true;	
		}else{
			return false;
		}	
	}
	
	private boolean temLotesVencendoUmMes() {
		if(lotesVencendoUmMes>0){
			return true;	
		}else{
			return false;
		}	
	}
	
	private int lotesVencendoUmMes() {
		LoteDAO loteDAO = new LoteDAO();
		return loteDAO.contarLotesVencendoUmMes();
	}
	
	public int getLotesVencendoUmMes() {
		return lotesVencendoUmMes;
	}

	public void setLotesVencendoUmMes(int lotesVencendoUmMes) {
		this.lotesVencendoUmMes = lotesVencendoUmMes;
	}

	public boolean isTemLotesVencendoUmMes() {
		return temLotesVencendoUmMes;
	}

	public void setTemLotesVencendoUmMes(boolean temLotesVencendoUmMes) {
		this.temLotesVencendoUmMes = temLotesVencendoUmMes;
	}
}
