import java.util.Scanner;
import java.util.HashMap;
import java.util.Arrays;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Decryptor{
    public static void main(String[] args) throws Exception {
        if(args.length == 0){
            printUsage();
            return;
        }
        
        if(args[0].equals("-i")){
            if(args.length == 2){
                interactiveMode(args[1]);
            }else{
                printUsage();
            }
        }else if(args[0].equals("-a")){
            gradingMode();
        }else{
            printUsage();
        }
    }

    static void interactiveMode(String afd_path) throws Exception{
        AFD afd = new AFD(afd_path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        LanguageValidator validator = new LanguageValidator(afd_path);
        System.out.println("---------- Interactive Mode ----------");
        while(true){
            String string = reader.readLine();
            if(validator.validateString(string)){
                System.out.println("String " + (afd.evaluate(string) ? "Accepted" : "Rejected"));
            }else{
                System.out.println("The evaluated string contains one or more invalid characters.");
            }
        }
    }

    static void gradingMode() throws Exception{
        String testsDir = "./tests/afd";
        String stringsDir = "./tests/strings";

        File directoryFile = new File(testsDir);
        String[] files = directoryFile.list();
        System.out.println("Tests directory contains " + files.length + " files");

        double score = 0.0;
        double answerValue = 100.0 / files.length;

        for(int i = 0; i < files.length;i++){
            String afdFilename = testsDir + "/" + files[i];
            String stringsFilename = stringsDir + "/" + files[i].replace(".afd", ".txt");
            
            System.out.println("-----------------------------------------------------------------");
            System.out.println("Analizando archivo '" + afdFilename + "'");

            File stringsFile = new File(stringsFilename);
            Scanner scan = new Scanner(stringsFile);
            String[] strings = scan.nextLine().split(",");
            String[] expected = scan.nextLine().split(",");
            scan.close();

            /*********************************************************************** */
            /************************** CREACION DEL AFD *****************************/
            /*********************************************************************** */
            AFD afd = new AFD(afdFilename);
            boolean[] obtained = afd.evaluateMany(strings);

            System.out.println("Obtenido: " + Arrays.toString(obtained));
            System.out.println("Esperado: " + Arrays.toString(expected));
            score+= score(obtained, expected, answerValue);
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Puntaje Obtenido: "+score+"/100.00");
    }

    static void printUsage(){
        System.out.println("Decriptor. Usage is 'java Descriptor (-i|-a)' AFD_PATH");
    }

    static double score(boolean[] obtained, String[] expected, double answerValue){
        double score = 0;
        boolean correct = true;
        if(obtained.length == expected.length){
            for(int i = 0; i < obtained.length; i++){
                if((expected[i].equals("true") && !obtained[i]) || (expected[i].equals("false") && obtained[i])){
                    correct = false;
                    break;
                }
            }
        }else{
            correct = false;
        }
        if(correct){
            score = answerValue;
        }
        System.out.println("Puntaje Obtenido: "+score+"/" + answerValue);
        return score;
    }
}

class LanguageValidator {
    HashMap<Character, Character> map;

    LanguageValidator(String path) throws Exception{
        Scanner scan = new Scanner(new File(path));
        scan.nextLine(); // We skip the number of states
        scan.nextLine(); // We skip the final states
        String[] language = scan.nextLine().split(",");
        scan.close();
        
        this.map = new HashMap<Character, Character>();
        for(int i = 0; i < language.length; i++){
            char character = language[i].charAt(0);
            map.put(character, character);
        }
    }

    boolean validateString(String string){
        char[] characters = string.toCharArray();

        for(int i = 0; i < characters.length; i++){
            char character = characters[i];
            if(! map.containsKey(character)){
                return false;
            }
        }

        return true;
    }
}