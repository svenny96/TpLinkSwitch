package tpLinkSwitch;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Hs100Api {

    public static final String COMMAND_SWITCH_ON = "{\"system\":{\"set_relay_state\":{\"state\":1}}}}";
    public static final String COMMAND_SWITCH_OFF = "{\"system\":{\"set_relay_state\":{\"state\":0}}}}";
    public static final String COMMAND_INFO = "{\"system\":{\"get_sysinfo\":null}}";
    public static final int port = 9999;


    private Hs100Api() {

    }

    public static void main(String[] args) {

    }

    //Decompiled encryption- and decrpytion-methods from the official Kasa Android-App
    public static byte[] decode(byte[] bArr) {
        if (bArr != null && bArr.length > 0) {
            byte b = -85;
            int i = 0;
            while (i < bArr.length) {
                byte b2 = bArr[i];
                bArr[i] = (byte) (b ^ bArr[i]);
                i++;
                b = b2;
            }
        }
        return bArr;
    }


    public static byte[] encode(byte[] bArr) {
        byte b = -85;
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (b ^ bArr[i]);
            b = bArr[i];
        }
        return bArr;
    }

    public static byte[] createHeader(String command) {
        byte[] test = command.getBytes(StandardCharsets.US_ASCII);
        byte[] encryptedMsg = encode(test);

        byte[] header = ByteBuffer.allocate(4).putInt(command.length()).array();

        //New Buffer to store the header and data
        ByteBuffer buffer = ByteBuffer.allocate(test.length+header.length);
        buffer.put(header);
        buffer.put(test);
        byte[] msgContent = buffer.array();
        return msgContent;

    }

    public static void switchOn (String ip) {
        //Send a switch-on command to the device with given ip
        byte[] content = createHeader(COMMAND_SWITCH_ON);
        try {
            Socket socket = new Socket(ip, port);
            OutputStream out = socket.getOutputStream();
            out.write(content);
            out.close();
        }
        catch (UnknownHostException e) {
            System.out.println("Couldn't reach the given Host Address.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchOff(String ip) {
        //Send a switch-off command to the device with given ip
        byte[] content = createHeader(COMMAND_SWITCH_OFF);
        try {
            Socket socket = new Socket(ip, port);
            OutputStream out = socket.getOutputStream();
            out.write(content);
            out.close();
        }
        catch (UnknownHostException e) {
            System.out.println("Couldn't reach the given Host Address.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
