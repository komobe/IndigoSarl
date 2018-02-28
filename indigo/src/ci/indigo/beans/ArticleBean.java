package ci.indigo.beans;

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
/*import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;*/

import ci.indigo.dao.DaoFactory;
import ci.indigo.dao.interfaces.ArticleDao;
import ci.indigo.entities.Article;

import static ci.indigo.dao.DaoUtilitaire.getCodeAutoGenerate;

@ManagedBean(name="articleBean")
@SessionScoped
public class ArticleBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ArticleDao articleDao = null;
	
	private static final String PREFIX_CODE_ARTICLE ="ART";
	
	//private List<Article> list;
	
	private List<Article> allArticles ;
	
	//private FacesContext facesContext;
	

	
	// Injection de notre EJB (Session Bean Stateless)
	@EJB
	private Article item;
		
	private Article itemBeforeEdit;
	
	private static Map<String,String> listeCategories;
	
   @PostConstruct     // Initialisation de l'entit� article
    public void init() {
	   	articleDao = DaoFactory.getInstance().getArticleDao();
	   	//facesContext=FacesContext.getCurrentInstance();
        //list = new ArrayList<Article>();
        item = new Article();
        allArticles =  articleDao.lister(); 
        listeCategories = articleDao.listerCategorie();
    }
	
    // Initialisation de l'entit� article
    public ArticleBean() { }
    
    
    // Méthode d'action appelée lors du clic sur le bouton du formulaire
    // d'ajout
    public void add(){
    	
    	item.setCode(PREFIX_CODE_ARTICLE + getCodeAutoGenerate("Articles"));
        initialiserDate();// Recuperation de la Date d'ajout de l'article
        FacesMessage message = null;
        if(articleDao.ajouter( item )) {
        	message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info: ", "L'article a  été ajouter avec succes" );
        }else {
        	message = new FacesMessage( FacesMessage.SEVERITY_ERROR ,"Error: " ,"L'article n'a pas pu etre ajouter" );
        }
        
        FacesContext.getCurrentInstance().addMessage( null , message );
        
        this.item = new Article();
        allArticles =  articleDao.lister(); 
    }
    
    
    public void delete(String idArticle) {
    	Article article = new Article();
    	article.setCode(idArticle);
    	articleDao.supprimer(article);
		FacesMessage message = new FacesMessage( "Suppression reussit !");
	    FacesContext.getCurrentInstance().addMessage( null, message );
	    allArticles =  articleDao.lister(); 
    }
    
    
    public String recupItemEdit(Article article) {

    	this.itemBeforeEdit=article;
    	this.item = this.itemBeforeEdit;
    	
    	return "modifier?faces-redirect=true";
    }
    
    // M�thode d'action appel�e lors du clic sur le bouton du formulaire
    // de la modification
    public String  update() {
    	
    	articleDao.modifier( item );
    	this.item = new Article();
    	allArticles =  articleDao.lister(); 
    	return "lister?faces-redirect=true";
    }

	public Article getItem() {
		return item;
	}

	public void setItem(Article item) {
		this.item = item;
	}
	
    public List<Article> getAllArticles() {	
    	return allArticles;
    }

    public Map<String,String> getListeCategories(){
    	return listeCategories;
    }
    
	 private void initialiserDate() {
	        Date date = new Date( System.currentTimeMillis() );
	        item.setDate(date );
	 }

	
	 
	 public String getCodeParam(FacesContext fc){
			
			Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
			return params.get("code");
			
	}
	
}
