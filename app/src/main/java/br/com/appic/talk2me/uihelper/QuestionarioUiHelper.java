package br.com.appic.talk2me.uihelper;

import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import br.com.appic.talk2me.R;

/**
 * Created by vagnnermartins on 25/08/14.
 */
public class QuestionarioUiHelper {

    public final View view;
    public TextView paginacao;
    public ViewFlipper main;
    public TextView avancar;
    public TextView voltar;

    public QuestionarioUiHelper(View view){
        this.view = view;
        init();
    }

    private void init() {
        paginacao = (TextView) view.findViewById(R.id.questionario_paginacao);
        main = (ViewFlipper) view.findViewById(R.id.questionario_main);
        avancar = (TextView) view.findViewById(R.id.questionario_avancar);
        voltar = (TextView) view.findViewById(R.id.questionario_voltar);
    }
}
