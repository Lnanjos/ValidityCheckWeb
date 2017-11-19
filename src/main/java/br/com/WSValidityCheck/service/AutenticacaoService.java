package br.com.WSValidityCheck.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.gson.Gson;

import br.com.WSValidityCheck.dao.UsuarioDAO;
import br.com.WSValidityCheck.domain.Usuario;

//http://localhost:8080/Validy_Check/ws/autenticacao
@Path("autenticacao")
public class AutenticacaoService {
	
	@GET
	public String isOnline(){
		return ""+true;
	}
	
	//caso seja implementado pra buscar se um usuario está ativo ou não
	@GET
	@Path("{codigo}")
	public String buscar(@PathParam("codigo")Long codigo){
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.buscar(codigo);
		
		Gson gson = new Gson();
		String json = gson.toJson(usuario);
		return json;
	}
	
	@POST
	public String salvar(String json){
		Gson gson = new Gson();
		System.out.println(json);
		Usuario usuario = gson.fromJson(json, Usuario.class);
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		System.out.println(usuario);
		usuario = usuarioDAO.autenticar(usuario.getLogin(), usuario.getSenha());
		
		String jsonSaida = "fail";
		if (usuario != null){
			System.out.println("cód:"+usuario.getCodigo());
			jsonSaida = usuario.getAtivoFormatado();	
		}
		return jsonSaida;
	}
}