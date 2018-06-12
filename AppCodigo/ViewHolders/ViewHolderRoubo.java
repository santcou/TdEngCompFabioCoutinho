package td2final.engecomp.td2final.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import td2final.engecomp.td2final.R;

/**
 * Created by Fabio on 23/01/2018.
 */

public class ViewHolderRoubo extends RecyclerView.ViewHolder
{
    public TextView textNomeRuaRoubo;
    public TextView textBairroRoubo;
    public TextView textDataHoraRoubo;
    public TextView textSolicitanteRoubo;
    public TextView textComercioRoubo;

    public ViewHolderRoubo(View itemView)
    {
        super(itemView);
        this.textNomeRuaRoubo = (TextView) itemView.findViewById(R.id.textRuaCardId);
        this.textBairroRoubo = (TextView) itemView.findViewById(R.id.textBairroCardId);
        this.textDataHoraRoubo = (TextView) itemView.findViewById(R.id.textDataHoraCardId);
        this.textSolicitanteRoubo = (TextView) itemView.findViewById(R.id.textSolicitanteCardId);
        this.textComercioRoubo = (TextView) itemView.findViewById(R.id.textComercioCardId);
    }
}
