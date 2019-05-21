package com.example.ysy.myapplication1;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable {
    String data[] = {"wait..."};
    Handler handler;
    String TAG = "kkk";
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDataStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);


        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY,"");
        Log.i("List","lastRateDateStr="+ logDate);


        final List<String> list1 = new ArrayList<String>();
        for(int i=1;i<100;i++){
            list1.add("item" + 1);
        }

        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        setListAdapter(adapter);//把当前页面用adapter管理

        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==7){
                    List<String> list2 = (List<String>) msg.obj;
                    ListAdapter adapter1 = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter1);
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void run() {
        //获取网络数据，放入list，带回主线程
        List<String> retList = new ArrayList<String>();

        String curDateStr = (new SimpleDateFormat("yyyy-MM-d")).format(new Date());
        Log.i("run","curDateStr:" + curDateStr + "logDate:" + logDate);

        if(curDateStr.equals(logDate)){
            //日期如果相等,就不从网络获取数据
            Log.i(TAG, "run: 日期相等，从数据库中获得数据");
            RateManager manager = new RateManager(this);
            for(RateItem item : manager.listAll()){
                retList.add(item.getCurName()+"-->"+item.getCurName());
            }
        }else {
            //日期不相等
            Log.i(TAG, "run: 日期不相等，从网络中获得数据");
            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
                //doc = Jsoup.parse(html);
                Log.i(TAG,"run:" + doc.title());
                Elements tables = doc.getElementsByTag("table");
            /*int i = 1;
            for(Element table: tables){
                Log.i(TAG, "run: table["+i+"]="+ table);
                i++;
            }*/
                Element table2 = tables.get(1);
                Log.i(TAG, "run: table2=" + table2);
                //获取td中的数据
                Elements tds = table2.getElementsByTag("td");
                List<RateItem> rateList = new ArrayList<RateItem>();

                for(int i=0;i<tds.size();i+=8){
                    Element td1 =tds.get(i);
                    Element td2 =tds.get(i+5);

                    String str1 = td1.text();
                    String val = td2.text();
                    Log.i(TAG, "run: text=" + str1+ "==>" + val);
                    retList.add(str1+ "==>" + val);
                    rateList.add(new RateItem(str1,val));
                }
                //把数据写入到数据库中
                RateManager manager = new RateManager(this);
                manager.deleteAll();
                manager.addAll(rateList);

                //记录更新日期
                SharedPreferences sp = getSharedPreferences ( "myrate", Context .MODE_PRIVATE);
                SharedPreferences. Editor edit = sp.edit();
                edit.putString(DATE_SP_KEY, curDateStr);
                edit. commit();
                Log. i("run" , "更新日期结束:" + curDateStr);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Message msg = handler.obtainMessage(7);
        msg.obj = retList;
        handler.sendMessage(msg);
    }
}
