package Lab4.SpaceGame.GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Lab4.SpaceGame.Client.ClientRemote;
import Lab4.SpaceGame.Core.CanHandleGameEvent;
import Lab4.SpaceGame.Core.CaptainCommand;
import Lab4.SpaceGame.Core.GameSession;
import Lab4.SpaceGame.Core.Player.Role;
import Lab4.SpaceGame.Core.SpaceshipMeasurements;
import Lab4.SpaceGame.Core.Utils;
import Lab4.SpaceGame.Core.CaptainCommands.EngineThrustCommand;
import Lab4.SpaceGame.Core.CaptainCommands.LightsCommand;
import Lab4.SpaceGame.Core.CaptainCommands.OilLevelCommand;
import Lab4.SpaceGame.Core.CaptainCommands.StearingWheelAngleCommand;
import Lab4.SpaceGame.Server.GameEvent;
import Lab4.SpaceGame.Server.ServerRemote;

public class CaptainFrame extends JFrame implements CanHandleGameEvent {

	ServerRemote look_up;
	ClientRemote client;

	private JPanel contentPane;
	private JTextField txtPlayersConnected;
	private JLabel lblSpaceShipStatus;
	private JTable tableSpaceship;
	private JButton btnStartGame;
	private JButton btnSetEngineThrust;
	private JSpinner spinnerEngineThrust;
	private JSpinner spinnerSteeringWheelAngle;
	private JButton btnLightsOn;
	private JButton btnLightsOff;
	private JButton btnSetSteeringWheelAngle;

	private JButton btnSetOilLevel;
	private JSpinner spinnerOilLevel;

	HashSet<Role> playerRoles;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CaptainFrame frame = new CaptainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CaptainFrame() {
		setTitle("Captain Frame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 531, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPlayersConnected = new JLabel("Players connected");
		lblPlayersConnected.setBounds(10, 11, 121, 14);
		contentPane.add(lblPlayersConnected);

		txtPlayersConnected = new JTextField();
		txtPlayersConnected.setEditable(false);
		txtPlayersConnected.setBounds(121, 8, 86, 20);
		contentPane.add(txtPlayersConnected);
		txtPlayersConnected.setColumns(10);

		lblSpaceShipStatus = new JLabel("Space Ship Status");
		lblSpaceShipStatus.setBounds(10, 62, 121, 14);
		contentPane.add(lblSpaceShipStatus);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 87, 495, 135);
		contentPane.add(scrollPane);

		tableSpaceship = new JTable();
		scrollPane.setViewportView(tableSpaceship);

		btnStartGame = new JButton("Start The Game");
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					GameEvent success = look_up.startGame();
					if (success == null) {
						Utils.log("Cant start the game right now");
						return;
					}

					btnStartGame.setVisible(false);

					updateGui();
					updateTableSpaceship();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		btnStartGame.setBounds(10, 390, 187, 23);
		contentPane.add(btnStartGame);

		btnSetEngineThrust = new JButton("Set Engine Thrust");
		btnSetEngineThrust.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int desiredValue = (Integer) spinnerEngineThrust.getValue();

				try {

					CaptainCommand<Integer> cmd = new EngineThrustCommand(desiredValue);

					look_up.captainSendsCommend(cmd);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		btnSetEngineThrust.setBounds(10, 244, 187, 23);
		contentPane.add(btnSetEngineThrust);

		spinnerEngineThrust = new JSpinner();
		spinnerEngineThrust.setModel(new SpinnerNumberModel(0, 0, 150, 1));
		spinnerEngineThrust.setBounds(207, 245, 94, 20);
		contentPane.add(spinnerEngineThrust);

		btnSetSteeringWheelAngle = new JButton("Set Steering Wheel Angle");
		btnSetSteeringWheelAngle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int desiredValue = (Integer) spinnerSteeringWheelAngle.getValue();
				try {

					CaptainCommand<Integer> cmd = new StearingWheelAngleCommand(desiredValue);

					look_up.captainSendsCommend(cmd);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		btnSetSteeringWheelAngle.setBounds(10, 311, 187, 23);
		contentPane.add(btnSetSteeringWheelAngle);

		spinnerSteeringWheelAngle = new JSpinner();
		spinnerSteeringWheelAngle.setModel(new SpinnerNumberModel(0, -180, 180, 1));
		spinnerSteeringWheelAngle.setBounds(207, 312, 94, 20);
		contentPane.add(spinnerSteeringWheelAngle);

		btnLightsOn = new JButton("Turn Lights On");
		btnLightsOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					CaptainCommand<Boolean> cmd = new LightsCommand(true);

					look_up.captainSendsCommend(cmd);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		btnLightsOn.setBounds(10, 345, 187, 23);
		contentPane.add(btnLightsOn);

		btnLightsOff = new JButton("Turn Lights Off");
		btnLightsOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CaptainCommand<Boolean> cmd = new LightsCommand(false);

					look_up.captainSendsCommend(cmd);
				} catch (RemoteException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnLightsOff.setBounds(10, 345, 187, 23);
		contentPane.add(btnLightsOff);

		btnSetOilLevel = new JButton("Set Oil Level");
		btnSetOilLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int desiredValue = (Integer) spinnerOilLevel.getValue();
				try {
					CaptainCommand<Integer> cmd = new OilLevelCommand(desiredValue);

					look_up.captainSendsCommend(cmd);
				} catch (RemoteException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnSetOilLevel.setBounds(10, 278, 187, 23);
		contentPane.add(btnSetOilLevel);

		spinnerOilLevel = new JSpinner();
		spinnerOilLevel.setModel(new SpinnerNumberModel(0, 0, 50, 1));
		spinnerOilLevel.setBounds(207, 276, 94, 20);
		contentPane.add(spinnerOilLevel);
	}

	public CaptainFrame(ServerRemote look_up, ClientRemote client) throws RemoteException {
		this();

		this.look_up = look_up;
		this.client = client;

		updateGui();
	}

	public void handleGameEvent(GameEvent event) throws RemoteException {
		if (event == null)
			return;

		System.out.println(event.getLogType());

		switch (event.getLogType()) {
		case EVENT_GAME_STARTED:
			btnStartGame.setVisible(false);

			playerRoles = (HashSet<Role>) event.getEventData();
			if (!playerRoles.contains(Role.Mechanic)) {
				btnSetEngineThrust.setVisible(false);
				spinnerEngineThrust.setVisible(false);

				btnSetOilLevel.setVisible(false);
				spinnerOilLevel.setVisible(false);
			}

			if (!playerRoles.contains(Role.Steersman)) {
				btnSetSteeringWheelAngle.setVisible(false);
				spinnerSteeringWheelAngle.setVisible(false);

				btnLightsOn.setVisible(false);
				btnLightsOff.setVisible(false);
			}
			break;
		case EVENT_GAME_ENDED:
			break;

		case EVENT_CAPTAIN_SENDS_COMMAND:
			break;

		case EVENT_GAME_MEASURMENT_PROPERTY_CHANGED:
			try {
				updateGui();
				updateTableSpaceship();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case EVENT_NEW_PLAYER_JOINED:
			int currentPlayersFromRemote = look_up.getGameSession().getPlayers().size();
			txtPlayersConnected.setText(Integer.toString(currentPlayersFromRemote));
			break;
		}
	}

	private void updateGui() throws RemoteException {
		GameSession session = look_up.getGameSession();
		SpaceshipMeasurements measurments = session.getMeasurements();

		int plyersConnected = session.getPlayers().keySet().size();
		txtPlayersConnected.setText(Integer.toString(plyersConnected));

		if (measurments != null) {

			btnLightsOff.setVisible(measurments.isLights());
			btnLightsOn.setVisible(!measurments.isLights());

			if (!playerRoles.contains(Role.Steersman)) {
				btnLightsOff.setVisible(false);
				btnLightsOn.setVisible(false);
			}
		}

		repaint();
	}

	private void updateTableSpaceship() throws RemoteException {
		SpaceshipMeasurements currentMeasurments = look_up.getGameSession().getMeasurements();

		String[] columnNames = { "Measurment Name", "Value" };
		Object[][] data = { new String[] { "Engine Thrust", Integer.toString(currentMeasurments.getEngineThrust()) },
				new String[] { "Oil Level", Integer.toString(currentMeasurments.getOilLevel()) },
				new String[] { "Steering Wheel Angle", Integer.toString(currentMeasurments.getSteeringWheelAngle()) },
				new String[] { "Lights", Boolean.toString(currentMeasurments.isLights()) } };
		DefaultTableModel model = new DefaultTableModel(data, columnNames);

		tableSpaceship.setModel(model);
	}
}
