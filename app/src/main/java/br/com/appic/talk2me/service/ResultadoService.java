package br.com.appic.talk2me.service;

import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.List;

import br.com.appic.talk2me.parse.EntrevistaParse;
import br.com.appic.talk2me.parse.PesquisaParse;
import br.com.appic.talk2me.parse.ResultadoParse;

/**
 * Created by vagnnermartins on 22/08/14.
 */
public class ResultadoService {

    public static ParseQuery buscarResultados(PesquisaParse pesquisa, FindCallback<ResultadoParse> callback){
        ParseQuery<EntrevistaParse> queryEntrevista = ParseQuery.getQuery(EntrevistaParse.class);
        queryEntrevista.fromLocalDatastore();
        queryEntrevista.whereEqualTo("pesquisa", pesquisa);
        ParseQuery<ResultadoParse> query = ParseQuery.getQuery(ResultadoParse.class);
        query.whereMatchesQuery("entrevista", queryEntrevista);
        query.fromLocalDatastore();
        query.findInBackground(callback);
        return query;
    }

    public static void salvarResultados(List<ResultadoParse> resultados){
    }
}
