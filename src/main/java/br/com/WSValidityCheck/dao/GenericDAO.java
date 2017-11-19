package br.com.WSValidityCheck.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.WSValidityCheck.util.HibernateUtil;

import java.lang.reflect.ParameterizedType;
import java.util.List;

// é a classe que ira receber as entidades 
public class GenericDAO<Entidade> {
	// a classe que se comunicar com ela passara a ter esse valor

	private Class<Entidade> classe;// metodo construtor

	@SuppressWarnings("unchecked")
	public GenericDAO() {
		this.classe = (Class<Entidade>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	public List<Entidade> listar() {
		// acessa a fabrica de sessoes
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			// é um componente do hibernate para listagem e para consultar
			Criteria consulta = sessao.createCriteria(classe);
			List<Entidade> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	//lista ordenada
	@SuppressWarnings("unchecked")
	public List<Entidade> listarOrdenado(String campoOrdenacao){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			// é um componente do hibernate para listagem e para consultar
			Criteria consulta = sessao.createCriteria(classe);
			//critério da ordenação o asc significa que vai do maior para o menor, e coloca o parametro
			consulta.addOrder(Order.asc(campoOrdenacao));
			List<Entidade> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public Entidade buscar(Long codigo){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try{
			Criteria consulta = sessao.createCriteria(classe);
			consulta.add(Restrictions.idEq(codigo));
			Entidade resultado = (Entidade)consulta.uniqueResult();
			return resultado;
		} catch (RuntimeException erro){
			throw erro;
		}finally{
			sessao.close();
		}
	}
	

	// criar uma entidade do tipo entidade
	@SuppressWarnings("unchecked")
	public Entidade salvar(Entidade entidade) {
		// criar conecçao com o banco
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		// é a acao que ocorre dentro da sessao
		// criaa transação que ocorrerá
		Transaction transacao = null;

		try {
			// inicia a transacao
			transacao = sessao.beginTransaction();
			// salva o registro no banco
			//sessao.save(entidade);
			// função do merge: se o objeto nao existe no banco ele vai salvar um novo, se ele ja existe ele vai atualizar
			entidade = (Entidade) sessao.merge(entidade);
			// commit encerra a transacao
			transacao.commit();
			return entidade;
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro;
		} finally {
			sessao.close();
		}

	}
	public void excluir(Entidade entidade) {
		// criar conecçao com o banco
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		// é a acao que ocorre dentro da sessao
		// criaa transação que ocorrerá
		Transaction transacao = null;
		//refere-se a uma acao que pode ou nao ser concluida, 
		//se da tudo certo ele conclui, se nao da um rollback e volta para o inicio 
	
		try {
			// inicia a transacao
			transacao = sessao.beginTransaction();
			// exclui o registro do banco
			sessao.delete(entidade);
			// commit encerra a transacao
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	public void editar(Entidade entidade) {
		// criar conecçao com o banco
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		// é a acao que ocorre dentro da sessao
		// criaa transação que ocorrerá
		Transaction transacao = null;
		//refere-se a uma acao que pode ou nao ser concluida, 
		//se da tudo certo ele conclui, se nao da um rollback e volta para o inicio 

		try {
			// inicia a transacao
			transacao = sessao.beginTransaction();
			// atualiza a o registro no banco
			sessao.update(entidade);
			// commit encerra a transacao
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro;
		} finally {
			sessao.close();
		}
	}

}
