package td2final.engecomp.td2final.Activitys;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;

import td2final.engecomp.td2final.Adapter.TabAdapter;
import td2final.engecomp.td2final.Model.EnderecoRoubo;
import td2final.engecomp.td2final.R;
import td2final.engecomp.td2final.Tabs.SlidingTabLayout;
import td2final.engecomp.td2final.Threads.CreateThreadConnect;
import td2final.engecomp.td2final.Threads.Servico;
import td2final.engecomp.td2final.Utilidades.Mqtt;
import td2final.engecomp.td2final.Utilidades.Preferencias;

public class MainActivity extends AppCompatActivity
{
    private FragmentManager fragmentManager;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private android.support.v7.widget.Toolbar toolbar;
    public static String TOPICO_SUBSCRIBE = "MQTTEnvia";
    public static String TOPICO_PUBLISH = "MQTTRecebe";
    private static final String AQUIVO_PREFERENCIA = "ArquivoPreferencia";
    public static ArrayList<EnderecoRoubo> listaRoubos = new ArrayList<>();
    private Preferencias preferencias;
    public static Mqtt mqtt;
    private CreateThreadConnect threadWatchDogTimer;
    public static Context contextMain;
    //public static String mensagemRecebida;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    startActivityForResult(new Intent(MainActivity.this,PesquisaActivity.class),1);
                    //finish();
                    return true;
                case R.id.navigation_navigation:
                    return true;
                case R.id.navigation_add_location:
                    startActivity(new Intent(MainActivity.this,CadastraDispositivoActivity.class));
                    //finish();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contextMain = MainActivity.this;
        startService(new Intent(MainActivity.this, Servico.class));

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TD2 - Final");
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //Configurar sliding tabs
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));

        TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager() );
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);

        preferencias = new Preferencias(MainActivity.this);

        mqtt = new Mqtt(getApplicationContext());
        threadWatchDogTimer = new CreateThreadConnect(mqtt);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_navigation);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
        navigation.setItemTextColor(ColorStateList.valueOf(Color.WHITE));

    }



    public void showFragment(Fragment fragment, String nome)
    {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment , nome);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, Servico.class));
        NotificationManager notificationManager = (NotificationManager)MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item_item:
            {

                ParseUser.logOut();
                preferencias.limparPreferencias();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                threadWatchDogTimer.paraWatchDogTimer();
                finish();
                return true;
            }

            case R.id.item_atualizar:
            {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                //startService(new Intent(MainActivity.this, Servico.class));
                finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
