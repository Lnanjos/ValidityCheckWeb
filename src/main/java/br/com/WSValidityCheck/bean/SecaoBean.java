package br.com.WSValidityCheck.bean;

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
import br.com.WSValidityCheck.dao.SecaoDAO;
import br.com.WSValidityCheck.domain.Secao;
import br.com.WSValidityCheck.util.HibernateUtil;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

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
			secoes = secaoDAO.listarOrdenado("codigo");
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

			secao = new Secao();
			secoes = secaoDAO.listar();

			Messages.addGlobalInfo("Seção salvo com sucesso");
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
		secao = (Secao) evento.getComponent().getAttributes().get("secaoSelecionado");
	}

	// o metodo excluir funciona como o editar, a cada atualização a lista é
	// gerada novamente com os dados que ainda estao salvos
	public void excluir(ActionEvent evento) {
		try {
			secao = (Secao) evento.getComponent().getAttributes().get("secaoSelecionado");

			SecaoDAO secaoDAO = new SecaoDAO();
			secaoDAO.excluir(secao);

			secoes = secaoDAO.listar();
			Messages.addGlobalInfo("Seção excluido com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar");
			erro.printStackTrace();
		}
	}

	public void imprimir() {
		try {
			DataTable tabela = (DataTable) Faces.getViewRoot().findComponent("formListagem:tabela");
			Map<String, Object> filtros = tabela.getFilters();
			
			System.out.println(filtros);

			String filterCodigo = "%"+(String) filtros.get("codigo")+"%";
			String filterNomeSecao = "%"+(String) filtros.get("nomeSecao")+"%";

			// caminho de acesso ao relatório
			String caminho = Faces.getRealPath("/reports/secoes.jasper");

			/*
			 * Método para usar a imagem no relatório String logo =
			 * Faces.getRealPath("/reports/logo2.png");
			 * System.out.println(logo); parametros.put("reportlogo", logo);
			 */

			Map<String, Object> parametros = new HashMap<>();
			if (!filtros.isEmpty()){
				if (!filterCodigo.equals("%null%")) {
					parametros.put("CODIGO", filterCodigo);
				}
				if (!filterNomeSecao.equals("%null%")){
					parametros.put("NOME_SECAO", filterNomeSecao);
				}	
			}
			
			System.out.println(parametros);
			
			// cria a conexão
			Connection conexao = HibernateUtil.getConexao();

			// Criação do relatório
			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
			JasperPrintManager.printReport(relatorio, false);

		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Ocorreu um erro ao gerar relatório");
		}
	}

}