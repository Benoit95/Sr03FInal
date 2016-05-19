package beans;

import java.util.List;

public class Questionnaire {
	long id;
	String sujet; // unique
	String statut;
	List<Question> LQuestions;
	
	//getter
	public long getId() {return this.id;}
	public String getSujet() {return this.sujet;}
	public String getStatut() {return this.statut;}

	//setter
	public void setId( long id ) {this.id = id;}
	public void setSujet( String sujet ) {this.sujet = sujet;}
	public void setStatut( String statut ) {this.statut = statut;}
	
	public List<Question> getLQuestions() {
		return LQuestions;
	}
	public void setLQuestions(List<Question> lQuestions) {
		LQuestions = lQuestions;
	}
	
	
}
