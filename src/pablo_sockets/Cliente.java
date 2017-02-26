package pablo_sockets;


import java.awt.event.*; 
import java.io.*;
import java.io.IOException;

import javax.swing.*;

import com.sun.media.sound.MidiOutDeviceProvider;

import java.net.*;
import java.util.ArrayList;


public class Cliente {

	public static void main(String[] args) {

		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		setTitle("CLIENTE");
		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		addWindowListener(new EnvioOnline());
		setVisible(true);
		
		
		
		}	
	
}
//envio de senal online---------------------
class EnvioOnline extends WindowAdapter{

	
	@Override
	public void windowOpened(WindowEvent e) {

		try{
			Socket misocket=new Socket("192.168.0.7",9999);
			PaqueteEnvio datoss = new PaqueteEnvio();
			datoss.setMensaje(" online1234");
			
			ObjectOutputStream paquete_datos= new ObjectOutputStream(misocket.getOutputStream());
			paquete_datos.writeObject(datoss);
			paquete_datos.close();
			misocket.close();
		}catch(Exception e2){
			System.out.println(e2.getMessage());
		}
	}
	
}
//--------------------------------------------

class LaminaMarcoCliente extends JPanel implements Runnable{
	
	public LaminaMarcoCliente(){
		
		String nick_usuario=JOptionPane.showInputDialog("Nick: ");
		
		JLabel n_nick=new JLabel("NIck: ");
		add(n_nick);
		
		nick=new JLabel();
		nick.setText(nick_usuario);
		add(nick);
	
		JLabel texto=new JLabel("Online: ");		
		add(texto);
		
		ip=new JComboBox();
		add(ip);
		
		campochat=new JTextArea(12,25);
		add(campochat);
	
		campo1=new JTextField(20);	
		add(campo1);
	
		miboton=new JButton("Enviar");
		EnviaTexto evento=new EnviaTexto();
		
		miboton.addActionListener(evento);
		add(miboton);	
		
		Thread mihilo=new Thread(this);
		mihilo.start();
	}
	
	private class EnviaTexto implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			campochat.append("\n"+campo1.getText());
			
			
			try {
				Socket misoket=new Socket("192.168.0.7",9999);
				
				PaqueteEnvio datos=new PaqueteEnvio();
				datos.setNick(nick.getText());
				datos.setIp(ip.getSelectedItem().toString());
				datos.setMensaje(campo1.getText());
				
				ObjectOutputStream paquete_datos=new ObjectOutputStream(misoket.getOutputStream());
				paquete_datos.writeObject(datos);
				paquete_datos.close();
				misoket.close();
				
				/*DataOutputStream flujosalida=new DataOutputStream(misoket.getOutputStream());
				flujosalida.writeUTF(campo1.getText());
				flujosalida.close();*/
				
				
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}
			
			
		}
		
	}
	
	
	
		
		
		
	private JTextField campo1;
	private JComboBox ip;
	private JLabel nick;
	private JTextArea campochat;	
	private JButton miboton;
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try{
			
			ServerSocket servidor_cliente=new ServerSocket(9090);
			
			Socket cliente;
			PaqueteEnvio paqueteRecibido;
			
			while(true){
				
				cliente = servidor_cliente.accept();
				ObjectInputStream flujoEntrada=new ObjectInputStream(cliente.getInputStream());
				
				paqueteRecibido=(PaqueteEnvio)flujoEntrada.readObject();
				
				if(!paqueteRecibido.getMensaje().equals(" online1234")){
					campochat.append("\n"+paqueteRecibido.getNick()+": "+paqueteRecibido.getMensaje());
				}else{
					//campochat.append("\n"+paqueteRecibido.getIps());
					
					ArrayList<String> IpsMenu=new ArrayList<String>();
					IpsMenu=paqueteRecibido.getIps();
					ip.removeAllItems();
					for(String z:IpsMenu){
						ip.addItem(z);
					}
				}
				
				
				
				
				
				
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
}

class PaqueteEnvio implements Serializable{

	private String nick, ip, mensaje;
	private ArrayList<String> Ips;

	public ArrayList<String> getIps() {
		return Ips;
	}

	public void setIps(ArrayList<String> ips) {
		this.Ips = ips;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
	
}