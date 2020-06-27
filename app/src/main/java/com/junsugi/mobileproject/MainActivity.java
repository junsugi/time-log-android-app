package com.junsugi.mobileproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mainView;
    private Vector<Object> buttons;

    private String today, fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //동적으로 생성한 버튼 저장하는 벡터
        buttons = new Vector<>();
        //메인화면 리니어 레이아웃을 가져온다.
        mainView = findViewById(R.id.mainView);
        //오늘 날짜를 가져온다.
        Date currentTime = Calendar.getInstance().getTime();
        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentTime);

        //저장된 파일을 읽어온다.
        FileSystem fileSystem = new FileSystem();
        File[] files = fileSystem.readAllToFile(getFilesDir());
        //저장된 파일을 가지고 버튼을 생성한다.
        createListButton(files);
    }


    private void createListButton(File[] files) {
        final File[] fFiles = files;
        for(int i = 0; i < files.length; i++){
            final int index = i;
            final Button button = new Button(this);
            button.setText(files[i].getName().toString());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), fFiles[index].getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent (getApplicationContext(), DetailActivity.class);
                    intent.putExtra("files", fFiles[index]);
                    startActivity(intent);
                }
            });
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    fileName = button.getText().toString();
                    return false;
                }
            });
            registerForContextMenu(button);
            mainView.addView(button);
            buttons.add(button);
        }
    }

    //액션바 커스텀
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar ab = getSupportActionBar();
        CustomActionbar actionbar = new CustomActionbar();
        actionbar.setActionBar(ab, "메인화면");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    //메뉴 버튼에 따라 다른 기능
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu1:
                Intent intent3 = new Intent(this, StatisticsActivity.class);
                startActivity(intent3);
                break;
            case R.id.write:
                Intent intent = new Intent(this, WriteActivity.class);
                startActivity(intent);
                break;
            case R.id.modify:
                Intent intent2 = new Intent(this, ModifyActivity.class);
                intent2.putExtra("today", today);
                startActivity(intent2);
                break;
            case android.R.id.home:
                Toast.makeText(this, "메인화면 입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("종료하기");
                builder.setMessage("종료하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
    }

    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.delete:
                File file = new File(getFilesDir()+"/"+this.fileName);
                if(file.exists()){
                    file.delete();
                    recreate();
                    Toast.makeText(getApplicationContext(), "새로고침 되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }
}
