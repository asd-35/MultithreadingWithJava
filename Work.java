import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;






public class Work extends JFrame {
	JPanel north , center;
	static JTextField  seat ,agent ,wait, text;
	JButton seatbtn, bookbtn;
	ArrayList<JTextField> seats = new ArrayList<JTextField>();
	ArrayList<String> redSeats = new ArrayList<String>();
	//frame
	public Work() {
	 setSize(1000,1000);
	 //setResizable(false);
	 setLocationRelativeTo(null);
	 setDefaultCloseOperation(EXIT_ON_CLOSE);
	 setLayout(new BorderLayout());
	 init();
	 setVisible(true);
	}
	//used panels for managing border layout
	public void init() {
		north = new JPanel();
		add(north,BorderLayout.NORTH);
		
		center = new JPanel();
		add(center,BorderLayout.CENTER);
		center.setFocusable(true);
		
		
		//buttons text fields etc.
		seat = new JTextField("Number of seats");
		
		north.add(seat);
		
		
		agent = new JTextField("Number of agents");
		north.add(agent);
		
		wait = new JTextField("Max waiting time");
		north.add(wait);
		//creating seats via action listener
		seatbtn = new JButton("Create Seats");
		seatbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(seatbtn)) {
					int num = Integer.parseInt(seat.getText());
					
					createSeat(num);
				}
				
			}
		});
		north.add(seatbtn);
		
		
		bookbtn = new JButton("Book");
		
		bookbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ExecutorService exe = Executors.newFixedThreadPool(Integer.parseInt(agent.getText()));
				//using thread pooling for agent control and getting all the text fields for recoloring and other stuff
				Thread a;
				for(int v = 0; v < seats.size();v++) {
					JTextField x = seats.get(v);
					 a = new Thread(new Runnable() {
						
						@Override
						public void run() {
							Random r = new Random();
							
							String agentnum = Thread.currentThread().getName().substring(14);
							
							x.setText("Booked by Agent " + agentnum);
							x.setEditable(false);
							x.setBackground(Color.red);
							redSeats.add(agentnum);
							center.add(x);
							center.revalidate();
							center.repaint();
							System.out.println(redSeats);
							try {
							Thread.sleep(r.nextInt(Integer.parseInt(wait.getText())));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
							e.printStackTrace();
							}
							
							
						}
					});
					
					exe.submit(a);
					
					
				}
				
				exe.shutdown();
				//the result shown by option pane
				try {
					
					exe.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//System.out.println(redSeats);
				//System.out.println(redSeats.size());
				//System.out.println(redSeats.get(0));
				JOptionPane.showMessageDialog(null, sumOfAgents(redSeats), "Message", JOptionPane.PLAIN_MESSAGE);
				
			}
		});
		north.add(bookbtn);
		
	}
		public static void main(String[] args) {
			Work w1 = new Work();
			//main class
		}
	
		//method for creating seats and adding them to a array
		public void createSeat(int i) {
		for(int a = 0; a <i;a++) {
			
			text = new JTextField("Not Booked");
			text.setEditable(false);
			center.add(text);
			seats.add(text);
			center.revalidate();
			center.repaint();
		}
	}
	//counting up the agents work and returning them as a string
	public  String sumOfAgents(ArrayList<String> a){
		String result = "";
		int count = 0;	
		for(int i = 1; i < Integer.parseInt(agent.getText()) + 1 ;i++) {
			for (int j = 0; j < a.size(); j++) {
				if(Integer.parseInt(a.get(j)) == i) {
					count++;
				}
			}
			result += "Agent " + i +" booked " + count + " seats\n";
			count = 0;
		}
		
		return result;
	}
	
	
	
}
	