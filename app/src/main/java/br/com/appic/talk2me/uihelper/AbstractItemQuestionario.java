package br.com.appic.talk2me.uihelper;

import android.view.View;

/**
 * Created by vagnnermartins on 25/08/14.
 */
public abstract class AbstractItemQuestionario {

    public View view;
    public boolean respondido;

    public AbstractItemQuestionario(View view) {
        this.view = view;
    }
}
