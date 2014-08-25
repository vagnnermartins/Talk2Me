package br.com.appic.talk2me.service;

import com.parse.FindCallback;
import com.parse.ParseQuery;

import br.com.appic.talk2me.parse.EntrevistaParse;
import br.com.appic.talk2me.parse.PesquisaParse;
import br.com.appic.talk2me.parse.RespostaParse;

/**
 * Created by vagnnermartins on 22/08/14.
 */
public class EntrevistaService {

    public static ParseQuery buscarEntrevistaInLocal(PesquisaParse pesquisa, FindCallback<EntrevistaParse> callback){
        ParseQuery<EntrevistaParse> query = ParseQuery.getQuery(EntrevistaParse.class);
        query.whereEqualTo("pesquisa", pesquisa);
        query.fromLocalDatastore();
        query.findInBackground(callback);
        return query;
    }

}
