package td2final.engecomp.td2final.Threads;

import android.util.Log;
import android.widget.Toast;

import td2final.engecomp.td2final.Utilidades.Mqtt;

/**
 * Created by Fabio on 24/01/2018.
 */

public class CreateThreadConnect {
    private Mqtt mqtt;

    public CreateThreadConnect(Mqtt mqtt)
    {
        this.mqtt = mqtt;
        whatchDogTimer.start();
    }


    public Thread whatchDogTimer = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true)
            {
                try
                {
                    if(!mqtt.getClient().isConnected())
                    {
                        mqtt.connect();
                        Log.i("STATUS","Thread conectou!!");
                    }
                    Thread.sleep(120000);
                    Log.i("STATUS","Conectado");
                    if (Thread.interrupted()) throw new InterruptedException();
                } catch (InterruptedException e)
                {
                    mqtt.disconnect();
                    e.printStackTrace();
                    return;
                }

            }
        }
    });

    public void paraWatchDogTimer() {
        whatchDogTimer.interrupt();
    }
}
