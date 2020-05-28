package com.candra.alarmmanager;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DialogDateListener mlistener;

    // Hanya sekali dipanggil dalam fragment dan berfungsi untuk mengkaitkan dengan activity pemanggil
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null){
            mlistener = (DialogDateListener) context;
        }
    }

    // Method ini hanya dipanggil sebelum fragmen tidak lagi dikaitkan dengan
    // activity pemanggil
    @Override
    public void onDetach() {
        super.onDetach();
        if (mlistener != null){
            mlistener = null;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        return new DatePickerDialog(getActivity(),this,year,month,date);
    }

    @Override
    // Fungsi Dateset akan dipanggil ketika memilih tanggal yang diiginkan
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mlistener.onDialogDataSet(getTag(),year,month,dayOfMonth);
    }
    // Setelah di pilih tanggal bulan dan tahun, maka akan dikirimkan ke main activity menggunakan
    // Method DialogDateListener
    public interface DialogDateListener{
        void onDialogDataSet(String tag, int year, int month, int dayOfMonth);
    }
}
