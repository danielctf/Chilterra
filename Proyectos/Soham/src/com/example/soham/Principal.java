package com.example.soham;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Principal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        
        
        if(width==320 && height==480){
        	setContentView(R.layout.principal320x480);
        }else{
        	if(width==480 && height==800){
        		setContentView(R.layout.principal480x800);
        	}else{
        		if(width==720 && height==1280){
        			setContentView(R.layout.principal720x1280);
        			Log.v("width: "+width, "height: "+height);
        		}
        		if(width==720 && height>1100 && height<1280){
        			setContentView(R.layout.principal720x1280);
        			Log.v("width: "+width, "height: "+height);
        		}else{
        			if(width==480 && height==854){
        				setContentView(R.layout.principal480x854);
        			}else{
        					if(width==240 && height==320){
        						setContentView(R.layout.principal240x320);
        					}else{
        						if(width==800 && height==1280){
        							setContentView(R.layout.principal800x1280);
        							Log.v("width: "+width, "height: "+height);
        						}
        						if(width==800 && height>1100 && height<1280){
        							setContentView(R.layout.principal800x1280);
        							Log.v("width: "+width, "height: "+height);
        						}else{
        							Log.v("ERROR width: "+width, "height: "+height);
        						}
        					}
        				}
        			}
        		}
        	}
        }
	
	public void goRelajacion(View v){
		Intent i = new Intent(Principal.this, Relajacion.class);
		startActivity(i);
	}
	
	public void goSoham(View v){
		Intent i = new Intent(Principal.this, Soham.class);
		startActivity(i);
	}
	
	public void goSamatha(View v){
		Intent i = new Intent(Principal.this, Samatha.class);
		startActivity(i);
	}
	
	public void goAprender(View v){
		Intent i = new Intent(Principal.this, Aprender.class);
		startActivity(i);
	}
	
	public void noDisponible(View v){
		Dialog dialog = new Dialog(this){		
			public boolean dispatchTouchEvent(MotionEvent event){
		        dismiss();
		        return false;
		    }
		};
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.nodisponible);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.show();
	}
	
	public void onBackPressed(){
		super.onBackPressed();
	}
	
	public void goAbout(View v){
		Dialog dialog = new Dialog(this){		
			public boolean dispatchTouchEvent(MotionEvent event){
		        dismiss();
		        return false;
		    }
		};
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.aboutdialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.show();
	}
}
