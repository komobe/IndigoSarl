package ci.indigo.dao.interfaces;

import java.util.List;

import ci.indigo.entities.User;


public interface UserDao {

	User verificationIdentifiants(User user);
	List<User> liste();
}
