package com.junsugi.mobileproject;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class StatisticsActivity extends AppCompatActivity {

    private PieChart chart1;
    private BarChart chart2;

    private File[] files;
    private Vector<String> times, loggings;
    private String[] colors= {"#CDA67F", "#FF0000", "#00BFFF", "#FF00FF", "#808000", "#f784a8"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        times = new Vector<>();
        loggings = new Vector<>();

        chart1 = findViewById(R.id.tab1_chart_1);
        chart2 = findViewById(R.id.tab1_chart_2);
        
        getContentData();
    }

    private void getContentData() {
        chart1.clearChart();
        chart2.clearChart();

        files = new File(getFilesDir().toString()).listFiles();
        //가장 최근 파일 가져오기
        File file = files[files.length-1];
        int timeResult1 = 0;
        int timeResult2 = 0;
        String contentResult = "";
        int index = 0;
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()) {
                String time1 = scanner.nextLine();
                String time2 = scanner.nextLine();
                contentResult = scanner.nextLine();

                if(time1.contains("PM")) {
                    String[] result = time1.split("시");
                    String rTime1 = result[0].replace("PM ", "");
                    String rTime2 = result[1].replace(" ", "");
                    rTime2 = rTime2.replace("분", "");
                    int hour = Integer.parseInt(rTime1);
                    hour += 12;
                    int minute = Integer.parseInt(rTime2);
                    timeResult1 = (hour*60) + minute;
                }
                if (time2.contains("PM")) {
                    String[] result = time2.split("시");
                    String rTime1 = result[0].replace("PM ", "");
                    String rTime2 = result[1].replace(" ", "");
                    rTime2 = rTime2.replace("분", "");
                    int hour = Integer.parseInt(rTime1);
                    hour += 12;
                    int minute = Integer.parseInt(rTime2);
                    timeResult2 = (hour*60) + minute;
                }

                if(time1.contains("AM")){
                    String[] result = time1.split("시");
                    String rTime1 = result[0].replace("AM ", "");
                    String rTime2 = result[1].replace(" ", "");
                    rTime2 = rTime2.replace("분", "");
                    int hour = Integer.parseInt(rTime1);
                    int minute = Integer.parseInt(rTime2);
                    timeResult1 = (hour*60) + minute;
                }

                if(time2.contains("AM")) {
                    String[] result = time2.split("시");
                    String rTime1 = result[0].replace("AM ", "");
                    String rTime2 = result[1].replace(" ", "");
                    rTime2 = rTime2.replace("분", "");
                    int hour = Integer.parseInt(rTime1);
                    int minute = Integer.parseInt(rTime2);
                    timeResult2 = (hour*60) + minute;
                }
                int result = timeResult2 - timeResult1;
                Log.d("test", String.valueOf(result));
                chart1.addPieSlice(new PieModel(contentResult, result, Color.parseColor(colors[index])));
                chart2.addBar(new BarModel(contentResult, result, Color.parseColor(colors[index])));
                index++;
            }
            chart1.startAnimation();
            chart2.startAnimation();


        } catch (FileNotFoundException e){

        }
    }

    //액션바 커스텀
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar ab = getSupportActionBar();
        CustomActionbar actionbar = new CustomActionbar();
        actionbar.setActionBar(ab, "최근 통계 보기");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
