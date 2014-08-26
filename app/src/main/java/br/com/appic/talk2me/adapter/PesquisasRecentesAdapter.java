package br.com.appic.talk2me.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.appic.talk2me.R;
import br.com.appic.talk2me.parse.PesquisaParse;

/**
 * Created by vagnnermartins on 21/08/14.
 */
public class PesquisasRecentesAdapter extends ArrayAdapter<PesquisaParse> {

    private final int resource;
    private List<PesquisaParse> itensOriginais;

    public PesquisasRecentesAdapter(Context context, int resource, List<PesquisaParse> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.itensOriginais = new ArrayList<PesquisaParse>(objects);
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

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                List<PesquisaParse> values = (List<PesquisaParse>) results.values;
                if(values == null){
                    values = itensOriginais;
                }
                for (PesquisaParse item : values) {
                    add(item);
                }
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();

                if (constraint != null && constraint.toString().length() > 0) {
                    List<PesquisaParse> founded = new ArrayList<PesquisaParse>();
                    for(PesquisaParse item: itensOriginais){
                        if(item.toString().toLowerCase().contains(constraint)){
                            founded.add(item);
                        }
                    }
                    result.values = founded;
                    result.count = founded.size();
                }
                return result;
            }
        };
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
