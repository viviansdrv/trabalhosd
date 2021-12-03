package calculadorasd;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import java.io.IOException;

public class CalculadoraServer extends Thread {

	private DatagramSocket socket;

    public CalculadoraServer() {
        try {
			socket = new DatagramSocket(4445);
		} catch (SocketException e) {		
			e.printStackTrace();
		}
    }

    public void run() {
        while (true) {
        	byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            String received = new String(packet.getData(), 0, packet.getLength());
            if (received.equals("end")) {
            	socket.close();
                System.exit(0);
            }
            received = received.replace( " ", "" );
            received = received.replace("\n", ""); 
            char symbol = '?';
            for ( int i = 0; i < received.length( ); i++ ) {
            	if ( !Character.isDigit( received.charAt( i ) ) ) {
            		symbol = received.charAt( i );
            		break;
            	}
            }
            String strSymbol = Character.toString(symbol);
            String splitter = strSymbol;
            if ( strSymbol.equals( "+" ) || strSymbol.equals( "*" ) ) {
            	splitter = "\\" + splitter;
            }
            String[ ] token = received.split( splitter );
            double op1 = Double.valueOf( token[ 0 ] );
            double op2 = Double.valueOf( token[ 1 ] );
            double result = 0.0;
            switch ( symbol ) {
            	case '+': result = op1 + op2; break;
            	case '-': result = op1 - op2; break;
            	case '*': result = op1 * op2; break;
            	case '/': result = op1 / op2; break;
            	default: result = 0.0;
            }
            buf = String.valueOf(result).getBytes(StandardCharsets.UTF_8);
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            
            try {
				socket.send(packet);
			} catch (IOException e) {				
				e.printStackTrace();
			}
        }
    }
    public static void main( String[ ] args ) {
		new CalculadoraServer( ).start( );
	}
    
}
