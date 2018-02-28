package ci.indigo.dao.implementation;

import static ci.indigo.dao.DaoUtilitaire.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ci.indigo.dao.DaoFactory;
import ci.indigo.dao.DaoException;
import ci.indigo.dao.interfaces.CommandeDao;
import ci.indigo.entities.Article;
import ci.indigo.entities.Commande;
import ci.indigo.entities.LigneCommande;


public class CommandeDaoImpl implements CommandeDao{
	private DaoFactory daoFactory;
	
	private static final String  SQL_SELECT_ALL_MODE_REGLEMENT = "SELECT * FROM mode_reglements ORDER BY code";
	private static final String  SQL_SELECT_ALL_COMMANDES = "SELECT * FROM commandes as com INNER JOIN mode_reglements as mr "
			+ "ON com.code_mode_reglements = mr.code ORDER BY date";
	private static final String  SQL_SELECT_COMMANDE_PAR_CODE = "SELECT * FROM commandes as com INNER JOIN mode_reglements as mr "
			+ "ON com.code_mode_reglements = mr.code WHERE com.code = ? ";
	//private static final String  SQL_SELECT_LIGNES_COMMANDE ="SELECT * FROM Lignes_Commande WHERE code_commande = ?";
	private static final String  SQL_INSERT_COMMANDE = "INSERT INTO Commandes(code ,total_ttc ,date,code_client,"
			+ "code_mode_reglements) VALUES(?,?,?,?,?) ";
	private static final String  SQL_INSERT_LIGNES_COMMANDE = "INSERT INTO Lignes_Commandes(code_commande,code_article,"
			+ "quantite,prix_unitaire) VALUES(?,?,?,?) ";
	
	private static final String  SQL_DELETE_COMMANDE_PAR_CODE = "DELETE FROM Commandes WHERE code = ?";
	
	private static final String  SQL_DELETE_LIGNES_COMMANDE_PAR_CODE = "DELETE FROM Lignes_Commandes WHERE code_commande = ?";
	
	private static final  String SQL_UPDATE_QUANTITE_ARTICLE_PAR_CODE = "UPDATE Articles SET quantite = quantite -  ? WHERE code = ?"; 
	 
	public CommandeDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public List<Commande> lister() {
		// TODO Auto-generated method stub
		List<Commande> commandes = new ArrayList<Commande>();
		Connection connexion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connexion = daoFactory.getConnection();
			statement = connexion.createStatement();
			resultSet=statement.executeQuery(SQL_SELECT_ALL_COMMANDES);
			
			while(resultSet.next()) {
				// Ajout du client dans le tableau
				commandes.add(mapCommande(resultSet));
			}
			
		}catch( SQLException e ) {
			System.out.println(e.getMessage());
		}finally {
			fermeturesSilencieuses( resultSet,statement, connexion );
		}
		
		return commandes;
	}

	@Override
	public Boolean ajouterCommande(Commande commande,Map<String, Article> articles) {
		
		Connection connexion = null;
		PreparedStatement preparedStatementCommande = null;
		PreparedStatement preparedStatementLignesCommande = null;
		PreparedStatement preparedStatementArticle = null;
		Boolean resultat = false;
	
		try {
			connexion = daoFactory.getConnection();
			connexion.setAutoCommit(false) ;
			
			preparedStatementCommande = initialisationRequetePreparee(
				connexion, SQL_INSERT_COMMANDE, false, 
				commande.getCode(),commande.getTotalTTC(), commande.getDate(),
				commande.getCodeClient(), commande.getCodeModeReglement() );
			
			preparedStatementCommande.executeUpdate();
						
			preparedStatementLignesCommande = connexion.prepareStatement(SQL_INSERT_LIGNES_COMMANDE);
			preparedStatementArticle = connexion.prepareStatement(SQL_UPDATE_QUANTITE_ARTICLE_PAR_CODE);
			
			for (Entry<String, Article> article : articles.entrySet()) {
				 
				 Article a = (Article) article.getValue();
				 
				 //
				 preparedStatementLignesCommande.setString(1, commande.getCode());
				 preparedStatementLignesCommande.setString(2, a.getCode());
				 preparedStatementLignesCommande.setLong(3, a.getQuantite());
				 preparedStatementLignesCommande.setDouble(4, a.getPrix());

				 preparedStatementLignesCommande.addBatch();
				 
				 
				 preparedStatementArticle.setLong(1, a.getQuantite());
				 preparedStatementArticle.setString(2, a.getCode());

				 preparedStatementArticle.addBatch();
				 				 				 
			 }
			
			
			 preparedStatementLignesCommande.executeBatch();	
			 preparedStatementArticle.executeBatch();
			 
			 connexion.commit();			
			 resultat = true;
			
		}catch( SQLException e ) {
			System.out.println(e.getMessage());
			throw new DaoException( e );
		}finally {
			fermeturesSilencieuses( preparedStatementCommande, connexion );
			fermeturesSilencieuses( preparedStatementLignesCommande, connexion );
			fermeturesSilencieuses( preparedStatementArticle, connexion );
		}
		
		return resultat;
	}

	
	@Override
	public Boolean supprimer(Commande commande) {
		// TODO Auto-generated method stub
		
		Connection connexion = null;
		PreparedStatement  preparedStatement1  = null;
		PreparedStatement  preparedStatement2  = null;
		Boolean resultat = false;
		
		try {
			connexion = daoFactory.getConnection();
			connexion.setAutoCommit(false);
			preparedStatement1 = initialisationRequetePreparee(connexion, SQL_DELETE_LIGNES_COMMANDE_PAR_CODE , true, commande.getCode() );
			preparedStatement1.executeUpdate();
			
			preparedStatement2 = initialisationRequetePreparee(connexion, SQL_DELETE_COMMANDE_PAR_CODE , true, commande.getCode() );
			preparedStatement2.addBatch();
			preparedStatement2.executeBatch();
			
			connexion.commit();
			
			resultat = true;
			
		} catch ( SQLException e ) {
			throw new DaoException( "Une erreur est intervenue lors  de la suppression de la commande" );
		} finally {
			fermeturesSilencieuses( preparedStatement1, connexion );
			fermeturesSilencieuses( preparedStatement2, connexion );
		}
		
		return resultat;
	}

	@Override
	public Commande trouverCommande(String codeCommande) {
		Commande commande = new Commande();
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_COMMANDE_PAR_CODE , false, codeCommande );
			resultSet=preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				commande = mapCommande(resultSet);
			}
			
		}catch( SQLException e ) {
			e.printStackTrace();
		}finally {
			fermeturesSilencieuses( preparedStatement, connexion );
		}
		
		return commande;
	}
	
	
	@Override
	public Map<String,Long> listeModeReglement() {
		Map<String,Long> listeModeReglement = new LinkedHashMap<String,Long>();
		Connection connexion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connexion = daoFactory.getConnection();
			statement = connexion.createStatement();
			resultSet = statement.executeQuery(SQL_SELECT_ALL_MODE_REGLEMENT);
			while(resultSet.next()) {
				listeModeReglement.put(resultSet.getString("type"),resultSet.getLong("code"));
			}
			
		}catch( SQLException e ) {
			System.out.println(e.getMessage());
		}finally {
			fermeturesSilencieuses( resultSet,statement, connexion );
		}
		
		return listeModeReglement;
	}


	private static Commande mapCommande( ResultSet resultSet ) throws SQLException {
		
		Commande commande = new Commande();
		
		commande.setCode(resultSet.getString( "code" ));
		commande.setTotalTTC(resultSet.getDouble( "total_ttc" ));
		commande.setDate(resultSet.getDate( "date" ));	
		commande.setCodeClient(resultSet.getString( "code_client" ));
		commande.setCodeModeReglement(resultSet.getLong( "code_mode_reglements" ));
		if(resultSet.getString( "type" ) != null) {
			commande.setLibelleModeReglement(resultSet.getString( "type" ));
		}
		
	    return commande;
	}
	
	@SuppressWarnings("unused")
	private static LigneCommande mapLigneCommande( ResultSet resultSet ) throws SQLException {
		
		LigneCommande ligneCommande = new LigneCommande();
		
		ligneCommande.setCodeCommande(resultSet.getString( "code_commande" ));
		ligneCommande.setCodeArticle(resultSet.getString( "code_categorie" ));
		ligneCommande.setQuantite(resultSet.getLong( "quantite" ));
		ligneCommande.setPrixUnitaire(resultSet.getDouble( "prix_unitaire" ));
	    
	    return ligneCommande;
	}




	
}
