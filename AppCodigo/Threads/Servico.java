package td2final.engecomp.td2final.Threads;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
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

import td2final.engecomp.td2final.Activitys.MainActivity;
import td2final.engecomp.td2final.Model.Dispositivo;
import td2final.engecomp.td2final.Model.EnderecoRoubo;
import td2final.engecomp.td2final.Utilidades.CreateProgressDialog;
import td2final.engecomp.td2final.Utilidades.Mqtt;
import td2final.engecomp.td2final.Utilidades.NotificacaoRoubo;

/**
 * Created by Fabio on 17/02/2018.
 */

public class Servico extends Service {

    //private Mqtt mqtt = new Mqtt(MainActivity.contextMain);
    public static final String CATEGORIA = "servico";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(!MainActivity.mqtt.getClient().isConnected())
            MainActivity.mqtt.connect();

        MainActivity.mqtt.getClient().setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getApplicationContext(), "Conexão Perdida", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception
            {
                String mensagem = new String(message.getPayload());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("mensagem",mensagem);
                NotificacaoRoubo notificacaoRoubo = new NotificacaoRoubo(getApplicationContext());
                notificacaoRoubo.show(1, intent, "Roubo em Andamento !!", "Roubo em Andamento!!!");
                startActivity(intent);

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    public void pararService()
    {
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}