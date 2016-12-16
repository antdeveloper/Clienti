package com.artec.mobile.clienti.main.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.domain.Util;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.libs.base.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ANICOLAS on 01/07/2016.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    Util util;
    private List<Client> clientList;
    private ImageLoader imageLoader;
    //Arreglo auxiliar para los indicadores
    private static final List<String> emails = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    private Context context;

    public MainAdapter(Util util, List<Client> clientList, ImageLoader imageLoader,
                       OnItemClickListener onItemClickListener) {
        this.util = util;
        this.clientList = clientList;
        this.imageLoader = imageLoader;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_client, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Client client = clientList.get(position);
        holder.setClickListener(client, onItemClickListener);

        String email = client.getEmail();
        double deudaTotal = client.calcDeudaTotal();
        boolean enOrden = deudaTotal <= 0;
        holder.txtUser.setText(client.getUsername());
        holder.txtEmail.setText(email);
        if (client.getProductos() != null) {
            holder.containerAdeudo.setVisibility(View.VISIBLE);
            holder.txtStatus.setText(String.format(Locale.ROOT, context.getString(
                    R.string.main_label_adeudo), deudaTotal));
                    /*"Adeudo:\n" + String.format(Locale.ROOT,
                    "%,.2f", deudaTotal));*/
            holder.imgStatus.setColorFilter(ContextCompat.getColor(context,
                    enOrden ? R.color.green_500 : R.color.red_500));
        }else {
            holder.containerAdeudo.setVisibility(View.GONE);
        }

        if (client.isLocalUser()) {
            holder.imgAvatar.setImageResource(R.drawable.ic_account);
        } else {
            imageLoader.load(holder.imgAvatar, util.getAvatarUrl(email));
        }
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public void add(Client client) {
        if (getIndexByEmail(client) == -1){
            clientList.add(client);
            addEmail(client.getEmail());
            notifyDataSetChanged();
        }else{
            update(client);
        }
    }

    public void update(Client client) {
        int index = getIndexByEmail(client);
        client.setProductos(clientList.get(index).getProductos());
        clientList.set(index, client);
        notifyDataSetChanged();
    }

    public void remove(Client client) {
        int index = getIndexByEmail(client);
        clientList.remove(index);
        deleteEmail(index);
        notifyDataSetChanged();
    }

    private int getIndexByEmail(Client client) {
        for (int i = 0; i< clientList.size(); i++){
            if (clientList.get(i).getEmail().equals(client.getEmail())){
                return i;
            }
        }
        return -1;
    }

    public List<Client> getClientList(){
        return clientList;
    }

    public double getDeudasTotal(){
        double adeudo = 0;
        for (Client client : clientList){
            adeudo += client.calcDeudaTotal();
        }
        return adeudo;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imgAvatar)
        CircleImageView imgAvatar;
        //AppCompatImageView imgAvatar;
        @Bind(R.id.txtUser)
        TextView txtUser;
        @Bind(R.id.txtEmail)
        TextView txtEmail;
        @Bind(R.id.txtStatus)
        TextView txtStatus;
        @Bind(R.id.imgStatus)
        ImageView imgStatus;
        @Bind(R.id.containerAdeudo)
        LinearLayout containerAdeudo;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }

        private void setClickListener(final Client client, final OnItemClickListener listener){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(client);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(client);
                    return true;
                }
            });
        }
    }

    /******
     * Metodos auxiliares para indicadores
     * *****/

    private void addEmail(String email){
        emails.add(email);
    }

    private void deleteEmail(int index){
        emails.remove(index);
    }

    public static List<String> getEmails(){
        return emails;
    }
}
