// test
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class BreakoutEdition extends Application{

    public static final String TITLE = "Example JavaFX";
    
    public static final String BALL_IMAGE = "ball.gif";
    public static final String PADDLE_IMAGE = "paddle.gif";
    
    public static final int SIZE = 400;
    public static final Paint BACKGROUND = Color.WHITE;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int KEY_INPUT_SPEED = 15;
    public static final double GROWTH_RATE = 1.1;
    
    public static final int BALL_RADIUS = 12;
    public static final int PADDLE_WIDTH = 50;
    public static final int PADDLE_LENGTH = PADDLE_WIDTH/5;
    public static final int ROTATION_LIMIT = 30;
    public static final int ROTATING_FACTOR = 5;
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
    
    //Fields for ball
    private ImageView myBouncer;
    private Rectangle myBouncerCollisionSensor;
    public double maxBouncerSpeed = 200;
    private double bouncerSpeedX = 0.0;
    private double bouncerSpeedY = maxBouncerSpeed;
    private boolean firePower = false;
    
    //Fields for paddle
    private ImageView myPaddleImage;
    private Rectangle myPaddle;
    
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
 public class Block{
    	
    	public static final int HITS_TO_BREAK = 2; 	
    	
    	private Rectangle blockCollisionDetector;
    	private int hitsLeft = 2;
    	private int type;
    	private ImageView brickImage; 
    	
    
    	
    	public Block(int xPosition, int yPosition, int width, int height, int aType){
    		hitsLeft = HITS_TO_BREAK;
    		type= aType;
    		
    		type = aType;
    		blockCollisionDetector = new Rectangle(xPosition, yPosition, width, height);
    		String typeOfBrick = "brick" + aType + ".gif";
            Image brick = new Image(getClass().getClassLoader().getResourceAsStream(typeOfBrick));
            brickImage = new ImageView(brick);
            brickImage.setX(xPosition);
            brickImage.setY(yPosition);
    	}
    	
    	public void setType(int aType){
    		type=aType;
    	}
    }
    
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
        createBall(width, height);
        
        //creates paddle and sets color
        createPaddle(width, height);

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
        	    blocks[row][cols] = new Block(WALL_TO_BRICK_SPACE + (BRICK_WIDTH*row), TOP_TO_BRICK_SPACE + (BRICK_HEIGHT*cols), BRICK_WIDTH, BRICK_HEIGHT, 0);
        		blocks[row][cols].blockCollisionDetector.setFill(Color.TRANSPARENT);
        	}
        }
    }
    
    public void createBall(int widthOfScreen, int heightOfScreen){
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
    }
    
    public void createPaddle(int widthOfScreen, int heightOfScreen){
        myPaddle = new Rectangle(widthOfScreen / 2 - (PADDLE_WIDTH/2), heightOfScreen / 2 + 150, PADDLE_WIDTH, PADDLE_LENGTH);
        myPaddle.setFill(Color.TRANSPARENT);
        
        Image paddleImage = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
        myPaddleImage = new ImageView(paddleImage);
        myPaddleImage.setX(widthOfScreen / 2 - (PADDLE_WIDTH/2));
        myPaddleImage.setY(heightOfScreen / 2 + 150);
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
    	root.getChildren().add(myPaddle);
        root.getChildren().add(myPaddleImage);  
        
        root.getChildren().add(topSectionRight);
        root.getChildren().add(middleSectionRight);
        root.getChildren().add(bottomSectionRight);
      
        root.getChildren().add(topSectionLeft);
        root.getChildren().add(middleSectionLeft);
        root.getChildren().add(bottomSectionLeft);
        
        root.getChildren().add(top);
        
        root.getChildren().add(myBouncer);
        root.getChildren().add(myBouncerCollisionSensor);
        
        for(int row = 0; row < ROWS; row++){
        	for(int cols = 0; cols < COLS; cols++){
        		root.getChildren().add(blocks[row][cols].blockCollisionDetector);
        		root.getChildren().add(blocks[row][cols].brickImage);
        	}
        }
    }
    
    private void step (double elapsedTime) {
    	
    	//Updates position of ball and collision detector
    	updateBall(elapsedTime);
    	updateBricks();

        // check if ball collision detector and paddle are colliding
        Shape intersectBallPaddle = Shape.intersect(myPaddle, myBouncerCollisionSensor);
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
    
    public void updateBall(double elapsedTime){
    	double newX = myBouncer.getX() + bouncerSpeedX * elapsedTime;
    	double newY = myBouncer.getY() + bouncerSpeedY * elapsedTime;
    	myBouncer.setX(newX);
    	myBouncer.setY(newY);
    	myBouncerCollisionSensor.setX(newX);
    	myBouncerCollisionSensor.setY(newY);
    }
    
    public void updateBricks(){
    	for(int row = 0; row < ROWS; row++){
        	for(int cols = 0; cols < COLS; cols++){
        		if(blocks[row][cols].hitsLeft ==1){
        			int type = blocks[row][cols].type;
        			type = type + 5;
        			root.getChildren().remove(blocks[row][cols].brickImage);
        			double x = blocks[row][cols].blockCollisionDetector.getX();
        			double y = blocks[row][cols].blockCollisionDetector.getY();
        			Image brick = new Image(getClass().getClassLoader().getResourceAsStream("brick" + type + ".gif"));
                    blocks[row][cols].brickImage = new ImageView(brick);
                    blocks[row][cols].brickImage.setX(x);
                    blocks[row][cols].brickImage.setY(y);
                    root.getChildren().add(blocks[row][cols].brickImage);
        		}
        		if(blocks[row][cols].hitsLeft == 0){
        			if(blocks[row][cols].type == 1){
        				bouncerSpeedX = 0;
        				bouncerSpeedY = maxBouncerSpeed;
        			}
        			if(blocks[row][cols].type == 2 && maxBouncerSpeed < UPPER_LIMIT_BOUNCER_SPEED){
        				maxBouncerSpeed=maxBouncerSpeed-50;
        			}
        			if(blocks[row][cols].type == 3 && maxBouncerSpeed > LOWER_LIMIT_BOUNCER_SPEED){
        				maxBouncerSpeed=maxBouncerSpeed+50;
        			}
        			if(blocks[row][cols].type==4){
        				firePower=true;
        			}
        			root.getChildren().remove(blocks[row][cols].brickImage);
        			destroyed++;
        			blocks[row][cols].hitsLeft = -1;
        		}
        	}
        }
    }
    
    //helper method for bouncing off paddle
    public void bounceOnPaddle(double colliding){
    	if(colliding != -1){
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
    	for(int row = 0; row < ROWS; row++){
        	for(int cols = 0; cols < COLS; cols++){
        		Shape intersect = Shape.intersect(blocks[row][cols].blockCollisionDetector, myBouncerCollisionSensor);
                if(intersect.getBoundsInLocal().getWidth() != -1){
                	if(myBouncer.getY() < blocks[row][cols].blockCollisionDetector.getY() &&
                	   myBouncer.getY() > blocks[row][cols].blockCollisionDetector.getY()+BRICK_HEIGHT){
                		bouncerSpeedX = -bouncerSpeedX;
                	}
                	bouncerSpeedY = -bouncerSpeedY;
                	if(firePower){
                		blocks[row][cols].hitsLeft =0;
                		firePower = false;
                	}
                	else{
                		blocks[row][cols].hitsLeft--;
                	}
                	if(blocks[row][cols].hitsLeft == 0){
                		blocks[row][cols].blockCollisionDetector.relocate(0, 0);
                	}
                }
        	}
        }
    }
    
    //makes paddle move left and right when corresponding arrow key is hit
    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT && myPaddle.getX() < SIZE - WALL_WIDTH - PADDLE_LENGTH - BUFFER_FACTOR) {
            myPaddle.setX(myPaddle.getX() + KEY_INPUT_SPEED);
            myPaddleImage.setX(myPaddleImage.getX() + KEY_INPUT_SPEED);
        }
        else if (code == KeyCode.LEFT && myPaddle.getX() > WALL_WIDTH) {
            myPaddle.setX(myPaddle.getX() - KEY_INPUT_SPEED);
            myPaddleImage.setX(myPaddleImage.getX() - KEY_INPUT_SPEED);
        }
        else if (code == KeyCode.UP && myPaddle.getRotate() < ROTATION_LIMIT) {
            myPaddle.setRotate(myPaddle.getRotate()+ROTATING_FACTOR);
            myPaddleImage.setRotate(myPaddleImage.getRotate()+ROTATING_FACTOR);
        }
        else if (code == KeyCode.DOWN && myPaddle.getRotate() > -ROTATION_LIMIT) {
        	myPaddle.setRotate(myPaddle.getRotate()-ROTATING_FACTOR);
        	myPaddleImage.setRotate(myPaddleImage.getRotate()-ROTATING_FACTOR);
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
        createBall(SIZE, SIZE);
        
        //creates paddle and sets color
        createPaddle(SIZE, SIZE);

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
    
    public void createBlocksLevelTwo(){
        for(int row = 0; row < ROWS; row++){
        	for(int cols = 0; cols < COLS; cols++){
        		Random rand = new Random();
        		int type = rand.nextInt(3);
        	    blocks[row][cols] = new Block(WALL_TO_BRICK_SPACE + (BRICK_WIDTH*row), TOP_TO_BRICK_SPACE + (BRICK_HEIGHT*cols), BRICK_WIDTH, BRICK_HEIGHT, type);
        		blocks[row][cols].blockCollisionDetector.setFill(Color.TRANSPARENT);
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
        createBall(SIZE, SIZE);
        
        //creates paddle and sets color
        createPaddle(SIZE, SIZE);

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
    
    public void createBlocksLevelThree(){
        for(int row = 0; row < ROWS; row++){
        	for(int cols = 0; cols < COLS; cols++){
        		Random rand = new Random();
        		int type = rand.nextInt(5);
        	    blocks[row][cols] = new Block(WALL_TO_BRICK_SPACE + (BRICK_WIDTH*row), TOP_TO_BRICK_SPACE + (BRICK_HEIGHT*cols), BRICK_WIDTH, BRICK_HEIGHT, type);
        		blocks[row][cols].blockCollisionDetector.setFill(Color.TRANSPARENT);
        	}
        }
    }
    
    public void checkIfGameOver(){
    	if(myBouncer.getY() > SIZE){
    		System.exit(0); //would have added dialog box but due to time constraint
    		                //decided to just end the game
    	}
    }
    
    public static void main (String[] args) {
        launch(args);
    }
}
    
