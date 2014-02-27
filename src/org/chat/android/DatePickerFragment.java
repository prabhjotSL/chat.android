package org.chat.android;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.chat.android.models.VaccineRecorded;
import org.chat.android.models.Visit;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	int visitId = 0;
	int clientId = 0;
	public Button activity_date_selected;

	public DatePickerFragment(View v) {
		activity_date_selected = (Button)v;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle b = this.getArguments();
        visitId = b.getInt("visitId");
        clientId = b.getInt("clientId");
        
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
	
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		int vaccineId = (Integer) activity_date_selected.getTag();
		Date d = new Date(year, month, day);
		VaccineRecorded vr = new VaccineRecorded(vaccineId, clientId, visitId, d);
    	
    	Dao<VaccineRecorded, Integer> vrDao;
    	DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
    	try {
    		vrDao = dbHelper.getVaccineRecordedDao();
    		vrDao.create(vr);
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} 
    	
		activity_date_selected.setText(String.valueOf(year) + "/" + String.valueOf(month+1) + "/" + String.valueOf(day));
	}
}