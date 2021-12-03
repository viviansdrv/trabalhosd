package calculadorasd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class CalculadoraClient {
	
	private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public CalculadoraClient() {
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

    public String sendMessage(String msg) {
        buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
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
        String received = new String(packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();          
    }
    
    public static void main (String [] args) throws IOException {
    	CalculadoraClient client = new CalculadoraClient( );
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String message = "";
		while ( true ) {
			System.out.print( "INPUT=" );
			message = br.readLine();
			if ( message.equals("end") ) {
				br.close();
				client.close();
				System.exit(0);
			}
		    String answer = client.sendMessage(message);
		    System.out.println(answer);
		}
    }
}

