package ci.indigo.entities;

import java.sql.Date;

import javax.ejb.Stateless;

@Stateless
public class Commande {
	
	private String code;
	private Double totalTTC;
	private Date date;
	private String codeClient;
	private Long codeModeReglement;
	private String libelleModeReglement;
	
	/*********Setters*********/
	public void setCode(String code) {
		this.code = code;
	}
	public void setTotalTTC(Double totalTTC) {
		this.totalTTC = totalTTC;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}
	public void setCodeModeReglement(Long codeModeReglement) {
		this.codeModeReglement = codeModeReglement;
	}

	public void setLibelleModeReglement(String libelleModeReglement) {
		this.libelleModeReglement = libelleModeReglement;
	}
	/*********Getters*********/
	
	public String getCode() {
		return code;
	}
	public Double getTotalTTC() {
		return totalTTC;
	}
	public Date getDate() {
		return date;
	}
	public String getCodeClient() {
		return codeClient;
	}
	public Long getCodeModeReglement() {
		return codeModeReglement;
	}
	public String getLibelleModeReglement() {
		return libelleModeReglement;
	}
	
	
	
}
