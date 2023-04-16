import java.io.*;
import java.util.*; 
/*
	Utilice esta clase para guardar la informacion de su
	AFD. NO DEBE CAMBIAR LOS NOMBRES DE LA CLASE NI DE LOS 
	METODOS que ya existen, sin embargo, usted es libre de 
	agregar los campos y metodos que desee.
*/
public class AFD{
	
	String[] array;
	String[] variables;
	String[] finalS;
	String[] finalStates;
	String[] valsTemp;
	String[] nextStep;
	int states;	


	public AFD(String path) throws Exception{
		Scanner scan = new Scanner(new File(path));
		scan.useDelimiter("\n");
		variables = scan.next().split(",");
		nextStep = new String[variables.length];
		states = Integer.parseInt(scan.next());
		finalStates = scan.next().split(",");
		int cont = 0;
		while ( cont < variables.length ) {
			nextStep[cont]=scan.next();
			cont++;
		}
	}



	public boolean alphabetExists(String string){
		boolean containsAlphabet = false;
		array = variables;
		List<String> list = Arrays.asList(array);

		int size = string.length();
		int start = 0;
		
		while ( start < size ) {
			if (!list.contains(string.substring(start, start + 1))) {
				containsAlphabet = false;
			}else{
				containsAlphabet = true;
			}

			start++;
		}		
		return containsAlphabet;
	}




	/*
		Implemente el metodo transition, que recibe de argumento
		un entero que representa el estado actual del AFD y un
		caracter que representa el simbolo a consumir, y devuelve 
		un entero que representa el siguiente estado
	*/

  
  
	public int getTransition(int current, char symbol){
    	valsTemp = this.variables;

		int count = 0;
		int stop = 0;

		while ( stop < valsTemp.length ) {			
			if ((int)symbol == (int)valsTemp[stop].charAt(0)) {
				break;
			}else{
				count++;
			}
			stop++;
		}
		array = this.nextStep[count].split(",");			
		return Integer.parseInt(array[current]);
	}

	/*
		Implemente el metodo accept, que recibe como argumento
		un String que representa la cuerda a evaluar, y devuelve
		un boolean dependiendo de si la cuerda es aceptada o no 
		por el afd
	*/
	public boolean evaluate(String string){		
		boolean result = false;
		int current = 1;
		int size = string.length();
		finalS = this.finalStates;

		int i = 0;
		int i2 = 0;

		while  ( i < size  ) {

			if ( this.getTransition(current,string.charAt(i)) != 0 ) {								
				current = this.getTransition(current,string.charAt(i));	
			}else{				
				current = 0;
				break;
			}		
			i++;	
		}

		if (current == 0) {
			result = false;

		}else{
			while( i2 < finalS.length ) {
				if (current == Integer.parseInt(finalS[i2])) {
					result = true;
					break;
				}

				i2++;
		}
	}

		return result;
	}

	/*
		El metodo main debe recibir como primer argumento el path
		donde se encuentra el archivo ".afd", como segundo argumento 
		una bandera ("-f" o "-i"). Si la bandera es "-f", debe recibir
		como tercer argumento el path del archivo con las cuerdas a 
		evaluar, y si es "-i", debe empezar a evaluar cuerdas ingresadas
		por el usuario una a una hasta leer una cuerda vacia (""), en cuyo
		caso debe terminar. Tiene la libertad de implementar este metodo
		de la forma que desee. 
	*/
	public static void main(String[] args) throws Exception{		
		String pathAFD = new String("tests\\afd\\"+args[0]+".afd");
		AFD AFDPrinc = new AFD(pathAFD);	
		if (args[1].equals("-i")) {
			while(true){
				System.out.print("Ingrese una cuerda:");
	        	BufferedReader AFDLectura = new BufferedReader(new InputStreamReader(System.in));
        		String input = AFDLectura.readLine();

        		if (input.equals("")) {
        			break;
        		}else{
        			if (AFDPrinc.alphabetExists(input)) {
        				if (AFDPrinc.evaluate(input)) {
		        			System.out.println("La cuerda SI es aceptada");        		
        				}else{
		        			System.out.println("La cuerdo NO es Aceptada");
    	    			}
        			}else{
        				System.out.println("La cuerdo NO es Aceptada");
        			}
        			
        		}
			}			
		} else if (args[1].equals("-f")) {
			Scanner file = new Scanner(new 
      		File("tests\\cuerdas\\"+args[2]+".txt"));		
			  file.useDelimiter("\n");
			
			int count = 1;

			while(file.hasNext()){
				System.out.print("Cuerda#" + count++ +" ");
				if (AFDPrinc.evaluate(file.next())) {
        			System.out.println("La cuerda SI es aceptada");        		
				}else{
        			System.out.println("La cuerdo NO es Aceptada");
    			}
			}
		}	
	}

	public boolean[] evaluateMany(String[] strings){
		return new boolean[0];
	}
}