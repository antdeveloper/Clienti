package com.artec.mobile.clienti.clientiInactive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.domain.Util;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.libs.base.ImageLoader;
import com.artec.mobile.clienti.main.ui.adapters.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ANICOLAS on 15/11/2016.
 */
public class ClientiInactiveAdapter extends RecyclerView.Adapter<ClientiInactiveAdapter.ViewHolder> {
    Util util;
    private List<Client> clientList;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;

    private Context context;

    public ClientiInactiveAdapter(Util util, List<Client> clientList, ImageLoader imageLoader,
                                  OnItemClickListener onItemClickListener) {
        this.util = util;
        this.clientList = clientList;
        this.imageLoader = imageLoader;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_client_inactive, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Client client = clientList.get(position);
        holder.setClickListener(client, onItemClickListener);

        String email = client.getEmail();
        holder.txtUser.setText(client.getUsername());
        holder.txtEmail.setText(email);

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

    public void setData(List<Client> clients){
        clientList.clear();
        for (Client client : clients){
            clientList.add(client);
        }
        notifyDataSetChanged();
    }

    public void remove(String email) {
        int index = getIndexByEmail(email);
        if (index != -1) {
            clientList.remove(index);
        }
        notifyDataSetChanged();
    }

    private int getIndexByEmail(String email) {
        for (int i = 0; i< clientList.size(); i++){
            if (clientList.get(i).getEmail().equals(email)){
                return i;
            }
        }
        return -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imgAvatar)
        CircleImageView imgAvatar;
        @Bind(R.id.txtUser)
        TextView txtUser;
        @Bind(R.id.txtEmail)
        TextView txtEmail;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }

        private void setClickListener(final Client client, final OnItemClickListener listener) {
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
}
