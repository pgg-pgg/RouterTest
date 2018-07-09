package com.example.shoplib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.annotation.StaticRouter;
import com.example.router.Router;
import com.example.router.rule.ActivityRule;
import com.example.utilslib.Application;
import com.example.utilslib.Logger;
import com.example.utilslib.UseContext;

/**
 * Created by pengganggui on 2018/7/8.
 */

@StaticRouter(ActivityRule.ACTIVITY_SCHEME+"shop.main")
public class ShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setTextSize(50);
        tv.setText("SHOP!!!");
        setContentView(tv);

        Logger.dump("TAG", "Hei! I am shop!!!");
        UseContext.use(Application.get());

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShopActivity.this, getResources().getString(com.example.reslib.R.string.click_notice), Toast.LENGTH_SHORT).show();
                if (Router.resolveRouter(ActivityRule.ACTIVITY_SCHEME + "com.example.bbslib.BBSActivity")) {
                    Intent it = Router.invoke(ShopActivity.this, ActivityRule.ACTIVITY_SCHEME + "com.example.bbslib.BBSActivity");
                    startActivity(it);
                }
            }
        });
    }
}
