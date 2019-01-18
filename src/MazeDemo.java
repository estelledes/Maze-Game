/*
/*
* Map Creator Assignment
* Michael Wang and Estelle Chung
* November 27th 2017
* ICS 3U
* Ms S.
*/
//IMPORTING
import java.awt.*;
import javax.swing.*;      
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.filechooser.*;
import javax.swing.Timer;


//CLIENT CODE-------------------------------------------------------------------------------------
public class MazeDemo{                        
 public static void main ( String[] args )   { 
        MenuFrame frame = new MenuFrame();
        frame.setSize(700,480);
		frame.setResizable(false);
        frame.setVisible( true );                                    
        frame.setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );   
    }                                                                                  
  } 
//MENU FRAME-------------------------------------------------------------------------------------
	// Creates a menu with rules,load, and map creator buttons
class MenuFrame extends JFrame implements ActionListener{
	//Initializing 
	 private JFileChooser fc;   
	 private JButton createMap,rules,loadMap;
	 public static JLabel bgPic;
	 private JPanel menuBut;
	 private Container c;
	 public static MyMap gameFrame;
	 private Player p;
	 private Wolf bigBad;
	 private ArrayList<Tile> mapIn;
	 //Constructor
	 public MenuFrame(){
		  createMap=new JButton("Create New Map");
		  createMap.addActionListener(this);
		  loadMap = new JButton("Load");
		  loadMap.addActionListener(this);
		  rules = new JButton ("Show Rules");
		  rules.addActionListener(this);
		  bgPic= new JLabel(new ImageIcon("menuPic.jpg"));
		  menuBut=new JPanel();
		  c = getContentPane();
		  c.setLayout(new BorderLayout(10,10));
		  c.add(bgPic,BorderLayout.CENTER);
		  c.add(menuBut,BorderLayout.SOUTH);
		  menuBut.add(createMap);
		  menuBut.add(loadMap);
		  menuBut.add(rules);
		  fc = new JFileChooser();
	 }
	 //METHODS
	 // Checks for button press
	public void actionPerformed(ActionEvent evt) {
		// Opens map creator
		if(evt.getActionCommand()==("Create New Map")){
			this.dispose();
			CreateFrame frame2 = new CreateFrame();
	        frame2.setSize(700,700);
			frame2.setResizable(false);
	        frame2.setVisible( true );                                    
	        frame2.setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );   
		}
		// Loads a file 
		else if((evt.getActionCommand()).equals("Load")){
			try{
		      int returnVal = fc.showOpenDialog(this);   // need to import javax.swing.filechooser.*;
      
              if (returnVal == JFileChooser.APPROVE_OPTION) {  // if user did not click cancel and picked file name
                 File file = fc.getSelectedFile();    // picked name
				 openMethod(file);
              }
			}
			catch(Exception ex){
                    System.out.println(ex);
			}		
			this.dispose();
		}
		 //Shows rules
		else if((evt.getActionCommand()).equals("Show Rules")) {
			JLabel rulesL= new JLabel(new ImageIcon("rules.jpg"));
			JFrame frame = new JFrame();
			frame.setSize(700,700);
			frame.setResizable(false);
			frame.setVisible( true );                                    
			frame.add(rulesL);
		}
	}
	// Opens the file we need to load
	public void openMethod(File filePath){
	     FileInputStream fileIn = null;
         ObjectInputStream objectIn = null;
		 try{ //try catch block
		   fileIn = new FileInputStream(filePath);
           objectIn = new ObjectInputStream(fileIn);
		   p = (Player)objectIn.readObject(); //opens player
		   bigBad= (Wolf)objectIn.readObject(); //opens wolf
           mapIn = (ArrayList<Tile>)objectIn.readObject(); //opens other tiles
           objectIn.close();
		   gameFrame = new MyMap(mapIn, p,bigBad);
           } catch (Exception ex) {
               System.out.println(ex);
          }
	}
}
//MY MAP -------------------------------------------------------------------------------------
// Game map (game will be played on this JFrame)
class MyMap extends JFrame{
	//Initializing 
  private Container c;
  private MyPanel gp;
  private JPanel menu;
  private static MyButton basket, bread;
  private static JLabel inventory, basketI, breadI,myB,totalB;
  
  	//Constructor
	public MyMap(ArrayList<Tile> t, Player p,Wolf bigBad){
		super("Game"); 
		c = getContentPane();
		c.setLayout(new BorderLayout(5,10));
		//menu.setLayout(new GridLayout(5,1,5,5));
		gp = new MyPanel(t, p,bigBad);
		menu=new JPanel();
		inventory = new JLabel("Inventory");
		basket= new MyButton();
		bread= new MyButton();
		basketI= new JLabel("Basket");
		breadI= new JLabel("Bread");
		myB= new JLabel ();
		totalB= new JLabel (Integer.toString(MyPanel.getBreadT()));
		gp.setPreferredSize(new Dimension(500, 500));
		menu.setPreferredSize(new Dimension(100, 500));
		c.add(gp, BorderLayout.CENTER);
		c.add(menu,BorderLayout.EAST );
		menu.add(inventory);
		menu.add(basketI);
		menu.add(basket);
		menu.add(breadI);
		menu.add(bread);
		menu.add(myB);
		menu.add(totalB);
		//basket.setEnabled(false);
		//bread.setEnabled(false);
		this.setVisible(true);
		setSize(700,700);
		setResizable(false);
	}
	//METHODS
	public static void setBaskI(ImageIcon i){  //sets basket icon
		basket.setIcon(i);
	}
	public static void setMyB(ImageIcon i,int breadNum){  //sets bread number
		bread.setIcon(i);
		myB.setText(Integer.toString(breadNum));
	}
	public static void selectBasket(){ //picks up basket
		basket.setBackground(Color.red);
		basket.setOpaque(true);
		basket.setBorderPainted(false);
	}
	public static void unselectB(){
		basket.setBackground(new Color(0,0,0,0));
		basket.setOpaque(false);
		basket.setContentAreaFilled(false);
		basket.setBorderPainted(false);
	}
	
	//PLAYER-------------------------------------------------------------------------------------
}
class Player extends Rectangle {
	//Initializing 
	private boolean hasBasket,right,left,up,down;
	private int breadCnt;
	private static Image rrhI,basketI;
	public boolean win;;
	//Constructors
      Player(int x, int y){
		super(x,y,18,18);
		hasBasket=false;
		win=false;
		breadCnt=0;
		rrhI = new ImageIcon("rrh.png").getImage();
		basketI = new ImageIcon("basket.png").getImage();
	}	
     //METHODS
	public void myDraw(Graphics g){ //draws images
        g.drawImage(rrhI, x , y, null);
		if(hasBasket && MyPanel.getIS()==true){
			g.drawImage(basketI, x - 10, y - 10, null);
		}
	}	
	public void move (ArrayList<Tile> w) { //moves player
		if (right) {
			x+=3;
			if (collide(w))
				x-=3;
		}
		if (left) {
			x-=3;
			if (collide(w))
				x+=3;
		}
		if (up) {
			y-=3;
			if (collide(w))
				y+=3;
		}
		if (down) {
			y+=3;
			if (collide(w))
				y-=3;
		}
	}
	public boolean collide(ArrayList<Tile> w){  //checks for collisions
		for(int i=0;i<w.size();i++) {
			if(this.intersects(w.get(i)) && (w.get(i).getType()==1 ||w.get(i).getType()==3)) //if intersects a tree or river
				return true;
			else if (this.getX()<3 || this.getY()<3 || this.getX()>570 || this.getY()>647) {//if hits side of frame
				return true;
			}
			else if (this.intersects(w.get(i)) && (w.get(i).getType()==2 )){ //intersects basket 
					w.remove(i);
					MyMap.setBaskI(MyButton.basketI);
					hasBasket = true;
			}
			else if ((this.intersects(w.get(i)) && (w.get(i).getType()==4)) && hasBasket &&MyPanel.getIS()==true) {//intersects bread 
					w.remove(i);
					breadCnt++;
					MyMap.setMyB(MyButton.breadI,breadCnt);
					if (breadCnt==MyPanel.getBreadT()) { //if all bread is collected
						win = true;
						MenuFrame.gameFrame.dispose();
						JOptionPane.showMessageDialog(null, "YOU WIN!", "CONGRATULATIONS", JOptionPane.INFORMATION_MESSAGE, null); 
						System.exit(0);
					}
			}
		}
		return false;
	}
	public void setD (boolean d){ //Sets directions
		down=d;
	}
	public void setU (boolean d){
		up=d;
	}
	public void setL (boolean d){
		left=d;
	}
	public void setR (boolean d){
		right=d;
	}
}
//WOLF -------------------------------------------------------------------------------------
class Wolf extends Rectangle{ 
	//Initializing 
	private static Image wolfUp,wolfDown,wolfRight,wolfLeft;
	private int wolfNum;
	//Constructor
    Wolf(int x, int y){
		super(x,y,18,18);
		wolfNum = 1;
		wolfUp = new ImageIcon("wolfup.png").getImage();
		wolfDown = new ImageIcon("wolfdown.png").getImage();
		 wolfRight = new ImageIcon("wolfright.png").getImage();
		 wolfLeft = new ImageIcon("wolfleft.png").getImage();
	}	
    //METHODS
	public void move(ArrayList<Tile> w,Player p) { //moves wolf, chases rrh
		if(!(p.win)){
			int pX = p.x;
			int pY = p.y;
			
			if(pX > x){
				x+= 3;

				if (collide(w,p)){
					x-=3;
				}
				wolfNum = 3;
			}else if(Math.abs(pX - x) < 5){
				
			}
			else if(pX < x){
				x-= 3;
				if (collide(w,p)){
					x+=3;
				}
				wolfNum = 4;
			}
			
			if(pY > y){
				y+= 3;
				if (collide(w,p)){
					y-=3;
				}
				wolfNum = 2;
			}else if(Math.abs(pY - y) < 5){
				
			}else if(pY < y){
				y-= 3;
				if (collide(w,p)){
					y+=3;
				}
				wolfNum = 1;
			}
		}
	}
	public boolean collide(ArrayList<Tile> w,Player p){ //checks for collisions 
		for(int i=0;i<w.size();i++) {
			if(this.intersects(w.get(i)) && (w.get(i).getType()==1 ||w.get(i).getType()==3)) //if intersects a tree or river
				return true;
			else if (this.getX()<3 || this.getY()<3 || this.getX()>570 || this.getY()>647) { //if hits edge of frame
				return true;
			}
			else if (this.intersects(p)){ //intersects RED RIDING HOOD, Game ends
				MenuFrame.gameFrame.dispose();
				JOptionPane.showMessageDialog(null, "YOU LOSE", "GAME OVER", JOptionPane.INFORMATION_MESSAGE, null); 
				System.exit(0);
			}
		}
		return false;
	}
	public void myDraw(Graphics g){ //draws images
		if(wolfNum == 1){
			g.drawImage(wolfUp, x, y, null);			
		}else if(wolfNum == 2){
			g.drawImage(wolfDown, x, y, null);		
		}else if(wolfNum == 3){
			g.drawImage(wolfRight, x, y, null);		
		}else if(wolfNum == 4){
			g.drawImage(wolfLeft, x, y, null);		
		}
	}	
}
//MY PANEL-------------------------------------------------------------------------------------
class MyPanel extends BgPanel  implements KeyListener,ActionListener{  //panel where game is played
	//Initializing 
	private ArrayList<Tile> allTiles;
	private Image img;
	private Player p;
	private Wolf bigBad;
	private Timer myTimer;
	private Timer wolfT;
	private static boolean invenSelect;
	private static int totalBread;
	//Constructor
	public MyPanel(ArrayList<Tile> t, Player p,Wolf bigBad){
		allTiles = t;
		totalBread=0;
		invenSelect=false;
		img = new ImageIcon("grass.jpg").getImage();
		this.bigBad=bigBad;
		this.p=p;
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(this);
		wolfT=new Timer (50,this); 
		myTimer=new Timer(25,this);
		myTimer.start();
		wolfT.start();
		for (int i=0;i<allTiles.size();i++) { //counts total bread
			if (allTiles.get(i).getType()==4) 
				totalBread++;
		}
	}
	//METHODS
	public void paintComponent(Graphics g){ //Draws the tiles
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
		if(allTiles != null){
			for(int i =0; i < allTiles.size(); i++){
				allTiles.get(i).myDraw(g);
			}
		}
		p.myDraw(g);
		bigBad.myDraw(g);
	}
	public void actionPerformed(ActionEvent e) { //timer calls
		if (e.getSource()==myTimer) {
	   	   p.move(allTiles);
		   repaint();
		}
		else if (e.getSource()==wolfT) {
			bigBad.move(allTiles,p);
			repaint();
		}
	}
	public void keyTyped(KeyEvent e) { }
	public void keyPressed(KeyEvent e) { //Key pushed down
		if(e.getKeyCode()==40){
			System.out.println("DOWN");
			p.setD(true);;
		}
		if(e.getKeyCode()==38){
			System.out.println("UP");
			p.setU(true);
		}
		if(e.getKeyCode()==37){
			System.out.println("LEFT");
			p.setL(true);
		}
		if(e.getKeyCode()==39){
			System.out.println("RIGHT");
			p.setR(true);
		}
		if (e.getKeyCode()==73) { //Inventory
			MyMap.selectBasket();
			invenSelect=true;
		}
	}
	public void keyReleased(KeyEvent e) { //key is released
		if(e.getKeyCode()==40){
			p.setD(false);
		}
		if(e.getKeyCode()==38){
			p.setU(false);
		}
		if(e.getKeyCode()==37){
			p.setL(false);
		}
		if(e.getKeyCode()==39){
			p.setR(false);
		}
		if (e.getKeyCode()==73) { //Inventory
			MyMap.unselectB();
			invenSelect=false;
		}
	}
	public static int getBreadT(){ //returns total bread
		return totalBread;
	}
	public static boolean getIS(){ //is inventory selected
		return invenSelect;
	}
}
//CREATE FRAME-------------------------------------------------------------------------------------
class CreateFrame extends JFrame implements ActionListener, MouseMotionListener{     
	//Initializing 
	private JFileChooser fc;      
	private MyButton tree,river,basket,bread,rrh,wolf, erase;
	private JButton menu,save;
	private MyButton [] buttons;
	private Container c;
	private BgPanel p;
	private JPanel p2,p3;
	private Image grass;
	private boolean yesWolf,yesRrh,yesBasket,yesBread;
	public int picture,breadCnt;
	//Constructor
	public CreateFrame(){
		super("Game");
		yesWolf = true;
		yesRrh = true;
		yesBasket = true;
		yesBread = true;
		picture=0;
		breadCnt=0;
		c = getContentPane();
		c.setLayout(new BorderLayout(5,10));
		p = new BgPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p2.setPreferredSize(new Dimension(100, 30));
		c.add(p, BorderLayout.CENTER);
		c.add(p2, BorderLayout.EAST);
		p2.add(p3, BorderLayout.CENTER);
		p.setLayout(new GridLayout(20,20,0,0));
		p.setOpaque(false);
		p3.setLayout(new GridLayout(9,1,5,5));
		//buttons
		basket = new MyButton("Basket");
		basket.setIcon(new ImageIcon("basket.png"));
		basket.addActionListener(this);
		erase = new MyButton("Erase");
		erase.addActionListener(this);
		tree = new MyButton("Tree");
		tree.setIcon(new ImageIcon("tree.png"));
		tree.addActionListener(this);
		river = new MyButton("River");
		river.setIcon(new ImageIcon("river.jpg"));
		river.addActionListener(this);
		bread = new MyButton("Bread");
		bread.setIcon(new ImageIcon("bread.png"));
		bread.addActionListener(this);
		rrh = new MyButton("RRH");
		rrh.setIcon(new ImageIcon("rrh.png"));
		rrh.addActionListener(this);
		wolf = new MyButton("Wolf");
		wolf.setIcon(new ImageIcon("wolfup.png"));
		wolf.addActionListener(this);
		save = new JButton("Save");
		save.addActionListener(this);
		menu = new JButton("Menu");
		menu.addActionListener(this);
		fc = new JFileChooser();
		buttons= new MyButton[400]; //array of buttons
		for (int i =0;i<400;i++){
			buttons[i]=new MyButton();
			buttons[i].addActionListener(this);
			buttons[i].addMouseMotionListener(this);
			p.add(buttons[i]);
		}		
		p3.add(tree);
		p3.add(basket);
		p3.add(river);
		p3.add(bread);
		p3.add(rrh);
		p3.add(wolf);
		p3.add(erase);
		p3.add(save);
		p3.add(menu);
	}  
	public void actionPerformed(ActionEvent evt){
		if((evt.getActionCommand()).equals("Save")){	 //if missing wolf, basket, rrh
			if (yesWolf==true) {
				JOptionPane.showMessageDialog(null, "Please place a wolf", "Error", JOptionPane.INFORMATION_MESSAGE, null); 
			}
			else if (yesBasket==true){
				JOptionPane.showMessageDialog(null, "Please place a basket", "Error", JOptionPane.INFORMATION_MESSAGE, null);
			}
			else if (yesRrh==true){
				JOptionPane.showMessageDialog(null, "Please place Red Riding Hood", "Error", JOptionPane.INFORMATION_MESSAGE, null);
			}
			else if (breadCnt==0){
				JOptionPane.showMessageDialog(null, "At least one loaf of Bread", "Error", JOptionPane.INFORMATION_MESSAGE, null);
			}
			else { //if all present, saves file
				try{
					int returnVal = fc.showSaveDialog(this);  
					   if (returnVal == JFileChooser.APPROVE_OPTION) {
						  File file = fc.getSelectedFile();
						  saveMethod(file);
					  }
					}catch(Exception ex){
							System.out.print("Error" + ex);
					}
			}
		}else if((evt.getActionCommand()).equals("Menu")){ //opens menu
				this.dispose();
				MenuFrame frame = new MenuFrame();
				frame.setSize(700, 700);
				frame.setResizable(false);
				frame.setVisible(true);
				frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		}else{ //sets picture from sidebar
			MyButton b = (MyButton)evt.getSource();
			if((b.getActionCommand()).equals("Tree")){
				picture = 1;
			}else if((b.getActionCommand()).equals("Basket")){
				picture = 2;
			}else if((b.getActionCommand()).equals("River")){
				picture = 3;
			}else if((b.getActionCommand()).equals("Bread")){
				picture = 4;
			}else if((b.getActionCommand()).equals("RRH")){
				picture = 5;
			}else if((b.getActionCommand()).equals("Wolf")){
				picture = 6;
			}else if((b.getActionCommand()).equals("Erase")){
				picture = 7;
			}else if(b.iconType == -1 || picture == 7){ //sets button array
					if(picture == 1){ //tree
						b.setIcon(MyButton.treeI);
						b.setBorderPainted(false);
					}
					else if(picture == 2 && yesBasket == true){ //basket (must have)
						b.setIcon(MyButton.basketI);
						basket.setEnabled(false);
						yesBasket = false;
						b.iconType = picture;
						b.setBorderPainted(false);
					}
					else if(picture == 3){ //river 
						b.setIcon(MyButton.riverI);
						b.setBorderPainted(false);
					}
					else if(picture == 4){ //bread (must have)
						b.setIcon(MyButton.breadI);
						b.setBorderPainted(false);
						breadCnt++;
						System.out.println("BREAD NUM  "+breadCnt);
					}
					else if(picture == 5 && yesRrh == true){ //player (must have)
						b.setIcon(MyButton.rrhI);
						rrh.setEnabled(false);
						yesRrh = false;
						b.iconType = picture;
						b.setBorderPainted(false);
					}
					else if(picture == 6 && yesWolf == true){ //wolf (must have)
						b.setIcon(MyButton.wolfI);
						wolf.setEnabled(false);
						yesWolf = false;
						b.iconType = picture;
						b.setBorderPainted(false);
					}
					else if(picture == 7){ 
						if(b.getIcon() == MyButton.basketI){
							yesBasket = true;
							basket.setEnabled(true);
						}else if(b.getIcon() == MyButton.rrhI){
							yesRrh = true;
							rrh.setEnabled(true);
						}else if(b.getIcon() == MyButton.wolfI){
							yesWolf = true;
							wolf.setEnabled(true);
						}	
						else if(b.getIcon() == MyButton.breadI){
							breadCnt--;
							System.out.println("BREAD NUM  "+breadCnt);
						}
						b.iconType = -1;
						b.setIcon(null);
						b.setBorderPainted(true);
					}
			if(picture != 6 && picture != 5 && picture != 2 && picture != 7){//if not one of the must haves
				b.iconType = picture;
				}
			}
		}
	}
	 public void mouseMoved( MouseEvent e ){   }
	 public void mouseDragged( MouseEvent e ){
		 int mAtX=e.getXOnScreen();    // mouse X and Y  relative to screen
		 int mAtY=e.getYOnScreen();
		 int w = buttons[0].getWidth();
		 
		 // width and height of first button which is the same for every button
		 int h=buttons[0].getHeight();
		 for (int i = 0; i < buttons.length; i++ ) {
			 int btnX= buttons[i].getLocationOnScreen().x;       // x and y of the button, relative to Screen
		     int btnY=buttons[i].getLocationOnScreen().y; 
				 
			 if(mAtX>=btnX && mAtX<=btnX+w && mAtY>=btnY && mAtY<=btnY+h ) {
				 buttons[i].setContentAreaFilled(false);
				 if(picture == 7){
					if(buttons[i].getIcon() == MyButton.basketI){
						yesBasket = true;
						basket.setEnabled(true);
					}else if(buttons[i].getIcon() == MyButton.rrhI){
						yesRrh = true;
						rrh.setEnabled(true);
					}else if(buttons[i].getIcon() == MyButton.wolfI){
						yesWolf = true;
						wolf.setEnabled(true);
					}
					else if(buttons[i].getIcon() == MyButton.breadI){
						breadCnt--;
					}
					buttons[i].setIcon(null);
					buttons[i].setBorderPainted(true);
					(buttons[i]).iconType = -1;
				 }else if(buttons[i].iconType == -1){ //Sets icons
					 if(picture == 1){
						 buttons[i].setIcon(MyButton.treeI);
						 buttons[i].setBorderPainted(false);
						}
					else if(picture == 2 && yesBasket == true){
						buttons[i].setIcon(MyButton.basketI);
						basket.setEnabled(false);
						yesBasket = false;
						(buttons[i]).iconType = picture;
						buttons[i].setBorderPainted(false);
						}
					else if(picture == 3){
						buttons[i].setIcon(MyButton.riverI);
						buttons[i].setBorderPainted(false);
					}
					else if(picture == 4 ){ //TODO
						buttons[i].setIcon(MyButton.breadI);
						breadCnt++;
						System.out.println("BREAD NUM  "+breadCnt);
						buttons[i].setBorderPainted(false);
					}
					else if(picture == 5 && yesRrh == true){
						buttons[i].setIcon(MyButton.rrhI);
						rrh.setEnabled(false);
						yesRrh = false;
						(buttons[i]).iconType = picture;
						buttons[i].setBorderPainted(false);
					}
					else if(picture == 6 && yesWolf == true){
						buttons[i].setIcon(MyButton.wolfI);
						wolf.setEnabled(false);
						yesWolf = false;
						(buttons[i]).iconType = picture;
						buttons[i].setBorderPainted(false);
					}
				 }
				if(picture != 6 && picture != 5 && picture != 2 && picture != 7){ //if not must have
					(buttons[i]).iconType = picture;
				}
			}
		 }
	 }
	// Writes the arraylist to the file
	public void saveMethod(File filePath){
	  FileOutputStream fout = null;
      ObjectOutputStream oos = null;
      //try catch block to save
      try {
			fout = new FileOutputStream(filePath);  // file name from file shooser
			oos = new ObjectOutputStream(fout);
			oos.writeObject(saveP());
			oos.writeObject(saveW());
			oos.writeObject(saveButs());
			fout.close();
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
	public Player saveP(){ //save player
		Player p=null;
		for (int i = 0; i < buttons.length; i++ ) {
		   if (buttons[i].getIconType() == 5){//if rrh, saves 
				p = new Player( buttons[i].getLocation().x, buttons[i].getLocation().y); 
		   } 	 
	    }
		return p;
	}
	public Wolf saveW(){ //saves wolf
		Wolf bigBad=null; 
		for (int i = 0; i < buttons.length; i++ ) {
		   if (buttons[i].getIconType() == 6){//if wolf, saves
				bigBad = new Wolf( buttons[i].getLocation().x, buttons[i].getLocation().y); 
		   } 	 
	    }
		return bigBad;
	}
	public ArrayList<Tile> saveButs(){ //saves the rest of buttons/tiles
		ArrayList<Tile> tileArr = new ArrayList<Tile>();
		for (int i = 0; i < buttons.length; i++ ) {
		   if (buttons[i].getIconType() != -1  && buttons[i].getIconType() != 5  && buttons[i].getIconType() != 6){
				tileArr.add(new Tile( buttons[i].getLocation().x, buttons[i].getLocation().y, buttons[i].getIconType()));
		   } 	 
	    }
		return tileArr;
	}
}
//TILE-------------------------------------------------------------------------------------
class Tile extends Rectangle implements Serializable{ // Tile class used to store tile location and type of image
	//Initializing 
	private int imgI; 
	private static Image treeI,riverI,basketI,breadI,rrhI, wolfI;
	//Constructor
	public Tile(int x, int y, int imgI){
		super(x,y,30,30);
		this.imgI = imgI;
		treeI = new ImageIcon("tree.png").getImage();
		riverI = new ImageIcon("river.jpg").getImage();
		basketI = new ImageIcon("basket.png").getImage();
		breadI = new ImageIcon("bread.png").getImage();
		rrhI = new ImageIcon("rrh.png").getImage();
		wolfI = new ImageIcon("wolfup.png").getImage();
	}
	//METHODS
	public void myDraw(Graphics g){ //draws images (used in loading)
		if(imgI == 1){
			g.drawImage(treeI, x , y, null);
		}else if(imgI == 2){
			g.drawImage(basketI, x , y, null);
		}else if(imgI == 3){
			g.drawImage(riverI, x , y, null);
		}else if(imgI == 4){
			g.drawImage(breadI, x , y, null);
		}else if(imgI == 5){
			g.drawImage(rrhI, x , y, null);
		}else if(imgI == 6){
			g.drawImage(wolfI, x , y, null);
		}
	}
	public int getType(){
		return imgI;
	}
}
//BACKGROUND PANEL-------------------------------------------------------------------------------------
class BgPanel extends JPanel{ //paints background image as grass
	private Image img;
	public BgPanel() {
		img = new ImageIcon("grass.jpg").getImage();
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}
}
//BUTTON CLASS-------------------------------------------------------------------------------------
class MyButton extends JButton{ 
	//Initializing
	public static ImageIcon treeI,riverI,basketI,breadI,rrhI,wolfI;
	public int iconType = -1;
	//Constructors
	public MyButton(String s,ImageIcon i){ 
		super(s,i);
		this.setContentAreaFilled(false);
		treeI = new ImageIcon("tree.png");
		riverI = new ImageIcon("river.jpg");
		 basketI = new ImageIcon("basket.png");
		breadI = new ImageIcon("bread.png");
		 rrhI = new ImageIcon("rrh.png");
		wolfI = new ImageIcon("wolfup.png");
	}
	public MyButton(){
		this(null,null);
	}
	public MyButton(String s){
		this(s,null);
	}
	public MyButton(ImageIcon i) {
		this(null,i);
	}
	//METHODS
	 public int getIconType(){
		return iconType;
	}
}
