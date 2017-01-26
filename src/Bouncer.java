//This entire file is part of my masterpiece.
//BELAL TAHER

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bouncer {
  
    public static final String BALL_IMAGE = "ball.gif";
    public static final int BALL_RADIUS = 12;
    
    private ImageView myBouncer;
    private Rectangle myBouncerCollisionSensor;
    
    public Bouncer(Group root, int widthOfScreen, int heightOfScreen){
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        myBouncer = new ImageView(image);
        
        //calculates starting x and y for ball
        //then sets image view and rectangle (used for collision detection) to said x and y
        double ballX = widthOfScreen / 2 - myBouncer.getBoundsInLocal().getWidth() / 2;
        double ballY = heightOfScreen / 2 - myBouncer.getBoundsInLocal().getHeight() / 2;
        myBouncer.setX(ballX);
        myBouncer.setY(ballY);
        
        //Creates a transparent rectangle that follows ball to sense collisions
        myBouncerCollisionSensor = new Rectangle(ballX, ballY, BALL_RADIUS, BALL_RADIUS);
        myBouncerCollisionSensor.setFill(Color.TRANSPARENT);
        
        root.getChildren().add(myBouncer);
        root.getChildren().add(myBouncerCollisionSensor);
    }
    
    public void updateBall(double elapsedTime, double bouncerSpeedX, double bouncerSpeedY){
    	double newX = myBouncer.getX() + bouncerSpeedX * elapsedTime;
    	double newY = myBouncer.getY() + bouncerSpeedY * elapsedTime;
    	myBouncer.setX(newX);
    	myBouncer.setY(newY);
    	myBouncerCollisionSensor.setX(newX);
    	myBouncerCollisionSensor.setY(newY);
    }
    
    public Rectangle getCollisionSensor(){
    	return myBouncerCollisionSensor;
    }
    
    public double getY(){
    	return myBouncer.getY();
    }
    
    public double getX(){
    	return myBouncer.getX();
    }
}
