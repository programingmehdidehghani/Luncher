package com.example.luncher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity {

    private PackageManager manager;
    private List<Item> apps;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);


        loadApps();
        loadListview();
        addClickListenner();
    }

    private void loadApps(){
        manager = getPackageManager();
        apps = new ArrayList<>();

        Intent i=new Intent(Intent.ACTION_MAIN,null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> avaliableActivities= manager.queryIntentActivities(i,0);
        for (ResolveInfo ri : avaliableActivities){
              Item app=new Item();
              app.lable = ri.activityInfo.packageName;
              app.name = ri.loadLabel(manager);
              app.icon = ri.loadIcon(manager);
              apps.add(app);
        }
    }

    private void loadListview(){
        listView = (ListView) findViewById(R.id.list);

        ArrayAdapter<Item> adapter= new ArrayAdapter<Item>(this,R.layout.item,apps){

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView  == null){
                    convertView = getLayoutInflater().inflate(R.layout.item,null);
                }
                ImageView appIcon =(ImageView) convertView.findViewById(R.id.icon);
                appIcon.setImageDrawable((Drawable) apps.get(position).icon);

                TextView appName =(TextView) convertView.findViewById(R.id.name);
                appName.setText(apps.get(position).name);

                return convertView;
            }
        };
        listView.setAdapter(adapter);
    }

    private void addClickListenner(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = manager.getLaunchIntentForPackage(apps.get(position).lable.toString());
                startActivity(i);
            }
        });
    }
}