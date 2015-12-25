package cl.a2r.custom;

import cl.a2r.alimentacion.R;

public class Iconos {
	
	public static int getIcon(int codigoApp){
		int icon = 0;
		
		switch(codigoApp){
		case 1:
			icon = R.drawable.iconofarmwalk;
			break;
		 
		}
		return icon;
	}
	
}
