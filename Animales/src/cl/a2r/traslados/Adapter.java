package cl.a2r.traslados;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.R;
import cl.a2r.sip.model.Instancia;
import cl.a2r.sip.model.Predio;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Adapter extends BaseAdapter{
	
    private Activity act;
    private SimpleDateFormat dfFecha;
    private SimpleDateFormat dfTime;
    private SimpleDateFormat dfComp;
    private List<Instancia> trasList;
    private ProgressBar loading;
    private ListView lvTraslados;
    private Date currentDate;

      public Adapter(Activity act, List<Instancia> trasList) {
          this.act = act;
          this.trasList = trasList;
          dfFecha = new SimpleDateFormat("dd MMM");
          dfTime = new SimpleDateFormat("HH:mm");
          dfComp = new SimpleDateFormat("dd-MM-yyyy");
          currentDate = new Date();
          loading = (ProgressBar)act.findViewById(R.id.loading);
          lvTraslados = (ListView)act.findViewById(R.id.lvTraslados);
      }

      @Override
      public int getCount() {
          // TODO Auto-generated method stub
          return trasList.size();
      }

      @Override
      public Object getItem(int position) {
          // TODO Auto-generated method stub
          return trasList.get(position);
      }

      @Override
      public long getItemId(int position) {
          // TODO Auto-generated method stub
          return 0;
      }

      @SuppressLint("InflateParams")
      public View getView(final int position, View convertView, ViewGroup parent) {
          // TODO Auto-generated method stub
          View grid;
          LayoutInflater inflater = (LayoutInflater) act.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          grid = new View(act.getApplicationContext());
          grid = inflater.inflate(R.layout.layout_lv_traslados, null);

          Button btnES = (Button)grid.findViewById(R.id.btnES);
          TextView tvES = (TextView)grid.findViewById(R.id.tvES);
          TextView tvDetalle = (TextView)grid.findViewById(R.id.tvDetalle);
          
          Predio origen = trasList.get(position).getInstancia().getTraslado().getOrigen();
          if (origen.getId() != null && origen.getId().intValue() != 0){
        	  if (Aplicaciones.predioWS.getId().intValue() == origen.getId().intValue()){
        		  //Salida
        		  btnES.setText("S");
        		  btnES.setBackgroundResource(R.drawable.circlebutton_azul);
        		  tvES.setText("Salida");
        		  tvDetalle.setText("Destino: " + trasList.get(position).getInstancia().getTraslado().getDestino().getNombre());
        	  } else {
        		  //Entrada
        		  btnES.setText("E");
        		  btnES.setBackgroundResource(R.drawable.circlebutton_lightgreen);
        		  tvES.setText("Entrada");
        		  tvDetalle.setText("Origen: " + trasList.get(position).getInstancia().getTraslado().getOrigen().getNombre());
        	  }
          } else {
        	  //Salida borrador
        	  btnES.setText("S");
        	  btnES.setBackgroundResource(R.drawable.circlebutton_azul);
        	  tvES.setText("Salida");
        	  tvDetalle.setText("Destino:");
          }
          
          TextView tvGuia = (TextView)grid.findViewById(R.id.tvGuia);
          Integer nro_documento = trasList.get(position).getInstancia().getTraslado().getNro_documento();
          if (nro_documento != null && nro_documento.intValue() != 0){
        	  tvGuia.setText("Guia: " + nro_documento);
          } else {
        	  tvGuia.setText("Guia:");
          }
          
          ImageView ivEstado = (ImageView)grid.findViewById(R.id.ivEstado);
          String estado = trasList.get(position).getInstancia().getEstado();
          if (estado.equals("BO")){
        	  ivEstado.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
          } else if (estado.equals("EP")){
        	  ivEstado.setImageResource(R.drawable.ic_local_shipping_black_24dp);
          } else if (estado.equals("CO")){
        	  ivEstado.setImageResource(R.drawable.ic_done_all_black_24dp);
          }
          
          TextView tvFecha = (TextView)grid.findViewById(R.id.tvFecha);
          Date fechaMovtoOriginal = trasList.get(position).getInstancia().getTraslado().getFecha();
          if (fechaMovtoOriginal != null){
        	  Date fechaMovto = null, fechaActual = null;
			  try {
				fechaMovto = dfComp.parse(dfComp.format(fechaMovtoOriginal));
				fechaActual = dfComp.parse(dfComp.format(currentDate));
			  } catch (ParseException e) {}
        	  if (fechaMovto.before(fechaActual)){
        		  tvFecha.setText(dfFecha.format(fechaMovtoOriginal));
        	  } else {
        		  tvFecha.setText(dfTime.format(fechaMovtoOriginal));
        	  }
          } else {
        	  tvFecha.setText("");
          }
          
          return grid;
      }
      
//	  private void checkBtnState(int position){
//		  String estado = auList.get(position).getEstado();
//		  if (estado.equals("EP")){
//			  btnLock.setImageResource(R.drawable.ic_lock_open_white_36dp);
//			  btnLock.setBackgroundResource(R.drawable.circlebutton_green);
//		  } else {
//			  btnLock.setImageResource(R.drawable.ic_lock_outline_white_36dp);
//			  btnLock.setBackgroundResource(R.drawable.circlebutton_rojo);
//		  }
//	  }
}