package com.junsugi.mobileproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

public class WriteActivity extends MainActivity {

    private LinearLayout linearLayout, buttonLayout;
    private EditText calendar;
    private Button addContentBtn, deleteContentBtn;
    private String today;
    private Vector<Object> vTime1, vTime2, vLogging, add_content_views;

    //EditText 눌렀을 때 달력 셋팅 하는 부분
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            calendar.setText(sdf.format(myCalendar.getTime()));
            today = sdf.format(myCalendar.getTime());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        //벡터 생성해주기
        vTime1 = new Vector<>();
        vTime2 = new Vector<>();
        vLogging = new Vector<>();
        add_content_views = new Vector<>();

        //위젯 가져오기
        linearLayout = findViewById(R.id.rootView);
        calendar = findViewById(R.id.calendar);
        addContentBtn = findViewById(R.id.addContentBtn);
        deleteContentBtn = findViewById(R.id.deleteContentBtn);
        buttonLayout = findViewById(R.id.buttonGroup);

        //추가 버튼 클릭 시 위젯 동적 추가
        addContentBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Vector<Object> temp = new Vector<>();
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View add_content_view = inflater.inflate(R.layout.add_content_view, null);

                //시간 입력하는 위젯 add_content_view.xml 에서 가져오기
                EditText time1 = add_content_view.findViewById(R.id.time1);
                EditText time2 = add_content_view.findViewById(R.id.time2);
                //한 일 적는 위젯 add_content_view.xml 에서 가져오기
                EditText logging = add_content_view.findViewById(R.id.logging);
                //잠깐 편하게 쓰기 위해서 담는 임시 벡터
                temp.add(time1);
                temp.add(time2);
                //영구 저장하는 벡터
                vTime1.add(time1);
                vTime2.add(time2);
                vLogging.add(logging);
                add_content_views.add(add_content_view);

                for(int i = 0; i < temp.size(); i++){
                    final int index = i;
                    ((EditText) temp.get(i)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime.get(Calendar.MINUTE);
                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(WriteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    String state = "AM";
                                    // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                                    if (selectedHour > 12) {
                                        selectedHour -= 12;
                                        state = "PM";
                                    }
                                    // EditText에 출력할 형식 지정
                                    ((EditText) temp.get(index)).setText(state + " " + selectedHour + "시 " + selectedMinute + "분");
                                }
                            }, hour, minute, false);
                            mTimePicker.setTitle("시간 설정");
                            mTimePicker.show();
                        }
                    });
                }

                linearLayout.removeView(buttonLayout);

                linearLayout.addView(add_content_view);
                linearLayout.addView(buttonLayout);
            }
        });
        //최근에 추가한 위젯 삭제하기
        deleteContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add_content_views.size() == 0){
                    Toast.makeText(getApplicationContext(), "삭제할 입력창이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    linearLayout.removeView(buttonLayout);

                    linearLayout.removeView(((View)add_content_views.get(add_content_views.size()-1)));

                    add_content_views.removeElementAt(add_content_views.size()-1);
                    vTime1.removeElementAt(vTime1.size()-1);
                    vTime2.removeElementAt(vTime2.size()-1);
                    vLogging.removeElementAt(vLogging.size()-1);

                    linearLayout.addView(buttonLayout);
                }
            }
        });

        //날짜 입력 EditText 클릭 시 날짜 다이얼로그 띄우기
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(WriteActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    //액션바 커스텀
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar ab = getSupportActionBar();
        CustomActionbar actionbar = new CustomActionbar();
        actionbar.setActionBar(ab, "타임로그 작성하기");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sub_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.confirm:
                FileSystem fileSystem = new FileSystem();
                boolean confirm = checktInputContent();
                if(confirm){
                    boolean check = fileSystem.writeToFile(today, vTime1, vTime2, vLogging, getFilesDir());
                    if(check){
                        Toast.makeText(this, "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "저장에 문제가 발생했습니다. 다시 시도 해주십시오.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.cancel:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("작성 취소하기");
                builder.setMessage("취소 하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
                break;
        }
        return true;
    }

    private boolean checktInputContent() {
        boolean check = true;
        if(calendar.getText().toString().equals("")){
            Toast.makeText(this, "날짜 선택이 안되었습니다.", Toast.LENGTH_SHORT).show();
            calendar.requestFocus();
            check = false;
        } else {
            for(int i = 0; i < vLogging.size(); i++){
                EditText time1 = (EditText)vTime1.get(i);
                EditText time2 = (EditText)vTime2.get(i);
                EditText logging = (EditText)vLogging.get(i);

                if(time1.getText().toString().equals("")){
                    Toast.makeText(this, "시간 선택이 안되었습니다.", Toast.LENGTH_SHORT).show();
                    time1.requestFocus();
                    check = false;
                } else if(time2.getText().toString().equals("")){
                    Toast.makeText(this, "시간 선택이 안되었습니다.", Toast.LENGTH_SHORT).show();
                    time2.requestFocus();
                    check = false;
                } else if(logging.getText().toString().equals("")){
                    Toast.makeText(this, "한 일 작성이 안되었습니다.", Toast.LENGTH_SHORT).show();
                    logging.requestFocus();
                    check = false;
                }
            }
        }
        return check;
    }
}
