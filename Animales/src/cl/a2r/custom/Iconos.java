package cl.a2r.custom;

import cl.a2r.animales.R;

public class Iconos {
	
	public static int getIcon(int codigoApp){
		int icon = 0;
		
		switch(codigoApp){
		case 1:
			icon = R.drawable.iconobajas;
			break;
		case 2:
			icon = R.drawable.iconoareteo;
			break;
		case 3:
			icon = R.drawable.iconocambiodiio;
			break;
		case 4:
			icon = R.drawable.iconopartos;
			break;
		case 5:
			icon = R.drawable.iconoinseminaciones;
			break;
		case 6:
			icon = R.drawable.iconotraslado;
			break;
		case 7:
			icon = R.drawable.iconopesa;
			break;
		case 8:
			icon = R.drawable.iconoecografias;
			break;
		case 10:
			icon = R.drawable.iconoinventariorotativo;
			break;
		case 11:
			icon = R.drawable.iconoinducciones;
			break;
		case 12:
			icon = R.drawable.ic_maps_local_hospital;
			break;
		case 13:
			icon = R.drawable.iconoprediolibre;
			break;
		case 14:
			icon = R.drawable.ic_registro_de_leche48x48;
			break;
		case 15:
			icon = R.drawable.iconorb51;
			break;
		case 16:
			icon = R.drawable.iconoinventariorotativo;
			break;
		case 17:
			icon = R.drawable.ic_iconobusquedaxprocedimiento;
			break;
		}
		return icon;
	}
	
}
