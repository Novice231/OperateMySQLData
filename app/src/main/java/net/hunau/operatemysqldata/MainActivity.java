package net.hunau.operatemysqldata;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import net.hunau.entity.User;

import net.hunau.DBLink.DBLink;

public class MainActivity extends AppCompatActivity {
    private final String REMOTE_IP = "110.53.162.165";
    private final String URL = "jdbc:mysql://" + REMOTE_IP + "/test?";
    private final String USER = "root";
    private final String PASSWORD = "sx123456AaBb";

    private Connection conn;
    private Button onInsert;
    private Button onDelete;
    private Button onUpdate;
    private Button onQuery;
    protected TextView display;

    private EditText idText;
    private EditText nameText;
    private EditText pwdText;

    private RadioButton rb1 ;
    private RadioButton rb2 ;

    private RadioButton rb3 ;
    private RadioButton rb4 ;


    DBLink util = new DBLink();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onInsert = (Button) findViewById(R.id.onInsert);
        onDelete = (Button) findViewById(R.id.onDelete);
        onUpdate = (Button) findViewById(R.id.onUpdate);
        onQuery = (Button) findViewById(R.id.onQuery);

        display = (TextView) findViewById(R.id.display);

        idText= (EditText) findViewById(R.id.id);
        nameText = (EditText) findViewById(R.id.name);
        pwdText = (EditText) findViewById(R.id.pwd);

        rb1 = (RadioButton) findViewById(R.id.RadioButton01);
        rb2 = (RadioButton) findViewById(R.id.RadioButton02);

        rb3 = (RadioButton) findViewById(R.id.RadioButton03);
        rb4 = (RadioButton) findViewById(R.id.RadioButton04);


        onConn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                conn = null;
            } finally {
                conn = null;
            }
        }
    }

    public void onConn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn = util.openConnection(URL, USER, PASSWORD);
                Log.i("onConn", "onConn");
            }
        }).start();
    }

    public void onInsert(View view) {
        final String name = nameText.getText().toString();
        final String pwd = pwdText.getText().toString();
        final String sex;
        final String isused;
        if (rb1.isChecked()) {
            sex=rb1.getText().toString();
        } else {
            sex=rb2.getText().toString();
        }
        if (rb3.isChecked()) {
            isused = "1";
        } else {
            isused = "0";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql = "insert into tb_user(name,pwd,sexy,isused) values('" + name + "','" + pwd + "','" + sex + "'," + isused + ");";
                System.out.println(sql);
                util.execSQL(conn, sql);
                Log.i("onInsert", "onInsert");
            }
        }).start();
        display.setText("插入记录成功！");
    }

    public void onDelete(View view) {
        final String id = idText.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql = "delete from tb_user where id=" + id;
                util.execSQL(conn, sql);
                Log.i("onDelete", "onDelete");
            }
        }).start();
        display.setText("删除记录成功！");
    }

    public void onUpdate(View view) {
        final String id = idText.getText().toString();
        final String name = nameText.getText().toString();
        final String pwd = pwdText.getText().toString();
        final String sex;
        final String isused;
        if (rb1.isChecked()) {
            sex=rb1.getText().toString();
        } else {
            sex=rb2.getText().toString();
        }
        if (rb3.isChecked()) {
            isused="1";
        } else {
            isused="0";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql = "update tb_user set name='" + name + "',pwd = '" + pwd + "',sexy = '" + sex + "',isused =" + isused + " where id = " + id;
                System.out.println(sql);
                util.execSQL(conn, sql);
                Log.i("onUpdate", "onUpdate");
            }
        }).start();
        display.setText("更新记录成功！");
    }

    public void onQuery(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = "";
                List<User> list = util.query(conn, "select * from tb_user");
                Message msg = new Message();
                Log.i("onQuery", "onQuery");
                if (list == null) {
                    msg.what = 0;
                    msg.obj = "查询结果，空空如也";
                    //非UI线程不要试着去操作界面
                } else {
                    String ss = "";
                    for (int i = 0; i < list.size(); i++) {
                        ss += list.get(i).toString();
                    }
                    msg.what = 1;
                    msg.obj = ss;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void findById(View view) {
        final String id = idText.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = "";
                List<User> list = util.query(conn, "select * from tb_user where id = " + id);
                Message msg = new Message();
                Log.i("onQuery", "onQuery");
                if (list == null) {
                    msg.what = 0;
                    msg.obj = "查询结果，空空如也";
                    //非UI线程不要试着去操作界面
                } else {
                    String ss = "";
                    for (int i = 0; i < list.size(); i++) {
                        ss += list.get(i).toString();
                    }
                    msg.what = 1;
                    msg.obj = ss;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            ((TextView) findViewById(R.id.display)).setText((String) message.obj);
            String str = "查询不存在";
            if (message.what == 1) str = "查询成功";
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            return false;
        }
    });
}
