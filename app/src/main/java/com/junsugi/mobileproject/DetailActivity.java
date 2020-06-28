package com.junsugi.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DetailActivity extends AppCompatActivity {

    private LinearLayout detailView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
         
        detailView = findViewById(R.id.detail_view);

        Intent intent = getIntent();
        Object tempObj = intent.getSerializableExtra("files");

        File getFile = (File)tempObj;
        getContentToFile(getFile);
    }

    private void getContentToFile(File file) {
        try{
           Scanner scanner = new Scanner(file);

            while(scanner.hasNext()){
                //추가할 뷰 만들어서 가져오기
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View add_content_show = inflater.inflate(R.layout.add_content_show, null);

                TextView text_time1 = add_content_show.findViewById(R.id.text_time1);
                TextView text_time2 = add_content_show.findViewById(R.id.text_time2);
                TextView text_logging = add_content_show.findViewById(R.id.text_logging);

                text_time1.setText(scanner.nextLine());
                text_time2.setText(scanner.nextLine());
                text_logging.setText(scanner.nextLine());

                detailView.addView(add_content_show);
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "없는 파일 입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //액션바 커스텀
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar ab = getSupportActionBar();
        CustomActionbar actionbar = new CustomActionbar();
        actionbar.setActionBar(ab, "타임기록 보기");

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
