package ci.indigo.entities;

public class LigneCommande {
	
	private String codeCommande;
	private String codeArticle;
	private Long quantite;
	private Double prixUnitaire;
	
	
	/*********Setters*********/
	public void setCodeCommande(String codeCommande) {
		this.codeCommande = codeCommande;
	}
	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}
	public void setQuantite(Long quantite) {
		this.quantite = quantite;
	}
	public void setPrixUnitaire(Double prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}
	
	
	/*********Getters*********/
	public String getCode_commande() {
		return codeCommande;
	}
	public String getCode_article() {
		return codeArticle;
	}
	public Long getQuantite() {
		return quantite;
	}
	public Double getPrixUnitaire() {
		return prixUnitaire;
	}
	public Double getTotal() {
		return (Double) (this.quantite * this.prixUnitaire);
	}
	
}
