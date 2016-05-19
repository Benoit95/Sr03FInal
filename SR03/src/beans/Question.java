package beans;

import java.util.List;

public class Question {
	long id;
	long id_questionnaire;
	String statut;
	String text;
	List<Reponse> LReponses;
	int ordre;
	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId_questionnaire() {
		return id_questionnaire;
	}
	public void setId_questionnaire(long id_questionnaire) {
		this.id_questionnaire = id_questionnaire;
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
	public List<Reponse> getLReponses() {
		return LReponses;
	}
	public void setLReponses(List<Reponse> lReponses) {
		LReponses = lReponses;
	}
	public int getOrdre() {
		return ordre;
	}
	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}
	
	
	
	
}
