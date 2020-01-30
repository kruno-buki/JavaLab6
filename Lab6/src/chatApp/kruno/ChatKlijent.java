package chatApp.kruno;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;

//***=>
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ChatKlijent extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JButton buttonPosalji;
	private JTextArea textArea;
	JButton buttonPostavke;
	
	private Socket clientSocket; 
	private PrintWriter pw; 
	private BufferedReader br;
	//***=>
	Logger log =LoggerFactory.getLogger(ChatKlijent.class);
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatKlijent frame = new ChatKlijent();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
				
	}
	
	//novo dodano
	private void connect(){ 
		try {
	
			//log:
			
			clientSocket = new Socket(UserConfig.getHost(), UserConfig.getPort());
			
			pw = new PrintWriter(clientSocket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String response; 
			try {
				
			response = br.readLine(); 
			if (textArea.getText().length()>0)
				textArea.append("\n");
			textArea.append(response); 
			textField.setText(null);
			} catch (IOException e) 
			{ 
				//...=>
				log.error("Greška kod èitanja inicijalnog odgovora", e);
				JOptionPane.showMessageDialog(textArea, "Greška kod èitanja inicijalnog odgovora", "Greška!", JOptionPane.ERROR_MESSAGE); 
				}
		} catch (UnknownHostException e)
		{ 
			//...=>
			log.error("Nepoznati host", e); 
			this.dispose();
		} catch (IOException e)
		{ 
			//...=>
			log.error("IO iznimka", e); 
			this.dispose();
		}
		//...=>
		log.info("Connected (SPOJENO)!!!");
		
}
	//NOVO
	private void send(){ 
		pw.println(textField.getText()); 
		if (pw.checkError()) {
		JOptionPane.showMessageDialog(textArea, "Greška kod slanja poruke",
		"Greška!", JOptionPane.ERROR_MESSAGE); 
		}
		String response;
		
		try {
		response = br.readLine(); 
		if (textArea.getText().length()>0) 
			textArea.append("\n");
		textArea.append(response);
		textField.setText(null);
		} catch (IOException e) { 
			//...=>
			log.error("Greška kod èitanja", e); 
			JOptionPane.showMessageDialog(textArea, "Greška kod èitanja odgovora",
		"Greška!", JOptionPane.ERROR_MESSAGE);
			}
		//...=>
		log.info("Data Sent");
		}
	

	/**
	 * Create the frame.
	 */
	public ChatKlijent() {
		setTitle("CHAT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		
		
		
		
		
		
		buttonPosalji = new JButton("Pošalji");
		buttonPosalji.setBackground(new Color(152, 251, 152));
		buttonPosalji.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				//textArea.setText(textField.getText());
				
				String name  = textField.getText();
				
				//_----------------------
				textField.setText("");
				//-------------------
				textArea.setText(textArea.getText().trim()+"\nJA:  "+name);
			/*ili*/	//textArea.append(name+'\n');
				
				//-------------------
				send();
				//-------------------
				
			}
		});
		contentPane.add(buttonPosalji);
		
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.BOLD, 15));
		textArea.setForeground(new Color(255, 0, 51));
		sl_contentPane.putConstraint(SpringLayout.NORTH, textArea, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, textArea, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textArea, -30, SpringLayout.NORTH, buttonPosalji);
		sl_contentPane.putConstraint(SpringLayout.EAST, textArea, -49, SpringLayout.EAST, contentPane);
		textArea.setEditable(false);
		contentPane.add(textArea);
		
		buttonPostavke = new JButton("Postavke");
		buttonPostavke.setBackground(new Color(255, 255, 153));
		sl_contentPane.putConstraint(SpringLayout.SOUTH, buttonPostavke, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, buttonPosalji, 0, SpringLayout.NORTH, buttonPostavke);
		sl_contentPane.putConstraint(SpringLayout.EAST, buttonPosalji, -6, SpringLayout.WEST, buttonPostavke);
		sl_contentPane.putConstraint(SpringLayout.EAST, buttonPostavke, -10, SpringLayout.EAST, contentPane);
		
		buttonPostavke.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Postavke dialog = new Postavke();
				dialog.setVisible(true);
				UserConfig.loadParams();
				
			}
		});
		contentPane.add(buttonPostavke);
		
		textField = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textField, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, textField, -25, SpringLayout.WEST, buttonPosalji);
		contentPane.add(textField);
		textField.setColumns(10);
		connect();
	}
}
