package ci.indigo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ci.indigo.dao.DaoFactory;

public class DaoUtilitaire {
	
	
	
	/*
	 * Initialise la requ�te pr�par�e bas�e sur la connexion pass�e en argument,
	 * avec la requ�te SQL et les objets donn�s.
	 */
	public static PreparedStatement initialisationRequetePreparee( Connection connexion, String sql, boolean returnGeneratedKeys, Object... objets ) throws SQLException {
	    PreparedStatement preparedStatement = connexion.prepareStatement( sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS );
	    for ( int i = 0; i < objets.length; i++ ) {
	        preparedStatement.setObject( i + 1, objets[i] );
	    }
	    return preparedStatement;
	}
	
	
	
	

	/* Fermeture silencieuse du resultset */
	public static void fermetureSilencieuse( ResultSet resultSet ) {
	    if ( resultSet != null ) {
	        try {
	            resultSet.close();
	        } catch ( SQLException e ) {
	            System.out.println( "�chec de la fermeture du ResultSet : " + e.getMessage() );
	        }
	    }
	}

	/* Fermeture silencieuse du statement */
	public static void fermetureSilencieuse( Statement statement ) {
	    if ( statement != null ) {
	        try {
	            statement.close();
	        } catch ( SQLException e ) {
	            System.out.println( "�chec de la fermeture du Statement : " + e.getMessage() );
	        }
	    }
	}

	/* Fermeture silencieuse de la connexion */
	public static void fermetureSilencieuse( Connection connexion ) {
	    if ( connexion != null ) {
	        try {
	            connexion.close();
	        } catch ( SQLException e ) {
	            System.out.println( "�chec de la fermeture de la connexion : " + e.getMessage() );
	        }
	    }
	}

	/* Fermetures silencieuses du statement et de la connexion */
	public static void fermeturesSilencieuses( Statement statement, Connection connexion ) {
	    fermetureSilencieuse( statement );
	    fermetureSilencieuse( connexion );
	}

	/* Fermetures silencieuses du resultset, du statement et de la connexion */
	public static void fermeturesSilencieuses( ResultSet resultSet, Statement statement, Connection connexion ) {
	    fermetureSilencieuse( resultSet );
	    fermetureSilencieuse( statement );
	    fermetureSilencieuse( connexion );
	}
	
	
	 public static Date initialiserDate() {
        Date date = new Date( System.currentTimeMillis() );
		return date;
	 }
	 
	 
	/**
	 * @author Moro
	 * @param table:String 
	 * @return  Long
	 * */	

	public static String getCodeAutoGenerate (String table) throws DaoException {
		String SQL_SELECT_PAR_DATE = "SELECT COUNT(code) FROM "  + table + " WHERE date LIKE ? ";
		String SQL_SELECT = "SELECT COUNT(code) FROM "  + table ;
		Long nbreNext = null;
		String codeAutoGenerate = null; 
		Date date = initialiserDate();
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connexion = DaoFactory.getInstance().getConnection();
			if(table.equals("Commandes")) {
				preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_PAR_DATE, false, date);
				resultSet = preparedStatement.executeQuery();
			}else {
				resultSet = connexion.createStatement().executeQuery(SQL_SELECT);
			}
			
			if(resultSet.next()) {
				nbreNext = resultSet.getLong("COUNT(code)") + 1;
			}
			
		}catch( SQLException e ) {
			throw new DaoException(e.getMessage());
		}finally {
			fermeturesSilencieuses( resultSet,preparedStatement, connexion );
		}
		
		if(table.equals("Commandes")) {
			
			if(nbreNext<10) {
				codeAutoGenerate = "000" + nbreNext; 
			}else if(nbreNext<100 ) {
				codeAutoGenerate = "00" + nbreNext; 
			}else if(nbreNext<1000) {
				codeAutoGenerate = "0" + nbreNext; 
			}
			
			codeAutoGenerate = date.toString().replaceAll("-", "") + codeAutoGenerate;
			
		}else {
			
			if(nbreNext<10) {
				codeAutoGenerate = "00" + nbreNext; 
			}else if(nbreNext<100 ) {
				codeAutoGenerate = "0" + nbreNext; 
			}			
		}
		
		
		return codeAutoGenerate;
	}
	
	
}
