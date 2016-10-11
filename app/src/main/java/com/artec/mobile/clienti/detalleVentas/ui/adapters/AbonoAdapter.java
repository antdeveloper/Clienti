package com.artec.mobile.clienti.detalleVentas.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.entities.Abono;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ANICOLAS on 10/10/2016.
 */
public class AbonoAdapter extends RecyclerView.Adapter<AbonoAdapter.ViewHolder>{
    private Context context;
    private List<Abono> abonoList;
    private OnAbonoClickListener onItemClickListener;

    public AbonoAdapter(Context context, OnAbonoClickListener onItemClickListener) {
        this.context = context;
        this.abonoList = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public AbonoAdapter(List<Abono> abonoList, OnAbonoClickListener onItemClickListener) {
        this.abonoList = abonoList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_abono, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Abono element = abonoList.get(position);
        String strTip = context.getString(R.string.ventas_detalle_label_abono, element.getValor());
        String strFecha = element.getDateFormatted();

        holder.setOnItemClickListener(element, onItemClickListener);

        holder.txtContent.setText(strTip);
        holder.txtFecha.setText(strFecha);
    }

    @Override
    public int getItemCount() {
        return abonoList.size();
    }


    public void add(Abono abono) {// FIXME: 10/10/2016
        abonoList.add(0, abono);
        notifyDataSetChanged();
    }

    public void update(Abono abono){// FIXME: 10/10/2016
        int index = getIndexById(abono);
        abonoList.set(index, abono);
        notifyDataSetChanged();
    }

    public void remove(Abono abono) {// FIXME: 10/10/2016
        int index = getIndexById(abono);
        abonoList.remove(index);
        notifyDataSetChanged();
    }

    private int getIndexById(Abono abono) {// FIXME: 10/10/2016
        for (int i = 0; i< abonoList.size(); i++){
            if (abonoList.get(i).getId().equals(abono.getId())){
                return i;
            }
        }
        return -1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.txtContent)
        TextView txtContent;
        @Bind(R.id.txtFecha)
        TextView txtFecha;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setOnItemClickListener(final Abono element, final OnAbonoClickListener onAbonoClickListener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAbonoClickListener.OnItemLongClick(element);
                }
            });
        }
    }
}
