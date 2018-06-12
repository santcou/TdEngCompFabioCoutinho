package td2final.engecomp.td2final.Fragments;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import td2final.engecomp.td2final.Activitys.MainActivity;
import td2final.engecomp.td2final.Model.Dispositivo;
import td2final.engecomp.td2final.Model.EnderecoRoubo;
import td2final.engecomp.td2final.R;
import td2final.engecomp.td2final.Utilidades.CreateProgressDialog;
import td2final.engecomp.td2final.Utilidades.Mqtt;
import td2final.engecomp.td2final.Utilidades.NotificacaoMqtt;
import td2final.engecomp.td2final.Utilidades.NotificacaoRoubo;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private static double latitude;
    private static double longitude;
    private static double contClick;
    private EnderecoRoubo enderecoRoubo;
    private String aux[];
    private Mqtt mqtt;
    private ArrayList<Dispositivo> listaDispositivos;
    private ArrayList<Dispositivo> listaDispositivosRoubos;
    private SimpleDateFormat dataHora24h = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private MarkerOptions marker;
    private Dispositivo dispositivo;
    private Dispositivo dispositivoRoubos;
    private Marker marcadorGlobal;
    private Marker marcadorRoubos;
    private Geocoder gcRoubos;
    private Geocoder gc;
    private boolean flagRouboCadastrado = false;
    private LatLng localRoubo;
    private CreateProgressDialog dialogRoubos;
    private String mensagemRecebida;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
        mqtt = new Mqtt(getActivity());
        listaDispositivos = new ArrayList<>();
        listaDispositivosRoubos = new ArrayList<>();
        marker = new MarkerOptions();
        dialogRoubos = new CreateProgressDialog("Carregando Chamadas...", false, getActivity());
        gcRoubos = new Geocoder(getActivity());
        gc = new Geocoder(getActivity());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            //Código que pega a localização atual do aparelho.
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            //Toast.makeText(getApplicationContext()," Latitude: " + location.getLatitude(), Toast.LENGTH_SHORT).show();
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(provider);
                mMap.setMyLocationEnabled(true);

            } else {
                // Show rationale and request permission.
                Toast.makeText(getActivity(), "Erro ao pedir permissão", Toast.LENGTH_SHORT).show();
                return;
            }
            mMap.setOnMapClickListener(this);
            mMap.setOnMarkerClickListener(this);
            mMap.getUiSettings().setZoomControlsEnabled(true); // Botão de Zoom
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            ParseQuery<ParseObject> filtro = ParseQuery.getQuery("Roubos");
            filtro.whereEqualTo("Flag", "TRUE");
            filtro.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject object : objects) {
                            Log.i("BBB", "Objetos - Numero: " + object.get("Numero") + " Rua: " + object.get("Rua"));
                            dispositivoRoubos = new Dispositivo();
                            dispositivoRoubos.setRuaDispositivo(object.get("Rua").toString());
                            dispositivoRoubos.setNumeroDispositivo(object.get("Numero").toString());
                            dispositivoRoubos.setResponsavelDispositivo(object.get("Solicitante").toString());
                            //dispositivoRoubos.setTelefoneDispositivo(object.get("Telefone").toString());
                            //dispositivoRoubos.setPmResponsavelDispositivo(object.get("PmResponsavel").toString());
                            dispositivoRoubos.setCidadeDispositivo(object.get("Cidade").toString());
                            dispositivoRoubos.setNomeComercioDispositivo(object.get("Estabelecimento").toString());
                            dispositivoRoubos.setBairroDispositivo(object.get("Bairro").toString());

                            listaDispositivosRoubos.add(dispositivoRoubos);
                        }

                        for (Dispositivo disp : listaDispositivosRoubos) {
                            try {
                                List<Address> addressList = gcRoubos.getFromLocationName("Rua " + disp.getRuaDispositivo() +
                                        "," + disp.getNumeroDispositivo() + "," + "Santa Luzia  , Minas Gerais, Brasil ", 1);
                                latitude = addressList.get(0).getLatitude();
                                longitude = addressList.get(0).getLongitude();
                                String address = "Rua: " + addressList.get(0).getThoroughfare() + "\n";
                                address += "Nº: " + addressList.get(0).getFeatureName() + "\n";
                                address += "Bairro: " + addressList.get(0).getSubLocality() + "\n";
                                address += "Cidade: " + addressList.get(0).getSubAdminArea() + "\n";
                                address += "Estado: " + addressList.get(0).getAdminArea() + "\n";
                                address += "País: " + addressList.get(0).getCountryName();
                                //Toast.makeText(getApplicationContext(),"Endereço = "+address, Toast.LENGTH_LONG).show();
                                //Toast.makeText(getActivity(),"Latitude = " + latitude + "\nLongitude = " + longitude +"\n" +
                                // address, Toast.LENGTH_LONG).show();

                                LatLng latLng = new LatLng(latitude, longitude);

                                MarkerOptions marker = new MarkerOptions();
                                marker.position(latLng);
                                marker.title(disp.getRuaDispositivo() + "," + disp.getNumeroDispositivo() +
                                        "," + disp.getNomeComercioDispositivo());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));// move a camera para o ponto marker.
                                marcadorRoubos = mMap.addMarker(marker);
                                marcadorRoubos.setTag(0);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                dialogRoubos.getProgressDialog().dismiss();
                            }
                        }
                        dialogRoubos.getProgressDialog().dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Erro ao buscar Dispositivos !!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "ERRO", Toast.LENGTH_SHORT).show();
                        dialogRoubos.getProgressDialog().dismiss();
                    }
                }
            });

        } catch (SecurityException ex) {
            Log.e("ERRO", "ERROR", ex);
            dialogRoubos.getProgressDialog().dismiss();
        }
        preencheListaDipostitivos();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mqtt.connect();
        Bundle extra = getActivity().getIntent().getExtras();

        if (extra != null) {
            mensagemRecebida = extra.getString("mensagem");
            //Toast.makeText(getActivity(), mensagemRecebida, Toast.LENGTH_SHORT).show();

            listaDispositivos.clear();
            listaDispositivosRoubos.clear();
            flagRouboCadastrado = true;
            String dataHoraSistema = dataHora24h.format(new Date());
            enderecoRoubo = new EnderecoRoubo();
            String ruaRoubo;
            String numeroRoubo;
            String cidadeRoubo;
            String solicitanteRoubo;
            String bairroRoubo;
            String areaRoubo;
            String comercioRoubo;
            String notificacao = "Roubo em Andamento !!";
            //Toast.makeText(getActivity(), ""+notificacao, Toast.LENGTH_SHORT).show();


            //String mensagem = new String(message.getPayload());
            String mensagem = mensagemRecebida;
            //Toast.makeText(getActivity(),"Mensagem recebida = " + mensagem.concat("\n")
            //.concat(topic), Toast.LENGTH_SHORT).show();

            aux = new String[7];
            aux = mensagem.split(",");

            ruaRoubo = aux[0].toString();
            numeroRoubo = aux[1].toString();
            cidadeRoubo = aux[2].toString();
            bairroRoubo = aux[3].toString();
            comercioRoubo = aux[4].toString();
            areaRoubo = aux[5].toString();
            solicitanteRoubo = aux[6].toString();

            //Toast.makeText(getActivity(), "" + ruaRoubo + " "
            //        + numeroRoubo + " " + cidadeRoubo, Toast.LENGTH_SHORT).show();

            enderecoRoubo.setNomeRua(ruaRoubo);
            enderecoRoubo.setNumeroRua(numeroRoubo);
            enderecoRoubo.setCidade(cidadeRoubo);
            enderecoRoubo.setBairroRoubo(bairroRoubo);
            enderecoRoubo.setComercioRoubo(comercioRoubo);
            enderecoRoubo.setAreaCia(areaRoubo);
            enderecoRoubo.setSolicitanteRoubo(solicitanteRoubo);
            String auxDate[] = new String[2];
            auxDate = dataHoraSistema.split(" ");
            enderecoRoubo.setDataRoubo(auxDate[0]);
            enderecoRoubo.setHoraRoubo(auxDate[1]);

            try {
                List<Address> addressList = gc.getFromLocationName("Rua " + enderecoRoubo.getNomeRua() +
                        "," + enderecoRoubo.getNumeroRua() + "," + enderecoRoubo.getCidade() + " , Minas Gerais, Brasil ", 1);
                latitude = addressList.get(0).getLatitude();
                longitude = addressList.get(0).getLongitude();
                String address = "Rua: " + addressList.get(0).getThoroughfare() + "\n";
                address += "Nº: " + addressList.get(0).getFeatureName() + "\n";
                address += "Bairro: " + addressList.get(0).getSubLocality() + "\n";
                address += "Cidade: " + addressList.get(0).getSubAdminArea() + "\n";
                address += "Estado: " + addressList.get(0).getAdminArea() + "\n";
                address += "País: " + addressList.get(0).getCountryName();
                //Toast.makeText(getApplicationContext(),"Endereço = "+address, Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity(),"Latitude = " + latitude + "\nLongitude = " + longitude +"\n" +
                // address, Toast.LENGTH_LONG).show();

                localRoubo = new LatLng(latitude, longitude);

                marker.position(localRoubo);
                marker.title(enderecoRoubo.getNomeRua().toUpperCase() + "," + enderecoRoubo.getNumeroRua().toUpperCase());
                //mMap.addMarker(marker);
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localRoubo, 15));// move a camera para o ponto marker.
                //Toast.makeText(getActivity(), "Chamada de Roubo em " + marker.getTitle(), Toast.LENGTH_LONG).show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            //Toast.makeText(getActivity(), ""+dataHoraSistema , Toast.LENGTH_SHORT).show();

            ParseQuery<ParseObject> filtro = ParseQuery.getQuery("Roubos");
            filtro.whereEqualTo("DataRoubo", enderecoRoubo.getDataRoubo());
            filtro.whereEqualTo("HoraRoubo", enderecoRoubo.getHoraRoubo());
            filtro.whereEqualTo("Rua", enderecoRoubo.getNomeRua());
            filtro.whereEqualTo("Numero", enderecoRoubo.getNumeroRua());
            filtro.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        if (objects.isEmpty() && flagRouboCadastrado == true) {
                            flagRouboCadastrado = false;
                            ParseObject roubo = new ParseObject("Roubos");
                            roubo.put("Cidade", enderecoRoubo.getCidade().toString().toUpperCase());
                            roubo.put("HoraRoubo", enderecoRoubo.getHoraRoubo().toString().toUpperCase());
                            roubo.put("Solicitante", enderecoRoubo.getSolicitanteRoubo().toString().toUpperCase());
                            roubo.put("DataRoubo", enderecoRoubo.getDataRoubo().toString().toUpperCase());
                            roubo.put("Rua", enderecoRoubo.getNomeRua().toString().toUpperCase());
                            roubo.put("AreaCia", enderecoRoubo.getAreaCia().toString().toUpperCase());
                            roubo.put("Bairro", enderecoRoubo.getBairroRoubo().toString().toUpperCase());
                            roubo.put("Numero", enderecoRoubo.getNumeroRua().toString().toUpperCase());
                            roubo.put("Estabelecimento", enderecoRoubo.getComercioRoubo().toString().toUpperCase());
                            roubo.put("Flag", "TRUE");
                            roubo.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
//                                            Toast.makeText(getActivity(), "Roubo Cadastrado com Sucesso", Toast.LENGTH_SHORT).show();
                                        flagRouboCadastrado = false;

                                    } else {
                                        Toast.makeText(getActivity(), "Erro " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        flagRouboCadastrado = false;
                                    }
                                }
                            });
                        } else {
                            //Toast.makeText(getActivity(), "Roubo Já Cadastrado", Toast.LENGTH_SHORT).show();
                            flagRouboCadastrado = false;
                            return;
                        }

                    } else {
                        Toast.makeText(getActivity(), "Erro Cadastrar Roubo !!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "ERRO", Toast.LENGTH_SHORT).show();
                        flagRouboCadastrado = false;
                    }
                }
            });
            preencheListaDipostitivos();

            /*NotificacaoRoubo notificacaoRoubo = new NotificacaoRoubo(getActivity());
            Intent intent = new Intent(getActivity(), MainActivity.class);
            notificacaoRoubo.show(1, intent, "Roubo em Andamento !!", "Roubo em Andamento!!!");*/

        }
    }

    @Override
    public boolean onMarkerClick(final Marker marcador) {
        marcadorGlobal = marcador;
        String aux[] = new String[2];
        aux = marcador.getTitle().toString().split(",");
        String nomeRuaEstabelecimento = aux[0].toString();
        String numeroEstabelecimento = aux[1].toString();

        exibirDialogRoubo(nomeRuaEstabelecimento, numeroEstabelecimento);
        return false;
    }

    private void exibirDialogRoubo(String nomeRuaEstabelecimento, String numeroEstabelecimento) {

        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.card_solicitacao, null);
        TextView textoResponsavel = (TextView) view.findViewById(R.id.textSolicitanteSolicitacaoCardId);
        TextView textoRua = (TextView) view.findViewById(R.id.textRuaCardSolicitacaoId);
        TextView textoBairro = (TextView) view.findViewById(R.id.textBairroSolicitacaoCardId);
        TextView textoTelefone = (TextView) view.findViewById(R.id.textTelefoneSolicitacaoCardId);
        TextView textoComercio = (TextView) view.findViewById(R.id.textComercioCardSolicitacaoId);
        Button btnSimTracarRota = (Button) view.findViewById(R.id.btnTracarRota);
        Button btnLimparMarcador = (Button) view.findViewById(R.id.btnLimparMarcadorId);

        textoRua.setText("");
        textoBairro.setText("");
        textoResponsavel.setText("");
        textoTelefone.setText("");
        textoComercio.setText("");

        //Toast.makeText(getActivity(), ""+ nomeRuaEstabelecimento + " " + numeroEstabelecimento, Toast.LENGTH_SHORT).show();

        for (Dispositivo d : listaDispositivos) {
            if (d.getRuaDispositivo().equals(nomeRuaEstabelecimento) && d.getNumeroDispositivo().equals(numeroEstabelecimento)) {
                textoRua.setText(d.getRuaDispositivo() + ", Nº" + d.getNumeroDispositivo());
                textoBairro.setText(d.getBairroDispositivo());
                textoResponsavel.setText(d.getResponsavelDispositivo());
                textoTelefone.setText(d.getTelefoneDispositivo());
                textoComercio.setText(d.getNomeComercioDispositivo());
            }
        }

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setCancelable(true);
        alertDialog.setView(view);
        alertDialog.create();
        final AlertDialog ad = alertDialog.show();

        btnSimTracarRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String url = String.format(Locale.getDefault(), "https://maps.google.com?saddr=Current+Location&daddr=%s,%s",
                        String.valueOf(marcadorGlobal.getPosition().latitude), String.valueOf(marcadorGlobal.getPosition().longitude));
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        btnLimparMarcador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String auxFiltrarRoubo[] = new String[3];
                auxFiltrarRoubo = marcadorGlobal.getTitle().split(",");
                //Toast.makeText(getActivity(), auxFiltrarRoubo[0] + " " + auxFiltrarRoubo[1], Toast.LENGTH_SHORT).show();

                ParseQuery<ParseObject> filtro = ParseQuery.getQuery("Roubos");
                filtro.whereEqualTo("Rua", auxFiltrarRoubo[0].toUpperCase().toString());
                filtro.whereEqualTo("Numero", auxFiltrarRoubo[1].toUpperCase().toString());
                filtro.whereEqualTo("Flag", "TRUE");
                filtro.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            object.put("Flag", "FALSE");
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        //Toast.makeText(getActivity(), "Flag Mudada", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Erro " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            //Toast.makeText(getActivity(), "Erro ao Mudar Flag !!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });
                marcadorGlobal.remove();
                ad.dismiss();

            }
        });

    }

    private void preencheListaDipostitivos() {
        ParseQuery<ParseObject> filtroDipositivo = ParseQuery.getQuery("Dispositivos");
        filtroDipositivo.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        Log.i("BBB", "Objetos - Nome: " + object.get("Responsavel") + " Rua: " + object.get("Rua"));
                        dispositivo = new Dispositivo();
                        dispositivo.setRuaDispositivo(object.get("Rua").toString());
                        dispositivo.setNumeroDispositivo(object.get("Numero").toString());
                        dispositivo.setResponsavelDispositivo(object.get("Responsavel").toString());
                        dispositivo.setTelefoneDispositivo(object.get("Telefone").toString());
                        dispositivo.setPmResponsavelDispositivo(object.get("PmResponsavel").toString());
                        dispositivo.setCidadeDispositivo(object.get("Cidade").toString());
                        dispositivo.setNomeComercioDispositivo(object.get("NomeComercio").toString());
                        dispositivo.setBairroDispositivo(object.get("Bairro").toString());

                        listaDispositivos.add(dispositivo);
                    }

                } else {
                    Toast.makeText(getActivity(), "Erro ao buscar Dispositivos !!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "ERRO", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
