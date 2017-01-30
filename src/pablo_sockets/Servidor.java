package pablo_sockets;


import java.awt.event.*;
import java.io.*;
import java.io.IOException;
import javax.swing.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.*;

public class Servidor  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class MarcoServidor extends JFrame implements Runnable{
	
	public MarcoServidor(){
		
		setBounds(1200,300,280,350);
		setTitle("SERVER");
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);
		
		Thread hilo=new Thread(this);
		hilo.start();
		
		}
	
	private	JTextArea areatexto;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			ServerSocket servidor=new ServerSocket(9999);
			
			String nick, ip, mensaje;
			
			ArrayList<String> listaIP=new ArrayList<String>();
			PaqueteEnvio paquete_recibido;
			
			while(true){
			Socket misock=servidor.accept();
			
						
			ObjectInputStream paquete_datos=new ObjectInputStream(misock.getInputStream());
			
			paquete_recibido=(PaqueteEnvio) paquete_datos.readObject();
			nick=paquete_recibido.getNick();
			ip=paquete_recibido.getIp();
			mensaje=paquete_recibido.getMensaje();
			
			if(!mensaje.equals(" online1234")){
				
			
			areatexto.append("\n"+ nick + ": "+mensaje+" para "+ip);
			/*
			*DataInputStream flujoentrada=new DataInputStream(misock.getInputStream());
			*String mensajer=flujoentrada.readUTF();			
			*areatexto.append("\n"+mensajer);
			*/
			Socket enviaDestinatario = new Socket(ip,9090);			
			ObjectOutputStream paquete_Reenvio=new ObjectOutputStream(enviaDestinatario.getOutputStream());
			paquete_Reenvio.writeObject(paquete_recibido);
			paquete_Reenvio.close();
			enviaDestinatario.close();
			misock.close();
			}else{
				//---detecta online----------
				InetAddress localizacion= misock.getInetAddress();
				String ipRemota=localizacion.getHostAddress();
				System.out.println("papa ete ta Online "+ipRemota);
				listaIP.add(ipRemota);
				
				paquete_recibido.setIps(listaIP);
				paquete_recibido.setMensaje(" online1234");
				
				
				//-----------------------------------------
				for(String z:listaIP){
					System.out.println("array: "+z);
					
					Socket enviaDestinatario = new Socket(z,9090);			
					ObjectOutputStream paquete_Reenvio=new ObjectOutputStream(enviaDestinatario.getOutputStream());
					paquete_Reenvio.writeObject(paquete_recibido);
					paquete_Reenvio.close();
					enviaDestinatario.close();
					misock.close();
				}
			}
			}
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
