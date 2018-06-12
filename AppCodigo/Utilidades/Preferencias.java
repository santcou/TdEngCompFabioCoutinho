package td2final.engecomp.td2final.Utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Fabio on 23/01/2018.
 */

public class Preferencias
{
    private static final String AQUIVO_PREFERENCIA = "ArquivoPreferencia";
    private SharedPreferences preferences;
    private Context contexto;
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    public Preferencias(Context contextoParametro)
    {
        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(AQUIVO_PREFERENCIA,MODE);
        editor = preferences.edit();
    }

    public void salvarLoginPreferencias(String emailPreferencias, String senhaPreferencias)
    {
        editor.putString("username",emailPreferencias);
        editor.putString("senha",senhaPreferencias);
        editor.commit();
    }

    public String getUsernamePreferencias()
    {
        String numeroPm = "";
        if(preferences.contains("username")) {
            numeroPm = preferences.getString("username", "username não definido");
        }
        return numeroPm;
    }

    public boolean confereUsuarioLogado()
    {
        if(preferences.contains("username") && preferences.contains("senha"))
        {
            String emailUsuario = preferences.getString("username","username não definido");
            String senhaUsuario = preferences.getString("senha","senha não definida");
            //Toast.makeText(contexto,emailUsuario + " " +senhaUsuario, Toast.LENGTH_SHORT).show();

            if(emailUsuario.equals("username não definido") || senhaUsuario.equals("senha não definida"))
                return false;
            else
                return true;
        }
        else
        {
            Toast.makeText(contexto, "Erro nas Preferencias", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void limparPreferencias()
    {
        editor.putString("username","email não definido");
        editor.putString("senha","senha não definida");
        editor.commit();
    }
}
