package br.com.WSValidityCheck.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.WSValidityCheck.domain.Lote;
import br.com.WSValidityCheck.domain.Produto;
import br.com.WSValidityCheck.domain.Secao;
import br.com.WSValidityCheck.util.HibernateUtil;

public class ProdutoDAO extends GenericDAO<Produto>{

	@SuppressWarnings("unchecked")
	public List<Produto> listarPorSecao(Secao secao){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Lote.class);
			if(secao!= null){
				consulta.add(Restrictions.eq("secao.codigo", secao.getCodigo()));
			}
			List<Produto> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
}
