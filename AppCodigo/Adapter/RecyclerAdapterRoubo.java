package td2final.engecomp.td2final.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import td2final.engecomp.td2final.Model.EnderecoRoubo;
import td2final.engecomp.td2final.R;
import td2final.engecomp.td2final.ViewHolders.ViewHolderRoubo;

/**
 * Created by Fabio on 23/01/2018.
 */

public class RecyclerAdapterRoubo extends RecyclerView.Adapter<ViewHolderRoubo>
{
    private ArrayList<EnderecoRoubo> listaRoubos = new ArrayList<>();
    private Context context;

    public RecyclerAdapterRoubo(ArrayList<EnderecoRoubo> listaRoubos, Context context)
    {
        this.context = context;
        this.listaRoubos = listaRoubos;

        /*ParseQuery<ParseObject> filtro = ParseQuery.getQuery("Roubos");

        filtro.whereEqualTo("DataRoubo","22/01/2018");
        filtro.setLimit(1);

        filtro.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects)
                    {

                        //Log.i("ListarDados", "Objetos - Nome: " + object.get("nome") + " pontos: " + object.get("pontos"));
                    }

                } else {
                    Log.i("ListarDados", "Erro ao consultar Objeto" + e.getMessage());
                }

            }
        });*/
    }

    @Override
    public ViewHolderRoubo onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_roubos, parent, false);
        return new ViewHolderRoubo(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderRoubo holder, int position)
    {
        EnderecoRoubo enderecoRoubo = new EnderecoRoubo();
        enderecoRoubo = listaRoubos.get(position);
        holder.textNomeRuaRoubo.setText(enderecoRoubo.getNomeRua() + " Nº" + enderecoRoubo.getNumeroRua());
        holder.textBairroRoubo.setText(enderecoRoubo.getBairroRoubo());
        holder.textSolicitanteRoubo.setText(enderecoRoubo.getSolicitanteRoubo());
        holder.textDataHoraRoubo.setText(enderecoRoubo.getDataRoubo() + " - " + enderecoRoubo.getHoraRoubo());
        holder.textComercioRoubo.setText(enderecoRoubo.getComercioRoubo());
    }

    @Override
    public int getItemCount()
    {
        return listaRoubos.size();
    }
}
