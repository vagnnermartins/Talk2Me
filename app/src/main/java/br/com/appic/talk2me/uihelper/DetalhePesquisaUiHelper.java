package br.com.appic.talk2me.uihelper;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import br.com.appic.talk2me.R;

/**
 * Created by vagnnermartins on 22/08/14.
 */
public class DetalhePesquisaUiHelper {

    public final View view;
    public TextView titulo;
    public TextView qtdQuestoes;
    public ScrollView scroll;
    public LinearLayout main;

    public DetalhePesquisaUiHelper(View view){
        this.view = view;
        init();
    }

    private void init() {
        this.main = (LinearLayout) view.findViewById(R.id.detalhe_pesquisa_main);
        this.titulo = (TextView) view.findViewById(R.id.detalhe_pesquisa_titulo);
        this.qtdQuestoes = (TextView) view.findViewById(R.id.detalhe_pesquisa_qtd_questoes);
        this.scroll = (ScrollView) view.findViewById(R.id.detalhe_pesquisa_scroll);
    }
}
