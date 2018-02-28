package ci.indigo.beans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import ci.indigo.dao.DaoFactory;
import ci.indigo.dao.interfaces.UserDao;

import static ci.indigo.dao.SessionUtils.*;

import ci.indigo.entities.User;


@ManagedBean(name="userBean")
@SessionScoped
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private User item ;
	
	private UserDao userDao;
	
	
	 @PostConstruct
	 public void init() {
	   	userDao = DaoFactory.getInstance().getUserDao();
        item = new User();
	 }
	
	 public UserBean() {}
	 
	 public User getItem() {
			return item;
	}

	 public void setItem(User item) {
		this.item = item;
	 }
			 
	 //validate login
	 public String login() {
		User user = userDao.verificationIdentifiants(item);
		if (user != null) {
			HttpSession session = getSession();
			session.setAttribute("user", user);
			user = new User();
			return  "client/lister?faces-redirect=true";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Passowrd",
							"Please enter correct username and Password"));
			return "login?faces-redirect=true";
		}
	}
	
	
	//logout event, invalidate session
	public String logout() {
		HttpSession session = getSession();
		session.invalidate();
		return "/login.xhtml?faces-redirect=true";
	}
	
}
