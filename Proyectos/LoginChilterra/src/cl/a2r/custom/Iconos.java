package cl.a2r.custom;

import cl.a2r.login.R;

public class Iconos {

	public static int getIcon(String codigoApp){
		int icon = 0;
		
		switch(codigoApp){
		case "RM1":
			icon = R.drawable.ic_editor_insert_drive_file;
			break;
		}
		return icon;
	}
}
