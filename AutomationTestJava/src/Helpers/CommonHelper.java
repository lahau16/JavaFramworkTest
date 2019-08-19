package Helpers;

import Common.ConsoleType;

public class CommonHelper {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	public static void  WriteConsole(String message, ConsoleType type)
    {
		System.out.print(java.time.LocalDateTime.now());
		System.out.print(" - ");
        switch(type)
        {
            case Infor:
            	System.out.print(ANSI_GREEN + "infor" + ANSI_RESET);
                break;
            case Error:
                System.out.print(ANSI_RED + "error" + ANSI_RESET);
                break;
        }
        System.out.print(" - ");
        System.out.println(message);
    }
	
	public static void  WriteConsole(String message)
    {
		System.out.print(java.time.LocalDateTime.now());
		System.out.print(" - ");
    	System.out.print(ANSI_GREEN + "infor" + ANSI_RESET);
        System.out.print(" - ");
        System.out.println(message);
    }
}
