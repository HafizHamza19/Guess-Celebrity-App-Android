package com.example.hafizhamza.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> Celeburls=new ArrayList<String>();
    ArrayList<String> Celebname=new ArrayList<String>();
    String[] answer=new  String[4];
    ImageView imageView;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
int choosecele;
int LocationofCorrect;
public void Question()
{
    try {


        Random random = new Random();
        choosecele = random.nextInt(Celeburls.size());
        downloadimage image = new downloadimage();
        Bitmap bitmap = image.execute(Celeburls.get(choosecele)).get();
        imageView.setImageBitmap(bitmap);
        String a = Celebname.get(choosecele);
        LocationofCorrect = random.nextInt(4);
        int incorrect;
        for (int i = 0; i < 4; i++) {
            if (LocationofCorrect == i) {
                answer[i] = a;
            } else {
                incorrect = random.nextInt(Celebname.size());
                while (incorrect == choosecele) {
                    incorrect = random.nextInt(Celebname.size());
                }
                answer[i] = Celebname.get(incorrect);
            }
        }
        button0.setText(answer[0]);
        button1.setText(answer[1]);
        button2.setText(answer[2]);
        button3.setText(answer[3]);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

}
    public void ButtonPressed(View view) {
        if (view.getTag().toString().equals(Integer.toString(LocationofCorrect)))
        {
            Toast.makeText(this,"Correct",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"InCorrect",Toast.LENGTH_SHORT).show();
        }
        Question();
    }

    public  class downloadimage extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {


            try {
                URL url=new URL(urls[0]);
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream=connection.getInputStream();
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

public class downloadtask extends AsyncTask<String,Void,String>
{

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        URL url;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(urls[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data;
            data = reader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();

            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.MyimageView);
        button0=(Button)findViewById(R.id.Mybutton0);
        button1=(Button)findViewById(R.id.Mybutton1);
        button2=(Button)findViewById(R.id.Mybutton2);
        button3=(Button)findViewById(R.id.Mybutton3);
        downloadtask task=new downloadtask();
       String result;
        try {
            result=task.execute("http://www.posh24.se/kandisar").get();
            String[] splitresult=result.split("<div class=\"listedArticle\">");
            Pattern p=Pattern.compile("img src=\"(.*?)\"");
            Matcher m=p.matcher(splitresult[0]);
            while (m.find())
            {
               Celeburls.add(m.group(1));
            }
            p=Pattern.compile("alt=\"(.*?)\"");
            m=p.matcher(splitresult[0]);
            while (m.find())
            {
                Celebname.add(m.group(1));
            }
           Question();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
