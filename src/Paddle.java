//This entire file is part of my masterpiece.
//BELAL TAHER

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {
	
    public static final String PADDLE_IMAGE = "paddle.gif";
    public static final int KEY_INPUT_SPEED = 30;
    public static final int ROTATION_LIMIT = 30;
    public static final int ROTATING_FACTOR = 5;
	
	private Rectangle myPaddleCollisionSensor;
	private ImageView myPaddleImage;

	public Paddle(Group root, int width, int height, int paddleWidth, int paddleHeight){
	        myPaddleCollisionSensor = new Rectangle(width / 2 - (paddleWidth/2), height / 2 + 150, paddleWidth, paddleHeight);
	        myPaddleCollisionSensor.setFill(Color.TRANSPARENT);
	        
	        Image paddleImage = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
	        myPaddleImage = new ImageView(paddleImage);
	        myPaddleImage.setX(width / 2 - (paddleWidth/2));
	        myPaddleImage.setY(height / 2 + 150);
	        
	    	root.getChildren().add(myPaddleCollisionSensor);
	        root.getChildren().add(myPaddleImage);
	    }
	
	public Rectangle getCollisionSensor(){
		return myPaddleCollisionSensor;
	}
	
	public void reactToKey(KeyCode code, int size, int wallWidth, int paddleLength, int bufferFactor){
		if (code == KeyCode.RIGHT && myPaddleImage.getX() < size - wallWidth - paddleLength - bufferFactor) {
			myPaddleCollisionSensor.setX(myPaddleCollisionSensor.getX() + KEY_INPUT_SPEED);
			myPaddleImage.setX(myPaddleImage.getX() + KEY_INPUT_SPEED);
		}
		else if (code == KeyCode.LEFT && myPaddleImage.getX() > wallWidth) {
			myPaddleCollisionSensor.setX(myPaddleCollisionSensor.getX() - KEY_INPUT_SPEED);
			myPaddleImage.setX(myPaddleImage.getX() - KEY_INPUT_SPEED);
		}
		else if (code == KeyCode.UP && myPaddleImage.getRotate() < ROTATION_LIMIT) {
			myPaddleCollisionSensor.setRotate(myPaddleCollisionSensor.getRotate()+ROTATING_FACTOR);
			myPaddleImage.setRotate(myPaddleImage.getRotate()+ROTATING_FACTOR);
		}
		else if (code == KeyCode.DOWN && myPaddleImage.getRotate() > -ROTATION_LIMIT) {
			myPaddleCollisionSensor.setRotate(myPaddleCollisionSensor.getRotate()-ROTATING_FACTOR);
			myPaddleImage.setRotate(myPaddleImage.getRotate()-ROTATING_FACTOR);
		}
	}
}
