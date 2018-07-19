package com.mcuhq.dristi2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    Button submit,camera;
    TextView txt;
    int result;
    TextToSpeech txtspeech;
    String text;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            img = (ImageView) findViewById(R.id.ImageView);
            submit = (Button) findViewById(R.id.button);
            txt = (TextView) findViewById(R.id.textView);
            camera=(Button)findViewById(R.id.camera);


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getText();
                }
            });

            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                }
            });

            txtspeech=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status==TextToSpeech.SUCCESS){
                        result=txtspeech.setLanguage(Locale.UK);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Unable to locate speech.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        protected void onActivityResult(int requestCode,int resultCode, Intent data){
            super.onActivityResult(requestCode,resultCode,data);
            Bitmap bitmap2=(Bitmap)data.getExtras().get("data");
            img.setImageBitmap(bitmap2);
        }

        //text to speech
        public void TTS(){
            text=txt.getText().toString();
            txtspeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }

        //image to text
        public void getText(){
            Bitmap bitmap= BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.ok2);
            TextRecognizer txtrec=new TextRecognizer.Builder(getApplicationContext()).build();

            if(!txtrec.isOperational())
                Toast.makeText(getApplicationContext(), "Text recognization is not operational.", Toast.LENGTH_SHORT).show();

            else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> items = txtrec.detect(frame);
                StringBuilder ab = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    TextBlock myitem = items.valueAt(i);
                    ab.append(myitem.getValue());
                }
                txt.setText(ab.toString());
                //call text to speech after extraction of text from image
                TTS();
            }
        }
    }
