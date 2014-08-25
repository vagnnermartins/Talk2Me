package br.com.appic.talk2me.service;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import br.com.appic.talk2me.parse.PesquisaParse;

/**
 * Created by vagnnermartins on 21/08/14.
 */
public class PesquisaService {

    public static void buscarPesquisasRecentes(FindCallback<PesquisaParse> callback){
        ParseQuery<PesquisaParse> query = ParseQuery.getQuery(PesquisaParse.class);
        query.fromLocalDatastore();
        query.findInBackground(callback);
    }

    public static ParseQuery buscarPesquisa(String objectId, GetCallback<PesquisaParse> callback){
        ParseQuery<PesquisaParse> query = ParseQuery.getQuery(PesquisaParse.class);
        query.getInBackground(objectId, callback);
        return query;
    }

    public static void savePesquisaInLocal(PesquisaParse pesquisa, SaveCallback callback){
        pesquisa.pinInBackground(callback);
    }
}
