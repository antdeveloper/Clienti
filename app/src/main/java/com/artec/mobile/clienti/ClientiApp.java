package com.artec.mobile.clienti;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.artec.mobile.clienti.addAbono.di.AddAbonoComponent;
import com.artec.mobile.clienti.addAbono.di.AddAbonoModule;
import com.artec.mobile.clienti.addAbono.di.DaggerAddAbonoComponent;
import com.artec.mobile.clienti.addAbono.ui.AddAbonoFragment;
import com.artec.mobile.clienti.addAbono.ui.AddAbonoView;
import com.artec.mobile.clienti.addClient.di.AddClientComponent;
import com.artec.mobile.clienti.addClient.di.AddClientModule;
import com.artec.mobile.clienti.addClient.di.DaggerAddClientComponent;
import com.artec.mobile.clienti.addClient.ui.AddClientFragment;
import com.artec.mobile.clienti.addClient.ui.AddClientView;
import com.artec.mobile.clienti.admonAbono.di.AdmonAbonoComponent;
import com.artec.mobile.clienti.admonAbono.di.AdmonAbonoModule;
import com.artec.mobile.clienti.admonAbono.di.DaggerAdmonAbonoComponent;
import com.artec.mobile.clienti.admonAbono.ui.AdmonAbonoFragment;
import com.artec.mobile.clienti.admonAbono.ui.AdmonAbonoView;
import com.artec.mobile.clienti.clientiInactive.di.ClientiInactiveComponent;
import com.artec.mobile.clienti.clientiInactive.di.ClientiInactiveModule;
import com.artec.mobile.clienti.clientiInactive.di.DaggerClientiInactiveComponent;
import com.artec.mobile.clienti.clientiInactive.ui.ClientiInactiveActivity;
import com.artec.mobile.clienti.clientiInactive.ui.ClientiInactiveView;
import com.artec.mobile.clienti.detalleVentas.di.DaggerDetalleVentaComponent;
import com.artec.mobile.clienti.detalleVentas.ui.adapters.OnAbonoClickListener;
import com.artec.mobile.clienti.domain.di.DomainModule;
import com.artec.mobile.clienti.indicadores.di.DaggerIndicadoresComponent;
import com.artec.mobile.clienti.indicadores.di.IndicadoresComponent;
import com.artec.mobile.clienti.indicadores.di.IndicadoresModule;
import com.artec.mobile.clienti.indicadores.ui.IndicadoresActivity;
import com.artec.mobile.clienti.indicadores.ui.IndicadoresView;
import com.artec.mobile.clienti.libs.di.LibsModule;
import com.artec.mobile.clienti.login.di.DaggerLoginComponent;
import com.artec.mobile.clienti.login.di.LoginComponent;
import com.artec.mobile.clienti.login.di.LoginModule;
import com.artec.mobile.clienti.login.ui.LoginView;
import com.artec.mobile.clienti.main.di.DaggerMainComponent;
import com.artec.mobile.clienti.main.di.MainComponent;
import com.artec.mobile.clienti.main.di.MainModule;
import com.artec.mobile.clienti.main.ui.MainActivity;
import com.artec.mobile.clienti.main.ui.MainView;
import com.artec.mobile.clienti.main.ui.adapters.OnItemClickListener;
import com.artec.mobile.clienti.productos.di.DaggerProductosComponent;
import com.artec.mobile.clienti.signup.di.DaggerSignUpComponent;
import com.artec.mobile.clienti.signup.di.SignUpComponent;
import com.artec.mobile.clienti.signup.di.SignUpModule;
import com.artec.mobile.clienti.signup.ui.SignUpView;
import com.artec.mobile.clienti.detalleVentas.di.DetalleVentaComponent;
import com.artec.mobile.clienti.detalleVentas.di.DetalleVentasModule;
import com.artec.mobile.clienti.ventas.di.DaggerVentasComponent;
import com.artec.mobile.clienti.ventas.di.VentasComponent;
import com.artec.mobile.clienti.ventas.di.VentasModule;
import com.artec.mobile.clienti.detalleVentas.ui.DetalleVentaActivity;
import com.artec.mobile.clienti.ventas.ui.VentasFragment;
import com.artec.mobile.clienti.ventas.ui.VentasView;
import com.artec.mobile.clienti.productos.di.ProductosComponent;
import com.artec.mobile.clienti.productos.di.ProductosModule;
import com.artec.mobile.clienti.productos.ui.ProductosView;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public class ClientiApp extends Application{
    private final static String USER_NAME = "username";
    private final static String EMAIL_KEY = "email";
    private final static String SHARED_PREFERENCES_NAME = "UserPrefs";
    private final static String FIREBASE_URL = "https://clienti.firebaseio.com/";

    private DomainModule domainModule;
    private ClientiAppModule clientiAppModule;

    @Override
    public void onCreate() {
        super.onCreate();
        initFirebase();
        initModules();
    }

    private void initModules() {
        clientiAppModule = new ClientiAppModule(this);
        domainModule = new DomainModule(FIREBASE_URL);
    }

    private void initFirebase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //Firebase.setAndroidContext(this);
    }

    public String getEmailKey() {
        return EMAIL_KEY;
    }

    public String getUserName() {
        return USER_NAME;
    }

    public String getSharedPreferencesName() {
        return SHARED_PREFERENCES_NAME;
    }

    public LoginComponent getLoginComponent(LoginView view, Activity activity){
        domainModule.setActivity(activity);
        return DaggerLoginComponent
                .builder()
                .clientiAppModule(clientiAppModule)
                .domainModule(domainModule)
                //.libsModule(new LibsModule(null))
                .libsModule(new LibsModule())
                .loginModule(new LoginModule(view))
                .build();
    }

    public SignUpComponent getSignUpComponent(SignUpView view){
        return DaggerSignUpComponent
                .builder()
                .clientiAppModule(clientiAppModule)
                .domainModule(domainModule)
                //.libsModule(new LibsModule(null))
                .libsModule(new LibsModule())
                .signUpModule(new SignUpModule(view))
                .build();
    }

    public MainComponent getMainComponent(MainActivity activity, MainView view,
                                          OnItemClickListener onItemClickListener){
        return DaggerMainComponent
                .builder()
                .clientiAppModule(clientiAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(activity))
                .mainModule(new MainModule(view, onItemClickListener))
                .build();
    }

    public ClientiInactiveComponent getClientiInactiveComponent(ClientiInactiveActivity activity,
                                                                ClientiInactiveView view,
                                                                OnItemClickListener onItemClickListener){
        return DaggerClientiInactiveComponent
                .builder()
                .clientiAppModule(clientiAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(activity))
                .clientiInactiveModule(new ClientiInactiveModule(view, onItemClickListener))
                .build();
    }

    public AddClientComponent getAddClientComponent(AddClientFragment dialogFragment, AddClientView view){
        return DaggerAddClientComponent
                .builder()
                .clientiAppModule(clientiAppModule)
                .domainModule(domainModule)
                //.libsModule(new LibsModule(null))
                //.libsModule(new LibsModule())
                .libsModule(new LibsModule(dialogFragment.getActivity()))
                .addClientModule(new AddClientModule(view))
                .build();
    }

    public ProductosComponent getVentasMainComponent(ProductosView view, FragmentManager manager, Fragment[] fragments, String[] titles){
        return DaggerProductosComponent
                .builder()
                .clientiAppModule(clientiAppModule)
                .domainModule(domainModule)
                //.libsModule(new LibsModule(null))
                .libsModule(new LibsModule())
                .productosModule(new ProductosModule(view, titles, fragments, manager))
                .build();
    }

    public VentasComponent getPhotoListComponent(VentasFragment fragment, VentasView view,
                                                 com.artec.mobile.clienti.ventas.ui.adapters.OnItemClickListener onItemClickListener){
        return DaggerVentasComponent
                .builder()
                .clientiAppModule(clientiAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(fragment.getActivity()))
                .ventasModule(new VentasModule(view, onItemClickListener))
                .build();
    }

    public AddAbonoComponent getAddAbonoComponent(AddAbonoFragment dialogFragment, AddAbonoView view){
        return DaggerAddAbonoComponent
                .builder()
                .clientiAppModule(clientiAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(dialogFragment.getActivity()))
                .addAbonoModule(new AddAbonoModule(view))
                .build();
    }

    public DetalleVentaComponent getDetalleAbonoComponent(DetalleVentaActivity activity, OnAbonoClickListener onAbonoClickListener){
        return DaggerDetalleVentaComponent
                .builder()
                .clientiAppModule(clientiAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(activity))
                .detalleVentasModule(new DetalleVentasModule(activity, onAbonoClickListener))
                .build();
    }

    public AdmonAbonoComponent getAdmonContactComponent(AdmonAbonoFragment dialogFragment, AdmonAbonoView view){
        return DaggerAdmonAbonoComponent
                .builder()
                .clientiAppModule(clientiAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(dialogFragment.getActivity()))
                .admonAbonoModule(new AdmonAbonoModule(view))
                .build();
    }

    public IndicadoresComponent getIndicadoresComponent(IndicadoresActivity activity,
                                                            IndicadoresView view){
        return DaggerIndicadoresComponent
                .builder()
                .clientiAppModule(clientiAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(activity))
                .indicadoresModule(new IndicadoresModule(view))
                .build();
    }
}
