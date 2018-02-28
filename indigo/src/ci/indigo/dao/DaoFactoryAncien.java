package ci.indigo.dao;


import java.sql.*;

/*
	import ci.indigo.dao.implementation.ArticleDaoImpl;
	import ci.indigo.dao.implementation.ClientDaoImpl;
	import ci.indigo.dao.implementation.CommandeDaoImpl;
	import ci.indigo.dao.implementation.UserDaoImpl;
	import ci.indigo.dao.interfaces.ArticleDao;
	import ci.indigo.dao.interfaces.ClientDao;
	import ci.indigo.dao.interfaces.CommandeDao;
	import ci.indigo.dao.interfaces.UserDao;
*/


public class DaoFactoryAncien {

	private String url;
	private String username;
	private String password;
	
	public DaoFactoryAncien(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public static DaoFactoryAncien getInstance() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			System.out.println("Impossible de se connecter a la BD");
		}
		
		DaoFactoryAncien instance = new DaoFactoryAncien("jdbc:mysql://localhost:3306/indigo","root","");
		return instance;
	}
	
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url, username, password);
	}
	
/*
	//Recuperation du Dao
	public UserDao getUserDao() {
		return new UserDaoImpl(this);
	}
	
	public ClientDao getClientDao() {
		return new ClientDaoImpl(this);
	}
	public CommandeDao getCommandeDao() {
		return new CommandeDaoImpl(this);
	}
	
	public ArticleDao getArticleDao() {
		return new ArticleDaoImpl(this);
	}
*/
	
}
