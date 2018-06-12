package td2final.engecomp.td2final.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import td2final.engecomp.td2final.Activitys.PesquisaActivity;
import td2final.engecomp.td2final.Adapter.RecyclerAdapterRoubo;
import td2final.engecomp.td2final.Model.EnderecoRoubo;
import td2final.engecomp.td2final.R;
import td2final.engecomp.td2final.Utilidades.MaskEditTextChangedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisaFragment extends Fragment
{
    private android.support.v7.widget.Toolbar toolbar;
    private ImageView imageCalendar;
    private ImageView imageBack;
    private EditText editTextNomeRuaAv;
    private EditText editTextHoraRoubo;
    private Spinner spinnerOpcoesPesquisar;
    private Spinner spinnerRuaAv;
    private String opcaoParaPesquisa;
    private ArrayList<String> listaOpcoesParaPesquisa;
    private ArrayList<String> listaRuaAv;
    private CalendarView calendarView;
    private TextView textDataSelecionada;
    private String dataSelecionada;
    private String opcaoRuaAv;
    private Button btnPesquisar;
    private String parametroBusca = "";
    private String tipoBusca = "";
    private EnderecoRoubo enderecoRoubo;
    private ArrayList<EnderecoRoubo> listaRoubos = new ArrayList<>();
    private RecyclerView recyclerPesquisar;
    private RecyclerAdapterRoubo adapterPesquisar;
    private Button btnLimpar;
    private TextView textoPesquisarPor;
    private ConstraintLayout separador1;
    private ConstraintLayout separador2;


    public PesquisaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        calendarView = (CalendarView) view.findViewById(R.id.calendarViewId);
        textDataSelecionada = (TextView) view.findViewById(R.id.textDataSelecionadaId);
        imageCalendar = (ImageView) view.findViewById(R.id.imageCalendarId);
        editTextNomeRuaAv = (EditText) view.findViewById(R.id.editTextNomeRuaId);
        editTextHoraRoubo = (EditText) view.findViewById(R.id.editTextHoraRouboId);
        btnPesquisar = (Button) view.findViewById(R.id.btnPesquisarId);
        btnLimpar = (Button) view.findViewById(R.id.btnLimparId);
        textoPesquisarPor = (TextView) view.findViewById(R.id.textPesquisarPorId);
        separador1 = (ConstraintLayout)view.findViewById(R.id.separador1Id);
        separador2 = (ConstraintLayout)view.findViewById(R.id.separador2Id);

        MaskEditTextChangedListener maskHoraRoubo = new MaskEditTextChangedListener("##:##", editTextHoraRoubo);
        editTextHoraRoubo.addTextChangedListener(maskHoraRoubo);

        recyclerPesquisar = (RecyclerView) view.findViewById(R.id.recyclerPesquisaId);
        recyclerPesquisar.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterPesquisar = new RecyclerAdapterRoubo(listaRoubos,getActivity());
        recyclerPesquisar.setAdapter(adapterPesquisar);

        listaOpcoesParaPesquisa = new ArrayList<>();
        spinnerOpcoesPesquisar = (Spinner) view.findViewById(R.id.spinnerOpcaoPesquisaId);

        listaOpcoesParaPesquisa.add("Endereço do Roubo");
        listaOpcoesParaPesquisa.add("Data do Roubo");
        listaOpcoesParaPesquisa.add("Hora do Roubo");
        listaOpcoesParaPesquisa.add("Todos");

        listaRuaAv = new ArrayList<>();
        spinnerRuaAv = (Spinner) view.findViewById(R.id.spinnerRuaAvId);
        listaRuaAv.add("Rua");
        listaRuaAv.add("Av");

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        spinnerOpcoesPesquisar.setVisibility(View.VISIBLE);
        spinnerRuaAv.setVisibility(View.INVISIBLE);
        calendarView.setVisibility(View.INVISIBLE);
        btnPesquisar.setVisibility(View.INVISIBLE);
        recyclerPesquisar.setVisibility(View.INVISIBLE);
        btnLimpar.setVisibility(View.INVISIBLE);
        textoPesquisarPor.setVisibility(View.VISIBLE);
        separador1.setVisibility(View.VISIBLE);
        separador2.setVisibility(View.VISIBLE);

        super.onResume();
        ArrayAdapter<String> arrayAdapterOpcoesPesquisa = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, listaOpcoesParaPesquisa);
        final ArrayAdapter<String> spinnerArrayAdapterOpcoesPesquisa = arrayAdapterOpcoesPesquisa;
        spinnerArrayAdapterOpcoesPesquisa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpcoesPesquisar.setAdapter(spinnerArrayAdapterOpcoesPesquisa);

        ArrayAdapter<String> arrayAdapterRuaAv = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, listaRuaAv);
        ArrayAdapter<String> spinnerArrayAdapterRuaAv = arrayAdapterRuaAv;
        spinnerArrayAdapterRuaAv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRuaAv.setAdapter(arrayAdapterRuaAv);

        spinnerOpcoesPesquisar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                opcaoParaPesquisa = parent.getItemAtPosition(position).toString();
                if (opcaoParaPesquisa.equals("Endereço do Roubo"))
                {
                    spinnerRuaAv.setVisibility(View.VISIBLE);
                    textDataSelecionada.setVisibility(View.INVISIBLE);
                    imageCalendar.setVisibility(View.INVISIBLE);
                    editTextNomeRuaAv.setVisibility(View.VISIBLE);
                    editTextHoraRoubo.setVisibility(View.INVISIBLE);
                    btnPesquisar.setVisibility(View.VISIBLE);
                } else if (opcaoParaPesquisa.equals("Data do Roubo"))
                {
                    spinnerRuaAv.setVisibility(View.INVISIBLE);
                    calendarView.setVisibility(View.INVISIBLE);
                    textDataSelecionada.setVisibility(View.INVISIBLE);
                    imageCalendar.setVisibility(View.VISIBLE);
                    editTextNomeRuaAv.setVisibility(View.INVISIBLE);
                    editTextHoraRoubo.setVisibility(View.INVISIBLE);
                    btnPesquisar.setVisibility(View.INVISIBLE);
                } else if (opcaoParaPesquisa.equals("Hora do Roubo"))
                {
                    spinnerRuaAv.setVisibility(View.INVISIBLE);
                    calendarView.setVisibility(View.INVISIBLE);
                    textDataSelecionada.setVisibility(View.INVISIBLE);
                    imageCalendar.setVisibility(View.INVISIBLE);
                    editTextNomeRuaAv.setVisibility(View.INVISIBLE);
                    editTextHoraRoubo.setVisibility(View.VISIBLE);
                    btnPesquisar.setVisibility(View.VISIBLE);
                } else if (opcaoParaPesquisa.equals("Todos"))
                {
                    spinnerRuaAv.setVisibility(View.INVISIBLE);
                    calendarView.setVisibility(View.INVISIBLE);
                    textDataSelecionada.setVisibility(View.INVISIBLE);
                    imageCalendar.setVisibility(View.INVISIBLE);
                    editTextNomeRuaAv.setVisibility(View.INVISIBLE);
                    editTextHoraRoubo.setVisibility(View.INVISIBLE);
                    btnPesquisar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                calendarView.setVisibility(View.INVISIBLE);
                textDataSelecionada.setVisibility(View.VISIBLE);
                String dia = "";
                spinnerOpcoesPesquisar.setVisibility(View.VISIBLE);

                if(i2 < 10)
                {
                    dia = "0"+i2;
                }
                else
                    dia = ""+i2;

                if (i1 < 10)
                {
                    dataSelecionada = "" + dia + "/0" + (i1 + 1) + "/" + i;
                }
                else
                    dataSelecionada = "" + dia + "/" +(i1 + 1) + "/" + i;

                textDataSelecionada.setText("Data Selecionada: " + dataSelecionada);
                imageCalendar.setVisibility(View.VISIBLE);
                btnPesquisar.setVisibility(View.VISIBLE);
                separador1.setVisibility(View.VISIBLE);
                separador2.setVisibility(View.VISIBLE);
            }
        });

        imageCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.setVisibility(View.VISIBLE);
                textDataSelecionada.setVisibility(View.INVISIBLE);
                imageCalendar.setVisibility(View.INVISIBLE);
                btnPesquisar.setVisibility(View.INVISIBLE);
                spinnerOpcoesPesquisar.setVisibility(View.INVISIBLE);
                separador1.setVisibility(View.INVISIBLE);
                separador2.setVisibility(View.INVISIBLE);

            }
        });

        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ParseQuery<ParseObject> filtro = null;

                if (spinnerOpcoesPesquisar.getSelectedItem().toString() == "Endereço do Roubo")
                {
                    if (editTextNomeRuaAv.getText().toString().isEmpty())
                    {
                        Toast.makeText(getActivity(), "O campo de Nome da Rua / Av deve ser preenchido!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tipoBusca = "Rua";
                    parametroBusca = spinnerRuaAv.getSelectedItem().toString() + " " + editTextNomeRuaAv.getText().toString();
                    Toast.makeText(getActivity(),tipoBusca + " " + parametroBusca, Toast.LENGTH_SHORT).show();
                    filtro = ParseQuery.getQuery("Roubos");
                    filtro.whereEqualTo(tipoBusca, parametroBusca.toUpperCase());
                }

                else if (spinnerOpcoesPesquisar.getSelectedItem().toString() == "Data do Roubo")
                {
                    parametroBusca = dataSelecionada;
                    tipoBusca = "DataRoubo";
                    Toast.makeText(getActivity(),tipoBusca + " " + parametroBusca, Toast.LENGTH_SHORT).show();
                    filtro = ParseQuery.getQuery("Roubos");
                    filtro.whereEqualTo(tipoBusca, parametroBusca.toUpperCase());
                }

                else if (spinnerOpcoesPesquisar.getSelectedItem().toString() == "Hora do Roubo")
                {
                    tipoBusca = "HoraRoubo";
                    parametroBusca = editTextHoraRoubo.getText().toString();
                    Toast.makeText(getActivity(),tipoBusca + " " + parametroBusca, Toast.LENGTH_SHORT).show();
                    filtro = ParseQuery.getQuery("Roubos");
                    filtro.whereEqualTo(tipoBusca, parametroBusca.toUpperCase());
                }

                else if (spinnerOpcoesPesquisar.getSelectedItem().toString() == "Todos")
                {
                    tipoBusca = "DataRoubo";
                    parametroBusca = " ";
                    Toast.makeText(getActivity(),tipoBusca + " " + parametroBusca, Toast.LENGTH_SHORT).show();
                    filtro = ParseQuery.getQuery("Roubos");
                    filtro.addAscendingOrder("DataRoubo");
                }

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
                            recyclerPesquisar.setVisibility(View.VISIBLE);
                            btnLimpar.setVisibility(View.VISIBLE);


                            btnPesquisar.setVisibility(View.INVISIBLE);
                            spinnerOpcoesPesquisar.setVisibility(View.INVISIBLE);
                            spinnerRuaAv.setVisibility(View.INVISIBLE);
                            textDataSelecionada.setVisibility(View.INVISIBLE);
                            imageCalendar.setVisibility(View.INVISIBLE);
                            editTextHoraRoubo.setVisibility(View.INVISIBLE);
                            editTextNomeRuaAv.setVisibility(View.INVISIBLE);
                            textoPesquisarPor.setVisibility(View.INVISIBLE);
                            separador1.setVisibility(View.INVISIBLE);
                            separador2.setVisibility(View.INVISIBLE);
                            adapterPesquisar.notifyDataSetChanged();

                            if(listaRoubos.isEmpty())
                            {
                                Toast.makeText(getActivity(), "Não há registros para os parâmetros pesquisados!", Toast.LENGTH_LONG).show();
                                return;
                            }


                        } else {
                            Log.i("AAAAAA", "Erro ao consultar Objeto" + e.getMessage());
                            Toast.makeText(getActivity(), "ERRO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                recyclerPesquisar.setVisibility(View.INVISIBLE);
                btnLimpar.setVisibility(View.INVISIBLE);

                btnPesquisar.setVisibility(View.INVISIBLE);
                spinnerOpcoesPesquisar.setVisibility(View.VISIBLE);
                spinnerRuaAv.setVisibility(View.VISIBLE);
                textDataSelecionada.setVisibility(View.INVISIBLE);
                imageCalendar.setVisibility(View.INVISIBLE);
                editTextHoraRoubo.setVisibility(View.INVISIBLE);
                editTextNomeRuaAv.setVisibility(View.VISIBLE);
                textoPesquisarPor.setVisibility(View.VISIBLE);
                separador1.setVisibility(View.VISIBLE);
                separador2.setVisibility(View.VISIBLE);
                spinnerOpcoesPesquisar.setSelection(0);


                listaRoubos.clear();
            }
        });

    }
}
