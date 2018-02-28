package ci.indigo.dao.interfaces;

import java.util.List;

import ci.indigo.dao.DaoException;
import ci.indigo.entities.Client;

public interface ClientDao {

	List<Client> lister();
	Boolean ajouter(Client client);
	Boolean supprimer(Client client) throws DaoException;
	Boolean modifier(Client client);
	Client trouver(String codeClient);
	
	
}
