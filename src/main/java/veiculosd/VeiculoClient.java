package veiculosd;

import java.rmi.registry.Registry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;
import java.util.ArrayList;

public class VeiculoClient {
	
	private List<Veiculo> list;
	
	public VeiculoClient( ) {
		list = new ArrayList<Veiculo>( );
	}
	
	private void run( ) {
		try {
			FileReader fr = new FileReader( "resources/veiculos.txt" );
			BufferedReader br = new BufferedReader( fr );
			String line = "";
			int contador = 0;
			Veiculo v = new Veiculo( );
			while ( ( line = br.readLine( ) ) != null ) {
				switch ( contador ) {
					case 0: v.setNomeCliente(line); break;
					case 1: v.setMarcaVeiculo(line); break;
					case 2: v.setValorVenda(Double.valueOf(line)); break;
					case 3: v.setAno(Integer.valueOf(line)); break;
					default: break;
				}
				contador++;
				if ( contador == 4 ) {
					list.add(v);
					v = new Veiculo();
					contador = 0;
				}
			}
			br.close( );
			fr.close( );
		}
		catch ( IOException e ) {
			e.printStackTrace( );
		}
		try {
			Registry registry = LocateRegistry.getRegistry();
			VeiculoInterface server = (VeiculoInterface) registry.lookup("VeiculoInterface");
			for ( Veiculo v : list ) {
				server.add(v);
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			while ( true ) {
				menu();
				String escolha = br.readLine();
				switch ( escolha ) {
					case "1": {
						System.out.print( "Nome do proprietário: ");
						String nome = br.readLine( );
						System.out.print( "Marca do veículo: ");
						String marca = br.readLine( );
						System.out.print( "Valor do veículo: ");
						String valor = br.readLine( );
						System.out.print( "Ano: ");
						String ano = br.readLine( );
						Veiculo v = new Veiculo();
						v.setNomeCliente(nome);
						v.setMarcaVeiculo(marca);
						v.setValorVenda(Double.valueOf(valor));
						v.setAno(Integer.valueOf(ano));
						list.add(v);
						server.add(v);
					}; break;
					case "2": {
						System.out.print( "Ano: ");
						String ano = br.readLine( );
						List<Veiculo> resultadoBusca = new ArrayList<Veiculo>( );
						resultadoBusca = server.search2Ano(Integer.valueOf(ano));
						for ( Veiculo encontrado : resultadoBusca ) {
							System.out.println(encontrado);
						}
					}; break;
					case "0": System.exit(0); break;
					default: break;
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void menu() {
		System.out.println("\nMENU:");
		System.out.println("(1) Cadastrar veiculo");
		System.out.println("(2) Consultar veiculo");
		System.out.println("(0) Sair");
		System.out.print("ESCOLHA: ");
	}
	
	public static void main( String[ ] args ) {
		VeiculoClient obj = new VeiculoClient( );
		obj.run( );
	}

}
