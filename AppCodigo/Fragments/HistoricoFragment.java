package td2final.engecomp.td2final.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import td2final.engecomp.td2final.Adapter.RecyclerAdapterRoubo;
import td2final.engecomp.td2final.Model.EnderecoRoubo;
import td2final.engecomp.td2final.R;
import td2final.engecomp.td2final.Utilidades.CreateProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricoFragment extends Fragment {
    private RecyclerView recyclerRoubos;
    private RecyclerAdapterRoubo adapterRoubo;
    private ArrayList<EnderecoRoubo> listaRoubos;
    private EnderecoRoubo enderecoRoubo;
    private CreateProgressDialog dialogRoubosHoje;
    private java.util.Date d = new Date();
    private String data;
    private TextView textoNaoHaRoubo;
    private SimpleDateFormat dataAtual = new SimpleDateFormat("dd/MM/yyyy");



    public HistoricoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historico, container, false);
        listaRoubos = new ArrayList<>();

        //dialogRoubosHoje = new CreateProgressDialog("Carregando roubos de Hoje...",false,getActivity());

        recyclerRoubos = (RecyclerView) view.findViewById(R.id.recyclerRoudoId);
        recyclerRoubos.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterRoubo = new RecyclerAdapterRoubo(listaRoubos, getActivity());
        recyclerRoubos.setAdapter(adapterRoubo);

        textoNaoHaRoubo = (TextView) view.findViewById(R.id.textNaoHaRoubosId);

        data = java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(d);

        textoNaoHaRoubo.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String dataSistema = dataAtual.format(new Date());        //Toast.makeText(getActivity(), dataFormatada, Toast.LENGTH_SHORT).show();
        ParseQuery<ParseObject> filtro = ParseQuery.getQuery("Roubos");
        filtro.whereEqualTo("DataRoubo", dataSistema);
        //filtro.setLimit(1);

        //listar os dados
        filtro.findInBackground(new FindCallback<ParseObject>()
        {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        Log.i("AAAAAA", "Objetos - Nome: " + object.get("Solicitante") + " Rua: " + object.get("Rua"));
                        //Toast.makeText(getActivity(), "SUCESS", Toast.LENGTH_SHORT).show();
                        enderecoRoubo = new EnderecoRoubo();
                        enderecoRoubo.setNomeRua(object.get("Rua").toString());
                        enderecoRoubo.setNumeroRua(object.get("Numero").toString());
                        enderecoRoubo.setDataRoubo(object.get("DataRoubo").toString());
                        enderecoRoubo.setHoraRoubo(object.get("HoraRoubo").toString());
                        enderecoRoubo.setSolicitanteRoubo(object.get("Solicitante").toString());
                        enderecoRoubo.setBairroRoubo(object.get("Bairro").toString());
                        enderecoRoubo.setComercioRoubo(object.get("Estabelecimento").toString());
                        listaRoubos.add(enderecoRoubo);
                    }

                    if (listaRoubos.isEmpty())
                    {
                        textoNaoHaRoubo.setVisibility(View.VISIBLE);
                        return;
                    }
                    adapterRoubo.notifyDataSetChanged();
                    //dialogRoubosHoje.getProgressDialog().dismiss();

                } else {
                    Log.i("AAAAAA", "Erro ao consultar Objeto" + e.getMessage());
                    Toast.makeText(getActivity(), "ERRO", Toast.LENGTH_SHORT).show();
                    //dialogRoubosHoje.getProgressDialog().dismiss();
                }
            }
        });
        //dialogRoubosHoje.getProgressDialog().dismiss();
        listaRoubos.clear();
    }

    private String formataData(String data)
    {
        String aux[] = new String[5];
        String mesReferente = "";
        aux = data.split(" ");

        switch (aux[2])
        {
            case "jan":
            {
                mesReferente = "01";
            }break;

            case "fev":
            {
                mesReferente = "02";
            }break;

            case "mar":
            {
                mesReferente = "03";
            }break;

            case "abr":
            {
                mesReferente = "04";
            }break;

            case "mai":
            {
                mesReferente = "06";
            }break;

            case "jun":
            {
                mesReferente = "06";
            }break;

            case "jul":
            {
                mesReferente = "07";
            }break;

            case "ago":
            {
                mesReferente = "08";
            }break;

            case "set":
            {
                mesReferente = "09";
            }break;

            case "out":
            {
                mesReferente = "10";
            }break;

            case "nov":
            {
                mesReferente = "11";
            }break;

            case "dez":
            {
                mesReferente = "12";
            }break;
        }
        return aux[0] + "/" + mesReferente + "/" + aux[4];
    }
}
