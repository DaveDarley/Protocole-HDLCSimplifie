import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Scanner;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


/*
 * Temporisateur de 3 secondes pour les pertes de trames, n'a pas ete fait !!!
 * Apres ca , tt autre fonctionnalite , c'est fait !!!
 */



public class Emetteur {
	

	
    static createTrames createTram = new createTrames();
    static Emetteur convertBinary = new Emetteur();
    

	static String Text ; 
	static String CRC = "10001000000100001";

	
	// trames qu'on a envoye mais on pas encore eu de reponses!!
	static ArrayList<String> tramesActuelles = new ArrayList<String>();
	static String Flag = "01111110";
	static String Trames = "";
	 
	 

	
	
 public String intToBinary(int a) {
        String temp = Integer.toBinaryString(a);
        while(temp.length() < 8){
            temp = "0"+temp;
    }
    
    return temp+"";
}
	


	
	
 public static void readFile(String fileName) { 
	   String text = "" ; // ici sera mis le text lu
	   try {
		      File myObj = new File(fileName);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        
		        text += data;
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	  
	   Trames =  toBinary(text,8); // contient juste le texte en binaire
	   
  
	 
 }
 
 
 // Source : Stack overflow
 public static String toBinary(String str, int bits) {
	    String result = "";
	    String tmpStr;
	    int tmpInt;
	    char[] messChar = str.toCharArray();

	    for (int i = 0; i < messChar.length; i++) {
	        tmpStr = Integer.toBinaryString(messChar[i]);
	        tmpInt = tmpStr.length();
	        if(tmpInt != bits) {
	            tmpInt = bits - tmpInt;
	            if (tmpInt == bits) {
	                result += tmpStr;
	            } else if (tmpInt > 0) {
	                for (int j = 0; j < tmpInt; j++) {
	                    result += "0";
	                }
	                result += tmpStr;
	            } else {
	                System.err.println("argument 'bits' is too small");
	            }
	        } else {
	            result += tmpStr;
	        }
	        
	    }

	    return result;
	}

 
 // Creation des trames de donnees et de fin de communication !!!
 public  String createTram( int nbTrame) throws IOException { 
	 

      // faut pouvoir envoyer 7 trames exacts ou moins
	 
	 if(Trames.length() <= 56 && Trames.length() != 0) {
		 if(Trames.length() > 8) {
		 String trames = Trames.substring(0,8);
		 
		 
		 
		 String tramesCrees = createTram.createTrams(Flag, toBinary("I",8),intToBinary(7-nbTrame) , trames, CRC);
		 
		 tramesActuelles.add(tramesCrees);
		 
		 Trames = Trames.substring(8);
		 return createTram(nbTrame-1);
		 }
		 else {
			 
			 String trames = Trames.substring(0,8);
			 String tramesCrees = createTram.createTrams(Flag, toBinary("I",8),intToBinary(7-nbTrame) , trames, CRC);
			 tramesActuelles.add(tramesCrees);
			 Trames = Trames.substring(8);
			 
			 String toSendTram = "";
			 for(int i = 0; i<tramesActuelles.size();i++) {
				 toSendTram += tramesActuelles.get(i);
			 }
			 System.out.println(" ");
			 System.out.println("********** LA TRAME QUI EST ENVOYE **************"); // Test
			 System.out.println( toSendTram); // Test
			 System.out.println("**************************************************");
			 System.out.println(" ");
			 


			 return toSendTram;
			 
			 
			 
		 }
	 }
	 
	 
		 
		 
	if(Trames.length() == 0) { // trames de fin de communication
		String type = toBinary("F",8);
		String trames = createTram.createTrams("", type, "", "", "");
		
		System.out.println("******* TRAME DE FIN DE COMMUNICATION **********"); // Test
		System.out.println(trames);
	    System.out.println("**************************************************");
		
	    return trames ;
		
	}
	
		 
	 String toSendTram = "";
	 String text = Trames.substring(0,8); // chaque donnee est un 8-bit
	 
	 int nb = 7;

	 if(nbTrame>=1 && Trames.length()!= 0) {
		 String tramesCrees = createTram.createTrams(Flag, toBinary("I",8),intToBinary(7-nbTrame) , text, CRC);
		 tramesActuelles.add(tramesCrees);
		 
		 if(Trames.length() != 0) {
			 Trames = Trames.substring(8);
		 }
		 return createTram(nbTrame - 1);
	 }
	 
	 
	 
	 for(int i = 0; i<tramesActuelles.size();i++) {
		 toSendTram += tramesActuelles.get(i);
	 }
	 System.out.println(" ");
	 System.out.println("********** LA TRAME QUI EST ENVOYE **************"); // Test
	 System.out.println( toSendTram); // Test
	 System.out.println("**************************************************");
	 System.out.println(" ");
	 


	 return toSendTram;
	 
	 

	}
 
 
 // Go-Back-N ; Toujours accepte par le recepteur !!
 public  String communicationTrames() throws IOException {
	 
	     System.out.println("******* ENVOI DE LA TRAME DE CONNEXION.... *********\n");
	 

		 String Type = toBinary("C",8);
		 

		 String trames = createTram.createTrams("",Type , "", "", "");
		 
		 System.out.println("******** MA TRAME DE DEMANDE DE CONNEXION! ***********");
		 System.out.println(trames);
		 System.out.println("******************************************************");
		 

		 return trames;

 }
 
 /*
  * Comme taille fenetre = 7 ; recepteur renvoie un A6 qd les 7 trames sont recus sans erreurs
  */
 
  
 public  String fromRecepToEmet(String trames) throws IOException {
	 

	 
	 String Trames = trames;
	 
	 
	 String trameWithoutStuff = createTram.Flag + createTram.removeStuffing("",Trames.substring(8,Trames.length()-8)) +  createTram.Flag;

     String retour = "";
	 
    
     
	 String typeSansStuff =  trameWithoutStuff.substring(8,16);
	 String numSansStuff  =  trameWithoutStuff.substring(16,24);


	 String accept = toBinary("A",8);
	 String rejet = toBinary("R",8);
	 String renvoieTrames = "";
	 
	 
     if(typeSansStuff.equals(accept)) { // Fenetre = 7 et donc on accepte 7 trames d'un coup , si y a pas d'erreurs!!
    	 
    	 tramesActuelles.clear();
    	 retour += createTram(7);
    	 
     }
     
     if(typeSansStuff.equals(rejet)) { 

    	 
    	 int nbTramesAttendu = Integer.parseInt(numSansStuff,2);
    	 
    	
    	 
    	 for(int i = nbTramesAttendu ; i<tramesActuelles.size(); i++) {
    		 renvoieTrames += tramesActuelles.get(i);
    	 }
    	 
    	 System.out.println("************** RENVOIE DES DONNEES ERONNEES ************");
    	 System.out.println(renvoieTrames);
    	 System.out.println("********************************************");
    	
    	 
    	 retour  +=  renvoieTrames;

    	 
    	 
     }
     return retour;
	
	 }





 
	public static void main(String[] args ) throws IOException { 
		
		
		

		
		
       
        System.out.println("###############################################");
        System.out.println("#                                             #");
        System.out.println("#           COMMUNICATION PAR TRAME           #");
        System.out.println("#                   EMETTEUR                  #");
        System.out.println("#                                             #");
        System.out.println("###############################################");
        System.out.println();
        System.out.println();


        

        
        Emetteur em = new Emetteur();
        em.readFile("src/monfichier.txt");
        
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(1205);
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + 1205 + ", " + e);
            System.exit(1);
        }

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Accept failed: " + 1205 + ", " + e);
            System.exit(1);
        }

        try {
            BufferedReader br = new BufferedReader(
                                 new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter pw = new PrintWriter(
                             new BufferedOutputStream(clientSocket.getOutputStream(), 1024), false);
            
            String inputLine, outputLine;

         
            
            outputLine = em.communicationTrames();
            pw.println(outputLine);
            pw.flush();
            
            

            while ((inputLine = br.readLine()) != null) {
            	
            	System.out.println("from recepteur "+inputLine);
                
            	
            	
            	
            	outputLine = em.fromRecepToEmet(inputLine);
            	
            	
                 pw.println(outputLine);
                 pw.flush();
                 
            }
            pw.close();
            br.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
        
        
	}       
        
        
 
        
        
		
		

	
	
	
}

