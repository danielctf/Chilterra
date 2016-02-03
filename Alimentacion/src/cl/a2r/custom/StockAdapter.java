package cl.a2r.custom;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.BaseAdapter;
import cl.a2r.alimentacion.R;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.cobertura.StockM;

public class StockAdapter extends BaseAdapter{

    private Context mContext;
    private List<Medicion> meds;
    private SimpleDateFormat df;

      public StockAdapter(Context c, List<Medicion> meds) {
          mContext = c;
          this.meds = meds;
          df = new SimpleDateFormat("dd-MM-yyyy");
      }

      @Override
      public int getCount() {
          // TODO Auto-generated method stub
          return meds.size();
      }

      @Override
      public Object getItem(int position) {
          // TODO Auto-generated method stub
          return meds.get(position);
      }

      @Override
      public long getItemId(int position) {
          // TODO Auto-generated method stub
          return 0;
      }

      @SuppressLint("InflateParams")
      public View getView(int position, View convertView, ViewGroup parent) {
          // TODO Auto-generated method stub
          View grid;
          LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          grid = new View(mContext);
          grid = inflater.inflate(R.layout.card_layout, null);
          if (meds.get(position).getId() != null){
	          LinearLayout container = (LinearLayout) grid.findViewById(R.id.container);
	          TextView tvPotrero = (TextView) grid.findViewById(R.id.tvPotrero);
	          TextView tvClick = (TextView) grid.findViewById(R.id.tvClick);
	          TextView tvClickNumber = (TextView) grid.findViewById(R.id.tvClickNumber);
	          TextView tvFecha = (TextView) grid.findViewById(R.id.tvFecha);
	          //TextView tvMS = (TextView) grid.findViewById(R.id.tvMS);
	          TextView tvTipoMuestra = (TextView) grid.findViewById(R.id.tvTipoMuestra);
	          
	          tvPotrero.setText("P" + Integer.toString(((Medicion) meds.get(position)).getNumeroPotrero()));
	          
	          double clickFinal = ((Medicion) meds.get(position)).getClickFinal();
	          double clickInicial = ((Medicion) meds.get(position)).getClickInicial();
	          double muestras = ((Medicion) meds.get(position)).getMuestras();
	          double click = (clickFinal - clickInicial) / muestras;
	          //tvClick.setText("Click: " + Double.toString(roundForDisplay(click)));
	          tvClickNumber.setText(Double.toString(roundForDisplay(click)));
	          
	          tvFecha.setText(df.format(((Medicion) meds.get(position)).getFecha()));
	          
	          
	          //tvMS.setText("MS: " + Integer.toString(((StockM) meds.get(position)).getMed().getMateriaSeca()));
	          tvTipoMuestra.setText(((Medicion) meds.get(position)).getTipoMuestraNombre());
	
	          //container.setBackgroundResource(getColor(click));
	          tvPotrero.setBackgroundResource(getColor(click));
          } else {
              TextView tvPotrero = (TextView) grid.findViewById(R.id.tvPotrero);
              tvPotrero.setText("P" + Integer.toString(((Medicion) meds.get(position)).getNumeroPotrero()));
              tvPotrero.setBackgroundResource(getColor(0));
          }
          return grid;
      }
      
  	private double roundForDisplay(double click){
		double res = 0;
		res = click * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
  	
  	private int getColor(double click){
  		if (click == 0){
  			return R.drawable.circlebutton_rojo;
  		}else if (click > 0 && click <= 7){
  			return R.drawable.circlebutton_amarillo;
  		} else if (click > 7 && click <= 20){
  			return R.drawable.circlebutton_lightgreen;
  		} else {
  			return R.drawable.circlebutton_cafe;
  		}
  	}
	
}
