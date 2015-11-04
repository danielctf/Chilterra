package cl.a2r.custom;

import cl.a2r.login.R;

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
		case 9:
			icon = R.drawable.iconoinventariorotativo;
			break;
		case 10:
			icon = R.drawable.iconoinventariorotativo;
			break;
		}
		return icon;
	}
	
}
