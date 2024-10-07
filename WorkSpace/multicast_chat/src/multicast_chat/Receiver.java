package multicast_chat;

import java.awt.EventQueue;
import java.awt.TextArea;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver extends JFrame {

	private JPanel contentPane;
	private TextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Receiver frame = new Receiver();
					frame.setVisible(true);
					frame.startListening();  // Bắt đầu lắng nghe tin nhắn
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Receiver() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Tạo TextArea để hiển thị các tin nhắn
		textArea = new TextArea();
		textArea.setBounds(10, 10, 400, 240);
		contentPane.add(textArea);
	}
	
	// Phương thức để bắt đầu lắng nghe tin nhắn
	public void startListening() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try (MulticastSocket socket = new MulticastSocket(12345)) {
					InetAddress group = InetAddress.getByName("230.0.0.1");
					socket.joinGroup(group);
					byte[] buffer = new byte[256];
					
					while (true) {
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
						socket.receive(packet);  // Nhận tin nhắn
						String receivedMessage = new String(packet.getData(), 0, packet.getLength());
						
						// Hiển thị tin nhắn lên TextArea
						textArea.append("Received: " + receivedMessage + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
