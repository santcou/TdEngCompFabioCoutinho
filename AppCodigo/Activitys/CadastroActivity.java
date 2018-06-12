package td2final.engecomp.td2final.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import td2final.engecomp.td2final.Model.Usuario;
import td2final.engecomp.td2final.R;
import td2final.engecomp.td2final.Utilidades.CreateProgressDialog;
import td2final.engecomp.td2final.Utilidades.MaskEditTextChangedListener;
import td2final.engecomp.td2final.Utilidades.Mqtt;
import td2final.engecomp.td2final.Utilidades.Preferencias;

public class CadastroActivity extends AppCompatActivity
{
    private ParseUser usuarioParser;
    private Usuario usuario;
    private Button btnCadastrar;
    private EditText editNome;
    private EditText editSenhaConfirmacao;
    private EditText editNumeroPM;
    private EditText editSenha;
    private EditText editEmail;
    private Preferencias preferencias;
    private CreateProgressDialog dialogCadastro;
    private Mqtt mqtt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        btnCadastrar = (Button) findViewById(R.id.btnCadastroId);
        editNome = (EditText) findViewById(R.id.nomeCadastroId);
        editSenhaConfirmacao = (EditText) findViewById(R.id.senhaConfirmacaoCadastroId);
        editNumeroPM = (EditText) findViewById(R.id.numeroPmCadastroId);
        editSenha = (EditText) findViewById(R.id.senhaCadastroId);
        editEmail = (EditText) findViewById(R.id.emailCadastroId);

        MaskEditTextChangedListener maskNumeroPM = new MaskEditTextChangedListener("#######", editNumeroPM);
        editNumeroPM.addTextChangedListener(maskNumeroPM);

        preferencias = new Preferencias(CadastroActivity.this);
        mqtt = new Mqtt(CadastroActivity.this);

    }

    @Override
    protected void onResume()
    {

        super.onResume();
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!checaCamposVazios())
                {
                    Toast.makeText(CadastroActivity.this, "O preenchimento de todos os campos é " +
                            "obrigatório !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!confereSenhas())
                {
                    Toast.makeText(CadastroActivity.this, "As senhas diigitadas são diferentes !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialogCadastro = new CreateProgressDialog("Cadastrando Usuário ...",false,CadastroActivity.this);

                usuario = new Usuario(editNome.getText().toString(), editSenhaConfirmacao.getText().toString(), editSenha.getText().toString()
                        , editEmail.getText().toString(),editNumeroPM.getText().toString());

                usuarioParser = new ParseUser();
                usuarioParser.setUsername(editNumeroPM.getText().toString());
                usuarioParser.setPassword(editSenha.getText().toString());
                usuarioParser.setEmail(editEmail.getText().toString());

                usuarioParser.signUpInBackground(new SignUpCallback()
                {
                    @Override
                    public void done(ParseException e)
                    {
                        if(e == null)
                        {
                            Toast.makeText(CadastroActivity.this, "Usuario Cadastrado com Sucesso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CadastroActivity.this,MainActivity.class));
                            preferencias.salvarLoginPreferencias(usuario.getUsuarioNumeroPM(),usuario.getUsuarioSenha());
                            dialogCadastro.getProgressDialog().dismiss();
                            mqtt.connect();
                        }
                        else
                        {
                            Toast.makeText(CadastroActivity.this, "Erro ao criar Usuario " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialogCadastro.getProgressDialog().dismiss();
                        }
                    }
                });
            }
        });
    }

    public boolean checaCamposVazios()
    {
        if(editSenhaConfirmacao.getText().toString().isEmpty() || editSenhaConfirmacao.getText().toString().isEmpty() ||
        editSenhaConfirmacao.getText().toString().isEmpty() || editSenhaConfirmacao.getText().toString().isEmpty()||
        editSenhaConfirmacao.getText().toString().isEmpty())
        {
            return false;
        }
        return true;
    }

    public boolean confereSenhas()
    {
        if (editSenha.getText().toString().equals(editSenhaConfirmacao.getText().toString()))
            return true;
        return false;
    }
}
