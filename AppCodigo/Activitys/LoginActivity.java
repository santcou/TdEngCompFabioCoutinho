package td2final.engecomp.td2final.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import td2final.engecomp.td2final.R;
import td2final.engecomp.td2final.Utilidades.CreateProgressDialog;
import td2final.engecomp.td2final.Utilidades.MaskEditTextChangedListener;
import td2final.engecomp.td2final.Utilidades.Mqtt;
import td2final.engecomp.td2final.Utilidades.Preferencias;

public class LoginActivity extends AppCompatActivity
{
    private EditText editNumeroPmLogin;
    private EditText editSenhaLogin;
    private TextView textCadastrese;
    private Button btnLogar;
    private Preferencias preferencias;
    private CreateProgressDialog dialogLogin;
    private Mqtt mqtt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editNumeroPmLogin = (EditText) findViewById(R.id.numeroPmLoginId);
        editSenhaLogin = (EditText) findViewById(R.id.senhaLoginId);
        btnLogar = (Button) findViewById(R.id.btnLoginId);
        textCadastrese = (TextView) findViewById(R.id.textCadastreseId);

        MaskEditTextChangedListener maskNumeroPM = new MaskEditTextChangedListener("#######", editNumeroPmLogin);
        editNumeroPmLogin.addTextChangedListener(maskNumeroPM);

        preferencias = new Preferencias(LoginActivity.this);

        mqtt = new Mqtt(LoginActivity.this);

        if (preferencias.confereUsuarioLogado())
        {
            mqtt.connect();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        textCadastrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogLogin = new CreateProgressDialog("Logando ...",false,LoginActivity.this);
                ParseUser.logInInBackground(editNumeroPmLogin.getText().toString(),editSenhaLogin.getText().toString() , new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e)
                    {
                        if(e == null)
                        {
                            Toast.makeText(LoginActivity.this, "Logado com Sucesso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            preferencias.salvarLoginPreferencias(editNumeroPmLogin.getText().toString(),editSenhaLogin.getText().toString());
                            dialogLogin.getProgressDialog().dismiss();
                            mqtt.connect();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Erro ao Logar " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialogLogin.getProgressDialog().dismiss();
                        }
                    }
                });
            }
        });

    }
}
