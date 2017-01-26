//This entire file is part of my masterpiece.
//BELAL TAHER

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block{
    	
    	public static final int HITS_TO_BREAK = 2; 	
    	
    	private Rectangle blockCollisionDetector;
    	private int hitsLeft = 2;
    	private int type;
    	private ImageView brickImage; 
    	
    
    	
    	public Block(Group root, int xPosition, int yPosition, int width, int height, int aType){
    		hitsLeft = HITS_TO_BREAK;
    	  		
    		type = aType;
    		blockCollisionDetector = new Rectangle(xPosition, yPosition, width, height);
    		String typeOfBrick = "brick" + aType + ".gif";
            Image brick = new Image(getClass().getClassLoader().getResourceAsStream(typeOfBrick));
            brickImage = new ImageView(brick);
            brickImage.setX(xPosition);
            brickImage.setY(yPosition);
    		blockCollisionDetector.setFill(Color.TRANSPARENT);
       		root.getChildren().add(blockCollisionDetector);
    		root.getChildren().add(brickImage);
    	}
    	
    	public void setType(int aType){
    		type=aType;
    	}
    	
    	public void updateBrick(Group root){
    		if(hitsLeft ==1 && type+5 <= 9){
    			type = type + 5;
    			root.getChildren().remove(brickImage);
    			double x = blockCollisionDetector.getX();
    			double y = blockCollisionDetector.getY();
    			Image brick = new Image(getClass().getClassLoader().getResourceAsStream("brick" + type + ".gif"));
                brickImage = new ImageView(brick);
                brickImage.setX(x);
                brickImage.setY(y);
                root.getChildren().add(brickImage);
    		}
    		if(hitsLeft ==0){
    			root.getChildren().remove(brickImage);
    		}
    	}
    	
    	public int getHitsLeft(){
    		return hitsLeft;
    	}
    	
    	public void setHitsLeft(int newHitsLeft){
    		hitsLeft = newHitsLeft;
    	}
    	
    	public int getType(){
    		return type;
    	}
    	
    	public Rectangle getBlockCollisionDetector(){
    		return blockCollisionDetector;
    	}
    }

