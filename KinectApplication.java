import processing.core.*;
import SimpleOpenNI.*;
import fullscreen.*;
import java.util.*;

// The Kinect Application code is based on a Processing applet
public class KinectApplication extends PApplet {

SoftFullScreen fs;

SimpleOpenNI          context;

// NITE
XnVSessionManager     sessionManager;
XnVSelectableSlider2D trackPad;

int gridX = 9;
int gridY = 7;

// The following variables pertain to the textboxes in the corners of the application
String topLeft = "Fitbit";
int topLeftSize = 185;

String topRight = "Exercise of the Day";
int topRightSize = 185;
int topRightX = 421;
int topRightY = 18;

String botLeft = "Daily Calorie Calculator";
int botLeftSize = 185;
int botLeftX = 33;
int botLeftY = 275;

String botRight = "Fitness and Diet Articles";
int botRightSize = 185;
int botRightX = 421;
int botRightY = 277;

int botRightCounter = 1;

String center = "";
int centerSize = 0;
int centerX = 227;
int centerY = 148;

Trackpad   trackPadViz;

static WebBrowser botRightBrowser = new WebBrowser();

public static void main(String args[]) {
	PApplet.main(new String[] {"--present", "KinectApplication"});
	botRightBrowser.openBrowser();
}


public void setup()
{
  context = new SimpleOpenNI(this,SimpleOpenNI.RUN_MODE_MULTI_THREADED);
   
  // mirror is by default enabled
  context.setMirror(true);
  
  // enable depthMap generation 
  if(context.enableDepth() == false)
  {
     println("Can't open the depth map, maybe the camera is not connected!"); 
     exit();
     return;
  }
  
  // enable the hands + gesture
  context.enableGesture();
  context.enableHands();
  context.enableRGB();
  //context.enableRGB(1280,1024,15);
 
  // setup NITE 
  sessionManager = context.createSessionManager("Click,Wave", "RaiseHand");

  trackPad = new XnVSelectableSlider2D(gridX,gridY);
  sessionManager.AddListener(trackPad);

  trackPad.RegisterItemHover(this);
  trackPad.RegisterValueChange(this);
  trackPad.RegisterItemSelect(this);
  
  trackPad.RegisterPrimaryPointCreate(this);
  trackPad.RegisterPrimaryPointDestroy(this);

  // create gui viz
  trackPadViz = new Trackpad(new PVector(context.depthWidth()/2, context.depthHeight()/2,0),
                                         gridX,gridY,50,50,15);  

  size(context.depthWidth(), context.depthHeight());
  fs = new SoftFullScreen(this);
  fs.enter(); 
  smooth();
  
   // info text
  println("1. Wave till the tiles get green");  
  println("2. The relative hand movement will select the tiles");  
  
}

public void draw()
{
  // update the cam
  context.update();
  
  // update nite
  context.update(sessionManager);
  
  // draw rgbImageMap
  image(context.rgbImage(), 0, 0);
  
  trackPadViz.draw();
  
  // Top Left Text Box
  fill(0,255,0,200);
  rect(33,18,topLeftSize,topLeftSize);
  fill(255,0,0);
  text(topLeft, 45, 30, topLeftSize - 20, topLeftSize - 20);
  
  // Top Right Text Box
  fill(255,0,0,200);
  rect(topRightX, topRightY, topRightSize, topRightSize);
  fill(0,0,255);
  text(topRight, topRightX + 12, topRightY + 12, topRightSize - 20, topRightSize - 20);
  
  // Bottom Left Text Box
  fill(100,100,100,200);
  rect(botLeftX, botLeftY, botLeftSize, botLeftSize);
  fill(20,200,20);
  text(botLeft, botLeftX + 12, botLeftY + 12, botLeftSize - 20, botLeftSize - 20);
  
  // Bottom Right Text Box
  fill(0,0,255,200);
  rect(botRightX, botRightY, botRightSize, botRightSize);
  fill(255,225,0);
  text(botRight, botRightX + 12, botRightY, botRightSize - 20, botRightSize - 20);
  
  // Center Submit Text Box
  fill(100,35,255,200);
  rect(centerX, centerY, centerSize, centerSize);
  fill(255,125,0);
  text(center, centerX + 12, centerY, 165, 165);
  textAlign(CENTER, CENTER);
  
}

public void keyPressed()
{
  switch(key)
  {
  case 'e':
    // end sessions
    sessionManager.EndSession();
    println("end session");
    break;
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
// session callbacks

void onStartSession(PVector pos)
{
  println("onStartSession: " + pos);
}

void onEndSession()
{
  println("onEndSession: ");
}

void onFocusSession(String strFocus,PVector pos,float progress)
{
  println("onFocusSession: focus=" + strFocus + ",pos=" + pos + ",progress=" + progress);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
// XnVSelectableSlider2D callbacks

void onItemHover(int nXIndex,int nYIndex)
{
  println("onItemHover: nXIndex=" + nXIndex +" nYIndex=" + nYIndex);
  
  trackPadViz.update(nXIndex,nYIndex);
}

void onValueChange(float fXValue,float fYValue)
{
 // println("onValueChange: fXValue=" + fXValue +" fYValue=" + fYValue);
}

/** This is where the bulk of the code is located because it is called when the user waves
 * their hand and selects a square on the screen.
 */
void onItemSelect(int nXIndex,int nYIndex,int eDir)
{
  println("onItemSelect: nXIndex=" + nXIndex + " nYIndex=" + nYIndex + " eDir=" + eDir);
  trackPadViz.push(nXIndex,nYIndex,eDir);
  
  botRightBrowser.display.syncExec(new Runnable() {public void run(){
	  if (botRightBrowser.getVisibility(botRightBrowser.cShell) == false) {
		  textReset();
		  sizeReset();
	  }
  }});
  
  // TOP RIGHT CORNER IS SELECTED
  if (nXIndex >= 6 && nYIndex >= 4) {
	  
    botRightBrowser.display.syncExec(new Runnable() {public void run(){
    	if (botRightBrowser.getVisibility(botRightBrowser.shell)) {
            // If the browser is displaying articles, the top right text box is a back button that
          	// displays the previous health article
            if (botRightCounter <= 11 && botRightCounter > 1) {
    			botRightCounter--;
    		}
    		else botRightCounter = 1;
            System.out.println(botRightCounter);
            updateWebsite();
        }
    // If the bottom left shell (daily calorie calculator) is visible, the top right text box 
    //	is an increase height button
    	else if (botRightBrowser.getVisibility(botRightBrowser.cShell)) {
    		botRightBrowser.childShell.incHeight();
    	}
    // If no other windows are open, a browser will be opened with an exercise video
    	else {
    		//Calendar rightNow = Calendar.getInstance();
    		topLeft = "Close YouTube Video";
    	    // topRightX = 206;
    	    // topRightSize = 400;
    		botRightBrowser.setYouTubeBrowser();
    		botRightBrowser.setVisibility(botRightBrowser.shell, true);
    	    }
    }
    });
  }
  // BOTTOM RIGHT CORNER IS SELECTED
  else if (nXIndex >=6 && nYIndex <=2) {
	  botRightBrowser.display.syncExec(new Runnable() {public void run(){
	// If bottom left shell (daily calorie calculator) is visible, this text box 
	// is a decrease height button
	  if (botRightBrowser.getVisibility(botRightBrowser.cShell)) {
  		botRightBrowser.childShell.decHeight();
  	}
	// If no other windows are open, this text box opens health articles in a browser
	  else {  
	browserReset("cShell");
    botRight = "This corner will open fitness and health articles in a browser";
    
    	if (botRightBrowser.getVisibility(botRightBrowser.shell)) {
    		if (botRightCounter < 11) {
    			botRightCounter++;
    		}
    		else botRightCounter = 1;
            System.out.println(botRightCounter);
        }
    	 updateWebsite();
	  }
    }});
  }
  // TOP LEFT CORNER IS SELECTED
  else if (nXIndex <= 2 && nYIndex >=4) {
	  botRightBrowser.display.syncExec(new Runnable() {public void run(){
		  // If daily calorie calculator shell is open, this is an increase weight button
		  if (botRightBrowser.getVisibility(botRightBrowser.cShell)) {
	  		botRightBrowser.childShell.incWeight();
	  	}
		// If browser is open and set to fitbit activities, this changes it to fitbit foods
		  else if (botRightBrowser.getVisibility(botRightBrowser.shell)) {
			  if (botRightBrowser.getWebsite() == "http://www.fitbit.com/user/22S9ZX/activities") {
				  botRightBrowser.setFitbitWebsite("http://www.fitbit.com/user/22S9ZX/foods");
				  botRightBrowser.setVisibility(botRightBrowser.shell, true);
			  }
			  // If browser is open and set to Youtube or health articles, this closes the browser
			  else {
			  updateWebsite();
			  browserReset();
			  }
		  }
		  // If no other windows are open, this text box links to the user's Fitbit profile
		  else {
	browserReset();  
    topLeft = "This is where recent foods and activities will be pulled from Fitbit";
    topLeftSize = 400;
    botRightBrowser.setFitbitWebsite("http://www.fitbit.com/user/22S9ZX/foods");
    botRightBrowser.setVisibility(botRightBrowser.shell, true);
		  }
	  }});
  }
  // BOTTOM LEFT CORNER IS SELECTED
  else if (nXIndex <=2 && nYIndex <=2) {
	  botRightBrowser.display.syncExec(new Runnable() {public void run(){
		  // If daily calorie calculator shell is open, this is an increase weight button
		  if (botRightBrowser.getVisibility(botRightBrowser.cShell)) {
	  		botRightBrowser.childShell.decWeight();
	  	}
		  else if (botRightBrowser.getVisibility(botRightBrowser.shell)) {
			// If the browser is currently displaying Fitbit foods, now display activities
	            if (botRightBrowser.getWebsite().equals("http://www.fitbit.com/user/22S9ZX/foods")) {
	            	//set website to activities
	            	botRightBrowser.setFitbitWebsite("http://www.fitbit.com/user/22S9ZX/activities");
	                botRightBrowser.setVisibility(botRightBrowser.shell, true);
	            }
	            // If browser is open to health articles, this closes the browser
	            else {browserReset();}
    }
		  // If nothing else is open, this opens the daily calorie calculator
		  else {
			  //browserReset("shell");
			    botLeft = "Decrease Weight";
			    centerSize = 185;
			    center = "User inputs their height/weight then presses this box to " +
			    		"determine how many calories they should consume daily to lose weight \n" +
			    		"Submit";
			    topLeft = "Increase Weight";
			    topRight = "Increase Height";
			    botRight = "Decrease Height";
			    botRightBrowser.setVisibility(botRightBrowser.cShell, true);
  }
	  }});
	  
}
  // CENTER SUBMIT BUTTON IS SELECTED
  else if (nXIndex >= 3 && nXIndex <= 5 && nYIndex >= 2 && nYIndex <= 4) {
   botRightBrowser.display.syncExec(new Runnable() {public void run(){
		  // If daily calorie calculator shell is open, this is a submit button
		  if (botRightBrowser.getVisibility(botRightBrowser.cShell)) {
	  		botRightBrowser.childShell.centerSubmit();
	  	}
		  else {
			  browserReset();
  }
}});
   }
  // IF ANY SQUARE THAT IS NOT A COLORED TEXT BOX IS SELECTED:
  else {
	  browserReset();
	  textReset();
	  sizeReset();
  }
}
// Reset all textboxes to the main menu text
void textReset() {
  topLeft = "Fitbit";
  topRight = "Exercise of the Day";
  botLeft = "Daily Calorie Calculator";
  botRight = "Fitness and Diet Articles";
  center = "";
}
// Reset all textboxes to the main menu size
void sizeReset() {
  topLeftSize = 185;
  topRightSize = 185;
  botLeftSize = 185;
  botRightSize = 185;
  centerSize = 0;
  
  topRightX = 421;
  topRightY = 18;
  botLeftX = 33;
  botLeftY = 275;
  botRightX = 421;
  botRightY = 277;
}
// Hide all open browsers and shells
void browserReset() {
	botRightBrowser.display.syncExec(new Runnable() {public void run(){
        botRightBrowser.setVisibility(botRightBrowser.shell, false);
        botRightBrowser.setVisibility(botRightBrowser.cShell, false);
    }
  });
}
// Hide a particular browser or shell
// "shell" for the browser
// "cShell" for the daily calorie calculator
void browserReset(final String sh) {
	botRightBrowser.display.syncExec(new Runnable() {public void run(){
		if (sh == "shell") {
        botRightBrowser.setVisibility(botRightBrowser.shell, false);
        System.out.println("Browser closed");
		}
		else if (sh.equals("cShell")) {
        botRightBrowser.setVisibility(botRightBrowser.cShell, false);
		}
    }
  });
}
// Updates website based on a counter to determine which website should be displayed when the
// user cycles back and forth through health articles
void updateWebsite() {
	botRightBrowser.display.syncExec(new Runnable() {public void run(){
	switch (botRightCounter) {
	case 1:
		botRightBrowser.setWebsite("http://www.simplefitnesssolutions.com/articles/exercise_benefits.htm");
		botRightBrowser.setVisibility(botRightBrowser.shell, true);
		System.out.println("fitness article");
		break;
	case 2:
		botRightBrowser.setWebsite("http://www.helpguide.org/life/healthy_eating_diet.htm");
        botRightBrowser.setVisibility(botRightBrowser.shell, true);
        System.out.println("diet article pg1");
        break;
	case 3:
		botRightBrowser.setWebsite("http://www.helpguide.org/life/healthy_eating_diet.htm#tip1");
        botRightBrowser.setVisibility(botRightBrowser.shell, true);
        break;
	case 4:
		botRightBrowser.setWebsite("http://www.helpguide.org/life/healthy_eating_diet.htm#tip2");
        botRightBrowser.setVisibility(botRightBrowser.shell, true);
        break;
	case 5:
		botRightBrowser.setWebsite("http://www.helpguide.org/life/healthy_eating_diet.htm#tip3");
        botRightBrowser.setVisibility(botRightBrowser.shell, true);
        break;
	case 6:
		botRightBrowser.setWebsite("http://www.helpguide.org/life/healthy_eating_diet.htm#tip4");
        botRightBrowser.setVisibility(botRightBrowser.shell, true);
        break;
	case 7:
		botRightBrowser.setWebsite("http://www.helpguide.org/life/healthy_eating_diet.htm#tip5");
        botRightBrowser.setVisibility(botRightBrowser.shell, true);
        break;
	case 8:
		botRightBrowser.setWebsite("http://www.helpguide.org/life/healthy_eating_diet.htm#tip6");
        botRightBrowser.setVisibility(botRightBrowser.shell, true);
        break;
	case 9:
		botRightBrowser.setWebsite("http://www.helpguide.org/life/healthy_eating_diet.htm#tip7");
        botRightBrowser.setVisibility(botRightBrowser.shell, true);
        break;
	case 10:
		botRightBrowser.setWebsite("http://www.helpguide.org/life/healthy_eating_diet.htm#tip8");
        botRightBrowser.setVisibility(botRightBrowser.shell, true);
        break;
	case 11:
		botRightBrowser.setWebsite("http://www.helpguide.org/life/healthy_eating_diet.htm#tip9");
        botRightBrowser.setVisibility(botRightBrowser.shell, true);
        break;
	default:
		botRightBrowser.setWebsite("http://www.simplefitnesssolutions.com/articles/exercise_benefits.htm");
		botRightBrowser.setVisibility(botRightBrowser.shell, true);
		System.out.println("fitness article");
		break; }
	} 
});
}

// THE FOLLOWING CODE REMAINS UNCHANGED FROM THE "SLIDER2D" SIMPLEOPENNI DEMO EXAMPLE 
// WHICH THE KINECT APPLICATION CODE IS BASED OFF 
void onPrimaryPointCreate(XnVHandPointContext pContext,XnPoint3D ptFocus)
{
  println("onPrimaryPointCreate");
  
  trackPadViz.enable();
}

void onPrimaryPointDestroy(int nID)
{
  //println("onPrimaryPointDestroy");
  
  trackPadViz.disable();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
// Trackpad

class Trackpad
{
  int     xRes;
  int     yRes;
  int     width;
  int     height;
  
  boolean active;
  PVector center;
  PVector offset;
  
  int      space;
  
  int      focusX;
  int      focusY;
  int      selX;
  int      selY;
  int      dir;
  
  
  Trackpad(PVector center,int xRes,int yRes,int width,int height,int space)
  {
    this.xRes     = xRes;
    this.yRes     = yRes;
    this.width    = width;
    this.height   = height;
    active        = false;
    
    this.center = center.get();
    offset = new PVector();
    offset.set(-(float)(xRes * width + (xRes -1) * space) * .5f,
               -(float)(yRes * height + (yRes -1) * space) * .5f,
               0.0f);
    offset.add(this.center);
    
    this.space = space;
  }
  
  void enable()
  {
    active = true;
    
    focusX = -1;
    focusY = -1;
    selX = -1;
    selY = -1;
  }
  
  void update(int indexX,int indexY)
  {
    focusX = indexX;
    focusY = (yRes-1) - indexY;
  }
  
  void push(int indexX,int indexY,int dir)
  {
    selX = indexX;
    selY =  (yRes-1) - indexY;
    this.dir = dir;
  }
  
  void disable()
  {
    active = false;
  }
  
  void draw()
  {    
    pushStyle();
    pushMatrix();
    
      translate(offset.x,offset.y);
    
      for(int y=0;y < yRes;y++)
      {
        for(int x=0;x < xRes;x++)
        {
          if(active && (selX == x) && (selY == y))
          { // selected object 
            fill(100,100,220,190);
            strokeWeight(3);
            stroke(100,200,100,220);
          }
          else if(active && (focusX == x) && (focusY == y))
          { // focus object            
            fill(100,255,100,220);
            strokeWeight(3);
            stroke(100,200,100,220);
          }
          else if(active)
          {  // normal
            strokeWeight(3);
            stroke(100,200,100,220);
            noFill();
          }
          else
          {
            strokeWeight(2);
            stroke(200,100,100,60);
            noFill();
          }
           rect(x * (width + space),y * (width + space),width,height);  
        }
      }
    popMatrix();
    popStyle();  
  }
}

}
