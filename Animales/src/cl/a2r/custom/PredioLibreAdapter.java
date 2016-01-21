package cl.a2r.custom;

import java.text.SimpleDateFormat;
import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.Login;
import cl.a2r.animales.PredioLibreLobby;
import cl.a2r.animales.R;
import cl.a2r.animales.R.layout;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.PredioLibre;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PredioLibreAdapter extends BaseAdapter {
	
    private Activity act;
    private List<PredioLibre> list;
    private SimpleDateFormat df;
    private Integer instancia;
    private ImageButton btnClosePL;

      public PredioLibreAdapter(Activity act, List<PredioLibre> list) {
          this.act = act;
          this.list = list;
          df = new SimpleDateFormat("dd-MM-yyyy");
      }

      @Override
      public int getCount() {
          // TODO Auto-generated method stub
          return list.size();
      }

      @Override
      public Object getItem(int position) {
          // TODO Auto-generated method stub
          return list.get(position);
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
          LayoutInflater inflater = (LayoutInflater) act.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          grid = new View(act.getApplicationContext());
          grid = inflater.inflate(R.layout.layout_lv_predio_libre, null);

          //TextView tvFundo = (TextView)grid.findViewById(R.id.tvFundo);
          TextView tvFechaInicio = (TextView)grid.findViewById(R.id.tvFechaInicio);
          TextView tvEstado = (TextView)grid.findViewById(R.id.tvEstado);
          btnClosePL = (ImageButton)grid.findViewById(R.id.btnClosePL);
          
          final int pos = position;
          btnClosePL.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				btnClosePL.setVisibility(View.INVISIBLE);
				instancia = list.get(pos).getId();
				hand.postDelayed(run, 100);
			}
        	  
          });
          
          tvFechaInicio.setText("Fecha Inicio:\n" + df.format(((PredioLibre) list.get(position)).getFecha_inicio()));
          tvEstado.setText("Estado: " + ((PredioLibre) list.get(position)).getEstado());
          
          return grid;
      }
      
      Handler hand = new Handler();
      Runnable run = new Runnable() {
          public void run() {
  			try {
  				WSPredioLibreCliente.cerrarInstancia(Login.user, instancia);
  				((PredioLibreLobby) act).getPrediosLibreWS();
  			} catch (AppException e) {
  				ShowAlert.showAlert("Error", e.getMessage(), act);
  			} finally {
  				btnClosePL.setVisibility(View.VISIBLE);
  			}
          }
      };

}
