//import txtDownloader.java;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class TxtGenerator {
    static String[] inputs = new String[7];
    static String[] inputsNeeded = new String[7];


    public static void getInput() {

        BufferedReader reader = new BufferedReader(
            new InputStreamReader( System.in ) );

        int i = 0;
        while( i < inputs.length ) {
            //Enter data using BufferReader


            // Reading data using readLine
            //            String name = "";
            System.out.println( "input " + inputsNeeded[i] + ":" );

            try {
                inputs[i] = reader.readLine();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            // Printing the read line
            System.out.println( inputs[i] );
            i++;
        }


    }


    public static void main( String[] args ) {
        String novel = "Novel Name";
        String author = "Author";
        String baseUrl = "baseUrl";
        String sourceUrl = "sourceUrl";
        String listCssQuery = "listCssQuery";
        String skipN = "skipN";
        String contentCssQuery = "contentCssQuery";

        inputsNeeded[0] = novel;
        inputsNeeded[1] = author;
        inputsNeeded[2] = baseUrl;
        inputsNeeded[3] = sourceUrl;
        inputsNeeded[4] = listCssQuery;
        inputsNeeded[5] = skipN;
        inputsNeeded[6] = contentCssQuery;


        getInput();

        TXTDownloader machine = new TXTDownloader( inputs[0], inputs[1],
            inputs[2], inputs[3], inputs[4], Integer.parseInt( inputs[5] ),
            inputs[6] );

        //        System.out.println( inputs[i] );

        try {
            int returnValue = machine.run();
            while( returnValue == 1 ) {
                System.out.println( "Check your input and retry!" );

                getInput();

                machine = new TXTDownloader( inputs[0], inputs[1], inputs[2],
                    inputs[3], inputs[4], Integer.parseInt( inputs[5] ),
                    inputs[6] );
                returnValue = machine.run();
            }


        } catch ( IOException e ) {
        }
    }
}
