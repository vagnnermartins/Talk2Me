package br.com.appic.talk2me;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.appic.talk2me.R;
import br.com.appic.talk2me.app.App;
import br.com.appic.talk2me.enums.StatusEnum;
import br.com.appic.talk2me.parse.EntrevistaParse;
import br.com.appic.talk2me.parse.PesquisaParse;
import br.com.appic.talk2me.parse.QuestaoParse;
import br.com.appic.talk2me.parse.RespostaParse;
import br.com.appic.talk2me.parse.ResultadoParse;
import br.com.appic.talk2me.service.EntrevistaService;
import br.com.appic.talk2me.service.PesquisaService;
import br.com.appic.talk2me.service.RespostaService;
import br.com.appic.talk2me.service.ResultadoService;
import br.com.appic.talk2me.uihelper.CardDetalheGraficoUiHelper;
import br.com.appic.talk2me.uihelper.CardDetalheSincroniaUiHelper;
import br.com.appic.talk2me.uihelper.CardIniciarPesquisaUiHelper;
import br.com.appic.talk2me.uihelper.DetalhePesquisaUiHelper;
import br.com.appic.talk2me.util.NavegacaoUtil;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class DetalhePesquisaActivity extends Activity {

    private static final int ITENS_PARA_ATUALIZAR = 2;
    private int itensAtualizados;

    private App app;

    private DetalhePesquisaUiHelper uiHelper;
    private CardIniciarPesquisaUiHelper cardIniciarPesquisaUiHelper;
    private CardDetalheSincroniaUiHelper cardDetalheSincroniaUiHelper;
    private CardDetalheGraficoUiHelper cardDetalheGraficoUiHelper;

    private PullToRefreshAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_pesquisa);
        init();
        verificaStatus(StatusEnum.INICIO);
    }

    private void init() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        attacher = PullToRefreshAttacher.get(this);
        app = (App) getApplication();
        uiHelper = new DetalhePesquisaUiHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        uiHelper.titulo.setText(app.getPesquisaParse().getTitulo());
        cardIniciarPesquisaUiHelper = new CardIniciarPesquisaUiHelper(getLayoutInflater().inflate(R.layout.card_iniciar_pesquisa, null));
        cardIniciarPesquisaUiHelper.view.setOnClickListener(configurarOnIniciarClickListener(false));
        cardDetalheSincroniaUiHelper = new CardDetalheSincroniaUiHelper(getLayoutInflater().inflate(R.layout.card_detalhe_sincronia, null));
        cardDetalheGraficoUiHelper = new CardDetalheGraficoUiHelper(getLayoutInflater().inflate(R.layout.card_detalhe_grafico, null));
        cardDetalheSincroniaUiHelper.view.setVisibility(View.GONE);
        cardDetalheSincroniaUiHelper.view.setOnClickListener(configurarOnSincronizarClickListener());
        uiHelper.main.addView(cardIniciarPesquisaUiHelper.view);
        uiHelper.main.addView(cardDetalheSincroniaUiHelper.view);
        uiHelper.main.addView(cardDetalheGraficoUiHelper.view);
    }

    private void verificaStatus(StatusEnum status){
        if(status == StatusEnum.INICIO){
            statusInicio();
            verificaStatus(StatusEnum.EXECUTANDO);
        }else if(status == StatusEnum.EXECUTANDO){
            attacher.setRefreshing(true);
        }else if(status == StatusEnum.EXECUTADO){
            statusExecutado();
        }
    }

    private void statusInicio() {
        itensAtualizados = ITENS_PARA_ATUALIZAR;
        RespostaService.buscarRespostasInLocal(app.getPesquisaParse(), configurarBuscarRespostasCallback());
        EntrevistaService.buscarEntrevistaInLocal(app.getPesquisaParse(), configurarBuscarEntrevistaCallback());
    }

    private void statusExecutado() {
        itensAtualizados--;
        if(itensAtualizados == 0){
            attacher.setRefreshComplete();
            cardIniciarPesquisaUiHelper.view.setOnClickListener(configurarOnIniciarClickListener(true));
        }
    }

    private FindCallback<RespostaParse> configurarBuscarRespostasCallback() {
        return new FindCallback<RespostaParse>() {
            @Override
            public void done(List<RespostaParse> result, ParseException error) {
                if(error == null){
                    app.setQuestoesRepostas(new HashMap<QuestaoParse, List<RespostaParse>>());
                    for(RespostaParse resposta : result){
                        List<RespostaParse> list = app.getQuestoesRepostas().get(resposta.getQuestao());
                        if(list == null){
                            list = new ArrayList<RespostaParse>();
                        }
                        list.add(resposta);
                        app.getQuestoesRepostas().put(resposta.getQuestao(), list);
                    }
                    uiHelper.qtdQuestoes.setText(String.valueOf(app.getQuestoesRepostas().size()));
                }
                verificaStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    private FindCallback<EntrevistaParse> configurarBuscarEntrevistaCallback() {
        return new FindCallback<EntrevistaParse>() {
            @Override
            public void done(List<EntrevistaParse> result, ParseException error) {
                if(error == null){
                    app.setEntrevistas(result);
                    if(!result.isEmpty()){
                        cardDetalheSincroniaUiHelper.qtdPesquisas.setText(String.valueOf(result.size()));
                        cardDetalheSincroniaUiHelper.view.setVisibility(View.VISIBLE);
                    }

                }
                verificaStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    private View.OnClickListener configurarOnIniciarClickListener(final boolean isCarregado) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCarregado){
                    NavegacaoUtil.navegar(DetalhePesquisaActivity.this, QuestionarioActivity.class);
                }else{
                    Toast.makeText(DetalhePesquisaActivity.this, R.string.detalhes_pesquisa_aguarde_carregar, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private View.OnClickListener configurarOnSincronizarClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(app.isInternetConnection()){
                    ResultadoService.buscarResultados(app.getPesquisaParse(), configurarBuscarResultadosCallback());
                }
            }

            private FindCallback<ResultadoParse> configurarBuscarResultadosCallback() {
                return new FindCallback<ResultadoParse>() {
                    @Override
                    public void done(List<ResultadoParse> result, ParseException error) {
                        if(error == null){
                            ResultadoService.salvarResultados(result);
                        }
                    }
                };
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
