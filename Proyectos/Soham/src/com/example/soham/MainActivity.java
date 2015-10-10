package com.example.soham;

import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity implements OnTouchListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        
        boolean isResolution=false;
        
        if(width==240 && height==320){
        	isResolution=true;
        }else{
        	if(width==320 && height==480){
            	isResolution=true;
        	}else{
        		if(width==480 && height==800){
                	isResolution=true;
        		}else{
        			if(width==480 && height==854){
        	        	isResolution=true;
        			}else{
        				if(width==720 && height==1280){
        		        	isResolution=true;
        				}
        				if(width==720 && height >1100 && height<1280){
        		        	isResolution=true;
        				}else{
        					if(width==800 && height==1280){
        			        	isResolution=true;
        					}
        					if(width==800 && height>1100 && height<1280){
        			        	isResolution=true;
        					}
        				}
        			}
        		}
        	}
        }
        
        if(isResolution==false){
    		Dialog dialog = new Dialog(this){		
    			public boolean dispatchTouchEvent(MotionEvent event){
    		        dismiss();
    		        System.exit(0);
    		        return false;
    		    }
    		};
    		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    		dialog.setContentView(R.layout.screensizeerror);
    		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    		dialog.show();
        }
        
        setContentView(R.layout.activity_main);
        RelativeLayout layout= (RelativeLayout)findViewById(R.id.relativelay1);
        layout.setOnTouchListener(this);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Intent i = new Intent(MainActivity.this, Principal.class);
		startActivity(i);
		return true;
	}
}
