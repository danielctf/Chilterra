package cl.a2r.custom;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;
import cl.a2r.alimentacion.R;
import cl.a2r.sap.model.Medicion;

public class StockAdapter extends BaseAdapter{

    private Context mContext;
    private List<Medicion> meds;

      public StockAdapter(Context c, List<Medicion> meds) {
          mContext = c;
          this.meds = meds;
      }

      @Override
      public int getCount() {
          // TODO Auto-generated method stub
          return meds.size();
      }

      @Override
      public Object getItem(int position) {
          // TODO Auto-generated method stub
          return null;
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

              
          } else {
              grid = (View) convertView;
          }

          return grid;
      }
	
}
