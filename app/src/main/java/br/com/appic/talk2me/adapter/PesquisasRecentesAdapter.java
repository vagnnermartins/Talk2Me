package br.com.appic.talk2me.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.appic.talk2me.R;
import br.com.appic.talk2me.parse.PesquisaParse;

/**
 * Created by vagnnermartins on 21/08/14.
 */
public class PesquisasRecentesAdapter extends ArrayAdapter<PesquisaParse> {

    private final int resource;

    public PesquisasRecentesAdapter(Context context, int resource, List<PesquisaParse> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        PesquisaRecenteViewHolder viewHolder = null;
        if(convertView == null){
            convertView = parent.inflate(getContext(), resource, null);
            viewHolder = new PesquisaRecenteViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (PesquisaRecenteViewHolder) convertView.getTag();
        }
        PesquisaParse item = getItem(position);
        viewHolder.item = item;
        viewHolder.titulo.setText(item.getTitulo());
        viewHolder.objectId.setText(item.getObjectId());
        return convertView;
    }

    public class PesquisaRecenteViewHolder{
        TextView titulo;
        TextView objectId;
        public PesquisaParse item;

        public PesquisaRecenteViewHolder(View view){
            titulo = (TextView) view.findViewById(R.id.item_pesquisa_recente_titulo);
            objectId = (TextView) view.findViewById(R.id.item_pesquisa_recente_codigo);
        }
    }
}
