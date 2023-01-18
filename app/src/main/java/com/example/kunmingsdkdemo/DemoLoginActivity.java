package com.example.kunmingsdkdemo;

import static common.utils.AppInit.appId;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import common.utils.PersonInfoManager;
import common.utils.ToastUtils;

public class DemoLoginActivity extends AppCompatActivity {
    private EditText userIdEtv;
    private EditText tel;
    private EditText adress;
    private EditText nickNameEtv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_login);
        userIdEtv = findViewById(com.szrm.videodetail.demo.R.id.userId);
        tel = findViewById(com.szrm.videodetail.demo.R.id.tel);
        adress = findViewById(com.szrm.videodetail.demo.R.id.headProfile);
        nickNameEtv = findViewById(com.szrm.videodetail.demo.R.id.nickName);

        //登录
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonInfoManager.getInstance().setRequestUserId("123879419");
                PersonInfoManager.getInstance().setRequestUserHead("测试");
                PersonInfoManager.getInstance().setRequestUserNickName("loginA");
                PersonInfoManager.getInstance().setRequestUserPhone("123131231");
                ToastUtils.showShort("登录成功");
                finish();
            }
        });

    }
}