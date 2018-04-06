package Lab4.SpaceGame.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Lab4.SpaceGame.Client.ClientRemote;
import Lab4.SpaceGame.Core.CaptainCommand;
import Lab4.SpaceGame.Core.GameSession;
import Lab4.SpaceGame.Core.Player;
import Lab4.SpaceGame.Core.Utils;
import Lab4.SpaceGame.Server.GameEvent.EventType;

public class GameServer extends UnicastRemoteObject implements ServerRemote {
	public static final String SERVER_LOOKUP = "//localhost/MyServer";
	public static final int SERVER_PORT = 1099;
	
	private List<ClientRemote> connectedClients;
	
	private GameSession gameSession;

	public GameServer() throws RemoteException {
		super();
		
		connectedClients = new ArrayList<>();
		
		gameSession = new GameSession();
	}
	
	@Override
	public GameEvent joinGame(ClientRemote client, Player newPlayer) throws RemoteException {
		
		connectedClients.add(client);
		
		GameEvent event = gameSession.joinGame(newPlayer);
		
		publish(event);
		return event;
	}

	@Override
	public GameSession getGameSession() throws RemoteException {
		return gameSession;
	}

	@Override
	public GameEvent startGame() throws RemoteException {
		GameEvent event = gameSession.startGame();
		
		publish(event);
		return event;
	}

	@Override
	public GameEvent endGame() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameEvent captainSendsCommend(CaptainCommand cmd) throws RemoteException {
	
		GameEvent event = gameSession.captainSendsCommend(cmd);
		
		publish(event);
		return event;
	}

	@Override
	public GameEvent trySetSteeringWheelAngle(int newangle) throws RemoteException {
		GameEvent event = gameSession.trySetSteeringWheelAngle(newangle);
		
		publish(event);
		return event;
	}

	@Override
	public GameEvent trySetEngineThrust(int newEngineThrust) throws RemoteException {
		GameEvent event = gameSession.trySetEngineThrust(newEngineThrust);
		
		publish(event);
		return event;
	}
	
	public void publish(GameEvent event) {
		connectedClients.stream().forEach(c -> {
			try {
				c.handleGameEvent(event);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		});
	}
}
