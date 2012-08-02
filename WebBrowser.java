import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import java.util.Calendar;

/** This class is in charge of displaying the web browser that appears when the bottom right
 box is selected and the "Daily Calorie Calculator" Shell that appears when the bottom
 left box is selected */
// The browser was originally only going to be used when the bottom right box was selected,
// but now the browser is used for the top two boxes as well to link to Fitbit and Youtube
public class WebBrowser {
	Display display = new Display();
	// Variables for the bottom right browser
	final Shell shell = new Shell(display);
	Browser browser = null;
	String website = "http://www.simplefitnesssolutions.com/articles/exercise_benefits.htm";
	// Variables for the bottom left shell
	Shell cShell = null;
	ChildShell childShell = new ChildShell(display);

	// The ChildShell inner class creates the "Daily Calorie Calculator" Shell
	public class ChildShell 
	  { 
		int weight = 150;
		int heightFeet = 5;
		int heightInches = 5;
		double calories = 1750;
		Button btnUp;
		final Label lblWeight;
		Button btnDown;
		Button btnUpHeight;
		final Label lblHeight;
		Button btnDownHeight;
		Button btnSubmit;
		final Label lblCalorie;
		
	     private ChildShell(Display display) 
	     { 
	        System.out.println("Creating Daily Calorie Calculator Shell"); 
	        
	        // ========================================= 
	        // Create a Shell (window) from the Display 
	        // ========================================= 
	        cShell = new Shell(display, SWT.CLOSE); 

	        // ===================== 
	        // Set the Window Title 
	        // ===================== 
	        cShell.setText("Daily Calorie Calculator"); 

	        // For Weight Changes
	        btnUp = new Button(cShell, SWT.NONE); 
	        btnUp.setText("Increase"); 
	        btnUp.setBounds(25, 10, 100, 20);
	        lblWeight = new Label(cShell, SWT.NONE);
	        lblWeight.setText("Weight = " + weight + " lbs");
	        lblWeight.setBounds(25, 40, 120, 20);
	        btnDown = new Button(cShell, SWT.NONE);
	        btnDown.setText("Decrease");
	        btnDown.setBounds(25, 70, 100, 20);
	        
	        //For Height Changes
	        btnUpHeight = new Button(cShell, SWT.NONE); 
	        btnUpHeight.setText("Increase"); 
	        btnUpHeight.setBounds(175, 10, 100, 20);
	        lblHeight = new Label(cShell, SWT.NONE);
	        lblHeight.setText("Height = " + heightFeet + "ft" + heightInches + "ins");
	        lblHeight.setBounds(175, 40, 120, 20);
	        btnDownHeight = new Button(cShell, SWT.NONE);
	        btnDownHeight.setText("Decrease");
	        btnDownHeight.setBounds(175, 70, 100, 20);
	        
	        btnSubmit = new Button(cShell, SWT.NONE);
	        btnSubmit.setText("Submit");
	        btnSubmit.setBounds(100, 110, 100, 40);
	        lblCalorie = new Label(cShell, SWT.NONE);
	        lblCalorie.setBounds(40, 170, 210, 80);
	        lblCalorie.setAlignment(3);
	        
	        cShell.setBounds(10, 450, 300, 300); 
	        cShell.open(); 
	        cShell.setVisible(false);
	        
	        // Add functionality to all of the buttons
	        btnUp.addSelectionListener(new SelectionListener() 
	        { 
	           @Override 
	           public void widgetSelected(SelectionEvent e) 
	           { 
	              weight+=2;
	              lblWeight.setText("Weight = " + weight + " lbs");
	              lblCalorie.setText("");
	           } 
	           @Override 
	           public void widgetDefaultSelected(SelectionEvent e) 
	           { 
	              widgetSelected(e); 
	           } 
	        }); 
	        btnDown.addSelectionListener(new SelectionListener() 
	        { 
	           @Override 
	           public void widgetSelected(SelectionEvent e) 
	           { 
	              weight-=2;
	              lblWeight.setText("Weight = " + weight + " lbs");
	              lblCalorie.setText("");
	           } 
	           @Override 
	           public void widgetDefaultSelected(SelectionEvent e) 
	           { 
	              widgetSelected(e); 
	           } 
	        }); 
	        btnUpHeight.addSelectionListener(new SelectionListener() 
	        { 
	           @Override 
	           public void widgetSelected(SelectionEvent e) 
	           { 
	              if (heightInches < 11) {
	        	   heightInches++;
	              }
	              else {
	            	  heightFeet++; 
	            	  heightInches = 0;
	              }
	              lblHeight.setText("Height = " + heightFeet + "ft" + heightInches + "ins");
	              lblCalorie.setText("");
	           } 
	           @Override 
	           public void widgetDefaultSelected(SelectionEvent e) 
	           { 
	              widgetSelected(e); 
	           } 
	        }); 
	        btnDownHeight.addSelectionListener(new SelectionListener() 
	        { 
	           @Override 
	           public void widgetSelected(SelectionEvent e) 
	           { 
	              if (heightInches > 0) {
	        	   heightInches--;
	              }
	              else {
	            	  heightFeet--; 
	            	  heightInches = 11;
	              }
	              lblHeight.setText("Height = " + heightFeet + "ft" + heightInches + "ins");
	              lblCalorie.setText("");
	           } 
	           @Override 
	           public void widgetDefaultSelected(SelectionEvent e) 
	           { 
	              widgetSelected(e); 
	           } 
	        }); 
	        btnSubmit.addSelectionListener(new SelectionListener() 
	        { 
	           @Override 
	           public void widgetSelected(SelectionEvent e) 
	           { 
	        	  calories = 1.2* ((10 * weight * 0.453592) + 6.25 * 2.54 * (heightFeet * 12 + heightInches) - 150 - 161);
	              int calInt = (int) calories;
	              int weightCals = (int) (0.8 * calInt);
	        	  lblCalorie.setText("To maintain your weight, consume: \n " + 
	              calInt + " calories daily \n\n To lose weight, consume: \n " + weightCals
	              + " calories daily");
	           } 
	           @Override 
	           public void widgetDefaultSelected(SelectionEvent e) 
	           { 
	              widgetSelected(e); 
	           } 
	        }); 

	        // ============================================================= 
	        // Register a listener for the Close event on the child Shell. 
	        // This disposes the child Shell 
	        // ============================================================= 
	        
	        cShell.addListener(SWT.Close, new Listener() 
	        { 
	           @Override 
	           public void handleEvent(Event event) 
	           { 
	              System.out.println("Child Shell handling Close event, about to dispose this Shell"); 
	              cShell.dispose(); 
	           } 
	        }); 
	     }
	     // The following methods are called through the Kinect Application class after the
	     // user has selected the bottom left corner "Daily Calorie Calculator" and then 
	     // selects the respective box for increasing height/weight
	     public void incWeight() {
	    	 weight+=2;
             lblWeight.setText("Weight = " + weight + " lbs");
             lblCalorie.setText("");
	        }
	     public void decWeight() {
	    	 weight-=2;
             lblWeight.setText("Weight = " + weight + " lbs");
             lblCalorie.setText("");
	     }
	     public void incHeight() {
	    	 if (heightInches < 11) {
	        	   heightInches++;
	              }
	              else {
	            	  heightFeet++; 
	            	  heightInches = 0;
	              }
	              lblHeight.setText("Height = " + heightFeet + "ft" + heightInches + "ins");
	              lblCalorie.setText("");
	     }
	     public void decHeight() {
	    	 if (heightInches > 0) {
	        	   heightInches--;
	              }
	              else {
	            	  heightFeet--; 
	            	  heightInches = 11;
	              }
	              lblHeight.setText("Height = " + heightFeet + "ft" + heightInches + "ins");
	              lblCalorie.setText("");
	     }
	     public void centerSubmit() {
	    	 calories = 1.2* ((10 * weight * 0.453592) + 6.25 * 2.54 * (heightFeet * 12 + heightInches) - 150 - 161);
             int calInt = (int) calories;
             int weightCals = (int) (0.8 * calInt);
       	     lblCalorie.setText("To maintain your weight, consume: \n " + 
             calInt + " calories daily \n\n To lose weight, consume: \n " + weightCals
             + " calories daily");
	     }
	  } 	
	
	public WebBrowser() {
		
	}

	public void setVisibility(Shell shell, boolean bool) {
		if (shell.getVisible() != bool) {
		shell.setVisible(bool);
		}
	}
	public boolean getVisibility(Shell shell) {
		return shell.getVisible();
	}
	
	public String getWebsite() {
		return this.website;
	}
	// Sets the website for the browser to navigate to
	public void setWebsite(String website) {
		shell.setText("Fitness and Diet Articles");
		shell.setBounds(25, 80, 1220, 888);
		if (this.website != website) {
		browser.setUrl(website);
		this.website = website;
		}
	}
	// Only difference is that the shell title is now "Fitbit Profile" instead of 
	// "Fitness and Diet Articles"
	public void setFitbitWebsite(String website) {
		shell.setText("Fitbit Profile");
		shell.setBounds(25, 80, 1220, 888);
		if (this.website != website) {
		browser.setUrl(website);
		this.website = website;
		}
	}
	
	// Uses the Java Calendar class for the day of the week so that a different video will
	// be played depending on what day it is
	// NOTE: Youtube displays a minute long ad before the video plays, try to get around that
	// by using the Google Data API for Youtube.
	public void setYouTubeBrowser() {
		Calendar c = Calendar.getInstance();
		shell.setText("Exercise of the Day");
		shell.setBounds(570,5,650,590);
		// I only put three different videos up, but four more can be added for the other
		// days of the week
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case 3:
			// Push Ups
			if (this.website != "http://www.youtube.com/watch?v=Q2Wi3NUhriY#t=03s") {
			browser.setUrl("http://www.youtube.com/watch?v=Q2Wi3NUhriY#t=03s");
			this.website = "http://www.youtube.com/watch?v=Q2Wi3NUhriY#t=03s";
			}
			break;
		case 6:
			// Crunches
			if (this.website != "http://www.youtube.com/watch?v=1V4RXxLHNCY#t=03s") {
			browser.setUrl("http://www.youtube.com/watch?v=1V4RXxLHNCY#t=03s");
			this.website = "http://www.youtube.com/watch?v=1V4RXxLHNCY#t=03s";
			}
			break;
		case 7:
			// Squats
			if (this.website != "http://www.youtube.com/watch?v=QKKZ9AGYTi4#t=03s") {
			browser.setUrl("http://www.youtube.com/watch?v=QKKZ9AGYTi4#t=03s");
			this.website = "http://www.youtube.com/watch?v=QKKZ9AGYTi4#t=03s";
			}
			break;
		default:
			// Squats
			if (this.website != "http://www.youtube.com/watch?v=QKKZ9AGYTi4#t=03s") {
			browser.setUrl("http://www.youtube.com/watch?v=QKKZ9AGYTi4#t=03s");
			this.website = "http://www.youtube.com/watch?v=QKKZ9AGYTi4#t=03s";
			}
			break;
		}
		
	}
	
	public void openBrowser() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		shell.setText("Fitness and Diet Articles");
		shell.setLayout(gridLayout);
		shell.setBounds(25, 80, 1220, 888);
		shell.forceActive();
		shell.setAlpha(200);
				
		GridData data = new GridData();
		data.horizontalSpan = 3;
		
		final Text location = new Text(shell, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		location.setLayoutData(data);
		
		browser = new Browser(shell, SWT.NONE);

		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		browser.setLayoutData(data);

		final Label status = new Label(shell, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		status.setLayoutData(data);

		final ProgressBar progressBar = new ProgressBar(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		progressBar.setLayoutData(data);

		// event handling 
		
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
					if (event.total == 0) return;                            
					int ratio = event.current * 100 / event.total;
					progressBar.setSelection(ratio);
			}
			public void completed(ProgressEvent event) {
				progressBar.setSelection(0);
			}
		});
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
				status.setText(event.text);	
			}
		});
		browser.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) {
				if (event.top) location.setText(event.location);
			}
			public void changing(LocationEvent event) {
			}
		});
		
		location.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				browser.setUrl(location.getText());
			}
		});
		
		shell.open();
		shell.setVisible(false);
		browser.setUrl(website);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
	
}