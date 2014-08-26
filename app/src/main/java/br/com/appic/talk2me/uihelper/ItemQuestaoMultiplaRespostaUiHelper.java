package br.com.appic.talk2me.uihelper;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import br.com.appic.talk2me.R;
import br.com.appic.talk2me.callback.Callback;
import br.com.appic.talk2me.parse.AlternativaParse;
import br.com.appic.talk2me.parse.QuestaoParse;

/**
 * Created by vagnnermartins on 25/08/14.
 */
public class ItemQuestaoMultiplaRespostaUiHelper extends AbstractItemQuestionario{

    public TextView enunciado;
    public LinearLayout opcoes;
    public Set<AlternativaParse> selecionadas;

    public ItemQuestaoMultiplaRespostaUiHelper(View view, Callback callback, QuestaoParse questao) {
        super(view, callback, questao);
        init();
    }

    private void init() {
        selecionadas = new HashSet<AlternativaParse>();
        enunciado = (TextView) super.view.findViewById(R.id.item_questionario_multipla_respostas_enunciado);
        opcoes = (LinearLayout) super.view.findViewById(R.id.item_questionario_multipla_respostas_opcoes);
    }

    public CompoundButton.OnCheckedChangeListener configurarOnCheckedChangeListener(){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                respostaSelecionada = (AlternativaParse) compoundButton.getTag();
                if(isCheck){
                    selecionadas.add(respostaSelecionada);
                }else{
                    selecionadas.remove(respostaSelecionada);
                }
                callback.onReturn(null, questao, selecionadas);
            }
        };
    }

}
