import java.io.IOException;



public class test {
	
	static Emetteur em = new Emetteur();
	static Recepteur rec = new Recepteur();
/*
 * 3 cas :
 * Sans erreurs 
 * Avec erreurs
 * perte de trame
*/

	
	public static void testSansErreur() throws IOException {
		Emetteur.main(null);
		
	}
	
    // on introduit un erreur 
	public static void testAvecErreur() throws IOException {
		if(rec.mesTrames.size() == 0) {
			
		}
		
		
	}
	
	public static void main(String[] args) throws IOException {
		testSansErreur();
	}
	
	
	
}
