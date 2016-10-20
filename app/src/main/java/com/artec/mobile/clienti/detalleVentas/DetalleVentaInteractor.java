package com.artec.mobile.clienti.detalleVentas;

import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 18/10/2016.
 */

public interface DetalleVentaInteractor {
    void execute(Producto producto, String path, String clientEmail);
}
