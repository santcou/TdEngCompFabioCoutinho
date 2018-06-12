package td2final.engecomp.td2final.Utilidades;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Fabio on 15/01/2018.
 */

public class CreateProgressDialog
{
    private String mensagem;
    private boolean status;
    private Context context;
    private ProgressDialog progressDialog;

    public CreateProgressDialog(String mensagem, boolean status, Context context)
    {
        this.context = context;
        this.mensagem = mensagem;
        this.status = status;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(mensagem);
        progressDialog.setCancelable(status);
        progressDialog.setCanceledOnTouchOutside(status);
        progressDialog.show();
    }

    public ProgressDialog getProgressDialog()
    {
        return progressDialog;
    }

}
