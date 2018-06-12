package td2final.engecomp.td2final.Parse;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Habilite armazenamento local.
        Parse.enableLocalDatastore(this);

        // Codigo de configuração do App
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("GRsqiWfeGAGWKfkoJeMiWLFiSNDkPd9VW2ZI3mIa")
                .clientKey("9xiVeUsPb9cSP52EHWdb54QP4j0mhjF16UR25fVE")
                .server("https://parseapi.back4app.com/")
                .build()
        );


        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        //ParseACL.setDefaultACL(defaultACL, true);
    }
}
