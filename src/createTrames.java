import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class createTrames {

	
	static Emetteur em = new Emetteur();
	static Recepteur rec = new Recepteur();
	static calculcrc calCrc = new calculcrc();
	

	static String Flag = "01111110";
	//static String Crc = "10001000000100001";
	
	static String connectRequest = em.toBinary("C",8);
	static String finCom = em.toBinary("F",8);
	static String pBitRequest = em.toBinary("P",8);
	static String accept = em.toBinary("A",8);
	static String rejet = em.toBinary("R",8);
	static String data = em.toBinary("I",8);
	
	/*
	 * On definit qu'on utilise le crc que pour les trames de donnees!!
	 */
	
	public static String createTrams(String flag, String Type, String Num, String Donnees,String crc){

        
		
		String returnTrame = "" ;
		
		
		if(Type.equals(connectRequest)) {
			
			String num0 = "00000000";
			String TN = Type + num0;
			String trameC = Flag + ajoutBitStuffing(TN) + Flag;
			returnTrame +=  trameC;
			
	
			
		}else if(Type.equals(finCom)) {
			String Num0 = "00000000";
			String TN = Type + Num0;
			String trameF = Flag + ajoutBitStuffing(TN) + Flag;
			returnTrame +=  trameF;
			
		}else if(Type == pBitRequest) {
			return "";
			
		}else if(Type.equals(accept)) {
			String TN  = ajoutBitStuffing(Type+Num);
			String trameA = Flag + TN + Flag;
			returnTrame +=  trameA;
			
		}else if(Type.equals(rejet)) {
			String TN = ajoutBitStuffing(Type + Num);
			String trameR = Flag + TN + Flag;
			returnTrame +=  trameR;
			
		}else if(Type.equals(data) ) {
			String CRC = calCrc.addZeros(Type+Num+Donnees, crc);
			String TNDC = ajoutBitStuffing(Type + Num + Donnees + CRC) ;

			String trameI = Flag + TNDC + Flag;
			returnTrame += trameI;
			
		}
		return returnTrame;
	}

	
	
	public static String ajoutBitStuffing(String partTrames) {
		
         return addBitStuffing("",partTrames,1);
	}
	
	// si on suite de 5 "0" alors en sixieme position on met un "1"
	// si on suite de 5 "1" alors sixieme position on met 0
	
	public static String addBitStuffing(String resultat,String trames,int iter) { 
 
	
		if(iter  == 5) {
			
			
			
			if(trames.charAt(0)=='1') {
				
				
				
				String res = resultat + trames.charAt(0) + "0";
				
				
				if(trames.length() == 1) {
					
					
					return (res +  trames.substring(1));
					
				}else { return  addBitStuffing(res,trames.substring(1),1); }
				
				
				
			}else {
				String res = resultat + trames.charAt(0) +  "1";	
				
				if(trames.length() == 1) {
					
					return (res + trames.substring(1));
					
				}else { return addBitStuffing(res,trames.substring(1),1); }
			}
			
			
		}else {
			
			if(trames.length() == 1) {
				return resultat+trames;
			}
			
			if(trames.charAt(0) == trames.charAt(1)) {
				String answer = resultat + trames.charAt(0);
				int Iter = iter +1;

				
				return addBitStuffing(answer,trames.substring(1),Iter);
			}
			
			if(trames.charAt(0) != trames.charAt(1)) {
				String answer = resultat + trames.charAt(0);
				int Iter = 1;

				return addBitStuffing(answer,trames.substring(1),Iter);
			}
			
			
		}
		return resultat; // on sera jamais rendu dans ce cas, sauf si fait expres!!
	}
	
	
	

	
	public static String removeStuffing(String resultat, String trames) {
		

		
		String res = "";
		
		
		if(trames.length()<=5) { 
			return resultat + trames;
		}
		
		
		
		if(trames.charAt(0)!=trames.charAt(1)) {
			String res1 = resultat + trames.charAt(0);
			return removeStuffing(res1,trames.substring(1));
		}else {
			
			for(int i = 0; i<trames.length();i++) {
			    res += trames.charAt(i);
				
				if(trames.charAt(0) != trames.charAt(i)) {
					String res2 = res.substring(0,res.length()-1);
					return removeStuffing(resultat+res2,trames.substring(i));
				}
				if(i==4) { 
					if(trames.length()<=6) {return resultat+res;} // gerer cas si bit stuffing est a la fin 
					return removeStuffing(resultat+res,trames.substring(6));
				}
			}
			
		}	
		
		return resultat;
	}
	


	
	
	public static void main(String[] args) {
		

		
	}

}
