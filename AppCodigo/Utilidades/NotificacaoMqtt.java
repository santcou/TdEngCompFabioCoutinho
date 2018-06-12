package td2final.engecomp.td2final.Utilidades;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import td2final.engecomp.td2final.R;

/**
 * Created by Fabio on 14/11/2017.
 */

public class NotificacaoMqtt
{

    private Context mContext;

    public NotificacaoMqtt(Context context){
        mContext = context;
    }

    public void show(int id, Intent intent, String titulo, String mensagem){
        if(intent == null){
            return;
        }

        if(titulo == null || mensagem == null){
            return;
        }

        PendingIntent pi = PendingIntent.getActivity(mContext.getApplicationContext(),id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        android.app.Notification notification = new android.app.Notification.Builder(mContext.getApplicationContext())
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setSmallIcon(R.drawable.icon_connect)
                //.setVibrate(new long[] {100,2000})
                .setOngoing(true)
                //.setSound(Uri.parse("android.resource://"
                //+ "com.fabio.coutinho.testefinaltcc" + "/" + R.raw.tiros))
                .setContentIntent(pi).build();

        NotificationManager notificationManager = (NotificationManager) mContext.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= android.app.Notification.FLAG_AUTO_CANCEL;
        //notification.defaults |= android.app.Notification.DEFAULT_SOUND;
        notificationManager.notify(id,notification);
    }
}
