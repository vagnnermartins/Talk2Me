package br.com.appic.talk2me.service;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import br.com.appic.talk2me.parse.PesquisaParse;
import br.com.appic.talk2me.parse.QuestaoParse;
import br.com.appic.talk2me.parse.RespostaParse;

/**
 * Created by vagnnermartins on 21/08/14.
 */
public class RespostaService {

    public static ParseQuery buscarRespostas(PesquisaParse pesquisa, FindCallback<RespostaParse> callback){
        ParseQuery<QuestaoParse> queryQuestao = ParseQuery.getQuery(QuestaoParse.class);
        queryQuestao.whereEqualTo("pesquisa", pesquisa);
        ParseQuery<RespostaParse> qResposta = ParseQuery.getQuery(RespostaParse.class);
        qResposta.whereMatchesQuery("questao", queryQuestao);
        qResposta.include("questao");
        qResposta.findInBackground(callback);
        return qResposta;
    }

    public static ParseQuery buscarRespostasInLocal(PesquisaParse pesquisa, FindCallback<RespostaParse> callback){
        ParseQuery<QuestaoParse> queryQuestao = ParseQuery.getQuery(QuestaoParse.class);
        queryQuestao.whereEqualTo("pesquisa", pesquisa);
        ParseQuery<RespostaParse> qResposta = ParseQuery.getQuery(RespostaParse.class);
        qResposta.whereMatchesQuery("questao", queryQuestao);
        qResposta.include("questao");
        qResposta.fromLocalDatastore();
        qResposta.findInBackground(callback);
        return qResposta;
    }

    public static void saveInLocal(List<RespostaParse> respostas){
        ParseObject.pinAllInBackground(respostas);
    }
}
