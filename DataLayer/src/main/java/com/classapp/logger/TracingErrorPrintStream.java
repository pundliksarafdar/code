package com.classapp.logger;

import java.io.PrintStream;

public class TracingErrorPrintStream extends PrintStream {
	 public TracingErrorPrintStream(PrintStream original) {
		    super(original);
		  }

		  // You'd want to override other methods too, of course.
		  @Override
		  public void println(String line) {
		    StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		    // Element 0 is getStackTrace
		    // Element 1 is println
		    // Element 2 is the caller
		    StackTraceElement caller = stack[2];
		    AppLogger.logError(line);
		    //super.println(caller.getClassName() + ": " + line);
		  }
		  
	@Override
	public void println(Object object) {
		 StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		    // Element 0 is getStackTrace
		    // Element 1 is println
		    // Element 2 is the caller
		    StackTraceElement caller = stack[2];
		    AppLogger.logError(object);
		
	}
		  
		  
		  
}
