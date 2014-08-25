package br.com.appic.talk2me.uihelper;

import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import br.com.appic.talk2me.R;
import br.com.appic.talk2me.parse.RespostaParse;

/**
 * Created by vagnnermartins on 25/08/14.
 */
public class ItemQuestionarioMultiplaEscolhaUiHelper extends  AbstractItemQuestionario{

    public TextView enunciado;
    public RadioGroup opcoes;
    public RespostaParse respostaSelecionada;

    public ItemQuestionarioMultiplaEscolhaUiHelper(View view) {
        super(view);
        init();
    }

    private void init() {
        enunciado = (TextView) super.view.findViewById(R.id.item_questionario_multipla_escolha_enunciado);
        opcoes = (RadioGroup) super.view.findViewById(R.id.item_questionario_multipla_escolha_opcoes);
        opcoes.setOnCheckedChangeListener(configurarOnCheckedChangeListener());
    }

    private RadioGroup.OnCheckedChangeListener configurarOnCheckedChangeListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                respondido = true;
                respostaSelecionada = (RespostaParse) radioGroup.findViewById(i).getTag();
            }
        };
    }

}
