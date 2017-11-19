package br.com.WSValidityCheck.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.WSValidityCheck.domain.Lote;
import br.com.WSValidityCheck.domain.Secao;
import br.com.WSValidityCheck.service.LoteService;
import br.com.WSValidityCheck.util.HibernateUtil;

public class LoteDAO extends GenericDAO<Lote>{

	public static final long FIFTEEN_DAYS = 1296000000; 
	
	@SuppressWarnings("unchecked")
	public List<Lote> listarPorSecao(Secao secao,String dataInicial,String dataFinal){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Lote.class);
			consulta.createAlias("produto", "s");
			
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
				System.out.println("Data Inicial: "+new Date(new Long(dataInicial)).toString()
						+"\nData Final: "+new Date(new Long(dataFinal)).toString());
			}
			List<Lote> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	public int contarLotesVencendo(){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Lote.class);
			
			Date today = new Date();
			Date till = new Date();
			till.setTime(till.getTime()+FIFTEEN_DAYS);
			
			consulta.add(Restrictions.between("validade", today, till));
			System.out.println("Vencendo: "+consulta.list().size());
			
			return consulta.list().size();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	public int contarLotesVencidos(){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Lote.class);
			
			Date today = new Date();
			
			consulta.add(Restrictions.lt("validade", today));
			System.out.println("Vencidos: "+consulta.list().size());
			
			return consulta.list().size();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	public int contarLotesVencendoUmMes() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Lote.class);
			
			Date today = new Date();
			Date till = new Date();
			till.setTime(till.getTime()+FIFTEEN_DAYS+FIFTEEN_DAYS);
			
			consulta.add(Restrictions.between("validade", today, till));
			System.out.println("Vencendo: "+consulta.list().size());
			
			return consulta.list().size();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
}
