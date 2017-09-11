package br.com.WSValidyCheck.service;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.gson.Gson;
import br.com.WSValidyCheck.dao.SecaoDAO;
import br.com.WSValidyCheck.domain.Secao;


//http://localhost:8080//ws/
@Path("secao")
public class SecaoService {
	@GET
	public String listar(){
		SecaoDAO SecaoDAO = new SecaoDAO();
		List<Secao> Secaos = SecaoDAO.listar();
		
		Gson gson = new Gson();
		String json = gson.toJson(Secaos);
		return json;
	}
	

	@GET
	@Path("{codigo}")
	public String buscar(@PathParam("codigo")Long codigo){
		SecaoDAO secaoDAO = new SecaoDAO();
		Secao secao = secaoDAO.buscar(codigo);
		
		Gson gson = new Gson();
		String json = gson.toJson(secao);
		return json;
	}
	
	@POST
	public String salvar(String json){
		Gson gson = new Gson();
		Secao secao = gson.fromJson(json, Secao.class);
		
		SecaoDAO secaoDAO = new SecaoDAO();
		System.out.println(secao);
		secaoDAO.salvar(secao);
		
		String jsonSaida = gson.toJson(secao);
		
		return jsonSaida;
	}
	
	@PUT
	public String editar(String json){
		Gson gson = new Gson();
		Secao secao = gson.fromJson(json, Secao.class);
		
		SecaoDAO secaoDAO = new SecaoDAO();
		secaoDAO.salvar(secao);
		
		String jsonSaida = gson.toJson(secao);
		return jsonSaida;
	}
	
	@DELETE
	public String excluir(String json){
		System.out.println(json);
		Gson gson = new Gson();
		Secao secao = gson.fromJson(json, Secao.class);
		System.out.println(secao);
		SecaoDAO secaoDAO = new SecaoDAO();
		secao = secaoDAO.buscar(secao.getCodigo());
		secaoDAO.excluir(secao);
		String jsonSaida = gson.toJson(secao);
		return jsonSaida;
	}
	 

}
