package br.com.appic.talk2me.service;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import br.com.appic.talk2me.parse.AlternativaParse;
import br.com.appic.talk2me.parse.PesquisaParse;
import br.com.appic.talk2me.parse.QuestaoParse;

/**
 * Created by vagnnermartins on 21/08/14.
 */
public class AlternativaService {

    public static ParseQuery buscarRespostas(PesquisaParse pesquisa, FindCallback<AlternativaParse> callback){
        ParseQuery<QuestaoParse> queryQuestao = ParseQuery.getQuery(QuestaoParse.class);
        queryQuestao.whereEqualTo("pesquisa", pesquisa);
        queryQuestao.orderByAscending("ordem");
        ParseQuery<AlternativaParse> qResposta = ParseQuery.getQuery(AlternativaParse.class);
        qResposta.whereMatchesQuery("questao", queryQuestao);
        qResposta.include("questao");
        qResposta.orderByAscending("ordem");
        qResposta.findInBackground(callback);
        return qResposta;
    }

    public static ParseQuery buscarRespostasInLocal(PesquisaParse pesquisa, FindCallback<AlternativaParse> callback){
        ParseQuery<QuestaoParse> queryQuestao = ParseQuery.getQuery(QuestaoParse.class);
        queryQuestao.whereEqualTo("pesquisa", pesquisa);
        queryQuestao.orderByAscending("ordem");
        ParseQuery<AlternativaParse> qResposta = ParseQuery.getQuery(AlternativaParse.class);
        qResposta.whereMatchesQuery("questao", queryQuestao);
        qResposta.include("questao");
        qResposta.orderByAscending("ordem");
        qResposta.fromLocalDatastore();
        qResposta.findInBackground(callback);
        return qResposta;
    }

    public static void saveInLocal(List<AlternativaParse> respostas){
        ParseObject.pinAllInBackground(respostas);
    }
}
