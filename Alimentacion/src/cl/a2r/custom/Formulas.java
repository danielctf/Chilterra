package cl.a2r.custom;

import java.util.Calendar;
import java.util.Date;
import cl.a2r.sap.model.Medicion;

public class Formulas {

	public static double roundForDisplay(double click){
		double res = 0;
		res = click * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
	
	public static double calculaClick(Medicion med){
		double res = 0;
		res = ((double)med.getClickFinal() - (double)med.getClickInicial()) / (double)med.getMuestras();
		return res;
	}
	
	public static int calculaMS(double click){
		int ms = 0;
		Date fecha = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		int mesActual = cal.get(Calendar.MONTH);
		mesActual++;
		
		switch (mesActual){
		case 1:
			ms = calculaMSJanuary(click);
			break;
		case 2:
			ms = calculaMSFebruary(click);
			break;
		case 3:
			ms = calculaMSMarch(click);
			break;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			ms = calculaMSWinter(click);
			break;
		case 10:
			ms = calculaMSOctober(click);
			break;
		case 11:
			ms = calculaMSNovember(click);
			break;
		case 12:
			ms = calculaMSDecember(click);
			break;
		}

		return ms;
		
	}
	
	public static double calculaCrecimiento(double click){
		double crecimiento = 0;
		Date fecha = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		int mesActual = cal.get(Calendar.MONTH);
		mesActual++;
		switch (mesActual){
		case 1:
			crecimiento = crecimientoJanuary(click);
			break;
		case 2:
			crecimiento = crecimientoFebruary(click);
			break;
		case 3:
			crecimiento = crecimientoMarch(click);
			break;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			crecimiento = crecimientoWinter(click);
			break;
		case 10:
			crecimiento = crecimientoOctober(click);
			break;
		case 11:
			crecimiento = crecimientoNovember(click);
			break;
		case 12:
			crecimiento = crecimientoDecember(click);
			break;
		}

		return crecimiento;
		
	}
	
	private static int calculaMSWinter(double click){
		int res = 0;
		res = (int) Math.round(click * (double)140 + (double)500);
		return res;
	}
	
	private static int calculaMSOctober(double click){
		int res = 0;
		res = (int) Math.round(click * (double)115 + (double)850);
		return res;
	}
	
	private static int calculaMSNovember(double click){
		int res = 0;
		res = (int) Math.round(click * (double)120 + (double)1000);
		return res;
	}
	
	private static int calculaMSDecember(double click){
		int res = 0;
		res = (int) Math.round(click * (double)140 + (double)1200);
		return res;
	}
	
	private static int calculaMSJanuary(double click){
		int res = 0;
		res = (int) Math.round(click * (double)165 + (double)1250);
		return res;
	}
	
	private static int calculaMSFebruary(double click){
		int res = 0;
		res = (int) Math.round(click * (double)185 + (double)1200);
		return res;
	}
	
	private static int calculaMSMarch(double click){
		int res = 0;
		res = (int) Math.round(click * (double)170 + (double)1100);
		return res;
	}
	
	private static double crecimientoWinter(double click){
		double res = 0;
		res = click * (double) 140;
		res = res * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
	
	private static double crecimientoOctober(double click){
		double res = 0;
		res = click * (double) 115;
		res = res * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
	
	private static double crecimientoNovember(double click){
		double res = 0;
		res = click * (double) 120;
		res = res * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
	
	private static double crecimientoDecember(double click){
		double res = 0;
		res = click * (double) 140;
		res = res * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
	
	private static double crecimientoJanuary(double click){
		double res = 0;
		res = click * (double) 165;
		res = res * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
	
	private static double crecimientoFebruary(double click){
		double res = 0;
		res = click * (double) 185;
		res = res * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
	
	private static double crecimientoMarch(double click){
		double res = 0;
		res = click * (double) 170;
		res = res * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
	
}
