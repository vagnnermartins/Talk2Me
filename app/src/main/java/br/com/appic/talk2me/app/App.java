package br.com.appic.talk2me.app;

import android.app.Application;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.appic.talk2me.R;
import br.com.appic.talk2me.constants.Constants;
import br.com.appic.talk2me.parse.AlternativaParse;
import br.com.appic.talk2me.parse.EmpresaParse;
import br.com.appic.talk2me.parse.EntrevistaParse;
import br.com.appic.talk2me.parse.PesquisaParse;
import br.com.appic.talk2me.parse.QuestaoParse;
import br.com.appic.talk2me.parse.RespostaParse;
import br.com.appic.talk2me.util.ConnectionDetectorUtils;

/**
 * Created by vagnnermartins on 21/08/14.
 */
public class App extends Application {

    private Map<String, PesquisaParse> pesquisasRecentes;
    private PesquisaParse pesquisaParse;
    private Map<QuestaoParse, List<AlternativaParse>> questoesRepostas;
    private List<EntrevistaParse> entrevistas;
    private List<ParseObject> resultados;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        pesquisasRecentes = new HashMap<String, PesquisaParse>();
        resultados = new ArrayList<ParseObject>();
        initParse();
    }

    private void initParse() {
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(EmpresaParse.class);
        ParseObject.registerSubclass(EntrevistaParse.class);
        ParseObject.registerSubclass(PesquisaParse.class);
        ParseObject.registerSubclass(QuestaoParse.class);
        ParseObject.registerSubclass(AlternativaParse.class);
        ParseObject.registerSubclass(RespostaParse.class);
        Parse.initialize(this, Constants.PARSE_ID, Constants.PARSE_CLIENT_KEY);
    }

    public Map<String, PesquisaParse> getPesquisasRecentes() {
        return pesquisasRecentes;
    }

    public void setPesquisasRecentes(Map<String, PesquisaParse> pesquisasRecentes) {
        this.pesquisasRecentes = pesquisasRecentes;
    }

    public PesquisaParse getPesquisaParse() {
        return pesquisaParse;
    }

    public void setPesquisaParse(PesquisaParse pesquisaParse) {
        this.pesquisaParse = pesquisaParse;
    }

    public Map<QuestaoParse, List<AlternativaParse>> getQuestoesRepostas() {
        return questoesRepostas;
    }

    public void setQuestoesRepostas(Map<QuestaoParse, List<AlternativaParse>> questoesRepostas) {
        this.questoesRepostas = questoesRepostas;
    }

    public List<EntrevistaParse> getEntrevistas() {
        return entrevistas;
    }

    public void setEntrevistas(List<EntrevistaParse> entrevistas) {
        this.entrevistas = entrevistas;
    }
    public boolean isInternetConnection(){
        ConnectionDetectorUtils cd = new ConnectionDetectorUtils(this);
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(this, R.string.exception_erro_err_internet_disconnected, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public List<ParseObject> getResultados() {
        return resultados;
    }

    public void setResultados(List<ParseObject> resultados) {
        this.resultados = resultados;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
