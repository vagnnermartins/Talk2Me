package br.com.appic.talk2me.uihelper;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import br.com.appic.talk2me.R;
import br.com.appic.talk2me.callback.Callback;
import br.com.appic.talk2me.parse.QuestaoParse;

/**
 * Created by vagnnermartins on 25/08/14.
 */
public class ItemQuestionarioMultiplaRespostaUiHelper extends  AbstractItemQuestionario{

    public TextView enunciado;

    public ItemQuestionarioMultiplaRespostaUiHelper(View view, Callback callback, QuestaoParse questao) {
        super(view, callback, questao);
        init();
    }

    private void init() {
        enunciado = (TextView) super.view.findViewById(R.id.item_questionario_multipla_respostas_enunciado);
    }
}
