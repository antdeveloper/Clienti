package com.artec.mobile.clienti.addClient;

import com.artec.mobile.clienti.addClient.events.AddClientEvent;
import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.User;
import com.artec.mobile.clienti.libs.Constants;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AddClientRepositoryImpl implements AddClientRepository {
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;

    public AddClientRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
    }

    @Override
    public void addClient(final String email, final String username, String customUsername) {
        if (!customUsername.isEmpty()) {
            registerNewUser(email, customUsername);
        }

        String preKey = email.replace("_", "__");
        final String key = preKey.replace(".", "_");
        //final String key = email.replace(".", "_");
        DatabaseReference userReference = firebaseAPI.getUserReference(email);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    Client client = new Client();
                    DatabaseReference myContactReference = firebaseAPI.getMyClientsReference();
                    client.setEmail(email);
                    client.setUsername(user.getUsername());
                    client.setPartner(false);
                    //client.setDown(false);
                    client.setEstatus(Constants.ESTATUS_ACTIVO);
                    myContactReference.child(key).setValue(client);

                    String currentUserKey = firebaseAPI.getAuthUserEmail();
                    String preKey = currentUserKey.replace("_", "__");
                    currentUserKey = preKey.replace(".", "_");
                    //currentUserKey = currentUserKey.replace(".", "_");

                    Client iClient = new Client();

                    iClient.setEmail(firebaseAPI.getAuthUserEmail());
                    iClient.setUsername(username);
                    DatabaseReference reverseContactReference = firebaseAPI.getClientsReference(email);
                    reverseContactReference.child(currentUserKey).setValue(iClient);

                    postSuccess();
                }else {
                    postError();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                postError();
            }
        });
    }

    private void registerNewUser(String email, String username){
        DatabaseReference userReference = firebaseAPI.getUserReference(email);
        if (email != null){
            User currentUser = new User();
            currentUser.setEmail(email);
            currentUser.setUsername(username);
            userReference.setValue(currentUser);
        }
    }

    private void postSuccess() {
        post(false);
    }

    private void postError(){
        post(true);
    }

    private void post(boolean error) {
        AddClientEvent event = new AddClientEvent();
        event.setError(error);
        eventBus.post(event);
    }
}
