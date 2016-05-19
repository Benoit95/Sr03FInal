package forms;

public class QuestionForm {
	
	public void validationText( String text ) throws Exception {
        if ( text == null || text.trim().length() == 0 ) {
            throw new Exception( "Merci d'entrer un text." );
        }
    }

	public void validationOrdre(String ordre) throws Exception{
        if ( ordre == null || ordre.trim().length() == 0 ) {
            throw new Exception( "Merci d'entrer un ordre." );
        }
	}
}
