package td2final.engecomp.td2final.Activitys;

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

import com.parse.ParseUser;

import java.util.ArrayList;

import td2final.engecomp.td2final.Adapter.TabAdapterPesquisa;
import td2final.engecomp.td2final.Model.EnderecoRoubo;
import td2final.engecomp.td2final.R;
import td2final.engecomp.td2final.Tabs.SlidingTabLayout;
import td2final.engecomp.td2final.Utilidades.Mqtt;
import td2final.engecomp.td2final.Utilidades.Preferencias;

public class PesquisaActivity extends AppCompatActivity
{
    private FragmentManager fragmentManager;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private android.support.v7.widget.Toolbar toolbar;
    public static ArrayList<EnderecoRoubo> listaRoubos = new ArrayList<>();
    private Preferencias preferencias;
    private Mqtt mqtt;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    return true;
                case R.id.navigation_navigation:
                    //startActivityForResult(new Intent(PesquisaActivity.this,MainActivity.class),1);
                    finish();
                    return true;
                case R.id.navigation_add_location:
                    startActivity(new Intent(PesquisaActivity.this,CadastraDispositivoActivity.class));
                    finish();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TD2 - Final");
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs2);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina2);

        //Configurar sliding tabs
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));

        TabAdapterPesquisa tabAdapter = new TabAdapterPesquisa( getSupportFragmentManager() );
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_search);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
        navigation.setItemTextColor(ColorStateList.valueOf(Color.WHITE));

        preferencias = new Preferencias(PesquisaActivity.this);

    }

    public void showFragment(Fragment fragment, String nome)
    {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container2, fragment , nome);
        transaction.commitAllowingStateLoss();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pesquisa,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item_voltar:
            {

                finish();
                //threadWatchDogTimer.paraWatchDogTimer();
                return true;
            }

            case R.id.item_atualizar:
            {
                startActivity(new Intent(PesquisaActivity.this, PesquisaActivity.class));
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

