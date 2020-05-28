package com.candra.alarmmanager;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */

    // TimePickerFragment diimplementasikan dengan metode TimePickerDialogOnTimeSetListener
public class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    DialogTimeListener timeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null ){
            timeListener = (DialogTimeListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (timeListener != null){
            timeListener = null;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int monute = calendar.get(Calendar.MINUTE);
        boolean formatHour24 = true;
        return new TimePickerDialog(getActivity(),this,hour,monute,formatHour24);

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeListener.onDialogTimeSet(getTag(),hourOfDay,minute);
    }

    public interface DialogTimeListener{
        void onDialogTimeSet(String tag, int hourOfDay,int minute);
    }
}
