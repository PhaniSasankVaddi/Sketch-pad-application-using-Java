/*This is a SketchPad application as part of HCI final project.
It consists of drawing different predefined types of shapes of coordinates as specified by the user.
The user can also draw free-hand lines and straight lines.
The user can move, delete, copy, paste the shapes drawn.
The user can undo & redo shapes drawn.
The user can save and load the sketch as a PNG image.
The user can also clear the canvas by deleted all the shapes created.
It is implemented by the following three persons:
Abhishek Raut - 251055990
Phani Sasank Vaddi - 251055273
Shreyas Satyamurthy - 251054240
Cheers!
*/


package javapaint;

import java.awt.event.*;        
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class SketchPad extends Frame implements ActionListener, WindowListener, MouseListener, MouseMotionListener, ItemListener {

    BufferedImage bg = null;    //BufferedImage to export and import images
    public float x = 0, y = 0;
    ArrayList<Shape> shapes = new ArrayList<Shape>();       //An arraylist to store list of drawn shapes
    ArrayList<Shape> undoList = new ArrayList<Shape>();     //A list to store the shapes that have been undone
    //ArrayList<Line2D> lineList = new ArrayList<Line2D>();   
    String selectedShape = "FreeDraw";
    String selectedColor = "Black";
    boolean Undo = false;       //Different modes to differentiate b/w operations to be performed
    boolean MoveOperation = false;
    boolean cutItem = false;
    boolean copyItem = false;
    boolean pasteItem = false;
    float upperX, upperY, W, H, point1, point2, point3, point4;     //Different options stored in lists below, to be displayed in menu
    String[] optionsList = {"Clear Canvas", "Move", "Delete", "Copy", "Paste", "Undo", "Redo", "Save", "Load"};
    String[] colorList = {"Black", "Cyan", "Green", "Yellow", "Magenta", "Red", "Blue"};
    String[] shapeList = {"FreeDraw", "Line", "Rectangle", "Square", "Circle", "Ellipse"};
    public Shape selection;
    static int frameLength = 700;   //Window size defined
    static int frameWidth = 700;

    @Override //from WindowListener         //Overriden methods are written below
    public void windowClosing(WindowEvent eve) {
        System.exit(0);
    }

    @Override //from WindowListener
    public void windowActivated(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowOpened(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowIconified(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowClosed(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowDeactivated(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowDeiconified(WindowEvent we) {
    }

    @Override //from MouseMotionListener
    public void mouseMoved(MouseEvent me) {
    }

    @Override //from MouseListener
    public void mouseClicked(MouseEvent me) {
    }

    @Override //from MouseListener
    public void mouseExited(MouseEvent me) {
    }

    @Override //from MouseListener
    public void mouseEntered(MouseEvent me) {
    }

    @Override //from ItemListener
    public void itemStateChanged(ItemEvent ie) {
    }

    public SketchPad(String title) {        //Class constructor initializing menu items and MouseListeners
        super(title);
        addMouseMotionListener(this);
        addWindowListener(this);
        addMouseListener(this);
        setLayout(null);
        setMenuItems();
        setBackground(Color.white);
    }

    @Override //from ActionListener
    public void actionPerformed(ActionEvent actions) {  //Main user action method

        Graphics2D g2D = (Graphics2D) getGraphics();
        Object sh = actions.getActionCommand();
        for (int i = 0; i != colorList.length; i++) {   //For loop to select the color chosen by user
            if (sh.equals(colorList[i])) {
                selectedColor = colorList[i];
                return;
            }
        }
        for (int i = 0; i != shapeList.length; i++) {   //For loop to select the shape to be drawn as selected by the user
            if (sh.equals(shapeList[i])) {
                selectedShape = shapeList[i];
                return;
            }
        }
        if (sh.equals("Move")) {        //Selecting the operation to be performed
            MoveOperation = true;
        } else if (sh.equals("Delete")) {
            cutItem = true;
        } else if (sh.equals("Copy")) {
            copyItem = true;
        } else if (sh.equals("Paste")) {
            pasteItem = true;
        } else if (sh.equals("Undo") && shapes.size() > 0) {    //Condition to perform undo
            int undo = shapes.size() - 1;  //Selecting the last shape inserted in the shape arraylist
            Shape select;
            select = shapes.get(undo);
            this.shapes.remove(select);     //Removing the selected shape from the shape arraylist
            this.undoList.add(select);      //Adding the undone shape to the undo arraylist
            Graphics2D g = (Graphics2D) getGraphics();
            Area oldArea = new Area(select);    //adding the shape to an area
            g.setColor(Color.white); //setting the color white to to the area and drawing it on canvas
            g.draw(oldArea);
        } else if (sh.equals("Redo") && undoList.size() > 0) {  //Condition to perform redo
            int redo = undoList.size() - 1;     //Selectig the last shape inserted in the undo arrayList
            Shape select = undoList.get(redo);
            this.shapes.add(select);        //Adding the selected shape to the shape arraylist  
            this.undoList.remove(select);       //Removing the selected shape from the undo arraylist
            Graphics2D g = (Graphics2D) getGraphics();
          /*  float s1 = (float) select.getBounds2D().getBounds().getX();
            float s2 = (float) select.getBounds2D().getBounds().getY();
            float s3 = (float) select.getBounds2D().getBounds().getHeight();
            float s4 = (float) select.getBounds2D().getBounds().getWidth();
            String temp = (select.toString()); */
            Area diff1 = new Area(select);
            g.draw(diff1);              //Redrawing the shape that is selected
        } else if (sh.equals("Save")) {     //Condition to perform save 
            BufferedImage newImage = new BufferedImage(frameLength, frameWidth, BufferedImage.TYPE_INT_RGB);    //Creating a buffered image of the frame length, width
            Graphics2D imageG = newImage.createGraphics();      //Creating a graphics image
            imageG.setBackground(Color.white);
            for (int i = 0; i < shapes.size(); i++) {       //Adding all the shapes in the shapes arraylist to the graphics object
                imageG.draw(shapes.get(i));     //Drawing the shapes to the image created
            }
            try {
                String fileName = JOptionPane.showInputDialog("Enter Filename");        //User enters filename to be saved
                if (ImageIO.write(newImage, "png", new File("./" + fileName + ".png"))) {       //File is saved using fileoutputstream as a png image
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving file, please look at stacktrace");
            }
        } else if (sh.equals("Load")) {     //Condition to perform load operation
            String fileName = JOptionPane.showInputDialog("Enter Filename");        //User enters filename to be retreived
            try {
                g2D.setBackground(Color.white);
                bg = ImageIO.read(new FileInputStream("./" + fileName + ".png"));       //Using fileinputstream, image is read an put on a background
                g2D.clearRect(0, 0, frameLength, frameWidth);       //Frame is cleared and image is displayed
                g2D.drawImage(bg, 0, 0, null);
            } catch (IOException e) {
            }
        } else if (sh.equals("Clear Canvas")) {     //Reset condition - This is used to clear the screen and delete all the shapes present in the arrayList
            for (int i = 0; i < shapes.size(); i++) {       //Finding all shapes in the shapes arraylist and removing them
                this.shapes.remove(shapes.get(i));
            }
            g2D.clearRect(0, 0, frameLength, frameWidth);   //Clearing the canvas
        }
    }

    void chooseColor(Graphics ga) {     //Method to choose the color - this method is used to map the user's choice of color to the 7 colors provided
        for (int i = 0; i != colorList.length; i++) {
            if (selectedColor.equals(colorList[i])) {
                switch (i) {
                    case 0:
                        ga.setColor(Color.black);
                        break;
                    case 1:
                        ga.setColor(Color.cyan);
                        break;
                    case 2:
                        ga.setColor(Color.green);
                        break;
                    case 3:
                        ga.setColor(Color.yellow);
                        break;
                    case 4:
                        ga.setColor(Color.magenta);
                        break;
                    case 5:
                        ga.setColor(Color.red);
                        break;
                    case 6:
                        ga.setColor(Color.blue);
                }
            }
        }
    }

    @Override //from MouseListener
    public void mouseReleased(MouseEvent me) {  //Method for when mouse is released
        Graphics2D ga = (Graphics2D) getGraphics();
        if (MoveOperation || pasteItem) {       //Condition for Move and Paste are similar, hence they are written in a combined manner
            W = (float) selection.getBounds2D().getBounds().getWidth();     //Getting the width of the selected shape
            H = (float) selection.getBounds2D().getBounds().getHeight();    //Getting the height of the selected shape
            String temp = (selection.toString());   //Getting the string format of the selected shape
            if (temp.contains("Rectangle")) {   //Shape gets stored in the format where we can differentiate b/w them using this constraint
                Shape movedRec = new Rectangle2D.Float(me.getX(), me.getY(), W, H); //If the shape is rectangle(& Square), we create a new rectangle at the new coordinates, where the user releases the mouse, but its width & height remain the same as the previous shape
                Area diff1 = new Area(movedRec);        //Creating an area with the new shape
                ga.draw(diff1);     //Drawing the new area 
                this.shapes.add(movedRec);      //Adding the new shape to the shapes arrayList
                if (MoveOperation) {        //If the condition is Paste, this condition is invalid
                    this.shapes.remove(selection);  //If it is a move operation, the shape at the previous coordinates need to be deleted
                }
            } else if (temp.contains("Line")) {     //Condition to move a line, which is not functioning properly
                Shape movedLine = new Line2D.Float(point1, point2, me.getX(), me.getY());   //Creating the new shape at the new coordinates
                Area diff3 = new Area(movedLine);   
                ga.draw(diff3);
                this.shapes.add(movedLine);
                if (MoveOperation) {
                    this.shapes.remove(selection);
                }
            } else if (temp.contains("Ellipse")) {      ///If the shape is Ellipse(& Circle), we create a new ellipse at the new coordinates, where the user releases the mouse, but its width & height remain the same as the previous shape
                Shape movedEllipse = new Ellipse2D.Float(me.getX(), me.getY(), W, H);       //Creating the new shape at the new coordinates
                Area diff2 = new Area(movedEllipse);   //Creating the new area with the new shape, drawing the area
                ga.draw(diff2);
                this.shapes.add(movedEllipse);    //Adding the new shape to the shapes arrayList
                if (MoveOperation) {    //If the condition is Paste, this condition is invalid
                    this.shapes.remove(selection);  //If it is a move operation, the shape at the previous coordinates need to be deleted
                }
            }
            pasteItem = false;      //Setting all the conditions to be false
            copyItem = false;
            MoveOperation = false;
            selection = null;
        } else {        //This condition is chosen if the user is selected to draw a particuar shape (not freedraw)
            chooseColor(ga);
            point3 = me.getX();
            point4 = me.getY();
            if ((selectedShape.equals("Line") && cutItem == false)) {   //Condition to draw a line
                Line2D nl = new Line2D.Float(point1, point2, point3, point4);       //Line with the X,Y coordinates of the mouse clicked and mouse released points
                Shape newline = new Line2D.Float(point1, point2, point3, point4);   //Creating a new shape of this line
                ga.draw(newline);       //Drawing the line
                this.shapes.add(newline);   //Adding the shape to the shape arraylist
               // this.lineList.add(nl);
            }
            if (selectedShape.equals("Circle")) {   // Calling the drawSelectedShape method() depending on the shape selected by the user
                drawSelectedShape(ga, "Circle");
            } else if (selectedShape.equals("Square")) {
                drawSelectedShape(ga, "Square");
            } else if (selectedShape.equals("Rectangle")) {
                drawSelectedShape(ga, "Rectangle");
            } else if (selectedShape.equals("Ellipse")) {
                drawSelectedShape(ga, "Ellipse");
            }
        }
    }

    void drawSelectedShape(Graphics2D ga, String sel_shape) {   //Method to draw the selected shape (new shape)
        Shape shapetoadd = null;
        Area difference;
        upperX = Math.min(point1, point3);  //Getting the (X1, Y1 coordinates)
        upperY = Math.min(point2, point4);  //Getting the (X2, Y2 coordinates)
        W = Math.abs(point1 - point3);      //Calculating the width and height
        H = Math.abs(point2 - point4);

        Shape square = new Rectangle2D.Float(upperX, upperY, W, W);     //Creating the shapes with the required coordinates
        Shape rectangle = new Rectangle2D.Float(upperX, upperY, W, H);
        Shape circle = new Ellipse2D.Float(upperX, upperY, W, W);
        Shape ellipse = new Ellipse2D.Float(upperX, upperY, W, H);
        if (sel_shape.equals("Square")) {   //For square, the width will be the same(i.e. the breadth)
            difference = new Area(square);  //Adding the shape to area and drawing
            ga.draw(difference);
            shapetoadd = new Rectangle2D.Float(upperX, upperY, W, W);   
        } else if (sel_shape.equals("Rectangle")) { //For Rectangle, the breadth and length will be different
            difference = new Area(rectangle);   //Adding shape to area and drawing
            ga.draw(difference);
            shapetoadd = new Rectangle2D.Float(upperX, upperY, W, H);
        } else if (sel_shape.equals("Circle")) {    //For circle, height and width will be the same
            difference = new Area(circle);  //Adding the shape to area and drawing
            ga.draw(difference);
            shapetoadd = new Ellipse2D.Float(upperX, upperY, W, W);
        } else if (sel_shape.equals("Ellipse")) {   //For ellipse, the height and width will be different
            difference = new Area(ellipse); //Adding the shape to area and drawing
            ga.draw(difference);
            shapetoadd = new Ellipse2D.Float(upperX, upperY, W, H);
        }
        if ("Circle".equals(selectedShape) || "Square".equals(selectedShape) || "Rectangle".equals(selectedShape) || "Ellipse".equals(selectedShape)) {
            this.shapes.add(shapetoadd);        //Adding the shape to the shapes arraylist
        }

    }

    @Override //from MouseMotionListener
    public void mouseDragged(MouseEvent me) {   //Method when the mouse is dragged by user
        Graphics2D ga = (Graphics2D) getGraphics();
        point3 = me.getX(); //Getting the X2, Y2 coordinates
        point4 = me.getY();
        if (MoveOperation || pasteItem) {       //If operation is move or paste, the new coordinates are calculated by finding the distance b/w the coordinates
            x = x + point3 - point1;
            y = y + point4 - point2;
            point1 = point3;
            point2 = point4;
        } else if (selectedShape.equals("FreeDraw")) {      //Code for freedraw
            if (ga != null) {   //Runs in an iterative manner, where the line will be drawn along with the mouse dragged
                ga.draw(new Line2D.Float(point1, point2, point3, point4));
                point1 = point3;
                point2 = point4;
            }
        }
    }

    @Override //from MouseListener
    public void mousePressed(MouseEvent me) {   //Method for Mouse clicked by the user
        if (MoveOperation) {        //Operation performed is Moving a shape
            for (int i = 0; i < shapes.size(); i++) {       //Finding the shape selected by the user, by checking if the coordinates of the mouse clicked() is present in the shape
                if (shapes.get(i).contains(me.getX(), me.getY())) {
                    selection = shapes.get(i);  //Selecting the particular shape
                }
            }
            point1 = me.getX();
            point2 = me.getY();
            Graphics2D ga = (Graphics2D) getGraphics();
            Area oldArea = new Area(selection);     //adding the shape to an area
            ga.setColor(Color.white); //Setting the color white and drawing the old area - this is done to clear the area of the shape at its previous location
            ga.draw(oldArea);
        } else if (cutItem) {   //Operation to be performed is deletion of the shape
            for (int i = 0; i < shapes.size(); i++) {
                if (shapes.get(i).contains(me.getPoint())) {    //Finding the shape selected by the user, by checking if the coordinates of the mouse clicked() is present in the shape
                    selection = shapes.get(i);      //The shape to be deleted is selected
                    this.shapes.remove(selection);      //Removing the shape from the shapes arraylist
                }
            }
            Graphics2D ga = (Graphics2D) getGraphics();
            point1 = me.getX();
            point2 = me.getY();
            Area oldArea = new Area(selection);     //Drawing white over the area of the shape just deleted
            ga.setColor(Color.white);
            ga.draw(oldArea);
            cutItem = false;        //Resetting the condition of the item deleted mode
        } else if (copyItem) {      //Condition to perform the copy operation
            for (int i = 0; i != shapes.size(); i++) {      //Finding the shape selected by the user, by checking if the coordinates of the mouse clicked() is present in the shape
                if (shapes.get(i).contains(me.getX(), me.getY())) {
                    selection = shapes.get(i);      //The copied item is stored in the global shape selection. This can be used to paste the item elsewhere
                }
            }
            point1 = me.getX();     //Getting the X1, Y1 coordinates
            point2 = me.getY();

        } else {
            upperX = 0;
            upperY = 0;
            W = 0;
            H = 0;
            point1 = me.getX();
            point2 = me.getY();
            Graphics ga = getGraphics();
        }
    }

    void setMenuItems() {       //Method to set the menu items to the menubar
        MenuBar mBar = new MenuBar();       //Shape list created is added to the menubar
        Menu menuShape = new Menu("Select Shape");
        for (int i = 0; i != shapeList.length; i++) {
            menuShape.add(shapeList[i]);
        }
        mBar.add(menuShape);
        menuShape.addActionListener(this);  //List of colors is added to the menubar
        Menu menuColor = new Menu("Select Colors");
        for (int i = 0; i != colorList.length; i++) {
            menuColor.add(colorList[i]);
        }
        mBar.add(menuColor);
        menuColor.addActionListener(this);      //All the other options such as delete, copy, move etc. are added to the menubar
        Menu otherOptions = new Menu("Other Options");
        for (int i = 0; i != optionsList.length; i++) {
            otherOptions.add(optionsList[i]);
        }
        mBar.add(otherOptions);
        otherOptions.addActionListener(this);
        setMenuBar(mBar);
    }

    public static void main(String[] args) {    //Main program
        SketchPad sp = new SketchPad("SketchPad");      //Constructor called
        sp.setSize(frameLength, frameWidth);        //Size of the window defined at the start
        sp.setVisible(true);

    }
}