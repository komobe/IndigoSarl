package ci.indigo.dao.interfaces;

import java.util.List;
import java.util.Map;

import ci.indigo.entities.Article;
import ci.indigo.entities.Commande;

public interface CommandeDao {

	List<Commande> lister();
	Boolean ajouterCommande(Commande commande,Map<String, Article> articles);
	Boolean supprimer(Commande commande);
	Commande trouverCommande(String codeCommande);
	Map<String,Long> listeModeReglement();
}
	