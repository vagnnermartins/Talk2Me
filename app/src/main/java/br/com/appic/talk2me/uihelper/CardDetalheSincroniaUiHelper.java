package br.com.appic.talk2me.uihelper;

import android.view.View;
import android.widget.TextView;

import br.com.appic.talk2me.R;

/**
 * Created by vagnnermartins on 22/08/14.
 */
public class CardDetalheSincroniaUiHelper {

    public final View view;
    public TextView descricao;
    public TextView clickParaSincronizar;
    public TextView qtdPesquisas;

    public CardDetalheSincroniaUiHelper(View view) {
        this.view = view;
        init();
    }

    private void init() {
        descricao = (TextView) view.findViewById(R.id.card_detalhe_sincronia_descricao);
        clickParaSincronizar = (TextView) view.findViewById(R.id.card_detalhe_sincronia_descricao);
        qtdPesquisas = (TextView) view.findViewById(R.id.card_detalhe_sincronia_qtd_pesquisa);
    }
}
