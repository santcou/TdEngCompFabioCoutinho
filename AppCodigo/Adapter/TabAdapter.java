package td2final.engecomp.td2final.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import td2final.engecomp.td2final.Fragments.HistoricoFragment;
import td2final.engecomp.td2final.Fragments.MapsFragment;

public class TabAdapter extends FragmentStatePagerAdapter
{
    private String[] tituloAbas = {"NAVEGAÇÃO","HIST. DIÁRIO"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;

        switch (position)
        {
            case 0:
            {
                fragment = new MapsFragment();
                break;
            }
            case 1:
            {
                fragment = new HistoricoFragment();
                break;
            }
        }

        return fragment;
    }

    @Override
    public int getCount()
    {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tituloAbas[position];
    }
}
