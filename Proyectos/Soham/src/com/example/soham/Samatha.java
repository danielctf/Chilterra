package com.example.soham;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Samatha extends ActionBarActivity implements OnSeekBarChangeListener, OnCompletionListener{
	private MediaPlayer mp1;
	private MediaPlayer sel;
	private SeekBar sb;
	private Handler sh = new Handler();
	private ImageButton play;
	private ImageButton pause;
	private ImageButton stop;
	private boolean isSelected;
	private boolean fix;
	private TextView tv;
	
	protected void onCreate(Bundle savedInstanceState) {
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
        
        if(width==240 && height==320){
        	setContentView(R.layout.samatha240x320);
        }else{
        	if(width==320 && height==480){
        		setContentView(R.layout.samatha320x480);
        	}else{
        		if(width==480 && height==800){
        			setContentView(R.layout.samatha480x800);
        		}else{
        			if(width==480 && height==854){
        				setContentView(R.layout.samatha480x854);
        			}else{
        				if(width==720 && height==1280){
        					setContentView(R.layout.samatha720x1280);
        				}
        				if(width==720 && height >1100 && height<1280){
        					setContentView(R.layout.samatha720x1280);
        				}else{
        					if(width==800 && height==1280){
        						setContentView(R.layout.samatha800x1280);
        					}
        					if(width==800 && height>1100 && height<1280){
        						setContentView(R.layout.samatha800x1280);
        					}
        				}
        			}
        		}
        	}
        }
		AudioManager audioManager =(AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume (AudioManager.STREAM_MUSIC,10,0);
	
		sel = MediaPlayer.create(this, R.raw.air_event_runedrop_2);
		sel.setVolume(10,10);
		play = (ImageButton)findViewById(R.id.playButton);
		pause = (ImageButton)findViewById(R.id.pauseButton);
		stop = (ImageButton)findViewById(R.id.stopButton);
		pause.setVisibility(View.INVISIBLE);
		isSelected=false;
		fix=true;
		sb = (SeekBar)findViewById(R.id.seekBarRelajacion);
		sb.setEnabled(false);
		tv = (TextView)findViewById(R.id.seleccion);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.samatha, menu);
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
	
	public void minuto5(View v){
		if(sel.isPlaying()){
			sel.pause();
			sel.seekTo(0);
			sel.start();
		}else{
			sel.start();
		}
		if(isSelected && fix){
			stopSong(stop);
		}
		mp1 = MediaPlayer.create(this, R.raw.healingcircle);
		mp1.setOnCompletionListener(this);
		tv.setText("5 Mins");
		fix=false;
		enablePlay();
		playSong(v);
	}
	
	public void minuto15(View v){
		if(sel.isPlaying()){
			sel.pause();
			sel.seekTo(0);
			sel.start();
		}else{
			sel.start();
		}
		if(isSelected && fix){
			stopSong(stop);
		}
		mp1 = MediaPlayer.create(this, R.raw.lovingtouch);
		mp1.setOnCompletionListener(this);
		tv.setText("15 Mins");
		fix=false;
		enablePlay();
		playSong(v);
	}
	
	public void minuto30(View v){
		if(sel.isPlaying()){
			sel.pause();
			sel.seekTo(0);
			sel.start();
		}else{
			sel.start();
		}
		if(isSelected && fix){
			stopSong(stop);
		}
		mp1 = MediaPlayer.create(this, R.raw.aum);
		mp1.setOnCompletionListener(this);
		tv.setText("30 Mins");
		fix=false;
		enablePlay();
		playSong(v);
	}
	
	Runnable run = new Runnable(){
		public void run(){
			sb.setProgress(mp1.getCurrentPosition());
			sh.postDelayed(run, 1000);
		}
	};
	
	public void playSong(View v){
		if(isSelected){
			play.setVisibility(View.INVISIBLE);
			pause.setVisibility(View.VISIBLE);
			fix=true;
			mp1.start();
		}
	}
	
	public void pauseSong(View v){
		if(isSelected){
			pause.setVisibility(View.INVISIBLE);
			play.setVisibility(View.VISIBLE);
			mp1.pause();
		}
	}

	public void stopSong(View v){
		if(isSelected && fix){
			mp1.pause();
			mp1.seekTo(0);
			pause.setVisibility(View.INVISIBLE);
			play.setVisibility(View.VISIBLE);
			fix=false;
		}
	}
	
	public void enablePlay(){
		sb.setEnabled(true);
		sb.setOnSeekBarChangeListener(this);
		sb.setMax(mp1.getDuration());
		sb.setProgress(mp1.getCurrentPosition());
		sh.postDelayed(run, 1000);
		isSelected=true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if(fromUser && isSelected){
			mp1.seekTo(progress);
			seekBar.setProgress(progress);
		}
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	public void onBackPressed(){
		if(mp1 != null){
			if(mp1.isPlaying()){
				mp1.stop();
			}
		}
		super.onBackPressed();
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		stopSong(stop);
	}
	
}
