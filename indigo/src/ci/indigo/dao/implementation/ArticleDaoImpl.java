package ci.indigo.dao.implementation;

import static ci.indigo.dao.DaoUtilitaire.fermeturesSilencieuses;
import static ci.indigo.dao.DaoUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ci.indigo.dao.DaoFactory;
import ci.indigo.dao.DaoException;
import ci.indigo.dao.interfaces.ArticleDao;
import ci.indigo.entities.Article;



public class ArticleDaoImpl implements ArticleDao{
	
	private DaoFactory daoFactory;
	
	private static final String SQL_SELECT_ALL = "SELECT  a.code, a.code_categorie, a.designation, a.quantite,"
			+ " a.prix_unitaire, a.date, c.designation as libelleCategorie FROM Articles as a, Categories as c"
			+ " WHERE a.code_categorie = c.code ORDER BY a.code";
	//private static final String SQL_SELECT_PAR_CODE = "SELECT * FROM Articles  WHERE code = ?";
	private static final String SQL_SELECT_ARTICLES_PAR_CODE_COMMANDE = "SELECT a.code, a.code_categorie, a.designation, "
			+ "lc.quantite, a.prix_unitaire, a.date,c.designation as libelleCategorie FROM articles as a, lignes_commandes as lc,Categories as c "
			+ "WHERE a.code IN (SELECT code_article FROM lignes_commandes WHERE code_commande = ? ) "
			+ "AND a.code = lc.code_article AND  a.code_categorie = c.code AND lc.code_commande = ?";
	private static final String SQL_SELECT_PAR_DATE = "SELECT COUNT(code) FROM Articles  WHERE date LIKE ?";
	
	private static final String SQL_SELECT_ALL_CATEGORIES = "SELECT code, designation FROM categories";
	
	private static final String SQL_INSERT = "INSERT INTO Articles (code,code_categorie, designation,quantite,prix_unitaire,date)"
			+ "VALUES (?,?,?,?,?,?)";
	private static final String SQL_MODIFIER_PAR_CODE = "UPDATE Articles SET code_categorie= ?, designation = ?,"
			+ "quantite = ?,prix_unitaire= ? WHERE code = ?";
	private static final String SQL_DELETE_PAR_CODE = "DELETE FROM Articles WHERE code = ?";
	private static final String SQL_DELETE_ARTICLE_DANS_LIGNES_COMMANDES = "DELETE FROM lignes_commandes WHERE code_article = ?";
	
	public ArticleDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<Article> lister() {
		List<Article> articles = new ArrayList<Article>();
		Connection connexion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connexion = daoFactory.getConnection();
			statement = connexion.createStatement();
			resultSet=statement.executeQuery(SQL_SELECT_ALL);
			
			while(resultSet.next()) {
				// Ajout de l'article dans le tableau
				articles.add(map(resultSet));
			}
			
		}catch( SQLException e ) {
			System.out.println(e.getMessage());
		}finally {
			fermeturesSilencieuses( resultSet,statement, connexion );
		}
		
		return articles;
	}
	
	/**
	 * @author Moro
	 * @param String
	 * @return List<Article>
	 * */	
	
	@Override
	public List<Article> lister(String codeCommande) {
		List<Article> articles = new ArrayList<Article>();
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_ARTICLES_PAR_CODE_COMMANDE, false,codeCommande,codeCommande);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				// Ajout de l'article dans le tableau
				articles.add(map(resultSet));
			}
			
		}catch( SQLException e ) {
			System.out.println(e.getMessage());
		}finally {
			fermeturesSilencieuses( preparedStatement , connexion );
		}
		
		return articles;
	}	
	
	/**
	 * @author Moro
	 * @param void
	 * @return  Map<String, Article>
	 * */	
	@Override
	public Map<String, Article> listerMap() {
		Map<String,Article> articlesMap = new LinkedHashMap<String,Article>();
		Connection connexion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connexion = daoFactory.getConnection();
			statement = connexion.createStatement();
			resultSet=statement.executeQuery(SQL_SELECT_ALL);
			
			while(resultSet.next()) {
				// Ajout de l'article dans le tableau
				articlesMap.put(resultSet.getString("code"),map(resultSet));
			}
			
		}catch( SQLException e ) {
			System.out.println(e.getMessage());
		}finally {
			fermeturesSilencieuses( resultSet,statement, connexion );
		}
		
		return articlesMap;
	}
	
	/**
	 * @author Moro
	 * @param Article
	 * @return  Boolean
	 * */		
	@Override
	public Boolean ajouter(Article article) throws DaoException{
//
		
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		//Statement statement = null;
		Boolean resultat=false;
		//ResultSet resultSet = null;
		//ResultSet valeursAutoGenerees = null;
		
		try {
			
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(
			connexion, SQL_INSERT, true, 
			article.getCode(), article.getCodeCategorie(), article.getDesignation(),
			article.getQuantite(),article.getPrix(),article.getDate());
			int statut = preparedStatement.executeUpdate();
			
			if(statut!=0) {
				resultat = true;
			}
			
			
		}catch(SQLException e ) {
			System.out.println(e.getMessage());
		}finally {
			fermeturesSilencieuses( preparedStatement, connexion );
		}
		
		return resultat;
	}
	
	/**
	 * @author Moro
	 * @param Article
	 * @return  Boolean
	 * */	
	@Override
	public Boolean modifier(Article article) throws DaoException{
		Connection connexion = null;
		PreparedStatement  preparedStatement  = null;
		Boolean resultat = false;
		
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_MODIFIER_PAR_CODE, true,article.getCodeCategorie(),
					article.getDesignation(),article.getQuantite(),article.getPrix(),article.getCode() );
			int statut = preparedStatement.executeUpdate();
			if(statut == 0) {
				throw new DaoException( "echec de la modification de l'article, aucune ligne modifiée dans table." );
			}else {
				resultat = true;
			}
		} catch ( SQLException e ) {
			throw new DaoException( e );
		} finally {
			fermeturesSilencieuses( preparedStatement, connexion );
		}
		
		return resultat;
	}
	
	/**
	 * @author Moro
	 * @param Article
	 * @return  Boolean
	 * */	
	@Override
	public Boolean supprimer(Article article) {
		
		Connection connexion = null;
		PreparedStatement  preparedStatement1  = null;
		PreparedStatement  preparedStatement2  = null;
		Boolean resultat = false;
		
		try {
			connexion = daoFactory.getConnection();
			connexion.setAutoCommit(false);
						
			preparedStatement1 = initialisationRequetePreparee(connexion, SQL_DELETE_ARTICLE_DANS_LIGNES_COMMANDES, false, article.getCode() );
			preparedStatement1.executeUpdate();
			
			preparedStatement2 = initialisationRequetePreparee(connexion, SQL_DELETE_PAR_CODE, true, article.getCode() );
			preparedStatement1.executeUpdate();
			
			preparedStatement2.executeUpdate();
			
			connexion.commit();	
		
		} catch ( SQLException e ) {
			throw new DaoException( "echec de la suppression de l'article, aucune ligne supprimée de la table." );
		} finally {
			fermeturesSilencieuses( preparedStatement1, connexion );
		}
		
		return resultat;
	}
	
	/**
	 * @author Moro
	 * @param String
	 * @return  Article
	 * */	
	@Override
	public Article rechercher(String code) throws DaoException{
		return null;
	}
	
	/**
	 * @author Moro
	 * @param Date
	 * @return  Long
	 * */	
	@Override
	public Long rechercher(Date date) throws DaoException {
		Long nbre = null;
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_PAR_DATE, false, date);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				nbre = resultSet.getLong("COUNT(code)");
			}
			
		}catch( SQLException e ) {
			System.out.println(e.getMessage());
		}finally {
			fermeturesSilencieuses( resultSet,preparedStatement, connexion );
		}
		
		return nbre;
	}
	
	public Map<String, String> listerCategorie(){
		Map<String,String> listeCategorie = new LinkedHashMap<String,String>();
		Connection connexion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connexion = daoFactory.getConnection();
			statement = connexion.createStatement();
			resultSet = statement.executeQuery(SQL_SELECT_ALL_CATEGORIES);
			while(resultSet.next()) {
				listeCategorie.put(resultSet.getString("designation"),resultSet.getString("code"));
			}
			
		}catch( SQLException e ) {
			System.out.println(e.getMessage());
		}finally {
			fermeturesSilencieuses( resultSet,statement, connexion );
		}
		
		return listeCategorie;
	}
	
	
	/*
	 * Simple m�thode utilitaire permettant de faire la correspondance (le
	 * mapping) entre une ligne issue de la table des articles (un ResultSet) et un bean Article.
	 */
	private static Article map( ResultSet resultSet ) throws SQLException {
	
		Article article = new Article();
		
		article.setCode(resultSet.getString( "code" ));
		article.setCodeCategorie(resultSet.getString( "code_categorie" ));
		article.setDesignation(resultSet.getString( "designation" ));
		article.setQuantite(resultSet.getLong( "quantite" ));
		article.setPrix(resultSet.getDouble( "prix_unitaire" ));
		article.setDate(resultSet.getDate( "date" ));
		article.setLibelleCategorie(resultSet.getString( "libelleCategorie" ));
		
	    return article;
	}
	
}
