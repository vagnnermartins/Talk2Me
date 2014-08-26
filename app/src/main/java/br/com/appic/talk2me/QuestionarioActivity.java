package br.com.appic.talk2me;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.appic.talk2me.app.App;
import br.com.appic.talk2me.callback.Callback;
import br.com.appic.talk2me.enums.TipoQuestaoEnum;
import br.com.appic.talk2me.parse.AlternativaParse;
import br.com.appic.talk2me.parse.EntrevistaParse;
import br.com.appic.talk2me.parse.QuestaoParse;
import br.com.appic.talk2me.parse.RespostaParse;
import br.com.appic.talk2me.service.RespostaService;
import br.com.appic.talk2me.uihelper.AbstractItemQuestionario;
import br.com.appic.talk2me.uihelper.ItemQuestaoMultiplaEscolhaUiHelper;
import br.com.appic.talk2me.uihelper.ItemQuestaoMultiplaRespostaUiHelper;
import br.com.appic.talk2me.uihelper.QuestionarioUiHelper;
import br.com.appic.talk2me.util.DialogUtil;

public class QuestionarioActivity extends Activity implements View.OnTouchListener{

    private App app;
    private QuestionarioUiHelper uiHelper;
    private EntrevistaParse entrevista;
    private Map<QuestaoParse, AlternativaParse> respostasMultiplaEscolha;
    private Map<QuestaoParse, Set<AlternativaParse>> respostasMultiplaRespostas;

    private float lastX;
    private ProgressDialog progressSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionario);
        init();
    }

    private void init() {
        app = (App) getApplication();
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setSubtitle(app.getPesquisaParse().getTitulo());
        uiHelper = new QuestionarioUiHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        uiHelper.main.setOnTouchListener(this);
        uiHelper.avancar.setOnClickListener(configurarOnAvancarClickListener());
        uiHelper.voltar.setOnClickListener(configurarOnVoltarClickListener());
        entrevista = new EntrevistaParse();
        entrevista.setInicio(new Date());
        entrevista.setPesquisa(app.getPesquisaParse());
        respostasMultiplaEscolha = new HashMap<QuestaoParse, AlternativaParse>();
        respostasMultiplaRespostas = new HashMap<QuestaoParse, Set<AlternativaParse>>();
        carregarQuestoes();
    }

    private void carregarQuestoes() {
        View view = null;
        for(Map.Entry<QuestaoParse, List<AlternativaParse>> item : app.getQuestoesRepostas().entrySet()){
            QuestaoParse questao = item.getKey();
            if(questao.getTipoQuestao() == TipoQuestaoEnum.MULTIPLA_ESCOLHA.getTipoQuestao()){
                View viewItem = getLayoutInflater().inflate(R.layout.item_questionario_multipla_escolha, null);
                ItemQuestaoMultiplaEscolhaUiHelper itemUiHelper = new ItemQuestaoMultiplaEscolhaUiHelper(viewItem, configurarOnItemMultiplaEscolhaSelectedCallback(), questao);
                itemUiHelper.enunciado.setText(questao.getTitulo());
                view = itemUiHelper.view;
                view.setTag(itemUiHelper);
                for(AlternativaParse respota : item.getValue()){
                    RadioButton opcao = new RadioButton(this);
                    opcao.setText(respota.getTitulo());
                    opcao.setTag(respota);
                    itemUiHelper.opcoes.addView(opcao);
                }
            }else if(questao.getTipoQuestao() == TipoQuestaoEnum.MULTIPLA_RESPOSTAS.getTipoQuestao()){
                View viewItem = getLayoutInflater().inflate(R.layout.item_questionario_multipla_respostas, null);
                ItemQuestaoMultiplaRespostaUiHelper itemUiHelper = new ItemQuestaoMultiplaRespostaUiHelper(viewItem, configurarOnItemMultiplaRespostaSelectedCallback(), questao);
                view = itemUiHelper.view;
                view.setTag(itemUiHelper);
                for(AlternativaParse respota : item.getValue()){
                    CheckBox opcao = new CheckBox(this);
                    opcao.setText(respota.getTitulo());
                    opcao.setTag(respota);
                    opcao.setOnCheckedChangeListener(itemUiHelper.configurarOnCheckedChangeListener());
                    itemUiHelper.opcoes.addView(opcao);
                }
            }
            uiHelper.main.addView(view);
        }
        verificarPaginacao(uiHelper.main.getDisplayedChild());
    }

    private Callback configurarOnItemMultiplaRespostaSelectedCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                QuestaoParse questao = (QuestaoParse) objects[0];
                Set<AlternativaParse> alternativas = (Set<AlternativaParse>) objects[1];
                respostasMultiplaRespostas.put(questao, alternativas);
            }
        };
    }

    private Callback configurarOnItemMultiplaEscolhaSelectedCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                QuestaoParse questao = (QuestaoParse) objects[0];
                AlternativaParse resposta = (AlternativaParse) objects[1];
                respostasMultiplaEscolha.put(questao, resposta);
            }
        };
    }

    private void verificarPaginacao(int atual) {
        uiHelper.paginacao.setText((atual + 1) + " de " + app.getQuestoesRepostas().size());
    }

    private void voltar() {
        uiHelper.main.setInAnimation(this, R.anim.in_esquerda_para_direita);
        uiHelper.main.setOutAnimation(this, R.anim.out_esquerda_para_direita);
        uiHelper.main.showPrevious();
        verificaBotaoVoltarEAvancar();
    }

    private void proximo(int atual) {
        AbstractItemQuestionario current = (AbstractItemQuestionario) uiHelper.main.getChildAt(atual).getTag();
        if(current.respostaSelecionada != null || current instanceof ItemQuestaoMultiplaRespostaUiHelper){
            uiHelper.main.setInAnimation(this, R.anim.in_direita_para_esquerda);
            uiHelper.main.setOutAnimation(this, R.anim.out_direita_para_esquerda);
            uiHelper.main.showNext();
        }else{
            exibirMensagem(R.string.questionario_validacao_questao_nao_respondida);
        }
        verificaBotaoVoltarEAvancar();
    }

    private void exibirMensagem(int resourceText){
        Toast.makeText(this, resourceText, Toast.LENGTH_LONG).show();
    }

    private void verificaBotaoVoltarEAvancar(){
        if(uiHelper.main.getDisplayedChild() == 0){
            uiHelper.voltar.setAlpha(0.3f);
        }else{
            uiHelper.voltar.setAlpha(1f);
        }
        if(uiHelper.main.getDisplayedChild() == (uiHelper.main.getChildCount() - 1)){
            uiHelper.avancar.setText(R.string.questionario_finalizar);
        }else{
            uiHelper.avancar.setText(R.string.questionario_proximo);
        }
    }

    private View.OnClickListener configurarOnVoltarClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uiHelper.main.getDisplayedChild() > 0){
                    voltar();
                }
                verificarPaginacao(uiHelper.main.getDisplayedChild());
            }
        };
    }

    private View.OnClickListener configurarOnAvancarClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int atual = uiHelper.main.getDisplayedChild();
                if(atual < uiHelper.main.getChildCount() - 1){
                    proximo(atual);
                }else{
                    validarESalvarQuestionario();
                }
                verificarPaginacao(uiHelper.main.getDisplayedChild());
            }
        };
    }

    private void validarESalvarQuestionario() {
        AbstractItemQuestionario current = (AbstractItemQuestionario) uiHelper.main.getChildAt(uiHelper.main.getDisplayedChild()).getTag();
        if(current.respostaSelecionada != null || current instanceof AbstractItemQuestionario){
            salvar();
        }else{
            exibirMensagem(R.string.questionario_validacao_ultima_questao);
        }
    }

    private void salvar() {
        progressSalvar = ProgressDialog.show(this, getString(R.string.questionario_salvando_questionario),
                getString(R.string.questionario_salvando_questionario_msg), true, false);
        entrevista.setFim(new Date());
        List<ParseObject> respostas = new ArrayList<ParseObject>();
        RespostaParse respostaParse;
        for(Map.Entry<QuestaoParse, AlternativaParse> item : respostasMultiplaEscolha.entrySet()){
            respostaParse = new RespostaParse();
            respostaParse.setEntrevista(entrevista);
            respostaParse.setAlternativa(item.getValue());
            respostas.add(respostaParse);
        }
        for(Map.Entry<QuestaoParse, Set<AlternativaParse>> item : respostasMultiplaRespostas.entrySet()){
            for (AlternativaParse alternativaParse : item.getValue()){
                respostaParse = new RespostaParse();
                respostaParse.setEntrevista(entrevista);
                respostaParse.setAlternativa(alternativaParse);
                respostas.add(respostaParse);
            }
        }
        RespostaService.salvarRespostasInLocal(respostas, configurarOnSalvarResultadosCallback());
    }

    private SaveCallback configurarOnSalvarResultadosCallback() {
        return new SaveCallback() {
            @Override
            public void done(ParseException error) {
                if(error == null){
                    exibirMensagem(R.string.questionario_salvo_com_sucesso);
                    progressSalvar.dismiss();
                }else{
                    salvar();
                }
                setResult(RESULT_OK);
                finish();
            }
        };
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                lastX = motionEvent.getX();
                break;
            }
            case MotionEvent.ACTION_UP: {
                int atual = uiHelper.main.getDisplayedChild();
                float currentX = motionEvent.getX();
                if (lastX < currentX && atual > 0){
                    voltar();
                }
                if (lastX > currentX && atual < uiHelper.main.getChildCount() - 1){
                    proximo(atual);
                }
                verificarPaginacao(uiHelper.main.getDisplayedChild());
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        confirmarSair();
    }

    private void confirmarSair() {
        DialogUtil.show(this, android.R.string.dialog_alert_title, getString(R.string.questionario_sair_confirmacao),
                configurarOnPositiveButton(), android.R.string.yes,
                configurarOnNegativeButton(), android.R.string.no);
    }

    private DialogInterface.OnClickListener configurarOnNegativeButton() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };
    }

    private DialogInterface.OnClickListener configurarOnPositiveButton() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                confirmarSair();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
