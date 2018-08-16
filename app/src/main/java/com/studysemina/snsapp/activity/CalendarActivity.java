package com.studysemina.snsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;
import com.studysemina.snsapp.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{

    private CalendarPickerView calendarView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        calendarView = (CalendarPickerView)findViewById(R.id.calendar_view);

        Calendar calendar = Calendar.getInstance();
        int minMonthInt = calendar.getActualMinimum(Calendar.MONTH);
        calendar.set(Calendar.MONTH,minMonthInt);
        int minDateInt = calendar.getActualMinimum(Calendar.DATE);
        calendar.set(Calendar.DATE,minDateInt);
        Date minDate = calendar.getTime();

        int maxMonthInt = calendar.getActualMaximum(Calendar.MONTH);
        calendar.set(Calendar.MONTH,maxMonthInt);
        int maxDateInt = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DATE,maxDateInt);
        calendar.add(Calendar.DATE,1);

        Date maxDate = calendar.getTime();

        calendarView.init(minDate, maxDate)
                .inMode(CalendarPickerView.SelectionMode.RANGE);
        calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String str = String.format(Locale.getDefault(),"Selected : %d %02d %02d",
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH)+1,
                        calendar.get(Calendar.DATE));
                Toast.makeText(CalendarActivity.this,str,Toast.LENGTH_SHORT).show();
                button.setEnabled(calendarView.getSelectedDates().size() > 1);
//                dateFinish();
            }

            @Override
            public void onDateUnselected(Date date) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String str = String.format(Locale.getDefault(),"Deselected : %d %02d %02d",
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH)+1,
                        calendar.get(Calendar.DATE));
//                Toast.makeText(CalendarActivity.this,str,Toast.LENGTH_SHORT).show();
                button.setEnabled(calendarView.getSelectedDates().size() > 1);
            }
        });
    }

    private void dateFinish(){
        if(calendarView.getSelectedDates().size() > 1){
            Toast.makeText(CalendarActivity.this,"FINISH",Toast.LENGTH_SHORT).show();
            //FINISH
        }
    }

    @Override
    public void onClick(android.view.View v) {
        if(calendarView.getSelectedDates().size() == 2){
            Toast.makeText(CalendarActivity.this,"FINISH",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(CalendarActivity.this,"날짜 2개를 입력해주세요.",Toast.LENGTH_SHORT).show();
        }
    }

}
