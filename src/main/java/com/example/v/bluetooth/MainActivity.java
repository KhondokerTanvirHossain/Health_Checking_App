package com.example.v.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import static com.example.v.bluetooth.QeryUtils.makeHttpRequest;

public class MainActivity extends AppCompatActivity {
    TextToSpeech textToSpeech;

    Button btPairedBlue;
    Button btConnectdBlue;
    Button btSendBlue;
    Button btReadBlue;
    Button btResult;
    Button btInsert;
    Button btData;
    TextView tv;
    TextView tvBpm;
    TextView tvSo2;
    TextView tvBL;
    TextView tvSL;
    TextView tvTL;
    TextView tvTem;
    BluetoothSocket mSocket = null;
    BluetoothDevice mDevice;
    BluetoothAdapter mBluetoothAdapter;
    int REQUEST_ENABLE_BT = 1;
    Context context;
    public static final int MESSAGE_READ = 1;
    Handler handler;
    private String url = "http://192.168.43.60/healthCare3/insert.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btConnectdBlue = (Button) findViewById(R.id.btConnectBlue);
        btPairedBlue = (Button) findViewById(R.id.btPairBlue);
        btSendBlue = (Button) findViewById(R.id.btSendBlue);
        btReadBlue = (Button) findViewById(R.id.btRead);
        btResult = (Button) findViewById(R.id.btResult);
        btInsert = (Button) findViewById(R.id.btInsert);
        btData = (Button) findViewById(R.id.btData);
        tv = (TextView) findViewById(R.id.tv);
        tvBpm = (TextView) findViewById(R.id.bpm);
        tvSo2 = (TextView) findViewById(R.id.so2);
        tvTem = (TextView) findViewById(R.id.tem);
        tvBL = (TextView) findViewById(R.id.bpmLevel);
        tvSL = (TextView) findViewById(R.id.spo2Level);
        tvTL = (TextView) findViewById(R.id.temLevel);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        context = this;

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
//                    textToSpeech.isLanguageAvailable(new Locale("bn_"));
                    int result = textToSpeech.setLanguage(Locale.US);
//                    int result = textToSpeech.setLanguage(new Locale("bn", "BD"));
                    //Check if the language is supported, if not print it on Android Monitor.
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TextToSpeechManager", "This Language is not supported");
//                        Toast.makeText(getApplicationContext(), "not supported", Toast.LENGTH_LONG).show();
                    }
                    else{
//                        Toast.makeText(getApplicationContext(), "supported", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });




        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        final StringBuilder stb = new StringBuilder();
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                byte[] readBuffer = (byte[]) msg.obj;
                String str = new String(readBuffer, 0, msg.arg1);
//                String str = new String(readBuffer, 0, 3);
                tv.setText("data:"+str);
                stb.append(" "+str+" ");
            }
        };
        final String[] arrayResult = new String[3];
        btResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = new String(stb);
                StringTokenizer st = new StringTokenizer(s);
                while(st.hasMoreElements()){
                    String tokenString = st.nextToken();
                    if (tokenString.equals("BPM"))
                        arrayResult[0] = st.nextToken();
                    else if (tokenString.equals("SO2") )
                        arrayResult[1] = st.nextToken();
                    else if (tokenString.equals("TEMP"))
                        arrayResult[2] = st.nextToken();
                }
                String bString, sString, tString;
                Float bb = Float.parseFloat(arrayResult[0]);
                if (bb >= 60.00 && bb <= 100)
                    bString = "NORMAL";
                else if (bb > 100)
                    bString = "HIGH";
                else
                    bString = "LOW";
                Float ss = Float.parseFloat(arrayResult[1]);
                if (ss >= 92.00 && ss <= 100)
                    sString = "NORMAL";
                else if (ss > 100)
                    sString = "HIGH";
                else
                    sString = "LOW";
                Float tt = Float.parseFloat(arrayResult[2]);
                if (tt >= 96.00 && tt <= 100)
                    tString = "NORMAL";
                else if (bb > 100)
                    tString = "HIGH";
                else
                    tString = "LOW";
                tvBpm.setText(arrayResult[0]);
                tvBL.setText(""+bString);
                tvSo2.setText(arrayResult[1]);
                tvSL.setText(""+sString);
                tvTem.setText(arrayResult[2]);
                tvTL.setText(""+tString);
                String sp = "your bpm is " + bString + " your spo2 is " + sString + " your temprature is " + tString;
                textToSpeech.speak(sp, TextToSpeech.QUEUE_FLUSH, null);

            }
        });
        btInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s1 = arrayResult[0];
                String s2 = arrayResult[1];
                String s3 = arrayResult[2];
//                new InsertAsync().execute(url,arrayResult[0], arrayResult[1], arrayResult[2]);
                new InsertAsync().execute(url,s1, s2, s3);
            }
        });
        btPairedBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MAC address
                        if (deviceName.equals("HC-05")){
                            mDevice = device;
                            Toast.makeText(context, "Paired with HC-05", Toast.LENGTH_SHORT).show();
                            tv.setText("PAired with HC-05");
                        }
                        if (deviceName.equals("BG5 0749C4")){
                            mDevice = device;
                            Toast.makeText(context, "Paired with HC-05", Toast.LENGTH_SHORT).show();
                            tv.setText("PAired with BG5 0749C4");
                        }
                    }
                }
            }
        });
        btConnectdBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("Connecting..");
                if (mSocket == null) {
                    new ConnectAsync().execute();
                }
                else if (!mSocket.isConnected()) {
                    new ConnectAsync().execute();
                }
                else if (mSocket.isConnected())
                    tv.setText("Already Connected");
            }
        });
        btSendBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte b = '1';
                if (mSocket != null && mSocket.isConnected())
                    new WriteAsync().execute(b);
                else
                    Toast.makeText(context, "Not connected Cant write", Toast.LENGTH_SHORT).show();
            }
        });

        btReadBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReadAsync().execute();
            }
        });
        btData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListActivity.class);
                startActivity(intent);
            }
        });
    }

    class ConnectAsync extends  AsyncTask<Void, Void, Void>{
        UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        @Override
        protected Void doInBackground(Void...voids) {
            try {
                mSocket = mDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                mBluetoothAdapter.cancelDiscovery();
                if (mSocket == null || !mSocket.isConnected()) {
                    mSocket.connect();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mSocket != null && mSocket.isConnected()) {
                tv.setText("Connected");
            }
            else
                tv.setText("Connection failed");
        }
    }
    class ReadAsync extends AsyncTask<Void, Void, byte[]>{
        InputStream in;
        byte[] b;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                in = mSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected byte[] doInBackground(Void...voids ) {
            b = new byte[1024];
            int size;
            while(in != null){
                try {
                    size = in.read(b);
                    handler.obtainMessage(MESSAGE_READ, size, -1, b).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
//                    in = null;
                    break;
                }
            }

            return b;
        }
        @Override
        protected void onPostExecute(byte[] b) {
            super.onPostExecute(b);
            Toast.makeText(context, "Trying to read..", Toast.LENGTH_SHORT).show();
            String str = new String(" Stop REading ");
            tv.setText(str);
        }
    }
    class WriteAsync extends AsyncTask<Byte, Void, Void>{
        boolean bool = false;
        OutputStream out;

        {
            try {
                out = mSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Byte...bytes) {
                try {

                    String str = "1";
                    byte[] name = str.getBytes();
                    out.write(name);
                    out.flush();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    bool = true;
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!bool) {
                tv.setText("Sendt data");
            }
            else
                tv.setText("Data sending failure");
        }
    }

    class InsertAsync extends AsyncTask<String,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject json = makeHttpRequest(strings[0], strings[1], strings[2], strings[3], context);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject == null)
                Toast.makeText(context, "CONNECTION ERROR", Toast.LENGTH_SHORT).show();
            else {
                try {
                    Toast.makeText(context, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "EXCEPTION OCCURED", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSocket != null){
            try {
                mSocket.close();
                tv.setText("Disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
