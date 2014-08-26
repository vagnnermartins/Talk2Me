package br.com.appic.talk2me.service;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import br.com.appic.talk2me.parse.PesquisaParse;

/**
 * Created by vagnnermartins on 22/08/14.
 */
public class RespostaService {

    private static final String RESPOSTAS = "respostas";

    public static ParseQuery buscarRespostas(PesquisaParse pesquisa, FindCallback<ParseObject> callback){
        ParseQuery<ParseObject> queryEntrevista = ParseQuery.getQuery("Entrevista");
        queryEntrevista.fromLocalDatastore();
        queryEntrevista.whereEqualTo("pesquisa", pesquisa);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Resposta");
        query.whereMatchesQuery("entrevista", queryEntrevista);
        query.fromLocalDatastore();
        query.findInBackground(callback);
        return query;
    }

    public static void salvarRespostasInLocal(List<ParseObject> resultados, SaveCallback saveCallback){
        ParseObject.pinAllInBackground(RESPOSTAS, resultados, saveCallback);
    }

    public static void salvarRespostas(List<ParseObject> resultados, SaveCallback callback){
        ParseObject.saveAllInBackground(resultados, callback);
    }

    public static void deletarRespostasInLocal(DeleteCallback callback) {
        ParseObject.unpinAllInBackground(RESPOSTAS, callback);
    }
}
