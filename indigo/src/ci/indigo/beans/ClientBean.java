package ci.indigo.beans;

import static ci.indigo.dao.DaoUtilitaire.getCodeAutoGenerate;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ci.indigo.entities.Client;
import ci.indigo.dao.DaoFactory;
import ci.indigo.dao.interfaces.ClientDao;
import ci.indigo.dao.interfaces.CommandeDao;

@ManagedBean(name="clientBean")
@SessionScoped
public class ClientBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ClientDao clientDao = null;
	private CommandeDao commandeDao = null;
	
	private static final String PREFIX_CODE_CLIENT = "CLT"; 

	private List<Client> allClients;
	
	@SuppressWarnings("unused")
	private FacesContext facesContext;
	
	@EJB // Injection de notre EJB (Session Bean Stateless)
	private Client item;
	
	private Client itemBeforeEdit;
	
   @PostConstruct      // Initialisation de l'entit� user
    public void init() {
	   	clientDao = DaoFactory.getInstance().getClientDao();
	   	commandeDao  =  DaoFactory.getInstance().getCommandeDao();
	   	facesContext=FacesContext.getCurrentInstance();
        item = new Client();
        allClients = clientDao.lister();
    }
	

    public ClientBean() { }
    

    
    // M�thode d'action appel�e lors du clic sur le bouton du formulaire
    // d'inscription
    public void add(){
    	
    	item.setCode(PREFIX_CODE_CLIENT + getCodeAutoGenerate("Clients"));
        initialiserDateInscription();
        this.item.setCarteFidelite( (long)((item.getBoolCarteFidelite())? 1 : 0) );
        clientDao.ajouter( item );
        FacesMessage message = new FacesMessage( "Succ�s de l'inscription !" );
        FacesContext.getCurrentInstance().addMessage( null, message );
        this.item = new Client();
        allClients = clientDao.lister();
    }
    
    
    public void delete(String idClient) {
    	Client client = new Client();
    	client.setCode(idClient);
    	clientDao.supprimer(client);
		FacesMessage message = new FacesMessage( "Suppression reussit !");
	    FacesContext.getCurrentInstance().addMessage( null, message );
	    allClients = clientDao.lister();
	    CommandeBean.reloadAllCommandes(commandeDao.lister());
    }
    
    
    public String recupItemEdit(Client client) {

    	this.itemBeforeEdit=client;
    	//this.itemBeforeEdit.setBoolCarteFidelite((client.getCarteFidelite() == 1)? true : false);
    	this.item = this.itemBeforeEdit;
    	
    	return "modifier?faces-redirect=true";
    }
    
    // M�thode d'action appel�e lors du clic sur le bouton du formulaire
    // de la modification

    public String  update() {

    	this.item.setCarteFidelite( (long)((item.getBoolCarteFidelite())? 1 : 0) );
    	clientDao.modifier( item );
    	this.item = new Client();
    	allClients = clientDao.lister();
    	return "lister?faces-redirect=true";
    }

    
	public Client getItem() {
		return item;
	}

	public void setItem(Client item) {
		this.item = item;
	}
	
	public List<Client> getAllClients() {	return allClients;	}
	
	private void initialiserDateInscription() {
	        Date date = new Date( System.currentTimeMillis() );
	        item.setDateInscription( date );
	}

	 
	 public String getCodeParam(FacesContext fc){
			
			Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
			return params.get("code");
			
	}
	
}
