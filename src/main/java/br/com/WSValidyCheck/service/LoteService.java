package br.com.WSValidyCheck.service;

import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gson.Gson;
import br.com.WSValidyCheck.dao.LoteDAO;
import br.com.WSValidyCheck.domain.Lote;
import br.com.WSValidyCheck.domain.Secao;

//http://localhost:8080/Validy_Check/ws/lote
@Path("lote")
public class LoteService {
	
	public static final Long EMPTY = (long) 0;
	
	@GET
	public String listar() {
		LoteDAO loteDAO = new LoteDAO();
		List<Lote> lotes = loteDAO.listarOrdenado("validade");

		Gson gson = new Gson();
		String json = gson.toJson(lotes);
		return json;
	}

	@GET
	@Path("listar")
	@Produces("text/plain")
	public String listarPorSecao(@QueryParam("secao") String secaoString,
			@QueryParam("dataInicial") String dataInicial,
			@QueryParam("dataFinal") String dataFinal) {
		Secao secao = null;
		Gson gson = new Gson();
		if(!secaoString.isEmpty()){
			secao = gson.fromJson(secaoString, Secao.class);
		}
		
		LoteDAO loteDAO = new LoteDAO();
		List<Lote> lotes = loteDAO.listarPorSecao(secao,dataInicial,dataFinal);
		String json = gson.toJson(lotes);
		return json;
	}
	
	@POST
	public String salvar(String json) {
		Gson gson = new Gson();
		Lote lote = gson.fromJson(json, Lote.class);

		LoteDAO loteDAO = new LoteDAO();
		System.out.println(lote);
		lote = loteDAO.salvar(lote);

		System.out.println("cód:" + lote.getCodigo());

		String jsonSaida = gson.toJson(lote);

		return jsonSaida;
	}

	@PUT
	public String editar(String json) {
		Gson gson = new Gson();
		Lote lote = gson.fromJson(json, Lote.class);

		LoteDAO loteDAO = new LoteDAO();
		loteDAO.salvar(lote);

		String jsonSaida = gson.toJson(lote);
		return jsonSaida;
	}

	@DELETE
	public String excluir(String json) {
		System.out.println(json);
		Gson gson = new Gson();
		Lote lote = gson.fromJson(json, Lote.class);
		System.out.println(lote);
		LoteDAO loteDAO = new LoteDAO();
		lote = loteDAO.buscar(lote.getCodigo());
		loteDAO.excluir(lote);
		String jsonSaida = gson.toJson(lote);
		return jsonSaida;
	}
}