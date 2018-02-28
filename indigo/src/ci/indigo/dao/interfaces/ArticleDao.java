package ci.indigo.dao.interfaces;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import ci.indigo.dao.DaoException;
import ci.indigo.entities.Article;


public interface ArticleDao {

	List<Article> lister();
	List<Article> lister(String codeCommande);
	Map<String,Article> listerMap();
	Boolean supprimer(Article article) throws DaoException;
	Boolean ajouter(Article article) throws DaoException;
	Boolean modifier(Article article) throws DaoException;
	Article rechercher(String code) throws DaoException;
	Long rechercher(Date date) throws DaoException;
	Map<String, String> listerCategorie();
		
}
