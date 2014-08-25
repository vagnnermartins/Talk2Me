package br.com.appic.talk2me.uihelper;

import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import br.com.appic.talk2me.R;

/**
 * Created by vagnnermartins on 21/08/14.
 */
public class MainUiHelper {

    public final View view;
    public SearchView search;
    public ListView pesquisasRecentes;
    public TextView pesquisaRecenteResultado;

    public MainUiHelper(View view){
        this.view = view;
        init();
    }

    private void init() {
        search = (SearchView) view.findViewById(R.id.main_search);
        pesquisasRecentes = (ListView) view.findViewById(R.id.main_pesquisas_recentes);
        pesquisaRecenteResultado = (TextView) view.findViewById(R.id.main_pesquisas_recentes_resultado);
    }
}
