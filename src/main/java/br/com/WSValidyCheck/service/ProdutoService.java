package br.com.WSValidyCheck.service;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import com.google.gson.Gson;
import br.com.WSValidyCheck.dao.ProdutoDAO;
import br.com.WSValidyCheck.domain.Produto;

//http://localhost:8080/Validy_Check/ws/produto
@Path("produto")
public class ProdutoService {
	@GET
	public String listar() {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		List<Produto> produtos = produtoDAO.listar();

		Gson gson = new Gson();
		String json = gson.toJson(produtos);
		return json;
	}

	@GET
	@Path("{codigo}")
	public String buscar(@PathParam("codigo") Long codigo) {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		Produto produto = produtoDAO.buscar(codigo);

		Gson gson = new Gson();
		String json = gson.toJson(produto);
		return json;
	}

	@POST
	public String salvar(String json) {
		Gson gson = new Gson();
		Produto produto = gson.fromJson(json, Produto.class);

		ProdutoDAO produtoDAO = new ProdutoDAO();
		System.out.println(produto);
		produto = produtoDAO.salvar(produto);

		System.out.println("cód:" + produto.getCodigo());

		String jsonSaida = gson.toJson(produto);

		return jsonSaida;
	}

	@PUT
	public String editar(String json) {
		Gson gson = new Gson();
		Produto produto = gson.fromJson(json, Produto.class);

		ProdutoDAO produtoDAO = new ProdutoDAO();
		produtoDAO.salvar(produto);

		String jsonSaida = gson.toJson(produto);
		return jsonSaida;
	}

	@DELETE
	public String excluir(String json) {
		System.out.println(json);
		Gson gson = new Gson();
		Produto produto = gson.fromJson(json, Produto.class);
		System.out.println(produto);
		ProdutoDAO produtoDAO = new ProdutoDAO();
		produto = produtoDAO.buscar(produto.getCodigo());
		produtoDAO.excluir(produto);
		String jsonSaida = gson.toJson(produto);
		return jsonSaida;
	}
}