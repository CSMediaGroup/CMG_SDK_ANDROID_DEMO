package com.example.kunmingsdkdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import common.callback.SdkInteractiveParam;
import common.callback.SdkParamCallBack;
import common.model.ShareInfo;
import common.model.ThirdUserInfo;
import common.utils.PersonInfoManager;
import common.utils.ToastUtils;
import event.SzrmRecommend;
import model.bean.SZContentModel;
import ui.activity.LoginActivity;
import ui.activity.WebActivity;

public class MainActivity extends AppCompatActivity {
    private TextView others_home_page;
    private TextView getListData;
    private TextView getLoadMoreListData;
    private RecyclerView recyclerview;
    private List<SZContentModel.DataDTO.ContentsDTO> contentsDTOS = new ArrayList<>();
    private RvAdatper adatper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 带UI界面的SDK首页
         */
        others_home_page = findViewById(R.id.others_home_page);
        others_home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                startActivity(intent);
            }
        });
        initRecyclerView();

        getListData = findViewById(R.id.getListData);
        getListData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 获取推荐列表接口调用
                 */
                SzrmRecommend.getInstance().requestContentList("10");
            }
        });

        /**
         * 获取推荐列表接口回调
         */
        SzrmRecommend.getInstance().contentsEvent.observe(this, new Observer<List<SZContentModel.DataDTO.ContentsDTO>>() {
            @Override
            public void onChanged(List<SZContentModel.DataDTO.ContentsDTO> contents) {
                contentsDTOS = contents;
                adatper.setNewData(contentsDTOS);
            }
        });


        getLoadMoreListData = findViewById(R.id.getLoadMoreListData);
        getLoadMoreListData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contentsDTOS.isEmpty()) {
                    ToastUtils.showShort("请先获取推荐列表数据");
                    return;
                }
                /**
                 * 调用获取推荐列表更多数据接口
                 */
                SzrmRecommend.getInstance().requestMoreContentList(contentsDTOS.get(contentsDTOS.size() - 1), "10");
            }
        });

        /**
         * 获取推荐列表更多数据回调
         */
        SzrmRecommend.getInstance().loadMoreContentEvent.observe(this, new Observer<List<SZContentModel.DataDTO.ContentsDTO>>() {
            @Override
            public void onChanged(List<SZContentModel.DataDTO.ContentsDTO> contents) {
                contentsDTOS.addAll(contents);
                adatper.setNewData(contentsDTOS);
            }
        });




        /**
         * 获取用户信息
         * 分享信息
         * 跳转登陆页面
         */
        SdkInteractiveParam.getInstance().setSdkCallBack(new SdkParamCallBack() {
            @Override
            public ThirdUserInfo setThirdUserInfo() {
                return null;
            }

            @Override
            public void shared(ShareInfo shareInfo) {
                Log.e("share", JSON.toJSONString(shareInfo));
                if (null != shareInfo) {
                    ToastUtils.showShort("分享成功");
                }
            }

            @Override
            public void toLogin() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initRecyclerView() {
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adatper = new RvAdatper(this,R.layout.rv_item_layout, contentsDTOS);
        recyclerview.setAdapter(adatper);
    }
}