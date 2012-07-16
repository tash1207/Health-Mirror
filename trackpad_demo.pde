/* --------------------------------------------------------------------------
 * Code based on SimpleOpenNI NITE Slider2d
 * --------------------------------------------------------------------------
 * Processing Wrapper for the OpenNI/Kinect library
 * http://code.google.com/p/simple-openni
 * --------------------------------------------------------------------------
 * prog:  Max Rheiner / Interaction Design / zhdk / http://iad.zhdk.ch/
 * date:  03/28/2011 (m/d/y)
 * ----------------------------------------------------------------------------
 *
 * ----------------------------------------------------------------------------
 */
import SimpleOpenNI.*;
import fullscreen.*;

FullScreen fs;

SimpleOpenNI          context;

// NITE
XnVSessionManager     sessionManager;
XnVSelectableSlider2D trackPad;

int gridX = 9;
int gridY = 7;

String topLeft = "Top Left";
int topLeftSize = 185;

String topRight = "Top Right";
int topRightSize = 185;
int topRightX = 421;
int topRightY = 18;

String botLeft = "Bottom Left";
int botLeftSize = 185;
int botLeftX = 33;
int botLeftY = 275;

String botRight = "Bottom Right";
int botRightSize = 185;
int botRightX = 421;
int botRightY = 277;

Trackpad   trackPadViz;

void setup()
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
  fs = new FullScreen(this);
  fs.enter(); 
  smooth();
  
   // info text
  println("-------------------------------");  
  println("1. Wave till the tiles get green");  
  println("2. The relative hand movement will select the tiles");  
  println("-------------------------------");   
}

void draw()
{
  // update the cam
  context.update();
  
  // update nite
  context.update(sessionManager);
  
  // draw depthImageMap
  image(context.rgbImage(), 0, 0);
  
  trackPadViz.draw();
  
  // Top Left Text Box
  fill(0,255,0,200);
  rect(33,18,topLeftSize,topLeftSize);
  fill(255,0,0,80);
  text(topLeft, 45, 30, topLeftSize - 20, topLeftSize - 20);
  
  // Top Right Text Box
  fill(255,0,0,200);
  rect(topRightX, topRightY, topRightSize, topRightSize);
  fill(0,0,255,80);
  text(topRight, topRightX + 12, topRightY + 12, topRightSize - 20, topRightSize - 20);
  
  // Bottom Left Text Box
  fill(100,100,100,200);
  rect(botLeftX, botLeftY, botLeftSize, botLeftSize);
  fill(20, 200, 20, 80);
  text(botLeft, botLeftX + 12, botLeftY + 12, botLeftSize - 20, botLeftSize - 20);
  
  // Bottom Right Text Box
  fill(0,0,255,200);
  rect(botRightX, botRightY, botRightSize, botRightSize);
  fill(255,0,0,80);
  text(botRight, botRightX + 12, botRightY + 12, botRightSize - 20, botRightSize - 20);
}

void keyPressed()
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

void onItemSelect(int nXIndex,int nYIndex,int eDir)
{
  println("onItemSelect: nXIndex=" + nXIndex + " nYIndex=" + nYIndex + " eDir=" + eDir);
  trackPadViz.push(nXIndex,nYIndex,eDir);
  textReset();
  sizeReset();
  if (nXIndex >= 6 && nYIndex >= 4) {
    //println("Top Right Corner has been selected");
    topRight = "You selected this corner!";
    topRightX = 206;
    topRightSize = 400;
  }
  if (nXIndex >=6 && nYIndex <=2) {
    //println("Bottom Right Corner has been selected");
    botRight = "You selected this corner!";
    botRightX = 206;
    botRightY = 62;
    botRightSize = 400;
  }
  if (nXIndex <= 2 && nYIndex >=4) {
    //println("Top Left Corner has been selected");
    topLeft = "You selected this corner!";
    topLeftSize = 400;
  }
  if (nXIndex <=2 && nYIndex <=2) {
    //println("Bottom Left Corner has been selected");
    botLeft = "You selected this corner!";
    botLeftY = 62;
    botLeftSize = 400;
  }
  if (nXIndex >= 3 && nXIndex <= 5 && nYIndex >= 5) {
    println("This is where the mood meter will be!");
    if (nXIndex == 3) println(":(");
    if (nXIndex == 5) println(":)");
  }
}

void textReset() {
  topLeft = "Top Left";
  topRight = "Top Right";
  botLeft = "Bottom Left";
  botRight = "Bottom Right";
}

void sizeReset() {
  topLeftSize = 185;
  topRightSize = 185;
  botLeftSize = 185;
  botRightSize = 185;
  
  topRightX = 421;
  topRightY = 18;
  botLeftX = 33;
  botLeftY = 275;
  botRightX = 421;
  botRightY = 277;
}

void onPrimaryPointCreate(XnVHandPointContext pContext,XnPoint3D ptFocus)
{
  println("onPrimaryPointCreate");
  
  trackPadViz.enable();
}

void onPrimaryPointDestroy(int nID)
{
  println("onPrimaryPointDestroy");
  
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
          /*
            if(selX <= 2 && selY <= 2) {
              //size(200,200);
              //background(0,25,255,190);
              fill(255, 20, 140, 150);
              text("Top Left Corner has been selected", 60, 62);
            }
            else if(selX >= 6 && selY <= 2) {
              fill(100,100,220,150);
              text("Top Right Corner has been selected", 386, 62);
            }
            else if(selX <= 2 && selY >= 4) {
              fill(255, 20, 140, 150);
              text("Bottom Left Corner has been selected", 60, 386);
            }
            else if(selX >= 6 && selY >= 4) {
              fill(100,100,220,150);
              text("Bottom Right Corner has been selected", 386, 386);
              //link("http://www.webmd.com/news/default.htm", "_self"); 
            }
            */
            //else {
            fill(100,100,220,190);
            strokeWeight(3);
            stroke(100,200,100,220);
            //}
          }
          else if(active && (focusX == x) && (focusY == y))
          { // focus object            
            if(focusX <= 4) {
              fill(255,225,10);
            }
            else {
            fill(100,255,100,220);
            strokeWeight(3);
            stroke(100,200,100,220);
            }
          }
          else if(active)
          {  // normal
            strokeWeight(3);
            stroke(100,200,100,190);
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
