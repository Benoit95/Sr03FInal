package beans;

public class Reponse {
	long id;
	long id_question;
	String statut;
	String text;
	String estValide;
	int ordre;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId_question() {
		return id_question;
	}
	public void setId_question(long id_question) {
		this.id_question = id_question;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getEstValide() {
		return estValide;
	}
	public void setEstValide(String estValide) {
		this.estValide = estValide;
	}
	public int getOrdre() {
		return ordre;
	}
	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}
}
