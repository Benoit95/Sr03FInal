package beans;

import org.joda.time.DateTime;

public class Parcours {
	long id_user;
	long id_questionnaire;
	long id_question;
	long id_reponse;
	
	int score;
	long duree;
	
	public long getId_user() {
		return id_user;
	}
	public void setId_user(long id_user) {
		this.id_user = id_user;
	}
	public long getId_questionnaire() {
		return id_questionnaire;
	}
	public void setId_questionnaire(long id_questionnaire) {
		this.id_questionnaire = id_questionnaire;
	}
	public long getId_question() {
		return id_question;
	}
	public void setId_question(long id_question) {
		this.id_question = id_question;
	}
	public long getId_reponse() {
		return id_reponse;
	}
	public void setId_reponse(long id_reponse) {
		this.id_reponse = id_reponse;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public long getDuree() {
		return duree;
	}
	public void setDuree(long duree) {
		this.duree = duree;
	}
	


	

	
}
