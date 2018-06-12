package td2final.engecomp.td2final.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import td2final.engecomp.td2final.Model.Dispositivo;
import td2final.engecomp.td2final.R;
import td2final.engecomp.td2final.Utilidades.CreateProgressDialog;
import td2final.engecomp.td2final.Utilidades.MaskEditTextChangedListener;
import td2final.engecomp.td2final.Utilidades.Mqtt;
import td2final.engecomp.td2final.Utilidades.Preferencias;

public class CadastraDispositivoActivity extends AppCompatActivity
{
    private android.support.v7.widget.Toolbar toolbar;
    private Preferencias preferencias;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    startActivity(new Intent(CadastraDispositivoActivity.this,PesquisaActivity.class));
                    finish();
                    return true;
                case R.id.navigation_navigation:
                    //startActivity(new Intent(CadastraDispositivoActivity.this,MainActivity.class));
                    finish();
                    return true;
                case R.id.navigation_add_location:
                    return true;
            }
            return false;
        }
    };
    private Button btnCadastrarDispositivo;
    private EditText editRuaDispositivo;
    private EditText editBairroDispositivo;
    private EditText editCidadeDispositivo;
    private EditText editNumeroDispositivo;
    private EditText editTelefoneDispositivo;
    private EditText editResponsavelDispositivo;
    private EditText editComercioDispositivo;
    private AlertDialog.Builder alertDialog;
    private Dispositivo dispositivo;
    private boolean flagComercio = false;
    private CreateProgressDialog dialogGravandoDispositivo;
    private Mqtt mqtt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_dispositivo);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_add_location);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
        navigation.setItemTextColor(ColorStateList.valueOf(Color.WHITE));

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TD2 - Final");
        setSupportActionBar(toolbar);

        editRuaDispositivo = (EditText) findViewById(R.id.editRuaDispositivoId);
        editNumeroDispositivo = (EditText) findViewById(R.id.editNumeroDispositivoId);
        editCidadeDispositivo = (EditText) findViewById(R.id.editCidadeDispositivoId);
        editBairroDispositivo = (EditText) findViewById(R.id.editBairroDispositivoId);
        editTelefoneDispositivo = (EditText) findViewById(R.id.editTelefoneDispositivoId);
        editResponsavelDispositivo = (EditText) findViewById(R.id.editResponsavelDispositivoId);
        editComercioDispositivo = (EditText) findViewById(R.id.editComercioDispositivoId);
        btnCadastrarDispositivo = (Button) findViewById(R.id.btnCadastrarDispositivoId);

        MaskEditTextChangedListener maskTelefone = new MaskEditTextChangedListener("(##) #########", editTelefoneDispositivo);
        editTelefoneDispositivo.addTextChangedListener(maskTelefone);

        alertDialog = new AlertDialog.Builder(CadastraDispositivoActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Tipo de Dispositivo.");
        alertDialog.setMessage("Este dipositivo será instalado em um comércio?");

        alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                editComercioDispositivo.setVisibility(View.VISIBLE);
                flagComercio = true;
                return;
            }
        });

        alertDialog.setNegativeButton("NÂO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                editComercioDispositivo.setText("NÃO É COMÉRCIO");
                editComercioDispositivo.setVisibility(View.INVISIBLE);
                flagComercio = false;
                return;
            }
        });
        alertDialog.create();
        alertDialog.show();

        dispositivo = new Dispositivo();
        preferencias = new Preferencias(CadastraDispositivoActivity.this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        btnCadastrarDispositivo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!checaCamposVazios())
                {
                    Toast.makeText(CadastraDispositivoActivity.this, "Todos os campos são de preenchimento obrigatório!!!"
                            , Toast.LENGTH_SHORT).show();
                    return;
                }

                /*if (flagComercio) {
                    dispositivo.setNomeComercioDispositivo(editComercioDispositivo.getText().toString().toUpperCase());
                }*/

                dispositivo.setNomeComercioDispositivo(editComercioDispositivo.getText().toString().toUpperCase());
                dispositivo.setBairroDispositivo(editBairroDispositivo.getText().toString().toUpperCase());
                dispositivo.setCidadeDispositivo(editCidadeDispositivo.getText().toString().toUpperCase());
                dispositivo.setResponsavelDispositivo(editResponsavelDispositivo.getText().toString().toUpperCase());
                dispositivo.setTelefoneDispositivo(editTelefoneDispositivo.getText().toString().toUpperCase());
                dispositivo.setRuaDispositivo(editRuaDispositivo.getText().toString().toUpperCase());
                dispositivo.setNumeroDispositivo(editNumeroDispositivo.getText().toString().toUpperCase());
                dispositivo.setPmResponsavelDispositivo(preferencias.getUsernamePreferencias().toString().toUpperCase());

                gravaDispositivoServidor(dispositivo);
            }
        });
    }

    private boolean checaCamposVazios()
    {
        if (editBairroDispositivo.getText().toString().isEmpty() || editCidadeDispositivo.getText().toString().isEmpty() ||
                editNumeroDispositivo.getText().toString().isEmpty() ||editRuaDispositivo.getText().toString().isEmpty() ||
                editTelefoneDispositivo.getText().toString().isEmpty() ||editComercioDispositivo.getText().toString().isEmpty() ||
                editResponsavelDispositivo.getText().toString().isEmpty())
        {
            return false;
        }
        return true;
    }

    private void gravaDispositivoServidor(Dispositivo dispositivo)
    {
        dialogGravandoDispositivo = new CreateProgressDialog("Gravando dispositivo...",
                false,CadastraDispositivoActivity.this);

        ParseObject dispositivoParse =  new ParseObject("Dispositivos");
        dispositivoParse.put("Rua", dispositivo.getRuaDispositivo());
        dispositivoParse.put("Numero",dispositivo.getNumeroDispositivo());
        dispositivoParse.put("Bairro",dispositivo.getBairroDispositivo());
        dispositivoParse.put("Cidade",dispositivo.getCidadeDispositivo());
        dispositivoParse.put("Responsavel",dispositivo.getResponsavelDispositivo());
        dispositivoParse.put("Telefone",dispositivo.getTelefoneDispositivo());
        dispositivoParse.put("PmResponsavel",dispositivo.getPmResponsavelDispositivo());
        dispositivoParse.put("NomeComercio",dispositivo.getNomeComercioDispositivo());

        dispositivoParse.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                {
                    Toast.makeText(CadastraDispositivoActivity.this,
                            "Dispositivo Salvo com Sucesso!", Toast.LENGTH_SHORT).show();
                    dialogGravandoDispositivo.getProgressDialog().dismiss();
                }else
                {
                    Log.i("SalvarPontos", "Erro ao salvar Dados " + e.getMessage());
                    Toast.makeText(CadastraDispositivoActivity.this,
                            "Falha ao salvar dispositivo " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialogGravandoDispositivo.getProgressDialog().dismiss();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        //threadWatchDogTimer.paraWatchDogTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pesquisa,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item_voltar:
            {
                finish();
                return true;
            }

            case R.id.item_atualizar:
            {
                startActivity(new Intent(CadastraDispositivoActivity.this, CadastraDispositivoActivity.class));
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
