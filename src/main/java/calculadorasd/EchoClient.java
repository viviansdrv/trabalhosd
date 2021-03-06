package calculadorasd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EchoClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public EchoClient() {
        try {
			socket = new DatagramSocket();
		} catch (SocketException e) {			
			e.printStackTrace();
		}
        
        try {
			address = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }

    public String sendEcho(String msg) {
        buf = msg.getBytes();
        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, address, 4445);
        try {
			socket.send(packet);
		} catch (IOException e) {			
			e.printStackTrace();
		}
        packet = new DatagramPacket(buf, buf.length);
        try {
			socket.receive(packet);
		} catch (IOException e) {			
			e.printStackTrace();
		}
        String received = new String(
          packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }    
	
	public static void main (String[] args) {
		EchoClient client = new EchoClient();
		String retorno = client.sendEcho("hello, world");
		System.out.println(retorno);
		retorno = client.sendEcho("end");
		System.out.println(retorno);
		client.close();		
	}         
}