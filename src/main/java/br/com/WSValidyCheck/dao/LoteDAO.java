package br.com.WSValidyCheck.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import br.com.WSValidyCheck.domain.Lote;
import br.com.WSValidyCheck.domain.Secao;
import br.com.WSValidyCheck.service.LoteService;
import br.com.WSValidyCheck.util.HibernateUtil;

public class LoteDAO extends GenericDAO<Lote>{

	@SuppressWarnings("unchecked")
	public List<Lote> listarPorSecao(Secao secao,String dataInicial,String dataFinal){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Lote.class);
			consulta.createAlias("produto", "s");
			System.out.println("Data Inicial: "+new Date(new Long(dataInicial)).toString()
					+"\nData Final: "+new Date(new Long(dataFinal)).toString());
			consulta.addOrder(Order.asc("validade"));
			if(secao!= null){
				consulta.add(Restrictions.eq("s.secao.codigo", secao.getCodigo()));
			}
			if(!dataInicial.equals(LoteService.EMPTY.toString())&&!dataFinal.equals(LoteService.EMPTY.toString())){
				consulta.add(Restrictions.between("validade",new Date(new Long(dataInicial)),new Date(new Long(dataFinal))));
			}else if(!dataInicial.equals(LoteService.EMPTY.toString())){
				consulta.add(Restrictions.gt("validade", new Date(new Long(dataInicial))));
			}else if(!dataFinal.equals(LoteService.EMPTY.toString())){
				consulta.add(Restrictions.lt("validade", new Date(new Long(dataFinal))));
			}
			List<Lote> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
}
