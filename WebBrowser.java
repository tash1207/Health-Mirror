import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;


public class WebBrowser {
	Display display = new Display();
	final Shell shell = new Shell(display);
	Browser browser = null;
	String website = "http://www.simplefitnesssolutions.com/articles/exercise_benefits.htm";
	Shell cShell = null;
	ChildShell childShell = new ChildShell(display);
	
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
	        System.out.println("Creating new child Shell"); 
	        
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
	        
	        shell.addListener(SWT.Close, new Listener() 
	        { 
	           @Override 
	           public void handleEvent(Event event) 
	           { 
	              System.out.println("Child Shell handling Close event, about to dispose this Shell"); 
	              shell.dispose(); 
	           } 
	        }); 
	     }
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
	
	public void setWebsite(String website) {
		if (this.website != website) {
		browser.setUrl(website);
		this.website = website;
		}
	}
	
	public void openBrowser() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);
		shell.setBounds(25, 80, 1220, 888);
		// In future allow ints as arguments
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

	
	/* public static void main(String [] args) {
		//PApplet.main(new String[] {"==present", "MyProcessingSketch"});
		WebBrowser shellBrowser = new WebBrowser();
		
		shellBrowser.shell.open();
		shellBrowser.browser.setUrl("http://www.simplefitnesssolutions.com/articles/exercise_benefits.htm");
		
		while (!shellBrowser.shell.isDisposed()) {
			if (!shellBrowser.display.readAndDispatch())
				shellBrowser.display.sleep();
		}
		shellBrowser.display.dispose();
		
		} */
	
}