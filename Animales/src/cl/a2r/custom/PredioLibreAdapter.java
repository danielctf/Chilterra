package cl.a2r.custom;

import java.text.SimpleDateFormat;
import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.Login;
import cl.a2r.animales.PredioLibreLobby;
import cl.a2r.animales.R;
import cl.a2r.animales.R.layout;
import cl.a2r.auditoria.Adapter;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.Auditoria;
import cl.a2r.sip.model.PredioLibre;
import cl.a2r.sip.wsservice.WSAuditoriaCliente;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PredioLibreAdapter extends BaseAdapter {
	
    private Activity act;
    private List<PredioLibre> list;
    private SimpleDateFormat df;
    private Integer instancia;
    private ImageButton btnLock;
    private ProgressBar loading;
    private ListView lvPredioLibre;

      public PredioLibreAdapter(Activity act, List<PredioLibre> list) {
          this.act = act;
          this.list = list;
          df = new SimpleDateFormat("dd-MM-yyyy");
          loading = (ProgressBar)act.findViewById(R.id.loading);
          lvPredioLibre = (ListView)act.findViewById(R.id.lvAuditoria);
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
      public View getView(final int position, View convertView, ViewGroup parent) {
          // TODO Auto-generated method stub
          View grid;
          LayoutInflater inflater = (LayoutInflater) act.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          grid = new View(act.getApplicationContext());
          grid = inflater.inflate(R.layout.layout_lv_auditoria, null);

          //TextView tvFundo = (TextView)grid.findViewById(R.id.tvFundo);
          TextView tvFechaInicio = (TextView)grid.findViewById(R.id.tvFechaInicio);
          TextView tvEstado = (TextView)grid.findViewById(R.id.tvEstado);
          tvFechaInicio.setText("Fecha: " + df.format(((PredioLibre) list.get(position)).getFecha_inicio()));
          tvEstado.setText("Estado: " + ((PredioLibre) list.get(position)).getEstado());
          
          btnLock = (ImageButton)grid.findViewById(R.id.btnLock);
          checkBtnState(position);
          
          btnLock.setOnClickListener(new OnClickListener(){
			  public void onClick(View arg0) {
				    String estado = list.get(position).getEstado();
				    if (estado.equals("CO")){
				    	ShowAlert.showAlert("Procedimiento Cerrado", "El procedimiento ya se encuentra cerrado", act);
				    	return;
				    }
				    
					ShowAlert.askYesNo("Cerrar Procedimiento", "¿Está seguro que desea cerrar el procedimiento?", act, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == -2){
								cerrarProcedimiento(position);
							}
						}
					});
			  }
          });
          
          return grid;
      }

	  private void cerrarProcedimiento(final int position){
			new AsyncTask<Void, Void, Void>(){
				
				String title, msg;
				
				protected void onPreExecute(){
					loading.setVisibility(View.VISIBLE);
					title = "";
					msg = "";
				}
				
				protected Void doInBackground(Void... arg0) {
					try {
						instancia = list.get(position).getId();
		  				WSPredioLibreCliente.cerrarInstancia(Login.user, instancia);
					} catch (AppException e) {
						title = "Error";
						msg = e.getMessage();
					}
					return null;
				}
				
				protected void onPostExecute(Void result){
					loading.setVisibility(View.INVISIBLE);
					if (!title.equals("Error")){
						((PredioLibreLobby) act).getPrediosLibreWS();
					} else {
						ShowAlert.showAlert(title, msg, act);
					}
				}
				
			}.execute();
	  }
	
	  private void checkBtnState(int position){
		  String estado = list.get(position).getEstado();
		  if (estado.equals("EP")){
			  btnLock.setImageResource(R.drawable.ic_lock_open_white_36dp);
			  btnLock.setBackgroundResource(R.drawable.circlebutton_green);
		  } else {
			  btnLock.setImageResource(R.drawable.ic_lock_outline_white_36dp);
			  btnLock.setBackgroundResource(R.drawable.circlebutton_rojo);
		  }
	  }
      
}
