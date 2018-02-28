package ci.indigo.dao.implementation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ci.indigo.dao.DaoFactory;
import ci.indigo.dao.DaoException;
import ci.indigo.dao.interfaces.ClientDao;
import ci.indigo.entities.Client;

import static ci.indigo.dao.DaoUtilitaire.*;
public class ClientDaoImpl implements ClientDao{
	
	private DaoFactory daoFactory;
	
	private static final String SQL_SELECT_ALL = "SELECT * FROM Clients ORDER BY date";
	private static final String SQL_SELECT_PAR_CODE = "SELECT * FROM Clients WHERE code = ?";
	private static final String SQL_SELECT_CODE_COMMANDES_CLIENT ="SELECT code FROM Commandes WHERE code_client = ?";
	private static final String SQL_INSERT = "INSERT INTO Clients (code,nom, prenom,carte_fidelite,date, adresse,code_postal,"
			+ "ville, tel_fixe,mobilis, email, remarques) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String SQL_UPDATE = "UPDATE Clients SET nom = ?, prenom = ?,carte_fidelite = ?,date = ?, "
			+ "adresse = ?,code_postal = ?, ville = ?, tel_fixe = ?,mobilis = ?, email = ?, remarques = ? WHERE code = ?";
	
	private static final String SQL_DELETE_PAR_CODE = "DELETE FROM Clients WHERE code = ?";
	private static final String SQL_DELETE_COMMANDES_DU_CLIENT="DELETE FROM Commandes WHERE code_client = ?";
	private static final String SQL_DELETE_LIGNES_COMMANDE_PAR_CODE = "DELETE FROM Lignes_Commandes WHERE code_commande = ?";
	
	
	
	public ClientDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public List<Client> lister() {
		// TODO Auto-generated method stub
		List<Client> clients = new ArrayList<Client>();
		Connection connexion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connexion = daoFactory.getConnection();
			statement = connexion.createStatement();
			resultSet=statement.executeQuery(SQL_SELECT_ALL);
			
			while(resultSet.next()) {
				// Ajout du client dans le tableau
				clients.add(map(resultSet));
			}
			
		}catch( SQLException e ) {
			throw new DaoException( e );
		}finally {
			fermeturesSilencieuses( resultSet,statement, connexion );
		}
		
		return clients;
	}

	@Override
	public Boolean ajouter(Client client) {
		//
		
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		Boolean resultat=false;
		//ResultSet resultSet = null;
		//ResultSet valeursAutoGenerees = null;
		
		try {
			
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(
			connexion, SQL_INSERT, true, 
			client.getCode(), client.getNom(), client.getPrenom(),
			client.getCarteFidelite(),client.getDateInscription(),client.getAdresse(),
			client.getCodePostal(), client.getVille(),client.getTelFixe(),
			client.getMobilis(),client.getEmail(), client.getRemarques() );
			int statut = preparedStatement.executeUpdate();
			
			if(statut!=0) {
				resultat = true;
			}
			
			
		}catch( SQLException e ) {
			throw new DaoException( e );
		}finally {
			fermeturesSilencieuses( preparedStatement, connexion );
		}
		
		return resultat;
	}

	@Override
	public Boolean supprimer(Client client)  throws DaoException{

		Connection connexion = null;
		PreparedStatement  preparedStatement1  = null;
		PreparedStatement  preparedStatement2  = null;
		PreparedStatement  preparedStatement3  = null;
		PreparedStatement  preparedStatement4  = null;
		ResultSet resultSet = null;
		Boolean resultat = false;
		
		try {
			connexion = daoFactory.getConnection();
			
			//Ici on recup�re les codes de toutes les commandes effectu�es par le client courant
			preparedStatement1 = initialisationRequetePreparee(connexion, SQL_SELECT_CODE_COMMANDES_CLIENT, false, client.getCode() );
			resultSet = preparedStatement1.executeQuery();
			
			connexion.setAutoCommit(false);	// Suppression du contenu des diff�rentes commandes effectu�es par le client 
			preparedStatement2 = connexion.prepareStatement(SQL_DELETE_LIGNES_COMMANDE_PAR_CODE);
			while(resultSet.next()) {
				
			 preparedStatement2.setString(1, resultSet.getString("code"));
			 preparedStatement2.addBatch();

			}
			
			preparedStatement2.executeBatch();	

			// Suppression de la commande suivis du client
			preparedStatement3 = initialisationRequetePreparee(connexion, SQL_DELETE_COMMANDES_DU_CLIENT, false, client.getCode() );
			preparedStatement3.addBatch();
			preparedStatement3.executeBatch();
			
			preparedStatement4 = initialisationRequetePreparee(connexion, SQL_DELETE_PAR_CODE, false, client.getCode() );
			preparedStatement4.addBatch();
			preparedStatement4.executeBatch();
			
			connexion.commit();
			
		} catch ( SQLException e ) {
			//throw new DaoException( "�chec de la suppression du client, aucune ligne supprim�e de la table." );
			throw new DaoException( e);
		} finally {
			fermeturesSilencieuses( preparedStatement1, connexion );
			fermeturesSilencieuses( preparedStatement2, connexion );
			fermeturesSilencieuses( preparedStatement3, connexion );
			fermeturesSilencieuses( preparedStatement4, connexion );
		}
		
		return resultat;
	}

	@Override
	public Boolean modifier(Client client) {
//
		
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		Boolean resultat=false;
		
		try {
			
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(
			connexion, SQL_UPDATE, true, 
			client.getNom(), client.getPrenom(),
			client.getCarteFidelite(),client.getDateInscription(),client.getAdresse(),
			client.getCodePostal(), client.getVille(),client.getTelFixe(),
			client.getMobilis(),client.getEmail(), client.getRemarques(),client.getCode());
			int statut = preparedStatement.executeUpdate();
			
			if(statut!=0) {
				resultat = true;
			}
			
			
		}catch(SQLException e ) {
			throw new DaoException( e );
		}finally {
			fermeturesSilencieuses( preparedStatement, connexion );
		}
		
		return resultat;
	}

	@Override
	public Client trouver (String codeClient) {
		Client client = new Client();
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_PAR_CODE , false, codeClient );
			resultSet=preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				client = map(resultSet);
			}
			
		}catch( SQLException e ) {
			e.printStackTrace();
		}finally {
			fermeturesSilencieuses( preparedStatement, connexion );
		}
		
		return client;
	}

	
	/*
	 * Simple m�thode utilitaire permettant de faire la correspondance (le
	 * mapping) entre une ligne issue de la table des client (un
	 * ResultSet) et un bean Client.
	 */
	private static Client map( ResultSet resultSet ) throws SQLException {
	
      //Recuperation des donnees du resultSet
		String code = resultSet.getString( "code" );
		String nom = resultSet.getString( "nom" );
		String prenom = resultSet.getString( "prenom" );
		Long carte_fidelite = resultSet.getLong( "carte_fidelite" );
		Date  dateInscription = resultSet.getDate("date");
		String adresse = resultSet.getString( "adresse" );
		String code_postal = resultSet.getString( "code_postal" );
		String ville = resultSet.getString( "ville" );
		String tel_fixe = resultSet.getString( "tel_fixe" );
		String mobilis = resultSet.getString( "mobilis" );
		String email = resultSet.getString( "email" );
		String remarques = resultSet.getString( "remarques" );
        
		Client client = new Client();
		
		client.setCode(code);
		client.setNom(nom);
		client.setPrenom(prenom);		
		client.setBoolCarteFidelite( (carte_fidelite == 1 )? true : false );	
		client.setCarteFidelite(carte_fidelite);
		client.setDateInscription(dateInscription);
		client.setAdresse(adresse);
		client.setCodePostal(code_postal);
		client.setVille(ville);
		client.setTelFixe(tel_fixe);
		client.setMobilis(mobilis);
		client.setEmail(email);
		client.setRemarques(remarques);
		
	    
	    return client;
	}
}
