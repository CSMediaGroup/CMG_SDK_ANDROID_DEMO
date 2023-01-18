package com.example.kunmingsdkdemo;

import static ui.activity.WebActivity.LOGIN_REQUEST_CODE;

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
import common.model.SdkUserInfo;
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
    private TextView clearLoginInfo;
    private TextView toLoginPage;
    private RecyclerView recyclerview;
    private List<SZContentModel.DataDTO.ContentsDTO> contentsDTOS = new ArrayList<>();
    private RvAdatper adatper;
    private ThirdUserInfo thirdUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 需要app首页初始化接口以便一进app就拿到你那边的登录信息
         * 在你登录/注销登录的地方也要实现接口来同步传递登录信息
         *
         * 获取用户信息
         * 分享信息
         * 跳转登陆页面
         */

        SdkInteractiveParam.getInstance().setSdkCallBack(new SdkParamCallBack() {
            @Override
            public ThirdUserInfo setThirdUserInfo() {
                ThirdUserInfo thirdUserInfo = new ThirdUserInfo();
                thirdUserInfo.setUserId(PersonInfoManager.getInstance().getRequestUserId());
                thirdUserInfo.setNickName(PersonInfoManager.getInstance().getRequestUserNickName());
                thirdUserInfo.setPhoneNum(PersonInfoManager.getInstance().getRequestUserPhone());
                thirdUserInfo.setHeadImageUrl(PersonInfoManager.getInstance().getRequestUserHead());
                return thirdUserInfo;
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
                Log.e("toLogin", "toLogin");
                //这里是你跳转你的登录页面 去登录
                Intent intent = new Intent(MainActivity.this, DemoLoginActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 清空用户信息
         */
        clearLoginInfo = findViewById(R.id.clear_login_info);
        clearLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonInfoManager.getInstance().clearThirdUserToken();
            }
        });
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

    }

    private void initRecyclerView() {
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adatper = new RvAdatper(this, R.layout.rv_item_layout, contentsDTOS);
        recyclerview.setAdapter(adatper);
    }
}