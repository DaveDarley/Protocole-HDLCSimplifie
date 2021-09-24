
public class calculcrc {
	
	static Emetteur em = new Emetteur();
	Recepteur rec = new Recepteur();

	
	//Ajoute p-1 zeros, p etant le degre le plus grand du polynome generateur (ici appele crc)
	public static String addZeros(String donnees, String crc) {
		int nbZeros = crc.length()-1;
		String zeros="";
		for (int i = 0; i < nbZeros; i++) {
			  zeros=zeros.concat("0");
		}
			
		return calculCrc(donnees+zeros,crc);
	}
	
	// ca retourne les donnees du champ FCS!!
	public static String calculCrc(String donnees,String crc) {  
		
		int lengthCrc = crc.length();
		String data = donnees;
		
		String newData = data.substring(lengthCrc);
		String dividende = data.substring(0,lengthCrc);
		

		
		String resultat = XOR(dividende,crc);
		

		// on enleve les "0" avant resultat;
		String nouveauDonnees = removeZeros(resultat) + newData;

		
		if(nouveauDonnees.length() < lengthCrc) {

			return addLeadingZeros(nouveauDonnees, lengthCrc-1);
		}
		else {return calculCrc(nouveauDonnees,crc);}
		
	}

	
	public static String XOR(String A , String B) { // Source: Internet
		
	      StringBuffer sb = new StringBuffer();
	      for (int i = 0; i < A.length(); i++) {
	         sb.append(A.charAt(i)^B.charAt(i));
	      }
		
		  String resultat = sb + "";
		  return resultat;
	}
	
	// s'occupe de retire les "0" envant d'un nb ; Utilise dans le calcul CRC
	public static String removeZeros(String A) { 
		if(A.charAt(0)!='0') {return A;
		}else {
			if(A.length()==1 && A.charAt(0)=='0') {return "";} // Cas string contient juste des 0
			return removeZeros(A.substring(1));
		}
		
	}
	
	// Pour que la longueur du reste de la division polynomiale soit tjrs longueur - 1;
	public static String addLeadingZeros(String elem, int longueur) {
		if(elem.length() < longueur) {
			return addLeadingZeros("0"+elem, longueur);
		}
		return elem;
	}


	
	
	
	public static void main(String[] args) {


	}

}
