package com.example.registroleche;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class Test extends ActionBarActivity {

	ImageView iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		iv = (ImageView)findViewById(R.id.imageView1);
		
		tryw();
	}

	private void tryw(){
		Login.connectSQL();
		try {
			Statement stm = Login.conn.createStatement();
			ResultSet res = stm.executeQuery("SELECT img FROM dbo2.test");
			String temp = null;
			if (res.next()){
				temp = res.getString(1);
				byte[] decodedString = Base64.decode(temp,Base64.NO_WRAP);
				InputStream inputStream  = new ByteArrayInputStream(decodedString);
				Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
				iv.setImageBitmap(bitmap);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
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
}
