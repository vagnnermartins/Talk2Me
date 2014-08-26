package br.com.appic.talk2me.uihelper;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import br.com.appic.talk2me.R;
import br.com.appic.talk2me.callback.Callback;
import br.com.appic.talk2me.parse.AlternativaParse;
import br.com.appic.talk2me.parse.QuestaoParse;

/**
 * Created by vagnnermartins on 25/08/14.
 */
public class ItemQuestionarioMultiplaEscolhaUiHelper extends  AbstractItemQuestionario{

    public TextView enunciado;
    public RadioGroup opcoes;

    public ItemQuestionarioMultiplaEscolhaUiHelper(View view, Callback callback, QuestaoParse questao) {
        super(view, callback, questao);
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
                respostaSelecionada = (AlternativaParse) radioGroup.findViewById(i).getTag();
                callback.onReturn(null, questao, respostaSelecionada);
            }
        };
    }

}
