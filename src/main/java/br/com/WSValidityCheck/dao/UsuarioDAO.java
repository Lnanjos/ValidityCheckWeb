package br.com.WSValidityCheck.dao;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.WSValidityCheck.domain.Usuario;
import br.com.WSValidityCheck.util.HibernateUtil;

public class UsuarioDAO extends GenericDAO<Usuario> {
	public Usuario autenticar(String login, String senha) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		try {
			Criteria consulta = sessao.createCriteria(Usuario.class);
			consulta.add(Restrictions.eq("login", login));
			consulta.add(Restrictions.eq("ativo", true));
			// algoritmo de criptografia
			// utilização do md5
			// gera uma sequencia hexadecimal de 32 caracteres
			SimpleHash hash = new SimpleHash("md5", senha);

			// tohex() é o metodo utilizado para aplicar o metodo
			consulta.add(Restrictions.eq("senha", hash.toHex()));
			//retorna apenas um resultado e faz um cast para usuario
			Usuario resultado = (Usuario) consulta.uniqueResult();

			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
}
