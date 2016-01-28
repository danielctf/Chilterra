package cl.a2r.animales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.ArrayAdapterBrucelosis;
import cl.a2r.custom.ArrayAdapterInyeccionTB;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Brucelosis;
import cl.a2r.sip.model.InyeccionTB;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class PredioLibreLogsBR extends Activity implements View.OnClickListener{

    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;
    private boolean mSwiping = false;
    private boolean mItemPressed = false;
    private ArrayAdapterBrucelosis mAdapter;
	private ImageButton goBack;
	private TextView app;
	private ListView listViewHistorial;
	private Spinner spinnerMangada;
	private HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_mangada_logs);
		
		cargarInterfaz();
		cargarListeners();
		getMangadas();
	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		listViewHistorial = (ListView)findViewById(R.id.listViewHistorial);
		app = (TextView)findViewById(R.id.app);
		spinnerMangada = (Spinner)findViewById(R.id.spinnerMangada);
	}
	
	private void getMangadas(){
		Integer cantMangadas = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			cantMangadas = extras.getInt("cantMangadas");
		}
		List<String> mangList = new ArrayList<String>();
		
		for (int i = 0; i <= cantMangadas.intValue(); i++){
			if (i == 0){
				mangList.add("Sincronizados");
			} else {
				mangList.add("Mangada " + i);
			}
		}
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mangList);
		spinnerMangada.setAdapter(mAdapter);
		spinnerMangada.setSelection(cantMangadas);
	}
	
	private void cargarListeners(){
		spinnerMangada.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				getDiiosMangada();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	private void getDiiosMangada(){
		app.setText("Candidatos Encontrados");
		List<Brucelosis> list = PredioLibreBrucelosis.listEncontrados;
		List<Brucelosis> listFiltrada = new ArrayList<Brucelosis>();
		for (Brucelosis b : list){
			if (b.getGanado().getMangada().intValue() == spinnerMangada.getSelectedItemPosition()){
				listFiltrada.add(b);
			}
		}
		mAdapter = new ArrayAdapterBrucelosis(this, android.R.layout.simple_list_item_1, listFiltrada, mTouchListener);
		listViewHistorial.setAdapter(mAdapter);
	}

	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		}
		
	}
	
	//---------------------------------------------------------------------
	//----------------------ANIMACION LISTVIEW HISTORIAL-------------------
	//---------------------------------------------------------------------
	
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        
        float mDownX;
        private int mSwipeSlop = -1;
        
        @Override
        public boolean onTouch(final View v, MotionEvent event) {
        	v.setBackgroundResource(R.drawable.button_pressed);
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(PredioLibreLogsBR.this).
                        getScaledTouchSlop();
            }
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mItemPressed) {
                    // Multi-item swipes not handled
                    return false;
                }
                mItemPressed = true;
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_CANCEL:
            	v.setBackgroundResource(R.drawable.button_normal);
                v.setAlpha(1);
                v.setTranslationX(0);
                mItemPressed = false;
                break;
            case MotionEvent.ACTION_MOVE:
                {
                    float x = event.getX() + v.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true;
                            listViewHistorial.requestDisallowInterceptTouchEvent(true);
                            //mBackgroundContainer.showBackground(v.getTop(), v.getHeight());
                        }
                    }
                    if (mSwiping) {
                        v.setTranslationX((x - mDownX));
                        v.setAlpha(1 - deltaXAbs / v.getWidth());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            	v.setBackgroundResource(R.drawable.button_normal);
                {
                    // User let go - figure out whether to animate the view out, or back into place
                    if (mSwiping) {
                        float x = event.getX() + v.getTranslationX();
                        float deltaX = x - mDownX;
                        float deltaXAbs = Math.abs(deltaX);
                        float fractionCovered;
                        float endX;
                        float endAlpha;
                        final boolean remove;
                        if (deltaXAbs > v.getWidth() / 4) {
                            // Greater than a quarter of the width - animate it out
                            fractionCovered = deltaXAbs / v.getWidth();
                            endX = deltaX < 0 ? -v.getWidth() : v.getWidth();
                            endAlpha = 0;
                            remove = true;
                        } else {
                            // Not far enough - animate it back
                            fractionCovered = 1 - (deltaXAbs / v.getWidth());
                            endX = 0;
                            endAlpha = 1;
                            remove = false;
                        }
                        // Animate position and alpha of swiped item
                        // NOTE: This is a simplified version of swipe behavior, for the
                        // purposes of this demo about animation. A real version should use
                        // velocity (via the VelocityTracker class) to send the item off or
                        // back at an appropriate speed.
                        long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
                        listViewHistorial.setEnabled(false);
                        v.animate().setDuration(duration).
                                alpha(endAlpha).translationX(endX).
                                withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Restore animated values
                                        v.setAlpha(1);
                                        v.setTranslationX(0);
                                        if (remove) {
                                        	//if (AppLauncher.getHasLogAccess()){
                                        		animateRemoval(listViewHistorial, v);
                                        	//}
                                        } else {
                                            //mBackgroundContainer.hideBackground();
                                            mSwiping = false;
                                            listViewHistorial.setEnabled(true);
                                        }
                                    }
                                });
                    }else{
                    	//lvItemClick(v);
                    }
                }
                mItemPressed = false;
                break;
            default:
            	return false;
            }
            return true;
        }
    };
	
    private void animateRemoval(final ListView listview, View viewToRemove) {
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = mAdapter.getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        // Delete the item from the adapter
        int position = listViewHistorial.getPositionForView(viewToRemove);

        if (mAdapter.getItem(position).getGanado().getMangada().intValue() == 0){
        	ShowAlert.showAlert("Error", "No puede eliminar registros ya sincronizados", this);
        	return;
        }
        try {
        	PredioLibreServicio.deletePLBrucelosisLogLecturaTB(mAdapter.getItem(position).getGanado().getId());
			PredioLibreServicio.deletePLBrucelosisLog(mAdapter.getItem(position).getGanado().getId());
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
        mAdapter.remove(mAdapter.getItem(position));

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = mAdapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        //mBackgroundContainer.hideBackground();
                                        mSwiping = false;
                                        listViewHistorial.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    //mBackgroundContainer.hideBackground();
                                    mSwiping = false;
                                    listViewHistorial.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }

}
