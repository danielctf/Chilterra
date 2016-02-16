package cl.a2r.salvatajes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cl.a2r.sip.model.Ganado;

public class SalvatajeAdapter extends ArrayAdapter<Ganado> {

	private List<Ganado> list;

   public SalvatajeAdapter(Context context, int resource, List<Ganado> list) {
		super(context, resource, list);
		
		this.list = list;
	}

   public View getView(int position, View convertView, ViewGroup parent) {

       View v = super.getView(position, convertView, parent);
       
       ((TextView)v.findViewById(android.R.id.text1)).setText("DIIO: " + list.get(position).getEid()
    		   + "   Obs: " + list.get(position).getObservacion());
       
       return v;
   }

   @Override
   public int getCount() {
       return super.getCount();
   }



}