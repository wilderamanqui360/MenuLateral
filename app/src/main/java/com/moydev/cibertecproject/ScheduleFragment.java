package com.moydev.cibertecproject;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.moydev.cibertecproject.db.Cities;
import com.moydev.cibertecproject.db.Matches;
import com.moydev.cibertecproject.db.Teams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {


    public static Matches staticMatches = new Matches();
    EditText txtDate,txtHour;
    private int mYear,mMonth,mDay;
    Spinner spCity, spLocal, spVisitor;
    Button btnSave, btnCancel;
    private BroadcastReceiver broadcastReceiver;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_schedule, container, false);
        spCity = (Spinner)V.findViewById(R.id.spCity);
        spLocal = (Spinner)V.findViewById(R.id.spLocal);
        spVisitor = (Spinner)V.findViewById(R.id.spVisitor);
        txtDate = (EditText)V.findViewById(R.id.txtDate);
        txtHour = (EditText)V.findViewById(R.id.txtHour);
        btnSave = (Button)V.findViewById(R.id.btnSave);
        btnCancel = (Button)V.findViewById(R.id.btnCancel);

        createMockupData();
        refreshSpCiudad();
        refreshSpTeams();
        Configuracion();

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                mYear = currentDate.get(Calendar.YEAR);
                mMonth = currentDate.get(Calendar.MONTH);
                mDay = currentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updateDisplay();
                    }
                };

                DatePickerDialog d = new DatePickerDialog(getActivity(), R.style.AppTheme, mDateSetListener, mYear, mMonth, mDay);
                d.show();
            }
        });

        txtHour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txtHour.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Seleccione la hora");
                mTimePicker.show();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matches matches = new Matches(txtDate.getText().toString(),txtHour.getText().toString(),spCity.getSelectedItem().toString(),spLocal.getSelectedItem().toString(),spVisitor.getSelectedItem().toString());
                matches.save();
                Toast.makeText(getActivity(), "Partido guardado.", Toast.LENGTH_LONG).show();
                clean();
                staticMatches = matches;
                //Setea una alarma fija
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+20000,pendingIntent);
                //Setea una alarma que se repetira cada cierto tiempo
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),5000,pendingIntent );
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    MatchListFragment matchListFragment= new MatchListFragment();
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter, R.anim.exit)
                            .replace(getId(), matchListFragment, null)
                            .addToBackStack(null)
                            .commit();

            }
        });

        return V;
    }

    public void updateDisplay() {
        GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        txtDate.setText(sdf.format(c.getTime()));
    }

    public void refreshSpCiudad(){

        Cities cities = new Cities();

        List<Cities> allCities = cities.listAll(Cities.class);
        List<String> allStringCities = new ArrayList<>();

        for(Cities c : allCities){
            allStringCities.add((c.getName()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, allStringCities);
        spCity.setAdapter(adapter);

    }

    public void refreshSpTeams(){

        Teams teams = new Teams();

        List<Teams> allTeams = teams.listAll(Teams.class);
        List<String> allStringTeams = new ArrayList<>();

        for(Teams c : allTeams){
            allStringTeams.add((c.getName()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, allStringTeams);
        spLocal.setAdapter(adapter);
        spVisitor.setAdapter(adapter);
    }

    public void createMockupData(){

        Cities c = new Cities();
        List<Cities> lCities = c.listAll(Cities.class);
        String[] arrayCities = {"Manaus","Fortaleza","Natal"};

        if(lCities.size() == 0){
            for(int i = 0; i < arrayCities.length; i++){
                Cities city = new Cities(arrayCities[i]);
                city.save();
            }
        }

    }

    public void generarNotificacion(Matches matches) {
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent= new Intent(getActivity(),NotificationActivity.class);
        intent.putExtra("local", matches.getLocal());
        intent.putExtra("visitor", matches.getVisitor());
        PendingIntent pendingIntent= PendingIntent.getActivity(getActivity(),0,intent,0);
        Notification notification= new Notification.Builder(getActivity())
                .setContentTitle(matches.getLocal() + " vs " + matches.getVisitor())
                .setSmallIcon(R.drawable.vs)
                .setContentText("Fecha: " + matches.getFecha() + " Hora: " + matches.getHora())
                .setContentIntent(pendingIntent)
//                .addAction(R.drawable.ic_action_cloud,"Call",pendingIntent)
                .build();


        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0,notification);

    }

    public void clean(){
        txtDate.setText("");
        txtHour.setText("");
        spCity.setSelection(0,true);
        spLocal.setSelection(0,true);
        spVisitor.setSelection(0,true);
    }

    private void Configuracion(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                generarNotificacion(staticMatches);
            }
        };

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("Algun_evento"));
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent("Algun_evento"),0);
        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

    }

    @Override
    public void onDestroy() {
        alarmManager.cancel(pendingIntent);
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    public void stopAlarmManager()
    {
        if(alarmManager != null)
            alarmManager.cancel(pendingIntent);
    }

}
