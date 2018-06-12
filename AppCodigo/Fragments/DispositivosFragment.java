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
import android.support.v7.widget.CardView;
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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import td2final.engecomp.td2final.Model.Dispositivo;
import td2final.engecomp.td2final.Model.EnderecoRoubo;
import td2final.engecomp.td2final.R;
import td2final.engecomp.td2final.Utilidades.CreateProgressDialog;

public class DispositivosFragment extends SupportMapFragment implements OnMapReadyCallback,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener
{
    private GoogleMap mMap;
    private LocationManager locationManager;
    private static double latitude;
    private static double longitude;
    private static double contClick;
    private EnderecoRoubo enderecoRoubo;
    private ArrayList<LatLng> listaCoordenadas = new ArrayList<>();
    private ArrayList<Dispositivo> listaDispositivos = new ArrayList<Dispositivo>();
    private Dispositivo dispositivo;
    private String aux[];
    private Geocoder gc;
    private Marker marcador;
    private CardView cardDispositivo;
    private CreateProgressDialog dialogBuscandoDispositivos;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try
        {
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

            ParseQuery<ParseObject> filtro = ParseQuery.getQuery("Dispositivos");
            //filtro.whereEqualTo("Rua", "AV DAS INDUSTRIAS".toUpperCase());
            dialogBuscandoDispositivos = new CreateProgressDialog("Buscando Dispositivos...",false,getActivity());


            filtro.findInBackground(new FindCallback<ParseObject>()
            {
                @Override
                public void done(List<ParseObject> objects, ParseException e)
                {
                    if (e == null)
                    {
                        for (ParseObject object : objects)
                        {
                            Log.i("BBB", "Objetos - Nome: " + object.get("Responsavel") + " Rua: " + object.get("Rua"));
                            //Toast.makeText(getActivity(), "SUCESS", Toast.LENGTH_SHORT).show();
                            /*enderecoRoubo = new EnderecoRoubo();
                            enderecoRoubo.setNomeRua(object.get("Rua").toString());
                            enderecoRoubo.setNumeroRua(object.get("Numero").toString());
                            enderecoRoubo.setDataRoubo(object.get("DataRoubo").toString());
                            enderecoRoubo.setHoraRoubo(object.get("HoraRoubo").toString());
                            enderecoRoubo.setSolicitanteRoubo(object.get("Solicitante").toString());
                            enderecoRoubo.setBairroRoubo(object.get("Bairro").toString());
                            enderecoRoubo.setComercioRoubo(object.get("Estabelecimento").toString());*/
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

                        gc = new Geocoder(getActivity());

                        for (Dispositivo disp : listaDispositivos)
                        {
                            try
                            {
                                List<Address> addressList = gc.getFromLocationName("Rua "+ disp.getRuaDispositivo()+
                                        ","+disp.getNumeroDispositivo()+"," + "Santa Luzia  , Minas Gerais, Brasil ", 1);
                                latitude = addressList.get(0).getLatitude();
                                longitude = addressList.get(0).getLongitude();
                                String address = "Rua: "+ addressList.get(0).getThoroughfare() + "\n";
                                address += "Nº: "+ addressList.get(0).getFeatureName() + "\n";
                                address += "Bairro: "+ addressList.get(0).getSubLocality()+ "\n";
                                address += "Cidade: " + addressList.get(0).getSubAdminArea() +"\n";
                                address += "Estado: " + addressList.get(0).getAdminArea() + "\n";
                                address += "País: " + addressList.get(0).getCountryName();
                                //Toast.makeText(getApplicationContext(),"Endereço = "+address, Toast.LENGTH_LONG).show();
                                //Toast.makeText(getActivity(),"Latitude = " + latitude + "\nLongitude = " + longitude +"\n" +
                                       // address, Toast.LENGTH_LONG).show();

                                LatLng latLng = new LatLng(latitude,longitude);

                                MarkerOptions marker = new MarkerOptions();
                                marker.position(latLng);
                                marker.title(disp.getRuaDispositivo() +","+ disp.getNumeroDispositivo() +
                                        "," + disp.getNomeComercioDispositivo() );
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));// move a camera para o ponto marker.
                                marcador = mMap.addMarker (marker);
                                marcador.setTag(0);
                            }
                            catch(IOException e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                        dialogBuscandoDispositivos.getProgressDialog().dismiss();

                    } else {
                        Toast.makeText(getActivity(), "Erro ao buscar Dispositivos !!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "ERRO", Toast.LENGTH_SHORT).show();
                        dialogBuscandoDispositivos.getProgressDialog().dismiss();
                    }
                }
            });



            /*LatLng sydney = new LatLng(-19, -43);
            LatLng BH = new LatLng(-18, -43);
            LatLng SL = new LatLng(-19, -42);

            listaCoordenadas.add(sydney);
            listaCoordenadas.add(BH);
            listaCoordenadas.add(SL);

            for(LatLng coord : listaCoordenadas)
            {
                MarkerOptions marker = new MarkerOptions();
                marker.position(coord);
                marker.title("Marker in Sydney");
                mMap.addMarker (marker);
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));// move a camera para o ponto marker.
            }*/


        } catch (SecurityException ex) {
            Log.e("ERRO", "ERROR", ex);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        //Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        /*if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(getActivity(),
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }*/

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).

        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.card_estabelecimento, null);
        TextView textoResponsavel = (TextView) view.findViewById(R.id.textSolicitanteEstabelecimentoCardId);
        TextView textoRua = (TextView) view.findViewById(R.id.textRuaCardEstabelecimentoId);
        TextView textoBairro = (TextView) view.findViewById(R.id.textBairroEstabelecimentoCardId);
        TextView textoTelefone = (TextView) view.findViewById(R.id.textTelefoneEstabelecimentoCardId);
        TextView textoComercio = (TextView) view.findViewById(R.id.textComercioCardEstabelecimentoId);
        Button btnSimTracarRota = (Button) view.findViewById(R.id.btnTracarEstabelecimentoRotaId);

        String aux[] = new String[2];
        String nomeRuaEstabelecimento = "";
        String numeroEstabelecimento = "";
        aux = marker.getTitle().toString().split(",");
        nomeRuaEstabelecimento = aux[0].toString();
        numeroEstabelecimento = aux[1].toString();

        Log.i("ASAS",nomeRuaEstabelecimento + " , " + numeroEstabelecimento);

        for (Dispositivo d: listaDispositivos)
        {
            Log.i("ASAS",d.getNomeComercioDispositivo());
            if(d.getRuaDispositivo().equals(nomeRuaEstabelecimento) && d.getNumeroDispositivo().equals(numeroEstabelecimento))
            {
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
                        String.valueOf(marker.getPosition().latitude),String.valueOf(marker.getPosition().longitude));
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        return false;
    }
}
