//This entire file is part of my masterpiece.
//BELAL TAHER

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
public class BreakoutEdition extends Application{
    public static final String TITLE = "Example JavaFX";
    
    public static final int SIZE = 400;
    public static final Paint BACKGROUND = Color.WHITE;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final double GROWTH_RATE = 1.1;
    
    
    public static final int PADDLE_WIDTH = 50;
    public static final int PADDLE_LENGTH = PADDLE_WIDTH/5;

    public static final int UPPER_LIMIT_BOUNCER_SPEED = 350;
    public static final int LOWER_LIMIT_BOUNCER_SPEED = 100;
    public static final int BUFFER_FACTOR = 40;
    public static final int INITIAL_MAX_BOUNCER_SPEED = 200;
    
    public static final int WALL_LENGTH = SIZE/3;
    public static final int WALL_WIDTH = WALL_LENGTH/6;
    public static final int ROWS = 5;
    public static final int COLS = 5;
    public static final int WALL_TO_BRICK_SPACE = WALL_WIDTH + 3;
    public static final int BRICK_WIDTH = 70; 
    public static final int TOP_TO_BRICK_SPACE = WALL_WIDTH + 10;
    public static final int BRICK_HEIGHT = 20;
    
    private Group root;
    
    private int level = 1;
    
    //The scene itself
    private Scene myScene;
    
    //Field for ball
    private Bouncer bouncer; 
    
    //Field for paddle  
    private Paddle paddle;
    
    public double maxBouncerSpeed = 200;
    private double bouncerSpeedX = 0.0;
    private double bouncerSpeedY = maxBouncerSpeed;
    private boolean firePower = false;
    
    //Fields for "walls" 
    private Rectangle topSectionRight;
    private Rectangle middleSectionRight;
    private Rectangle bottomSectionRight; 
    
    private Rectangle topSectionLeft;
    private Rectangle middleSectionLeft;
    private Rectangle bottomSectionLeft; 
    
    private Rectangle top;
    
    //count of how many bricks are destroyed
    private int destroyed = 0;
    
    //block has so many characteristics that I figured it would be more compact to create a separate class
 
    
    //Fields for blocks
    private Block[][] blocks = new Block[ROWS][COLS];
    
    public void start (Stage s){
    	//attach scene to the stage and display it
    	Scene scene = setupGameLevelOne(SIZE, SIZE, BACKGROUND);
        s.setScene(scene);
        s.setTitle(TITLE);
        s.show();
        // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                                      e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }
    
    //Create the game's "scene": what shapes will be in the game and their starting properties
    private Scene setupGameLevelOne (int width, int height, Paint background) {
        // create one top level collection to organize the things in the scene
        root = new Group();
        // create a place to see the shapes
        myScene = new Scene(root, width, height, background);
        
        //creates bricks for level
        createBlocksLevelOne();
        
        //creates ball
        bouncer = new Bouncer(root, SIZE, SIZE);
        
        //creates paddle and sets color
        paddle = new Paddle(root, SIZE, SIZE, PADDLE_WIDTH, PADDLE_LENGTH);
        
        //creates walls and sets color
        createRightBordersLevelOne();
        createLeftBordersLevelOne();
        
        //creates top
        createTop();
        
        //adds the paddle, ball, and collision detector to the scene
        addChildren(root);
             
        // respond to input
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return myScene;
    }
    
    public void createBlocksLevelOne(){
        for(int row = 0; row < ROWS; row++){
        	for(int cols = 0; cols < COLS; cols++){
        	    blocks[row][cols] = new Block(root, WALL_TO_BRICK_SPACE + (BRICK_WIDTH*row), TOP_TO_BRICK_SPACE + (BRICK_HEIGHT*cols), BRICK_WIDTH, BRICK_HEIGHT, 0);
        	}
        }
    }
    
    public void createRightBordersLevelOne(){
	    topSectionRight = new Rectangle(0, 0, WALL_WIDTH, WALL_LENGTH);
	    topSectionRight.setFill(Color.BLACK);
	    middleSectionRight = new Rectangle(0, WALL_LENGTH, WALL_WIDTH, WALL_LENGTH);
	    middleSectionRight.setFill(Color.BLACK);
	    bottomSectionRight = new Rectangle(0, WALL_LENGTH*2, WALL_WIDTH, WALL_LENGTH);
	    bottomSectionRight.setFill(Color.BLACK);
    }
    
    public void createLeftBordersLevelOne(){
    	topSectionLeft = new Rectangle(SIZE-WALL_WIDTH, 0, WALL_WIDTH, WALL_LENGTH);
    	topSectionLeft.setFill(Color.BLACK);
    	middleSectionLeft = new Rectangle(SIZE-WALL_WIDTH, WALL_LENGTH, WALL_WIDTH, WALL_LENGTH);
    	middleSectionLeft.setFill(Color.BLACK);
    	bottomSectionLeft = new Rectangle(SIZE-WALL_WIDTH, WALL_LENGTH*2, WALL_WIDTH, WALL_LENGTH);
    	bottomSectionLeft.setFill(Color.BLACK);
    }
    
    public void createTop(){
        top = new Rectangle(WALL_WIDTH, 0, SIZE-(WALL_WIDTH*2),WALL_WIDTH);
        top.setFill(Color.BLACK);
    }
    
    public void addChildren(Group root){  
        root.getChildren().add(top);  
        root.getChildren().add(topSectionRight);
        root.getChildren().add(middleSectionRight);
        root.getChildren().add(bottomSectionRight);
      
        root.getChildren().add(topSectionLeft);
        root.getChildren().add(middleSectionLeft);
        root.getChildren().add(bottomSectionLeft);
        for(int row = 0; row < ROWS; row++){
        	for(int cols = 0; cols < COLS; cols++){
 
        	}
        }
    }
    
    public void createRightBordersLevelTwo(){
	    topSectionRight = new Rectangle(0, 0, WALL_WIDTH, WALL_LENGTH);
	    topSectionRight.setFill(Color.RED);
	    middleSectionRight = new Rectangle(0, WALL_LENGTH, WALL_WIDTH, WALL_LENGTH);
	    middleSectionRight.setFill(Color.GREEN);
	    bottomSectionRight = new Rectangle(0, WALL_LENGTH*2, WALL_WIDTH, WALL_LENGTH);
	    bottomSectionRight.setFill(Color.RED);
    }
    
    public void createLeftBordersLevelTwo(){
    	topSectionLeft = new Rectangle(SIZE-WALL_WIDTH, 0, WALL_WIDTH, WALL_LENGTH);
    	topSectionLeft.setFill(Color.RED);
    	middleSectionLeft = new Rectangle(SIZE-WALL_WIDTH, WALL_LENGTH, WALL_WIDTH, WALL_LENGTH);
    	middleSectionLeft.setFill(Color.GREEN);
    	bottomSectionLeft = new Rectangle(SIZE-WALL_WIDTH, WALL_LENGTH*2, WALL_WIDTH, WALL_LENGTH);
    	bottomSectionLeft.setFill(Color.RED);
    }
    
    public void createRightBordersLevelThree(){
	    topSectionRight = new Rectangle(0, 0, WALL_WIDTH, WALL_LENGTH);
	    topSectionRight.setFill(Color.GREEN);
	    middleSectionRight = new Rectangle(0, WALL_LENGTH, WALL_WIDTH, WALL_LENGTH);
	    middleSectionRight.setFill(Color.GREEN);
	    bottomSectionRight = new Rectangle(0, WALL_LENGTH*2, WALL_WIDTH, WALL_LENGTH);
	    bottomSectionRight.setFill(Color.GREEN);
    }
    
    public void createLeftBordersLevelThree(){
    	topSectionLeft = new Rectangle(SIZE-WALL_WIDTH, 0, WALL_WIDTH, WALL_LENGTH);
    	topSectionLeft.setFill(Color.GREEN);
    	middleSectionLeft = new Rectangle(SIZE-WALL_WIDTH, WALL_LENGTH, WALL_WIDTH, WALL_LENGTH);
    	middleSectionLeft.setFill(Color.GREEN);
    	bottomSectionLeft = new Rectangle(SIZE-WALL_WIDTH, WALL_LENGTH*2, WALL_WIDTH, WALL_LENGTH);
    	bottomSectionLeft.setFill(Color.GREEN);
    }
    
    private void step (double elapsedTime) {
    	
    	//Updates position of ball and collision detector
    	bouncer.updateBall(elapsedTime, bouncerSpeedX, bouncerSpeedY);
    	updateBricks();
    	
    	Rectangle myBouncerCollisionSensor = bouncer.getCollisionSensor();
    	Rectangle myPaddleCollisionSensor = paddle.getCollisionSensor();
    	
        // check if ball collision detector and paddle are colliding
        Shape intersectBallPaddle = Shape.intersect(myPaddleCollisionSensor, myBouncerCollisionSensor);
        bounceOnPaddle(intersectBallPaddle.getBoundsInLocal().getWidth());
        
        // check if ball collision detector and top section on either side are colliding
        Shape intersectBallTopSectionLeft = Shape.intersect(topSectionLeft, myBouncerCollisionSensor);
        Shape intersectBallTopSectionRight = Shape.intersect(topSectionRight, myBouncerCollisionSensor);
        bounceOnWall(intersectBallTopSectionLeft.getBoundsInLocal().getWidth(), topSectionLeft.getFill());
        bounceOnWall(intersectBallTopSectionRight.getBoundsInLocal().getWidth(), topSectionRight.getFill()); //Tried consolidating this method into one by making
                                                                                  //an if statement with || in order to reduce lines in thsi method but
                                                                                  //for some reason this made the bouncer stop acting appropriately 
                                                                                  //couldn't find out why bc this method seems unrelated to bouncer properties
        // check if ball collision detector and middle section on either side are colliding
        Shape intersectBallMiddleSectionLeft = Shape.intersect(middleSectionLeft, myBouncerCollisionSensor);
        Shape intersectBallMiddleSectionRight = Shape.intersect(middleSectionRight, myBouncerCollisionSensor);
        bounceOnWall(intersectBallMiddleSectionLeft.getBoundsInLocal().getWidth(), middleSectionLeft.getFill());
        bounceOnWall(intersectBallMiddleSectionRight.getBoundsInLocal().getWidth(), middleSectionRight.getFill());
        
        // check if ball collision detector and bottom section on either side are colliding
        Shape intersectBallBottomSectionLeft = Shape.intersect(bottomSectionLeft, myBouncerCollisionSensor);
        Shape intersectBallBottomSectionRight = Shape.intersect(bottomSectionRight, myBouncerCollisionSensor);
        bounceOnWall(intersectBallBottomSectionLeft.getBoundsInLocal().getWidth(), bottomSectionLeft.getFill());
        bounceOnWall(intersectBallBottomSectionRight.getBoundsInLocal().getWidth(), bottomSectionRight.getFill());
        
        // check if ball collision detector and top are colliding 
        Shape intersectBallTop= Shape.intersect(top, myBouncerCollisionSensor);
        bounceOnTop(intersectBallTop.getBoundsInLocal().getWidth());
        
        // check if ball collision detector and any of the blockCollisionDetectors are colliding
        checkCollisionsWithBlocks();
        
        checkEnd();
        
        checkIfGameOver();
  
    }
    
    
    public void updateBricks(){
    	for(int row = 0; row < ROWS; row++){
        	for(int cols = 0; cols < COLS; cols++){
        		blocks[row][cols].updateBrick(root);
        		if(blocks[row][cols].getHitsLeft() == 0){
        			if(blocks[row][cols].getType() == 6){
        				bouncerSpeedX = 0;
        				bouncerSpeedY = maxBouncerSpeed;
        			}
        			if(blocks[row][cols].getType() == 7 && maxBouncerSpeed < UPPER_LIMIT_BOUNCER_SPEED){
        				maxBouncerSpeed=maxBouncerSpeed-50;
        			}
        			if(blocks[row][cols].getType() == 8 && maxBouncerSpeed > LOWER_LIMIT_BOUNCER_SPEED){
        				maxBouncerSpeed=maxBouncerSpeed+50;
        			}
        			if(blocks[row][cols].getType() == 9){
        				firePower=true;
        			}

        			destroyed++;
        			blocks[row][cols].setHitsLeft(-1);
        		}
        	}
        }
    }
    
    //helper method for bouncing off paddle
    public void bounceOnPaddle(double colliding){
    	if(colliding != -1){
    		Rectangle myPaddle = paddle.getCollisionSensor();
    		double yFactor = Math.cos(Math.toRadians(myPaddle.getRotate()));
        	double xFactor = Math.sin(Math.toRadians(myPaddle.getRotate()));
            bouncerSpeedY = -maxBouncerSpeed*yFactor;
            if(xFactor!=0){
            	bouncerSpeedX = maxBouncerSpeed*xFactor;
            }
    	}
    }
    
    //helper method for bouncing off wall
    public void bounceOnWall(double colliding, Paint colorOfWall){
    	if(colliding != -1){
    		bouncerSpeedX = -bouncerSpeedX;
        	if(colorOfWall.equals(Color.RED) && maxBouncerSpeed > LOWER_LIMIT_BOUNCER_SPEED){
        		maxBouncerSpeed = maxBouncerSpeed -50;
        	}
        	if(colorOfWall.equals(Color.GREEN) && maxBouncerSpeed < UPPER_LIMIT_BOUNCER_SPEED){
        		maxBouncerSpeed = maxBouncerSpeed + 50;
        	}
    	}
    }
    
    //helper method for bouncing off top
    public void bounceOnTop(double colliding){
    	if(colliding != -1){
    		bouncerSpeedY = -bouncerSpeedY;
    	}
    }
    
    public void checkCollisionsWithBlocks(){
    	Rectangle myBouncerCollisionSensor = bouncer.getCollisionSensor();
    	for(int row = 0; row < ROWS; row++){
        	for(int cols = 0; cols < COLS; cols++){
        		Rectangle blockCollisionDetector = blocks[row][cols].getBlockCollisionDetector();
        		Shape intersect = Shape.intersect(blockCollisionDetector, myBouncerCollisionSensor);
                if(intersect.getBoundsInLocal().getWidth() != -1){
                	if(bouncer.getY() < blockCollisionDetector.getY() &&
                	   bouncer.getY() > blockCollisionDetector.getY()+BRICK_HEIGHT){
                		bouncerSpeedX = -bouncerSpeedX;
                	}
                	bouncerSpeedY = -bouncerSpeedY;
                	if(firePower){
                		blocks[row][cols].setHitsLeft(0);
                		firePower = false;
                	}
                	else{
                		blocks[row][cols].setHitsLeft(blocks[row][cols].getHitsLeft()-1);
                	}
                	if(blocks[row][cols].getHitsLeft() == 0){
                		blockCollisionDetector.relocate(0, 0);
                	}
                }
        	}
        }
    }
    
    //makes paddle move left and right when corresponding arrow key is hit
    private void handleKeyInput (KeyCode code) {
    	if(code == KeyCode.RIGHT || 
    	   code == KeyCode.LEFT  ||
    	   code == KeyCode.UP    ||
    	   code == KeyCode.DOWN){
    		paddle.reactToKey(code, SIZE, WALL_WIDTH, PADDLE_LENGTH, BUFFER_FACTOR);
    	}
        else if(code == KeyCode.A){
        	destroyed=ROWS*COLS;
        }
        else if(code ==KeyCode.F){
        	firePower = true;
        }
        else if(code == KeyCode.S && maxBouncerSpeed > LOWER_LIMIT_BOUNCER_SPEED){
        	maxBouncerSpeed = maxBouncerSpeed -50;
        }
    }
    
    public void checkEnd(){
    	if(destroyed == (ROWS*COLS)){
    		level++;
    		changeLevels(level);
    	}
    }
    
    public void changeLevels(int level){
    	if(level ==2){
    		advanceLevelTwo();
    	}
    	if(level==3){
    		advanceLevelThree();
    	}
    	if(level > 3){
    		System.exit(0); //would have added a dialog box but just 
    	}                   //end game bc of time constraint
    }
    
    private void advanceLevelTwo () {
        root.getChildren().clear();
        bouncerSpeedX=0;
        bouncerSpeedY = INITIAL_MAX_BOUNCER_SPEED;
        destroyed = 0;
        
        //creates bricks for level
        createBlocksLevelTwo();
        
        //creates ball
        bouncer = new Bouncer(root, SIZE, SIZE);
        
        //creates paddle and sets color
        paddle = new Paddle(root, SIZE, SIZE, PADDLE_WIDTH, PADDLE_LENGTH);
        //creates walls and sets color
        createRightBordersLevelTwo();
        createLeftBordersLevelTwo();
        
        //creates top
        createTop();
        
        //adds the paddle, ball, and collision detector to the scene
        addChildren(root);
             
        // respond to input
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    }
    
    public void createBlocksLevelTwo(){
        for(int row = 0; row < ROWS; row++){
        	for(int cols = 0; cols < COLS; cols++){
        		Random rand = new Random();
        		int type = rand.nextInt(3);
        	    blocks[row][cols] = new Block(root, WALL_TO_BRICK_SPACE + (BRICK_WIDTH*row), TOP_TO_BRICK_SPACE + (BRICK_HEIGHT*cols), BRICK_WIDTH, BRICK_HEIGHT, type);
        	}
        }
    }
    
    public void advanceLevelThree(){
    	root.getChildren().clear();
    	bouncerSpeedX =0;
    	bouncerSpeedY = INITIAL_MAX_BOUNCER_SPEED;
    	destroyed = 0;
        
        //creates bricks for level
        createBlocksLevelThree();
        
        //creates ball
        bouncer = new Bouncer(root, SIZE, SIZE);
        
        //creates paddle and sets color
        paddle = new Paddle(root, SIZE, SIZE, PADDLE_WIDTH, PADDLE_LENGTH);
        //creates walls and sets color
        createRightBordersLevelThree();
        createLeftBordersLevelThree();
        
        //creates top
        createTop();
        
        //adds the paddle, ball, and collision detector to the scene
        addChildren(root);
             
        // respond to input
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    }

    
    public void createBlocksLevelThree(){
        for(int row = 0; row < ROWS; row++){
        	for(int cols = 0; cols < COLS; cols++){
        		Random rand = new Random();
        		int type = rand.nextInt(5);
        	    blocks[row][cols] = new Block(root, WALL_TO_BRICK_SPACE + (BRICK_WIDTH*row), TOP_TO_BRICK_SPACE + (BRICK_HEIGHT*cols), BRICK_WIDTH, BRICK_HEIGHT, type);
        	}
        }
    }
    
    public void checkIfGameOver(){
    	if(bouncer.getY() > SIZE){
    		System.exit(0); //would have added dialog box but due to time constraint
    		                //decided to just end the game
    	}
    }
    
    public static void main (String[] args) {
        launch(args);
    }
}
    