package ci.indigo.beans;

import static ci.indigo.dao.DaoUtilitaire.getCodeAutoGenerate;

import java.io.Serializable;
import java.sql.Date;
//import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;



import ci.indigo.dao.DaoException;
import ci.indigo.dao.DaoFactory;
import ci.indigo.dao.interfaces.ArticleDao;
import ci.indigo.dao.interfaces.ClientDao;
import ci.indigo.dao.interfaces.CommandeDao;
import ci.indigo.entities.Article;
import ci.indigo.entities.Client;
import ci.indigo.entities.Commande;

@ManagedBean(name="commandeBean")
@SessionScoped
public class CommandeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private CommandeDao commandeDao = null;
	private ArticleDao  articleDao = null;
	private ClientDao clientDao = null; 
	
	private static final String PREFIX_CODE_COMMANDE = "COM";
	
	private static List<Commande> allCommandes;
	
	//private FacesContext facesContext;
	
	
	@EJB  // Injection de notre EJB (Session Bean Stateless)
	private Commande item;
	
	@EJB
	private Article article;
	
	private Map<String,Long> listeModeReglement;
	
	private Commande itemBeforeEdit;
	
	private Map<String, Article> listeArticles;
	
	private Map<String, Article> articlesCommande;
	
	private Commande voirItem;
	private List<Article> voirArticlesCommande;
	private Client clientCommande ;
	
	private Double taux;
	
   @PostConstruct     // Initialisation de l'entit� commande
    public void init() {
	   	commandeDao = DaoFactory.getInstance().getCommandeDao();
	   	articleDao = DaoFactory.getInstance().getArticleDao();
	   	clientDao = DaoFactory.getInstance().getClientDao();
	   //	facesContext=FacesContext.getCurrentInstance();
        articlesCommande = new LinkedHashMap<String, Article>();
        listeArticles = articleDao.listerMap();
        listeModeReglement = commandeDao.listeModeReglement();
        item = new Commande();
        taux = 0.18;
        allCommandes = commandeDao.lister();
        voirArticlesCommande = new ArrayList<Article>(); 
    }
	
    public CommandeBean() { }
       
    // M�thode d'action appel�e lors du clic sur le bouton du formulaire
    // d'ajout pour passer la commande
    public void add(){
    	item.setCode(PREFIX_CODE_COMMANDE + getCodeAutoGenerate("Commandes"));
        initialiserDate(); // Recuperation de la date de la commande        
        this.item.setTotalTTC(getTotalTTC()); 
        if(commandeDao.ajouterCommande( this.item,this.articlesCommande )) {
        	FacesMessage message = new FacesMessage( "La commande a été effectuée avec success." );
	        FacesContext.getCurrentInstance().addMessage( null, message );
	        allCommandes = commandeDao.lister();
        }else {
    		FacesMessage message = new FacesMessage( "Une erreur s'est produite lors de l'enregistrementde la commande"
    				+ " : La commande n'a pas pu etre passer !" );
	        FacesContext.getCurrentInstance().addMessage( null, message );
    	}
       
        this.item = new Commande();
        this.articlesCommande = new LinkedHashMap<String, Article>();
    }
    
    
    public void delete(String idCommande) {
    	Commande commande = new Commande();
    	commande.setCode(idCommande);
    	commandeDao.supprimer(commande);
		FacesMessage message = new FacesMessage( "Suppression reussit !");
	    FacesContext.getCurrentInstance().addMessage( null, message );
	    allCommandes = commandeDao.lister();
    }
    
    
    public String recupItemEdit(Commande commande) {

    	this.itemBeforeEdit=commande;
    	this.item = this.itemBeforeEdit;
    	
    	return "modifier?faces-redirect=true";
    }
    
	public Commande getItem() {
		return item;
	}

	public void setItem(Commande item) {
		this.item = item;
	}
	
	
	
	 public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	
	public Commande getVoirItem() {
		return voirItem;
	}

	public void setVoirItem(Commande voirItem) {
		this.voirItem = voirItem;
	}

	public List<Article> getVoirArticlesCommande() {
		return voirArticlesCommande;
	}

	public void setVoirArticlesCommande(List<Article> voirArticlesCommande) {
		this.voirArticlesCommande = voirArticlesCommande;
	}

	public Client getClientCommande() {
		return clientCommande;
	}

	public void setClientCommande(Client clientCommande) {
		this.clientCommande = clientCommande;
	}
	
	public Double getTaux() {
		return taux;
	}

	public Map<String, Long> getListeModeReglement() {
		return this.listeModeReglement;
	}
	
	public Map<String, Article> getListeArticles() {
		return listeArticles;
	}

	public Map<String, Article> getArticlesCommande() {
		return articlesCommande;
	}

	public void addArticlesCommande() throws DaoException {		
		//this.listeArticles.get(key)
		try {
			if(article.getQuantite() > 0) {
				
				if(article.getQuantite() <= listeArticles.get(article.getCode()).getQuantite()) {
					
					if(articlesCommande.containsKey(article.getCode())) {
						article.setQuantite(articlesCommande.get(article.getCode()).getQuantite()  +  article.getQuantite());
					}else {
						article.setQuantite(article.getQuantite());
					}
					article.setPrix(listeArticles.get(article.getCode()).getPrix());
					this.articlesCommande.put(article.getCode(),article);
					
				}else {
					FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_ERROR ,"Error: ", "La quantite de saisie est superieure à celle disponible."
							+ " La quantite d'articles disponible est : " + listeArticles.get(article.getCode()).getQuantite());
			        FacesContext.getCurrentInstance().addMessage( null, message );
				}
				
			}else {
				FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_ERROR ,"Error: ", "La quantite de saisie doit etre  superieure à zéro."
						+ " La quantite d'articles disponible est : " + listeArticles.get(article.getCode()).getQuantite());
		        FacesContext.getCurrentInstance().addMessage( null, message );
			}
			
			
		}catch(NullPointerException e) {
			throw new DaoException("Veuillez verifier s'il y a des articles enregistrés des articles d'abord: "
					+ "Si oui, contactez l'administrateur");
		}finally{			
			article = new Article();	
		}
			
	}
	
	//Supprime un article de la liste des articles presents dans commande
	public void supprimerArticlesCommande(String codeArticle) {		
		this.articlesCommande.remove(codeArticle);
	}
	
	//Methode peermattant de recuperer  les codes des articles de la commande
	public List<String> getAllKeyArticlesCommande(){
		
		List<String> allKeyArticlesCommande = new ArrayList<String>();
		
		for(String key : this.articlesCommande.keySet()) {
		
			allKeyArticlesCommande.add(key);
			
		}
		
		return allKeyArticlesCommande;
	}
	
	/**
	 * 
	 * @param commandes
	 * 
	 */
	
	public static void reloadAllCommandes(List<Commande> commandes) {
		allCommandes = commandes;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Commande> getAllCommandes() {
	   return  allCommandes; 	
	}

	
	public String voirCommande(String codeCommande) throws DaoException {
		
		try {
			voirItem = commandeDao.trouverCommande(codeCommande);
			clientCommande = clientDao.trouver(voirItem.getCodeClient());
			voirArticlesCommande = articleDao.lister(codeCommande);		
		}catch(NullPointerException e) {
			throw new DaoException(e);
		}
		
		return "voir?faces-redirect=true";	
	}
	
	private void initialiserDate() {
	        Date date = new Date( System.currentTimeMillis() );
	        item.setDate(date );
	 }

	private Double getTotalTTC() {
		Double total = 0.0;
		for(Entry<String, Article> a : this.articlesCommande.entrySet()) {
			Double prixHT = a.getValue().getQuantite() * a.getValue().getPrix();			
			total += prixHT + (this.taux * prixHT); 
		}
		
		return total;
	}
	
	 public String getCodeParam(FacesContext fc){
			
			Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
			return params.get("code");
			
	}
	
}
