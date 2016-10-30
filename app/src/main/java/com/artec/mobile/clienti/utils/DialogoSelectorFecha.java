package com.artec.mobile.clienti.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by Alhayn21 on 23/06/2015.
 */
public class DialogoSelectorFecha extends DialogFragment
{
    public static final String FECHA = "fecha";

    private DatePickerDialog.OnDateSetListener escuchador;

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener escuchador)
    {
        this.escuchador = escuchador;
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Calendar calendario = Calendar.getInstance();
        Bundle args = this.getArguments();
        if(args != null)
        {
            long fecha = args.getLong(FECHA);
            calendario.setTimeInMillis(fecha);
        }

        int anyo = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), escuchador, anyo, mes, dia);
    }
}
