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
import cl.ar2.sqlite.cobertura.StockM;

public class StockAdapter extends BaseAdapter{

    private Context mContext;
    private List<StockM> meds;
    private SimpleDateFormat df;

      public StockAdapter(Context c, List<StockM> meds) {
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
	          
	          tvPotrero.setText("P" + Integer.toString(((StockM) meds.get(position)).getMed().getPotreroId()));
	          
	          double clickFinal = ((StockM) meds.get(position)).getMed().getClickFinal();
	          double clickInicial = ((StockM) meds.get(position)).getMed().getClickInicial();
	          double muestras = ((StockM) meds.get(position)).getMed().getMuestras();
	          double click = (clickFinal - clickInicial) / muestras;
	          //tvClick.setText("Click: " + Double.toString(roundForDisplay(click)));
	          tvClickNumber.setText(Double.toString(roundForDisplay(click)));
	          
	          tvFecha.setText(df.format(((StockM) meds.get(position)).getMed().getFecha()));
	          
	          
	          //tvMS.setText("MS: " + Integer.toString(((StockM) meds.get(position)).getMed().getMateriaSeca()));
	          tvTipoMuestra.setText(((StockM) meds.get(position)).getMed().getTipoMuestraNombre());
	
	          //container.setBackgroundResource(getColor(click));
	          tvPotrero.setBackgroundResource(getColor(click));
          } else {
              TextView tvPotrero = (TextView) grid.findViewById(R.id.tvPotrero);
              tvPotrero.setText("P" + Integer.toString(((StockM) meds.get(position)).getMed().getPotreroId()));
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
