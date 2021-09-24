import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Recepteur {
	 
	 

	 
	 
	static calculcrc Crc = new calculcrc();
	static Emetteur convertBinary = new Emetteur();
	static createTrames createTrame = new createTrames();
    static ArrayList<String> mesTrames = new ArrayList<String>();
	static String receivedBits = "";
	
	int testErreur = 0; // utiliser uniquement pour forcer un cas d'erreur et de retransmission de donnees
	

/*
 * Lien entre le serveur et ma classe recepteur	
 */


	public String recepTrames(String tramesRecus) throws IOException {
		

		
		String trameRecuS = tramesRecus;
		
		String trameWithoutStuff = "";
		
		String trame = trameRecuS.substring(8,trameRecuS.length()-8);
		String[] parts = trame.split("0111111001111110");
		
		for(int i =0; i<parts.length; i++) {
			String trames = createTrame.Flag + createTrame.removeStuffing("",parts[i]) + createTrame.Flag;
			trameWithoutStuff+= trames;
			
		}


		String type = trameWithoutStuff.substring(8,16);
		


		
		if(type.equals(createTrame.connectRequest)) {
			

			//double rand = Math.random();
			double rand = 0.6;
			
			if(rand>=0.5) { //on accepte la communication ; RR1 ou A 
				String tramRep = createTrame.createTrams("", convertBinary.toBinary("A",8), "00000000", "", "") ; 
				
				 System.out.println("ENVOI DE LA TRAME QUI AUTORISE LA CONNEXION...");
				 
				 System.out.println(" ");
				 System.out.println("Trame Reponse "+ tramRep);
				 System.out.println(" ");
				 
				 
				 return tramRep;

				
				
				
			}else { // on accepte pas !!  R1
			   String rep = createTrame.createTrams("", convertBinary.toBinary("R",8), "00000000", "", "");
			   System.out.println("REFUS DE CONNEXION...");
			   
			   System.out.println(" ");
			   System.out.println("Trame Reponse "+ rep);
			   System.out.println(" ");
			   
			   return rep;
			    
			   
			}
			
		}else if(type.equals(createTrame.finCom)){ 
			
			System.out.println("FIN COMMUNICATION !!");

			return readReceivedData();
			
		}else if(type.equals(createTrame.pBitRequest)) {
			
			
		}
		   else {
			     System.out.println("******** Reception des Trames ********");
			     
		         return organisationTrames(trameWithoutStuff);} // ici on a recu trames de donnees
		
		return "";
		}
			
		
	
	

public  boolean veraciteTramesRecus(String trames) { 
		
		
		
		String typeSansStuff = trames.substring(8,16);
		String numSansStuff = trames.substring(16,24);
		String donneesSansStuff =  trames.substring(24,32);
		String fcsSansStuff =  trames.substring(32,48);
		

		
		String FcsRecu = Crc.addZeros(typeSansStuff+numSansStuff+donneesSansStuff, convertBinary.CRC);
		

		return  (fcsSansStuff.equals(FcsRecu));
		

	}
	
	public  String organisationTrames(String tramesRecu) {
		

		
		 String retour = "";
		
		
		String trames = tramesRecu.substring(0,56);
		
		
		
		/*
		 * Forcons un cas d'erreur , pour voir le renvoie des trames!!!
		 * Cas Inversion de bits
		 */
		if(testErreur == 0) {
			
		  if(trames.charAt(8) == '0') {
			  trames = trames.substring(0,8)+"1"+trames.substring(9);
		  }else {
			  trames = trames.substring(0,8)+"0"+trames.substring(9);
		  }
		  testErreur ++;
		}
		
		
		
		System.out.println(" ");
		System.out.println("Trames recus de l'emetteur: "+trames);
		System.out.println(" ");
		
		
		if(veraciteTramesRecus(trames)) {
			mesTrames.add(trames.substring(24,32));
			
			if(tramesRecu.length() == 56) {
				String nouveauTrame = createTrame.createTrams("",convertBinary.toBinary("A",8) ,trames.substring(16,24), "", "");
				
				
				System.out.println("******** Trames Recus Correctes ***********");

				retour = nouveauTrame;
				
				
			}else {return organisationTrames(tramesRecu.substring(56));}
			
			
		}else {
			

			System.out.println("********** La trame "+ createTrame.removeStuffing("", trames.substring(16,24)) + " est incorrect **********");
			String trameR = createTrame.createTrams("",convertBinary.toBinary("R",8) ,trames.substring(16,24), "", "");
			

			retour =  trameR;
	     }
		
		
		return retour;
		
	}

	
	
    public static String readReceivedData() {
    	
        for (int i = 0; i < mesTrames.size(); i++) {
        	
        	char reponse = (char)Integer.parseInt(mesTrames.get(i), 2);
        	
            receivedBits += reponse;
          }
         
    	 return receivedBits;
    }
 
	
	public static void main(String[] args) throws IOException {
	
		
		
		
      
        System.out.println("###############################################");
        System.out.println("#                                             #");
        System.out.println("#           COMMUNICATION PAR TRAME           #");
        System.out.println("#                  RECEPTEUR                  #");
        System.out.println("#                                             #");
        System.out.println("###############################################");
        System.out.println();
        System.out.println();
   
        
        
        Recepteur receiver = new Recepteur();
        
        
        Socket kkSocket = null;
        PrintWriter pw = null;
        BufferedReader br = null;
        int i = 0;
        String reponse;

        try {
            kkSocket = new Socket("localhost", 1205);
            pw = new PrintWriter(kkSocket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: taranis");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: taranis");
        }
        
        
        
        if (kkSocket != null && pw != null && br != null) {
            try {

                int c;
                String fromServer;

                while ((fromServer = br.readLine()) != null) {
                	
                	System.out.println("From Server: " + fromServer);


                	
                	reponse  = receiver.recepTrames(fromServer);
                	
                    if (receiver.receivedBits.length() != 0)
                        break;
                	
                	System.out.println("reponse au serveur: "+reponse);
   

                    
                    pw.println(reponse);
                    pw.flush();
                    
                }
                
               System.out.println("LE texte recu: "+ receiver.receivedBits);

                pw.close();
                br.close();
                kkSocket.close();
            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
		

	}
}
