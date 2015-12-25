package cl.a2r.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList; 
import java.util.List;

import cl.a2r.alimentacion.R;
import cl.a2r.alimentacion.R.color;
import cl.a2r.sap.model.Aplicacion;

public class GridViewAdapter extends BaseAdapter{
	
      private Context mContext;
      private List<Aplicacion> apps = new ArrayList<Aplicacion>();
      private int width;
      private int height;
 
        public GridViewAdapter(Context c, List<Aplicacion> apps, int width, int height) {
            mContext = c;
            this.apps = apps;
            this.width = width;
            this.height = height;
        }
 
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return apps.size();
        }
 
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return apps.get(position);
        }
 
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
 
        @SuppressLint("InflateParams") @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                grid = new View(mContext);
                grid = inflater.inflate(R.layout.grilla_layout, null);
                TextView textView = (TextView) grid.findViewById(R.id.grid_text);
                ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
                
                //Si la app no esta activa, pone el texto e icono gris
                textView.setText(apps.get(position).getNombre());
                if (apps.get(position).getActiva().equals("N")){
                	textView.setTextColor(color.gris);
                	imageView = setLocked(imageView);
                }
                imageView.setImageResource(Iconos.getIcon(apps.get(position).getId()));

                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((width+height)/14, (width+height)/14);
                parms.gravity = Gravity.CENTER;
                parms.setMargins(0, 20, 0, 0);
                imageView.setLayoutParams(parms);
                
            } else {
                grid = (View) convertView;
            }
 
            return grid;
        }
        

		@SuppressWarnings("deprecation")
		private ImageView setLocked(ImageView v){
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);  //0 means grayscale
            ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
            v.setColorFilter(cf);
            v.setAlpha(128);   // 128 = 0.5
            return v;
            
        }
        
        /*
        private void setUnlocked(ImageView v)
        {
            v.setColorFilter(null);
            v.setAlpha(255);
        }
        */
        
}