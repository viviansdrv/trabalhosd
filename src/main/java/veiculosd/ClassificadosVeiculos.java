package veiculosd;

import java.util.List;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClassificadosVeiculos implements VeiculoInterface {
	
	private List<Veiculo> list;
	
	public ClassificadosVeiculos( ) {
		list = new ArrayList<Veiculo>( );
	}
	
	@Override
	public List<Veiculo> search2Ano(int anoVeiculo) {
		List<Veiculo> resultList = new ArrayList<Veiculo>( );
		for ( Veiculo v : list ) {
			if ( v.getAno( ) == anoVeiculo ) {
				resultList.add(v);
			}
		}
		return resultList;
	}
	
	@Override
	public void add(Veiculo v) {
		list.add(v);
	}
	
	public static void main( String[ ] args ) {
		VeiculoInterface server = new ClassificadosVeiculos( );
		try {
			VeiculoInterface stub = (VeiculoInterface) UnicastRemoteObject
			    .exportObject((VeiculoInterface)server, 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("VeiculoInterface", stub);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
