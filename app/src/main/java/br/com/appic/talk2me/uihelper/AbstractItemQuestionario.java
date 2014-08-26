package br.com.appic.talk2me.uihelper;

import android.view.View;

import br.com.appic.talk2me.callback.Callback;
import br.com.appic.talk2me.parse.AlternativaParse;
import br.com.appic.talk2me.parse.QuestaoParse;

/**
 * Created by vagnnermartins on 25/08/14.
 */
public abstract class AbstractItemQuestionario {

    public View view;
    public Callback callback;
    public QuestaoParse questao;
    public AlternativaParse respostaSelecionada;

    public AbstractItemQuestionario(View view, Callback callback, QuestaoParse questao) {
        this.view = view;
        this.callback = callback;
        this.questao = questao;
    }
}
