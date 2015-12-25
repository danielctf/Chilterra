package cl.a2r.animales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.a2r.custom.AppLauncher;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.LogsArrayAdapter;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.sip.model.GanadoLogs;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Logs extends Activity implements View.OnClickListener{

    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;
	
	private ImageButton goBack, undo;
	private Button confirmarCambios;
	private TextView deshacer, app;
	private ListView listViewHistorial;
	private List<String> diios, deletedLogId;
	public static int sr_ganado, diio;
	private List<GanadoLogs> list;
	
    private LogsArrayAdapter mAdapter;
    private boolean mSwiping = false;
    private boolean mItemPressed = false;
    @SuppressLint("UseSparseArrays") private HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_logs);
		
		cargarInterfaz();
		getLogsWS();
	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		listViewHistorial = (ListView)findViewById(R.id.listViewHistorial);
		//listViewHistorial.setOnClickListener(this);
		confirmarCambios = (Button)findViewById(R.id.confirmarCambios);
		confirmarCambios.setOnClickListener(this);
		confirmarCambios.setVisibility(View.INVISIBLE);
		undo = (ImageButton)findViewById(R.id.undo);
		undo.setOnClickListener(this);
		deshacer = (TextView)findViewById(R.id.deshacer);
		deshacer.setOnClickListener(this);
		app = (TextView)findViewById(R.id.app);
		diios = new ArrayList<String>();
		deletedLogId = new ArrayList<String>();
		
	}
	
	@SuppressWarnings("unchecked")
	private void getLogsWS(){
		list = AppLauncher.getLogs();
		if (list == null){
			return;
		}
		diios.clear();
		deletedLogId.clear();
		for (GanadoLogs g : list){
			if (Calculadora.isSalvataje){
				diios.add(g.getEid());
			} else {
				diios.add(Integer.toString(g.getDiio()));
			}
		}
		mAdapter = new LogsArrayAdapter(this, android.R.layout.simple_list_item_1, diios, mTouchListener);
		listViewHistorial.setAdapter(mAdapter);
		
	}
	
	private void lvItemClick(View v){
		if (isOnline() == false){
			return;
		}
		//OJO: View v significa la linea del respectivo ListView!!
		//Solo deja pasar a editar 1 diio si no esta borrando otros
		
		/*
		if (deletedGanadoId.size() == 0){
			int position = listViewHistorial.getPositionForView(v);
			setSrGanado(diios.get(position));
			Intent i = new Intent(this, AppLauncher.getEditableAppClass());
			startActivity(i);
		}
		*/
	}
	
    Handler hand = new Handler();
    Runnable run = new Runnable() { 
        public void run() { 
			AppLauncher.deleteLogs(deletedLogId);
			Toast.makeText(getApplicationContext(), "Registros eliminados exitosamente", Toast.LENGTH_LONG).show();
			finish();
        }
    }; 
	
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.confirmarCambios:
			confirmarCambios.setVisibility(View.INVISIBLE);
			hand.postDelayed(run, 100);
			break;
		case R.id.undo:
			confirmarCambios.setVisibility(View.INVISIBLE);
			updateStatus();
			getLogsWS();
			break;
		case R.id.deshacer:
			confirmarCambios.setVisibility(View.INVISIBLE);
			updateStatus();
			getLogsWS();
			break;
		}
	}
	
	private void updateStatus(){
		if (confirmarCambios.isShown()){
			undo.setVisibility(View.VISIBLE);
			deshacer.setVisibility(View.VISIBLE);
			goBack.setVisibility(View.INVISIBLE);
			app.setVisibility(View.INVISIBLE);
			confirmarCambios.setText(Integer.toString(deletedLogId.size()));
		}else{
			undo.setVisibility(View.INVISIBLE);
			deshacer.setVisibility(View.INVISIBLE);
			goBack.setVisibility(View.VISIBLE);
			confirmarCambios.setVisibility(View.INVISIBLE);
			app.setVisibility(View.VISIBLE);
		}
	}
	
	protected  void onStart(){
		super.onStart();
		
		if (Login.user == 0){
			finish();
		}
		
		ConnectThread.setHandler(mHandler);
		
		if (isOnline() == false){
			return;
		}
		
		updateStatus();
	}
	
	public void onBackPressed(){
		if (isOnline()){
			finish();
		}
	}
	
	private boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if ((netInfo != null && netInfo.isConnectedOrConnecting()) == false){
	    	ShowAlert.showAlert("Error", "No hay conexión a Internet", this);
	    }
	    return netInfo != null && netInfo.isConnectedOrConnecting();
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
                mSwipeSlop = ViewConfiguration.get(Logs.this).
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
                                        	if (AppLauncher.getHasLogAccess()){
                                        		animateRemoval(listViewHistorial, v);
                                        	}
                                        } else {
                                            //mBackgroundContainer.hideBackground();
                                            mSwiping = false;
                                            listViewHistorial.setEnabled(true);
                                        }
                                    }
                                });
                    }else{
                    	lvItemClick(v);
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
        for (int i = 0; i < list.size(); i++){
        	if (Calculadora.isSalvataje){
        		if (list.get(i).getEid().equals(mAdapter.getItem(position))){
        			deletedLogId.add(list.get(i).getEid());
        		}
        	} else {
	        	if (Integer.toString(list.get(i).getDiio()).equals(mAdapter.getItem(position))){
	        		deletedLogId.add(Integer.toString(list.get(i).getLogId()));
	        	}
        	}
        }
        mAdapter.remove(mAdapter.getItem(position));
        confirmarCambios.setVisibility(View.VISIBLE);
        updateStatus();

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

	//---------------------------------------------------------------------
	//-------------------FIN ANIMACION LISTVIEW HISTORIAL------------------
	//---------------------------------------------------------------------
    
	//---------------------------------------------------------------------------
	//------------------------DATOS ENVIADOS DESDE BASTÓN------------------------
	//---------------------------------------------------------------------------
	
    private Handler mHandler = new Handler(){
    	@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		switch(msg.what){
    		case ConnectThread.SUCCESS_CONNECT:
    			BluetoothSocket mmSocket = (BluetoothSocket) ((List<Object>) msg.obj).get(0);
    			BluetoothDevice mmDevice = (BluetoothDevice) ((List<Object>) msg.obj).get(1);
    	        ConnectedThread connectedThread = new ConnectedThread(mmSocket, mmDevice);
    	        connectedThread.start();
    			break;
    		case ConnectedThread.MESSAGE_READ:
    			String EID = (String) msg.obj;
    			System.out.println(EID);
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Logs.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
	
}
