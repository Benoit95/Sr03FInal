package forms;

public class QuestionnaireForm {
	
	public void validationSujet( String sujet ) throws Exception {
        if ( sujet == null || sujet.trim().length() == 0 ) {
            throw new Exception( "Merci d'entrer un sujet." );
        }
    }
}
