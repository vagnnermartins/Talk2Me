package br.com.appic.talk2me;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.SearchView;
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
import br.com.appic.talk2me.parse.AlternativaParse;
import br.com.appic.talk2me.parse.PesquisaParse;
import br.com.appic.talk2me.service.AlternativaService;
import br.com.appic.talk2me.service.PesquisaService;
import br.com.appic.talk2me.uihelper.MainUiHelper;
import br.com.appic.talk2me.util.NavegacaoUtil;


public class MainActivity extends Activity {

    private static final int FOTOGRAFAR_QR_CODE = 0;
    private MainUiHelper uiHelper;
    private ProgressDialog progress;
    private App app;

    private ParseQuery queryResposta;
    private ParseQuery queryPesquisa;
    private PesquisasRecentesAdapter pesquisaAdapter;

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
        uiHelper.search.setOnQueryTextListener(configurarOnQueryTextListener());
        uiHelper.pesquisasRecentes.setOnItemClickListener(configurarOnItemClickListener());
        uiHelper.tirarFoto.setOnClickListener(configurarOnTirarFotoClickListener());
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
                pesquisaAdapter = new PesquisasRecentesAdapter(MainActivity.this, R.layout.item_pesquisa_recente, itens);
                uiHelper.pesquisasRecentes.setAdapter(pesquisaAdapter);
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
                    progress.dismiss();
                }
            }

            private void sincronizarQuestoesERespostas(PesquisaParse pesquisa) {
                queryResposta = AlternativaService.buscarRespostas(pesquisa, configurarBuscarRespostasCallback(pesquisa));
            }

            private FindCallback<AlternativaParse> configurarBuscarRespostasCallback(final PesquisaParse pesquisaParse) {
                return new FindCallback<AlternativaParse>() {
                    @Override
                    public void done(List<AlternativaParse> alternativaParses, ParseException error) {
                        if(error == null){
                            AlternativaService.saveInLocal(alternativaParses);
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

    private SearchView.OnQueryTextListener configurarOnQueryTextListener() {
        return new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(pesquisaAdapter != null){
                    pesquisaAdapter.getFilter().filter(newText);
                }
                return false;
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
                procurarPesquisa(queryText);
            }
        };
    }

    private void procurarPesquisa(String queryText) {
        if(queryText != null && !queryText.equals("")){
            PesquisaParse pesquisa = app.getPesquisasRecentes().get(queryText);
            if(pesquisa != null){
                avancar(pesquisa);
            }else{
                if(app.isInternetConnection()){
                    verificaStatus(StatusEnum.EXECUTANDO);
                }
            }
        }
    }

    private View.OnClickListener configurarOnTirarFotoClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, FOTOGRAFAR_QR_CODE);
                } catch (Exception e) {
                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FOTOGRAFAR_QR_CODE) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                uiHelper.search.setQuery(contents, false);
                procurarPesquisa(contents);
            }
        }
    }
}
