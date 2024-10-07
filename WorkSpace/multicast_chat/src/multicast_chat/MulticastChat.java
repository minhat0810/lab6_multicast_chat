package multicast_chat;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import java.awt.TextArea;

public class MulticastChat extends JFrame {

	private JPanel 				contentPane;
	private JTextField 			txtIP;
	private	TextArea 			textArea;
	private TextArea 			txtMessage;
	private	String 				selectedOption;
	private	JComboBox			groupBox;
	private InetAddress 		groupAddress;
	private MulticastSocket 	multicastSocket;
	private boolean isInGroup = false;
	private String 				groupAddr ;
	private String	IPgroup1  = "230.0.0.1";
	private JLabel 				lblGroup;
	private static	MulticastChat		frame;
	private int PORT = 12345;
	private	JButton btnJoin;
	private InetAddress groupA;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 frame = new MulticastChat();
					frame.setVisible(true);
					frame.startListening();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MulticastChat() {
		setUpGroups();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 614);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		JLabel lblNhm = new JLabel("LIST GROUP");
		lblNhm.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNhm.setBounds(10, 26, 182, 31);
		contentPane.add(lblNhm);
		
		lblGroup = new JLabel("Group 1");
		lblGroup.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblGroup.setBounds(10, 105, 67, 31);
		contentPane.add(lblGroup);
		
	
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(229, 26, 12, 511);
		contentPane.add(separator);
		
		JLabel lblIp = new JLabel("IP:");
		lblIp.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIp.setBounds(241, 67, 29, 31);
		contentPane.add(lblIp);
		
		txtIP = new JTextField();
		txtIP.setColumns(10);
		txtIP.setBounds(267, 67, 232, 27);
		contentPane.add(txtIP);
		
		JLabel lblTo = new JLabel("TO:");
		lblTo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTo.setBounds(507, 67, 29, 31);
		contentPane.add(lblTo);
		
		String[] options = {"None","IP", "Group", "Broadcast"};
		JComboBox<String> sendToBox = new JComboBox(options);
		sendToBox.setSelectedIndex(0);
		sendToBox.setBounds(535, 67, 112, 28);
		contentPane.add(sendToBox);
		
		sendToBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 selectedOption = (String) sendToBox.getSelectedItem();
              //  label.setText("Selected: " + selectedOption);
                switch (selectedOption) {
				case "IP": {
					System.out.println("IP");
					txtIP.setEditable(true);
					break;
				}
				case "Group": {
					System.out.println("Group");
					break;
				}
				case "Broadcast": {
					txtIP.setText("255.255.255.255");
					txtIP.setEditable(false);
					System.out.println("Broadcast");
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + selectedOption);
				}
            }
        });
		

		
		JLabel lblGroup_1 = new JLabel("GROUP:");
		lblGroup_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblGroup_1.setBounds(662, 67, 67, 31);
		contentPane.add(lblGroup_1);
		
		 
		 groupBox = new JComboBox();
		groupBox.setBounds(739, 70, 112, 28);
		contentPane.add(groupBox);
		
		//join
		btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//	sendMessage(txtName.getName(), getName());
	            try {
	            	  multicastSocket.joinGroup(groupA);
	                  isInGroup = true;
	                  addGroupToComboBox("Group 1");
	                // Thêm nhóm vào JComboBox
	               // addGroupToComboBox(groupName);
	                
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }
			}
			// Kiểm tra xem nhóm đã có trong JComboBox hay chưa
			private boolean groupExists(String groupAddress) {
				for (int i = 0; i < groupBox.getItemCount(); i++) {
					if (groupBox.getItemAt(i).toString().contains(groupAddress)) {
						return true; // Đã tồn tại
					}
				}
				return false;
			}
		});
		btnJoin.setBounds(75, 107, 59, 31);
		contentPane.add(btnJoin);
		
		//leave
		JButton btnLeave = new JButton("Leave");
		btnLeave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isInGroup) {
					 try {
						multicastSocket.leaveGroup(groupA);
						removeGroupComboBox("Group 1");
						 JOptionPane.showMessageDialog(null, "Left the group.");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			         isInGroup = false;
	            } else {
	                JOptionPane.showMessageDialog(null, "You are not a member of any group.");
	            }
	        }
		});
		
		

		btnLeave.setBounds(144, 107, 75, 31);
		contentPane.add(btnLeave);
		
		JPanel panel = new JPanel();
		panel.setBounds(251, 107, 600, 393);
		contentPane.add(panel);
		panel.setLayout(null);
		
		
		
		textArea = new TextArea();
		textArea.setBounds(0, 0, 600, 393);
		textArea.setEditable(false);
		//panel.add(textArea);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(0, 0, 600, 393);
		panel.add(scrollPane);
		 txtMessage = new TextArea();
		txtMessage.setBounds(247, 506, 512, 49);
		contentPane.add(txtMessage);
		
		JButton btnSendMess = new JButton("SEND");
		btnSendMess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedOption = (String) sendToBox.getSelectedItem();
	              //  label.setText("Selected: " + selectedOption);
	                switch (selectedOption) {
					case "IP": {
						try {
							sendToIP(txtIP.getText(), txtMessage.getText());
							System.out.println(""+txtIP.getText()+ txtMessage.getText());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						break;
					}
					case "Group": {
						 if (isInGroup) {
			                    String message = txtMessage.getText();
			                    try {
									sendToGroup(message);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
			                } else {
			                    JOptionPane.showMessageDialog(null, "You are not in any group to send messages.");
			                }
						break;
					}
					case "Broadcast": {
						try {
							sendToBroadcast(txtMessage.getText());
							System.out.println(""+txtIP.getText()+ txtMessage.getText());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					}
					
					default:
						System.out.println("hi");
						JOptionPane.showMessageDialog(null, "Please select your destination");
						//throw new IllegalArgumentException("Unexpected value: " + selectedOption);
					}
			}
		});
		btnSendMess.setBounds(765, 506, 86, 31);
		contentPane.add(btnSendMess);	
}
	
	public void join() {
		
	}
	
	
	    // Gửi tin nhắn đến một địa chỉ IP cụ thể
	    private void sendToIP(String ip, String message) throws IOException {
	        InetAddress address = InetAddress.getByName(ip);
	        sendMessage(address,txtIP.getText(), message);
	    }
	
	    // Gửi tin nhắn đến một nhóm multicast
	    private void sendToGroup(String message) throws IOException {
	        if (groupBox.getSelectedItem() != null && !txtMessage.getText().isEmpty()) {
	            String selectedGroup1 = groupBox.getSelectedItem().toString();
	            String groupIP1 = "";
	            String senderIP = InetAddress.getLocalHost().getHostAddress();
	            // Kiểm tra IP của nhóm đã chọn
	            if (selectedGroup1.equals("Group 1")) {
	                groupIP1 = IPgroup1;
//	            } else if (selectedGroup1.equals("Group B")) {
//	                groupIP1 = groupB_IP;
//	            }

	            try {
	                String messageToSend = "[" + senderIP + "][" + selectedGroup1 + "] : " + txtMessage.getText();
	                byte[] buffer = messageToSend.getBytes();
	                
	                InetAddress group = InetAddress.getByName(groupIP1);
	                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
	               
	                
	                
	                multicastSocket.send(packet);

	                
	                txtMessage.setText(""); // Clear input field
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        } else {
	            JOptionPane.showMessageDialog(this, "Chọn nhóm và nhập tin nhắn để gửi.");
	        }
	        }
}
	
	    // Gửi tin nhắn broadcast tới tất cả các máy trên mạng
	    private void sendToBroadcast(String message) throws IOException {
	        InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
	        sendMessage(broadcastAddress,"Broadcast", message);
	    }
	    private void sendMessage(InetAddress address,String receiver, String message) throws IOException {
	    	 String senderIP = InetAddress.getLocalHost().getHostAddress();
	    	    
	    	    // Đính kèm IP của người gửi vào tin nhắn
	    	 String fullMessage = "[" + senderIP + "] [" + receiver + "] " + message;
	    	    byte[] buffer = fullMessage.getBytes();
	    	    
	    	    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 12345);
	    	    try (MulticastSocket socket = new MulticastSocket()) {
	    	        socket.send(packet);
	    	    }
	    }
	    public void Receiver() {
	    	//String sendTo = sele
	    }
	    private void addGroupToComboBox(String groupAddress) {
	        groupBox.addItem(groupAddress);
	    }
	    private void removeGroupComboBox(String groupAddress) {
	        groupBox.removeItem(groupAddress);
	    }
	    private void removeGroupFromComboBox(String groupAddress) {
	        for (int i = 0; i < groupBox.getItemCount(); i++) {
	            String item = groupBox.getItemAt(i).toString();
	            if (item.equals(groupAddress)) {
	                groupBox.removeItemAt(i); // Xóa nhóm khỏi JComboBox
	                break;
	            }
	        }
	    }
	    
	    // nhận message
	    
	     private void setUpGroups() {
	         try {
	             groupA = InetAddress.getByName(IPgroup1); // Group A IP
	            // Group B IP
	             multicastSocket = new MulticastSocket(PORT); // Random port for multicast
	             multicastSocket.setTimeToLive(10); 
	             

	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	     }
	    public void startListening() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					 byte[] buffer = new byte[1024];
				        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

				        while (true) {
				                try {
									multicastSocket.receive(packet);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				            String message = new String(packet.getData(), 0, packet.getLength());
				            System.out.println(message);
				            // Lấy IP người gửi
				            String senderIP = packet.getAddress().getHostAddress().toString(); 
				            // Kiểm tra xem tin nhắn có phải là gửi nhóm hay gửi toàn bộ
//				             addMessageToChat(senderIP + ": " + message);
				            if (message.contains("[Group")) {
				                // Gửi theo nhóm, phân tích cú pháp nhóm từ message
				              //  String groupName = message.substring(message.indexOf("[Group"), message.indexOf("]") + 1);
				              //  message = message.substring(message.indexOf("]") + 1).trim();
				                
				                // Hiển thị tin nhắn từ nhóm
				             //   addMessageToChat(senderIP + groupName + message);
				            	String receivedMessage = new String(packet.getData(), 0, packet.getLength());
//								
//								// Hiển thị tin nhắn lên TextArea
								textArea.append(" " + receivedMessage + "\n");
				            } else if (message.contains("[Broadcast]")) {
				            	String receivedMessage = new String(packet.getData(), 0, packet.getLength());
//								
//								// Hiển thị tin nhắn lên TextArea
								textArea.append(" " + receivedMessage + "\n");
				                // Gửi tới toàn bộ, phân tích cú pháp message
				              //  message = message.replace("[Broadcast]", "").trim();
				                
				                // Hiển thị tin nhắn broadcast
				               // addMessageToChat(senderIP + "[Broadcast] " + message);
				            } else {
				            	String receivedMessage = new String(packet.getData(), 0, packet.getLength());
//								
//								// Hiển thị tin nhắn lên TextArea
							textArea.append("Received: " + receivedMessage + "\n");
				                // Gửi đến IP cụ thể
				            //    addMessageToChat(senderIP + "-> " + message);
				            }
				}
			}
			}).start();
		}	  
		private void addMessageToChat(String message) {
		    if(!textArea.getText().contains(message)) {
		    	textArea.append(message + "\n");   
		    	//textArea.setCaretPosition(textArea.getDocument().getLength());
		    }
		}	

		
}
	    


