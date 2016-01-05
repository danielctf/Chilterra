package cl.a2r.custom;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.BaseAdapter;
import cl.a2r.alimentacion.R;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.cobertura.StockM;

public class StockDetalleAdapter extends BaseAdapter{

    private Context mContext;
    private List<StockM> meds;
    private SimpleDateFormat df;

      public StockDetalleAdapter(Context c, List<StockM> meds) {
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
          View grid;
          LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          grid = new View(mContext);
          grid = inflater.inflate(R.layout.stock_detalle_layout, null);
          
          LinearLayout container = (LinearLayout) grid.findViewById(R.id.container);
          TextView tvClick = (TextView) grid.findViewById(R.id.tvClick);
          TextView tvClickNumber = (TextView) grid.findViewById(R.id.tvClickNumber);
          TextView tvFecha = (TextView) grid.findViewById(R.id.tvFecha);

          TextView tvTipoMuestra = (TextView) grid.findViewById(R.id.tvTipoMuestra);
          
          double clickFinal = ((StockM) meds.get(position)).getMed().getClickFinal();
          double clickInicial = ((StockM) meds.get(position)).getMed().getClickInicial();
          double muestras = ((StockM) meds.get(position)).getMed().getMuestras();
          double click = (clickFinal - clickInicial) / muestras;

          tvClickNumber.setText(Double.toString(roundForDisplay(click)));
          tvFecha.setText(df.format(((StockM) meds.get(position)).getMed().getFecha()));
          tvTipoMuestra.setText(((StockM) meds.get(position)).getMed().getTipoMuestraNombre());
          
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
  		if (click <= 7){
  			return R.drawable.circlebutton_amarillo;
  		} else if (click > 7 && click <= 20){
  			return R.drawable.circlebutton_lightgreen;
  		} else {
  			return R.drawable.circlebutton_cafe;
  		}
  	}
	
}
