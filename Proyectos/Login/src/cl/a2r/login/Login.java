package cl.a2r.login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import cl.a2r.bolos.R;
import cl.a2r.common.AppException;
import cl.a2r.common.wsservice.WSAutorizacionCliente;
import java.util.List;

import com.example.bt.MainActivity;

public class Login extends Activity implements Button.OnClickListener {
    private EditText etUsuario;
    private EditText etPassword;
    private Button btLogin;
    
    // Autorizacion
    public static final Integer idApp = 1;
    public static String token;
    public static Integer idUsuario;
    public static String usuario;
    
    private static String nextActivity = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
		    // Here activity is brought to front, not created,
		    // so finishing this will get you to the last viewed activity
			finish();
			return;
		}
        setContentView(R.layout.login);
        etUsuario = (EditText) findViewById(R.id.usuario);
        etPassword = (EditText) findViewById(R.id.password);
        btLogin = (Button) findViewById(R.id.btlogin);

        etUsuario.setText(traeCuentaChilterra());
        btLogin.setOnClickListener(this);

        SharedPreferences sp = this.getPreferences(Login.MODE_PRIVATE);
        token = sp.getString("token", "");
        if (token != null && !token.equals("")) {
            try {
                List list = WSAutorizacionCliente.validaToken(Login.idApp, token);
                token = (String) list.get(0);
                idUsuario = (Integer) list.get(1);
                usuario = (String) list.get(2);
                Intent i = new Intent(this, Class.forName("com.example.bt.MainActivity"));
        		i.putExtra("ActivityName", nextActivity);
                startActivity(i);
                this.finish();
//            } catch (AppException ex) {
//            } catch (ClassNotFoundException ex) {
            } catch (Exception ex) {
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("token");
                editor.commit();
             }
        }
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			nextActivity = extras.getString("ActivityName");
		}
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private String traeCuentaChilterra() {
        String ret = "";

        AccountManager accManager = AccountManager.get(this);
        Account acc[] = accManager.getAccountsByType("com.google");
        
        for(Account cc : acc){
            if (cc.name.toLowerCase().contains("chilterra")) {
                ret = cc.name;
                break;
            }

        }

        return ret;
    }

    public void onClick(View view) {
        
        try {
            List list = WSAutorizacionCliente.autentica(Login.idApp, etUsuario.getText().toString(), etPassword.getText().toString());
            token = (String) list.get(0);
            idUsuario = (Integer) list.get(1);
            usuario = (String) list.get(2);

            SharedPreferences sp = this.getPreferences(Login.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("token", token);
            editor.commit();

            Intent i = new Intent(this, Class.forName("com.example.bt.MainActivity"));
    		i.putExtra("ActivityName", nextActivity);
            startActivity(i);
            this.finish();
        } catch (AppException ex) {
            AlertView.showAlert("Error", ex.getMessage(), this);
        } catch (ClassNotFoundException ex) {
        }
        
    }

}
