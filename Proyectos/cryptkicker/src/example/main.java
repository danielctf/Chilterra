package example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class main {	
	/**
	 * @author Boris Sotomayor Gómez
	 */
	public static void main(String [] args){
		HashMap hm = new HashMap();
		HashMap r = new HashMap();
		Scanner e = new Scanner(System.in);
		System.out.print("Ingrese cantidad de palabras: ");
		int n = e.nextInt();
		ArrayList lista = new ArrayList();
		e.nextLine();//resetea el scanner
		String in=""; //palabra ingresada.
		String resultado="";
		
		while(n!=0){
			System.out.print("Ingrese palabra: ");
			in=e.nextLine();
			if(hm.containsKey(in.length())==false){
				hm.put(in.length(), new ArrayList());// HM:|tamaño_palabra|listadePalabras|
			}
			((ArrayList)hm.get(in.length())).add(in);//se agrega la palabra a la lista de palabras.
			n--;
		}
				
		//cadena a transformar:
		System.out.print("Ingrese cadena a descrifar: ");
		in=e.nextLine();
		while(!in.equals("0")){
			//Se separarán las palabras de la cadena ingresada y se procesarán.
			String pal=in;
			String temp="";//guarda el resultado temporal.
			int index=0;
			do{
				index=in.indexOf(" ");//indice de primer espacio
				if(index!=-1){//si existe espacio entonces
					pal=in.substring(0, index);
					in=in.substring(index+1);
				}else{pal=in;}// última palabra no tiene espacio sino que termina con '\0'
				
				lista=(ArrayList)(hm.get(pal.length()));
				
				temp=traducir(pal,lista,r);//traduciremos una una palabra en realación a cada una de las palabras de la lista.
				
				if(pValida(temp,lista)){//si la traduccion es válida, la imprime
					if(resultado.length()==0) {resultado=temp;}
					else {resultado=resultado+" "+temp;}
				}
				else {//imprime cadena de *
					resultado=resultado+" ";
					for(int i=0;i<pal.length();i++) {
						resultado=resultado+"*";
					}
				}
			
			}while(index!=-1);
		resultado=resultado+"\n";
		in=e.nextLine();
		}
		System.out.println(resultado+"\n<FIN PROGRAMA>");
	}
	
	static String traducir(String palabra, ArrayList l, HashMap r){
		String traduccion="";
		int i=0; int jmax=0;
		boolean b=!(l.isEmpty());//si es falso (está vacia) no entra al while (nada q traducir)
		System.out.println(l);
		while(b && i<l.size()){//se ven las posibles traducciones (mismo largo)
			traduccion="";
			jmax=((String)l.get(i)).length();
			if(mapear(palabra,(String)l.get(i),r)){//es posible mapearla
				for(int j=0; j<jmax;j++){
					traduccion=traduccion+r.get(palabra.charAt(j));
					System.out.println(traduccion);
				}
			}
			else{traduccion="*";}
			i++;
		}
		System.out.println("Traduccion: " + traduccion);
		return traduccion;
	}
	
	static boolean pValida(String temp, ArrayList l){//retorna verdadero si encuentra una palabra igual en una lista de palabras.
		int i=0;
		while(i<l.size()){
			if(l.get(i).equals(temp)){//Si la palabra está retorna true
				return true;
			}
			i++;
		}
		return false;
	}
	
	static boolean mapear(String s1, String s2, HashMap r){
		if(s1.length()!=s2.length()){ return false; }// palabras de distinto tamaño no se pueden transformar.
		
		//Se recorre el string a transformar:
		for(int i=0;i<s1.length();i++){
			for(int j=0; j<s2.length(); j++){
				if(r.containsKey(s1.charAt(i))==false && r.containsValue(s2.charAt(j))==false){
					r.put(s1.charAt(i),s2.charAt(j));
					System.out.println(s1.charAt(i) + " " + s2.charAt(j));
				}
			}
		}
		return true; //retorna verdadero en otro caso =)
	}
	
}