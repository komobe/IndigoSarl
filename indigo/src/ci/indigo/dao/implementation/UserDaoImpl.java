package ci.indigo.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ci.indigo.dao.DaoFactory;
import ci.indigo.dao.interfaces.UserDao;
import ci.indigo.entities.User;

import static ci.indigo.dao.DaoUtilitaire.*;

public class UserDaoImpl implements UserDao {

	private DaoFactory daoFactory;
	
	
	private static final String SQL_SELECT_USER = "SELECT * FROM users WHERE username= ? AND password= ? ;";
	
	public UserDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	
	
	@Override
	public User verificationIdentifiants(User user) {
		
		Connection connexion = null;
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		
		
		
		try {
			
			//Connexion à la base de donn�es
	        connexion = daoFactory.getConnection();
	        /* Création de l'objet gérant les requêtes */
	        prepareStatement = initialisationRequetePreparee(connexion,SQL_SELECT_USER,false,user.getUsername(),user.getPassword());
	        resultSet=prepareStatement.executeQuery();
	        //resultSet=statement.executeQuery(SQL_SELECT_USER);
	        /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
	        if ( resultSet.next() ) {
	            user = map( resultSet );
	        }else {
	        	user = null;
	        }
	        
			
		}catch(SQLException e ) {
			System.out.println(e.getMessage());
		}finally {
			fermeturesSilencieuses( resultSet, prepareStatement, connexion );
		}
		
		return user;
	}
	

	@Override
	public List<User> liste() {
		List<User> users = new ArrayList<User>();
		Connection connexion = null;
		Statement statement = null;
		ResultSet resultSet = null;
	    try {
	       
	    	//Connexion � la base de données
	        connexion = daoFactory.getConnection();;
	        
	        /* Cr�ation de l'objet gérant les requétes */
	        statement = connexion.createStatement();
	       
	        /* Ex�cution d'une requête de lecture */
	        resultSet = statement.executeQuery( "SELECT * FROM users;" );
 
	        /* R�cup�ration des données du résultat de la requête de lecture */
	        while ( resultSet.next() ) {
	            users.add(map(resultSet));
	        }
	    } catch ( SQLException e ) {
	       //
	    } finally {
	    	fermeturesSilencieuses( resultSet, statement, connexion );
	    }
		return users;
	}

	/*
	 * Simple m�thode utilitaire permettant de faire la correspondance (le
	 * mapping) entre une ligne issue de la table des utilisateurs (un
	 * ResultSet) et un bean Utilisateur.
	 */
	private static User map( ResultSet resultSet ) throws SQLException {
		Long id = resultSet.getLong( "id" );
        String firstName = resultSet.getString( "first_name" );
        String lastName = resultSet.getString( "last_name" );
        String username = resultSet.getString( "username" );
        String email = resultSet.getString( "email" );
        String phone = resultSet.getString( "phone" );
        String role = resultSet.getString( "role" );
    	//  
	    User user = new User();
	    user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
	    user.setRole(role);
	    //
	    return user;
	}


	
}
