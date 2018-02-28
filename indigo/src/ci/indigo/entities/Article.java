package ci.indigo.entities;

import java.sql.Date;

import javax.ejb.Stateless;
@Stateless
public class Article {

	private String code;
	private String codeCategorie;
	private String designation;
	private Long quantite;
	private Double prix;
	private Date date;
	private String libelleCategorie;

	
	/*********Setters*********/
	
	public void setCode(String code) {
		this.code = code;
	}
	public void setCodeCategorie(String codeCategorie) {
		this.codeCategorie = codeCategorie;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public void setQuantite(Long quantite) {
		this.quantite = quantite;
	}
	public void setPrix(Double prix) {
		this.prix = prix;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public void setLibelleCategorie(String libelleCategorie) {
		this.libelleCategorie = libelleCategorie;
	}
	/*********Getters*********/
	
	
	public String getCode() {
		return code;
	}
	public String getCodeCategorie() {
		return codeCategorie;
	}
	public String getDesignation() {
		return designation;
	}
	public Long getQuantite() {
		return quantite;
	}
	public Double getPrix() {
		return prix;
	}
	public Date getDate() {
		return date;
	}

	public String getLibelleCategorie() {
		return libelleCategorie;
	}
}
