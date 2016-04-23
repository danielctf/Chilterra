package cl.a2r.traslados;

import java.text.SimpleDateFormat;
import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.Login;
import cl.a2r.animales.PredioLibreLobby;
import cl.a2r.animales.R;
import cl.a2r.animales.R.layout;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.custom.Signature;
import cl.a2r.custom.Utility;
import cl.a2r.salvatajes.SalvatajesV2;
import cl.a2r.sip.model.Auditoria;
import cl.a2r.sip.model.PredioLibre;
import cl.a2r.sip.model.Salvataje;
import cl.a2r.sip.wsservice.WSAuditoriaCliente;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.servicio.SalvatajesServicio;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class Adapter extends BaseAdapter{
	
    private Activity act;
    private SimpleDateFormat df;
    private List<Auditoria> auList;
    private ImageButton btnLock;
    private ProgressBar loading;
    private ListView lvAuditoria;

      public Adapter(Activity act, List<Auditoria> auList) {
          this.act = act;
          this.auList = auList;
          df = new SimpleDateFormat("dd-MM-yyyy");
          loading = (ProgressBar)act.findViewById(R.id.loading);
          lvAuditoria = (ListView)act.findViewById(R.id.lvAuditoria);
      }

      @Override
      public int getCount() {
          // TODO Auto-generated method stub
          return auList.size();
      }

      @Override
      public Object getItem(int position) {
          // TODO Auto-generated method stub
          return auList.get(position);
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

          TextView tvFechaInicio = (TextView)grid.findViewById(R.id.tvFechaInicio);
          tvFechaInicio.setText("Fecha: " + df.format(auList.get(position).getFecha_inicio()));
          TextView tvEstado = (TextView)grid.findViewById(R.id.tvEstado);
          tvEstado.setText("Estado: " + auList.get(position).getEstado().toString());
          btnLock = (ImageButton)grid.findViewById(R.id.btnLock);
          checkBtnState(position);
          
          btnLock.setOnClickListener(new OnClickListener(){
			  public void onClick(View arg0) {
				    String estado = auList.get(position).getEstado();
				    if (estado.equals("CO")){
				    	ShowAlert.showAlert("Procedimiento Cerrado", "El procedimiento ya se encuentra cerrado", act);
				    	return;
				    }
				    
					ShowAlert.askYesNo("Cerrar Procedimiento", "¿Está seguro que desea cerrar el procedimiento?", act, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == -2){
								
							}
						}
					});
			  }
          });
          
          return grid;
      }
	
	  private void checkBtnState(int position){
		  String estado = auList.get(position).getEstado();
		  if (estado.equals("EP")){
			  btnLock.setImageResource(R.drawable.ic_lock_open_white_36dp);
			  btnLock.setBackgroundResource(R.drawable.circlebutton_green);
		  } else {
			  btnLock.setImageResource(R.drawable.ic_lock_outline_white_36dp);
			  btnLock.setBackgroundResource(R.drawable.circlebutton_rojo);
		  }
	  }
}