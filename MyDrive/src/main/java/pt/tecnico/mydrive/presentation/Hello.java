package pt.tecnico.mydrive.presentation;

public class Hello {
	
	public static void main(String[] args) {
        System.out.println("Hello mydrive!");
    }
    public static String bye(String[] args) {
    	String bye = "Goodbye mydrive!";
        System.out.println(bye);
        return bye;
    }
    public static String greet(String[] args) {
    	if(args==null){
    		String hello="Hello";
    		System.out.println(hello);
            return hello;
    	}
    	String hello ="Hello "+args[0];
    	System.out.println(hello);
        return hello;
    }
    public static void execute(String[] args) {
		for (String s: args)
		    System.out.println("Execute "+args[0]+"?");
	}
    
    public static void sum(String[] args) {
    	int sum = 0;
    	for (String s: args) sum += Integer.parseInt(s);
    	System.out.println("sum="+sum);
    }
}