package cn.xbwl.zb_print_demo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zebra.sdk.comm.Connection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Button print_btn;
    private boolean isPrinting;
    private String finalAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        print_btn = (Button) findViewById(R.id.print_btn);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        String addr = "";
        for (int i = 0; i < devices.size(); i++) {
            BluetoothDevice device = (BluetoothDevice) devices.iterator().next();
            Log.e("addr----", device.getName());
            if (device.getName().equals("18J172102833")) {
                addr = device.getAddress();
            }
        }
        finalAddr = addr;
        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preparePrint();

//                if (!isPrinting) {
//                    isPrinting = true;
//                    count++;
//                    sendZplOverBluetooth(finalAddr);
//                } else {
//                    Toast.makeText(MainActivity.this, "正在打印", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }


    private int count = 0;

    private void sendZplOverBluetooth(final String theBtMacAddress, final PrintEvent event) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    long ts=System.currentTimeMillis();
                    Log.e("sendZplOverBluetooth", "sendZplOverBluetooth: "+(System.currentTimeMillis()-ts)+"" );

                    // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
                    Connection thePrinterConn = new BluetoothTest(theBtMacAddress);

                    // Initialize
                    Looper.prepare();

                    Log.e("sopen1", ""+(System.currentTimeMillis()-ts)+"" );
                    // Open the connection - physical connection is established here.
                    thePrinterConn.open();
                    Log.e("sopen2", ""+(System.currentTimeMillis()-ts)+"" );
                    // This example prints "This is a ZPL test." near the top of the label.
                    String zplData1 = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ";
                    String zplData = "^XA\n" +
                            "^CWZ,E:SIMSUN.TTF\n" +
                            "^CI28\n" +
                            "^FO60,20^AZN,32,32^FD" + String.valueOf(event.getCurrentNum()) + "^FS\n" +
                            "^FO50,50^AZN,32,32^FD画横线和竖线^FS\n" +
                            "^FO50,80^GB400,0,4,^FS\n" +
                            "^FO450,80^GBO,400,4,^FS\n" +
                            "^FO50,100^BY2^BCN^FD76669888^FS\n" +
                            "^FO320,150^BY2^BCR,100,N,N,N^FDLC12345678^FS\n" +
                            "^FO50,220^AZN,20,20^FD这是打印中文字体不加粗^FS\n" +
                            "^FO50,260^AZN,20,20^FD这是打印中文字体加粗的^FS\n" +
                            "^FO50,310^AZN,32,32^FD打印m,㎡,m³和kg^FS\n" +
                            "^FO50,350^AZN,50,50^FD123555^FS\n" +
                            "^FO50,450^AZN,32,32^TBN,300,100^FD打印文本块55yyyOK要实现自动换行功能中yyyyy888888uyauuu黄冈的数据uu^FS\n" +
                            "^FO480,50^AZN,20,20^FD中文缩放^FS\n" +
                            "^FO480,80^AZN,30,30^FD中文缩放^FS\n" +
                            "^FO480,120^AZN,40,40^FD中文缩放^FS\n" +
                            "^FO480,180^AZN,50,50^FD中文缩放^FS\n" +
                            "^FO480,220^AZN,70,70^FD中文缩放^FS\n" +
                            "^FO480,310^A0N,20,20^FD888656^FS\n" +
                            "^FO480,330^A0N,30,30^FD888656^FS\n" +
                            "^FO480,360^A0N,40,40^FD888656^FS\n" +
                            "^FO480,410^A0N,60,60^FD888656^FS\n" +
                            "^XZ";

                    // Send the data to printer as a byte array.
                    thePrinterConn.write(zplData.getBytes());
                    Log.e("write()", ""+(System.currentTimeMillis()-ts)+"" );

//                    thePrinterConn.write(zplData.getBytes());
//                    Log.e("write2()", ""+(System.currentTimeMillis()-ts)+"" );
//                    thePrinterConn.write(zplData.getBytes());
//                    Log.e("write2()", ""+(System.currentTimeMillis()-ts)+"" );
//                    thePrinterConn.write(zplData.getBytes());
//                    Log.e("write2()", ""+(System.currentTimeMillis()-ts)+"" );
//                    thePrinterConn.write(zplData.getBytes());
//                    Log.e("write2()", ""+(System.currentTimeMillis()-ts)+"" );
                    // Make sure the data got to the printer before closing the connection
//                    Thread.sleep(500);

                    // Close the insecure connection to release resources.
                    thePrinterConn.close();
                    Log.e("close", ""+(System.currentTimeMillis()-ts)+"" );

                    Looper.myLooper().quit();
                    EventBus.getDefault().post(event);
                    Log.e("over", ""+(System.currentTimeMillis()-ts)+"" );
                } catch (Exception e) {
                    // Handle communications error here.
                    e.printStackTrace();
                }

            }
        }).start();
    }


    private List<PrintEvent> mPrintData = new ArrayList<>();

    private void preparePrint() {
        count++;
        mPrintData.add( new PrintEvent(count));
        if(!isPrinting){
            isPrinting = true;
            sendZplOverBluetooth(finalAddr,mPrintData.get(0));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(PrintEvent event) {
        for (int i = 0; i < mPrintData.size(); i++) {
          //  Log.e("list",mPrintData.get(i).getCurrentNum()+"");
        }

        mPrintData.remove(event);
        if(mPrintData.size()>0){
            sendZplOverBluetooth(finalAddr,mPrintData.get(0));
        }else {
            isPrinting = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    static class PrintEvent {

        int currentNum;

        public PrintEvent(int num) {
            this.currentNum = num;
        }

        public int getCurrentNum() {
            return currentNum;
        }
    }
}
