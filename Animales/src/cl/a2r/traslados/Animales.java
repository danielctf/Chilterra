package cl.a2r.traslados;

import cl.a2r.animales.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Animales extends Fragment{
	
	private View v;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	v = inflater.inflate(R.layout.fragment_animales, container, false);

    	return v;
	}

}
