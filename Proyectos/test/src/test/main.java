package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;



public class main {
	static String[] predios;
	
	public static void main(String[]args){
		predios = new String[5];
		for (int i = 0; i < 5; i++){
			predios[i] = "test " + i;
			System.out.println(predios[i]);
		}
		
		
	}
}
