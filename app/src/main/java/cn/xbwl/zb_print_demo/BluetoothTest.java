package cn.xbwl.zb_print_demo;

import com.zebra.sdk.comm.BluetoothConnectionInsecure;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.internal.ConnectionInfo;
import com.zebra.sdk.comm.internal.NotMyConnectionDataException;

import java.io.IOException;

/**
 * Created by chenjinglan on 2017/11/3.
 * email:13925024285@163.com
 */

public class BluetoothTest extends BluetoothConnectionInsecure {
    protected BluetoothTest(ConnectionInfo connectionInfo) throws NotMyConnectionDataException {
        super(connectionInfo);
    }

    public BluetoothTest(String s) {
        super(s);
    }

    public BluetoothTest(String s, int i, int i1) {
        super(s, i, i1);
    }

    @Override
    public void close() throws ConnectionException {
        this.friendlyName = "";
        if (this.isConnected) {
            this.isConnected = false;

            try {
                this.outputStream.close();
                this.inputStream.close();
                this.commLink.close();
            } catch (IOException var2) {
                throw new ConnectionException("Could not disconnect from device: " + var2.getMessage());
            }
        }
    }
}
