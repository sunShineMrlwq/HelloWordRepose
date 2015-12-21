package com.example.lwq.damaiclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.activity.UserModule.LoginActivity;
import com.example.lwq.damaiclient.app.BaseActivity;
import com.example.lwq.damaiclient.constant.ConstantFinal;
import com.example.lwq.damaiclient.fragment.FileFragment;
import com.example.lwq.damaiclient.fragment.MineFragment;
import com.example.lwq.damaiclient.fragment.PerformFragment;
import com.example.lwq.damaiclient.fragment.RecommendFragment;
import com.example.lwq.damaiclient.utils.SharedPreferenceUtil;

/**
 * @author lwq
 *         主界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ImageView[] imageViews = new ImageView[4];
    private TextView[] textViews = new TextView[4];
    private Fragment[] fragments;
    private int currentTabIndex = 0, tab_index = 0;//当前索引和选择的索引
    private RecommendFragment recommendFragment ;
    private PerformFragment performFragment;
    private  FileFragment fileFragment;
    private MineFragment mineFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTabFragments();
    }

    //初始化布局
    public void initView() {
        imageViews[0] = (ImageView) findViewById(R.id.tj_image);
        imageViews[1] = (ImageView) findViewById(R.id.yanchu_image);
        imageViews[2] = (ImageView) findViewById(R.id.file_image);
        imageViews[3] = (ImageView) findViewById(R.id.mine_image);

        textViews[0] = (TextView) findViewById(R.id.tv_tj);
        textViews[1] = (TextView) findViewById(R.id.tv_yanchu);
        textViews[2] = (TextView) findViewById(R.id.tv_file);
        textViews[3] = (TextView) findViewById(R.id.tv_mine);

        imageViews[0].setSelected(true);
        textViews[0].setTextColor(getResources().getColor(R.color.red));

        findViewById(R.id.tj_lin).setOnClickListener(this);
        findViewById(R.id.yanchu_lin).setOnClickListener(this);
        findViewById(R.id.file_lin).setOnClickListener(this);
        findViewById(R.id.mine_lin).setOnClickListener(this);
    }
    //初始化fragments,各模块(我的模块用detach，因为每次显示都要数据刷新)
    public void initTabFragments(){
        recommendFragment=new RecommendFragment();
        performFragment=new PerformFragment();
        fileFragment=new FileFragment();
        mineFragment=new MineFragment();
        fragments=new Fragment[]{recommendFragment,performFragment,fileFragment,mineFragment};
        //初始显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_framelayout, recommendFragment)
                .add(R.id.fragment_framelayout, performFragment)
                .add(R.id.fragment_framelayout, fileFragment)
                .add(R.id.fragment_framelayout, mineFragment)
                .hide(performFragment).hide(fileFragment)
                .detach(mineFragment).show(recommendFragment).commit();
    }
    //各模块的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tj_lin:
                tab_index = 0;
                break;
            case R.id.yanchu_lin:
                tab_index = 1;
                break;
            case R.id.file_lin:
                tab_index = 2;
                break;
            case R.id.mine_lin:
                String username= SharedPreferenceUtil.getInstance("USERINFO").getshuxingData("username");
                if(TextUtils.isEmpty(username)){
                    startActivityForResult(new Intent(this, LoginActivity.class), ConstantFinal.islogin);
                    return;
                }else{
                    tab_index = 3;
                }
                break;
        }
        showModule();
    }

    //添加显示各模块
    public void showModule() {
        if (currentTabIndex != tab_index) {
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            if(currentTabIndex==3){
                ft.detach(fragments[currentTabIndex]);
            }else{
                ft.hide(fragments[currentTabIndex]);
            }

            if (!fragments[tab_index].isAdded()) {
                ft.add(R.id.fragment_framelayout, fragments[tab_index]);
            }
            if(tab_index==3){
                ft.attach(fragments[tab_index]).commit();
            }else{
                ft.show(fragments[tab_index]).commit();
            }
        }
        imageViews[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        imageViews[tab_index].setSelected(true);
        textViews[currentTabIndex].setTextColor(getResources().getColor(R.color.black));
        textViews[tab_index].setTextColor(getResources().getColor(R.color.red));
        currentTabIndex = tab_index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(currentTabIndex==3&&ConstantFinal.outlogin){
            String username= SharedPreferenceUtil.getInstance("USERINFO").getshuxingData("username");
            if(TextUtils.isEmpty(username)){
                tab_index = 0;
                ConstantFinal.outlogin=false;
                showModule();
            }
        }
    }


    //Activity的逻辑回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ConstantFinal.islogin ://授权成功的回调
                Log.d("MainActivity", "授权成功，显示我的模块");
                tab_index = 3;
                showModule();
                break;
        }
    }
}
