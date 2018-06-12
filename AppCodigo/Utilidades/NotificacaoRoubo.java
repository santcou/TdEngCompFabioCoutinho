package td2final.engecomp.td2final.Utilidades;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import td2final.engecomp.td2final.R;

/**
 * Created by Fabio on 14/02/2018.
 */

public class NotificacaoRoubo
{

    private Context mContext;

    public NotificacaoRoubo(Context context){
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
                .setSmallIcon(R.drawable.icon_gun_white2)
                .setVibrate(new long[] {100,2000})
                .setOngoing(false)
                .setSound(Uri.parse("android.resource://"
                + "td2final.engecomp.td2final" + "/" + R.raw.sirene_original))
                .setContentIntent(pi).build();

        NotificationManager notificationManager = (NotificationManager) mContext.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //notification.flags |= android.app.Notification.FLAG_AUTO_CANCEL;
        //notification.defaults |= android.app.Notification.DEFAULT_SOUND;
        notificationManager.notify(id,notification);
    }
}