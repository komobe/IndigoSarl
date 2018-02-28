package ci.indigo.entities;

import java.sql.Date;

import javax.ejb.Stateless;

@Stateless
public class Client {
	
	private String code;
	private String nom;
	private String prenom;
	private Long carteFidelite;
	private Boolean boolCarteFidelite;
	private Date dateInscription;
	private String adresse;
	private String codePostal;
	private String ville;
	private String telFixe;
	private String mobilis;
	private String email;
	private String remarques;
	
	

	/*********Setters*********/
	public void setCode(String code) {
		this.code = code;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public void setCarteFidelite(Long carte_fidelite) {
		this.carteFidelite = carte_fidelite;
	}
	public void setBoolCarteFidelite(Boolean boolCarteFidelite) {
		this.boolCarteFidelite = boolCarteFidelite;
	}
	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public void setCodePostal(String code_postal) {
		this.codePostal = code_postal;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public void setTelFixe(String tel_fixe) {
		this.telFixe = tel_fixe;
	}
	public void setMobilis(String mobilis) {
		this.mobilis = mobilis;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setRemarques(String remarques) {
		this.remarques = remarques;
	}
	
	
	
	/*********Getters*********/
	public String getCode() {
		return code;
	}
	public String getNom() {
		return nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public Long getCarteFidelite() {
		return carteFidelite;
	}
	public Boolean getBoolCarteFidelite() {
		return boolCarteFidelite;
	}
	public Date getDateInscription() {
		return dateInscription;
	}
	public String getAdresse() {
		return adresse;
	}
	public String getCodePostal() {
		return codePostal;
	}
	public String getVille() {
		return ville;
	}
	public String getTelFixe() {
		return telFixe;
	}
	public String getMobilis() {
		return mobilis;
	}
	public String getEmail() {
		return email;
	}
	public String getRemarques() {
		return remarques;
	}
	
	//
	
	public String fideliter() {
		return (this.boolCarteFidelite)? "Oui" : "Non";
	}
}
