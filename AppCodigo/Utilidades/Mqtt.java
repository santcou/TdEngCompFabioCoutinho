package td2final.engecomp.td2final.Utilidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import td2final.engecomp.td2final.Activitys.CadastraDispositivoActivity;
import td2final.engecomp.td2final.Activitys.MainActivity;

import static android.support.v4.content.ContextCompat.startActivity;


/**
 * @author Matheus Mayron
 * @since 05/06/2017.
 *
 * Classe responsavel por gerenciar uso do protocolo
 * MQTT.
 */

public class Mqtt {
    private Context mContext;
    private static String TAG = "Mqtt";
    //static String MQTTSERVER = "tcp://m14.cloudmqtt.com:12114";
    static String MQTTSERVER = "tcp://test.mosquitto.org:1883";
    private MqttAndroidClient client;
    private ProgressDialog progressDialog;
    private static int id_notificacao = 0;


    public Mqtt(Context mContext){
        this.mContext = mContext;

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(mContext.getApplicationContext(), MQTTSERVER,
                clientId);
    }


    public void connect(){

        try {
            /*progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Conectando com o Broker. Aguarde...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();*/
            //MqttConnectOptions options = new MqttConnectOptions();
            //options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            //options.setUserName("mqrybazf");
            //options.setPassword("gEVFOPGKbx6R".toCharArray());
            //IMqttToken token = client.connect(options);

            //final long time = System.currentTimeMillis();
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    NotificacaoMqtt notificacaoMqttConnect = new NotificacaoMqtt(mContext);
                    Intent intent = new Intent(mContext.getApplicationContext(),MainActivity.class);
                    notificacaoMqttConnect.show(id_notificacao,intent,"Dispositivo Conectado","Conectado com o Servidor");
                    // We are connected
                    //Toast.makeText(mContext, "Conectado...", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onSuccess");
                    //progressDialog.dismiss();
                    //Toast.makeText(mContext,"Conectado em " + String.valueOf(System.currentTimeMillis() - time), Toast.LENGTH_LONG).show();
                    subscribe(MainActivity.TOPICO_PUBLISH,1);
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    if(!client.isConnected())
                    {
                        NotificacaoServidorDesconectado notificacaoServidorDesconectado = new NotificacaoServidorDesconectado(mContext);
                        Intent intent = new Intent(mContext.getApplicationContext(),MainActivity.class);
                        notificacaoServidorDesconectado.show(id_notificacao, intent, "Servidor desconectado!",
                                "Falha na tentativa de conexão,\n reinicie o aplicativo para resolver o problema!!");
                        Log.d(TAG, "onFailure");
                    }
                    // Something went wrong e.g. connection timeout or firewall problems
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            IMqttToken disconToken = client.disconnect();
            final String notificacao = "Desconctado do Broker !!";
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    Log.d(TAG,"Desconectado com sucesso!");
                    NotificacaoServidorDesconectado notificacaoServidorDesconectado = new NotificacaoServidorDesconectado(mContext);
                    Intent intent = new Intent(mContext.getApplicationContext(),MainActivity.class);
                    notificacaoServidorDesconectado.show(id_notificacao, intent, "Servidor desconectado!",
                            "Desconectado do broker...");

                    connect();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Log.e(TAG,"Falha ao desconectar do broker!");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String payload, int qos, boolean retained){
        try {
            client.publish(topic, payload.getBytes(), qos, retained);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic, int qos){
        try{
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MqttAndroidClient getClient(){
        return client;
    }
}
