package br.com.appic.talk2me;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import br.com.appic.talk2me.adapter.PesquisasRecentesAdapter;
import br.com.appic.talk2me.app.App;
import br.com.appic.talk2me.enums.StatusEnum;
import br.com.appic.talk2me.parse.PesquisaParse;
import br.com.appic.talk2me.parse.RespostaParse;
import br.com.appic.talk2me.service.PesquisaService;
import br.com.appic.talk2me.service.RespostaService;
import br.com.appic.talk2me.uihelper.MainUiHelper;
import br.com.appic.talk2me.util.NavegacaoUtil;


public class MainActivity extends Activity {

    private MainUiHelper uiHelper;
    private ProgressDialog progress;
    private App app;

    private ParseQuery queryResposta;
    private ParseQuery queryPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        verificaStatus(StatusEnum.INICIO);
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        app = (App) getApplication();
        uiHelper = new MainUiHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        uiHelper.search.setOnSearchClickListener(configurarOnSearchClickListener());
        uiHelper.search.setOnClickListener(configurarOnSearchClickListener());
        uiHelper.pesquisasRecentes.setOnItemClickListener(configurarOnItemClickListener());
    }

    private void verificaStatus(StatusEnum status){
        if(status == StatusEnum.INICIO){
            statusInicio();
        }else if(status == StatusEnum.EXECUTANDO){
            statusExecutando();
        }else if(status == StatusEnum.EXECUTADO){
            statusExecutado();
        }
    }

    private void statusInicio() {
        PesquisaService.buscarPesquisasRecentes(configurarBuscarPesquisasRecentesCallback());
    }

    private void statusExecutando() {
        String queryText = uiHelper.search.getQuery().toString();
        queryPesquisa = PesquisaService.buscarPesquisa(queryText, configurarBuscarPesquisaCallback());
        progress = ProgressDialog.show(this,
                getString(R.string.main_buscando),
                getString(R.string.main_buscando_text), true, true, onCancelListener());
    }

    private void statusExecutado() {
    }

    private FindCallback<PesquisaParse> configurarBuscarPesquisasRecentesCallback() {
        return new FindCallback<PesquisaParse>() {
            @Override
            public void done(List<PesquisaParse> result, ParseException error) {
                if(error == null){
                    if(result.isEmpty()){
                        uiHelper.pesquisaRecenteResultado.setVisibility(View.VISIBLE);
                    }else{
                        uiHelper.pesquisaRecenteResultado.setVisibility(View.GONE);
                        setList(result);
                        for(PesquisaParse parse : result){
                            app.getPesquisasRecentes().put(parse.getObjectId(), parse);
                        }
                    }
                }
            }

            private void setList(List<PesquisaParse> itens) {
                uiHelper.pesquisasRecentes.setAdapter(new PesquisasRecentesAdapter(MainActivity.this, R.layout.item_pesquisa_recente, itens));
            }
        };
    }

    private GetCallback<PesquisaParse> configurarBuscarPesquisaCallback() {
        return new GetCallback<PesquisaParse>() {
            @Override
            public void done(PesquisaParse pesquisa, ParseException error) {
                if(error == null && pesquisa != null){
                    PesquisaService.savePesquisaInLocal(pesquisa, configurarSavePesquisaCallback());
                    sincronizarQuestoesERespostas(pesquisa);
                    app.setPesquisaParse(pesquisa);
                }else{
                    exibirToast(R.string.main_nenhuma_pesquisa_encontrada);
                }
            }

            private void sincronizarQuestoesERespostas(PesquisaParse pesquisa) {
                queryResposta = RespostaService.buscarRespostas(pesquisa, configurarBuscarRespostasCallback(pesquisa));
            }

            private FindCallback<RespostaParse> configurarBuscarRespostasCallback(final PesquisaParse pesquisaParse) {
                return new FindCallback<RespostaParse>() {
                    @Override
                    public void done(List<RespostaParse> respostaParses, ParseException error) {
                        if(error == null){
                            RespostaService.saveInLocal(respostaParses);
                            progress.dismiss();
                            verificaStatus(StatusEnum.INICIO);
                            avancar(pesquisaParse);
                        }else{
                            exibirToast(R.string.main_nenhuma_pesquisa_encontrada);
                        }
                    }
                };
            }

            private SaveCallback configurarSavePesquisaCallback() {
                return new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                    }
                };
            }
        };
    }

    private DialogInterface.OnCancelListener onCancelListener() {
        return new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                cancelarParseQuery(queryPesquisa);
                cancelarParseQuery(queryResposta);
                exibirToast(R.string.main_buscando_cancel);
            }
        };
    }

    private void exibirToast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener configurarOnSearchClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String queryText = uiHelper.search.getQuery().toString();
                PesquisaParse pesquisa = app.getPesquisasRecentes().get(queryText);
                if(pesquisa != null){
                    avancar(pesquisa);
                }else{
                    if(app.isInternetConnection()){
                        verificaStatus(StatusEnum.EXECUTANDO);
                    }
                }
            }
        };
    }

    private AdapterView.OnItemClickListener configurarOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PesquisasRecentesAdapter.PesquisaRecenteViewHolder holder = (PesquisasRecentesAdapter.PesquisaRecenteViewHolder) view.getTag();
                avancar(holder.item);
            }
        };
    }

    private void avancar(PesquisaParse pesquisaParse){
        app.setPesquisaParse(pesquisaParse);
        NavegacaoUtil.navegar(this, DetalhePesquisaActivity.class);
    }

    private void cancelarParseQuery(ParseQuery query){
        if(query != null){
            query.cancel();
        }
    }
}
