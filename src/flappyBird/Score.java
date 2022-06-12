package flappyBird;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Operates with score board - inserting and saving.
 * @author Kaczynski
 * @version 1.2.2
 */
public class Score {
    /**
     * Array containing five objects of class Element
     */
    private final Element[] items = new Element[5];

    /**
     * Reads data from HighScore.txt
     * Inserts data to items array.
     */
    public Score(){
        for(int i=0; i< items.length; i++) items[i] = new Element(0, "---");
        try (Scanner scanner = new Scanner(new File("src/HighScore.txt"))){
            int iterator = 0;
            while(iterator < 5 && scanner.hasNext()){
                items[iterator].score = scanner.nextInt();
                items[iterator].name = scanner.nextLine().replace(" ", "");
                iterator++;
            }
        }
        catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    /**
     *
     * @return score from the bottom of the score board
     */
    public int getLast(){
        return items[4].score;
    }

    /**
     * Prints content of index i from items array.
     * @param i index of array
     * @return text representing "name - score"
     */
    public String printItem(int i){
        return items[i].name +" - "+ items[i].score;
    }

    public void insert(int score, String name){
        for(int i=0;i<items.length;i++){
            if(score > items[i].score) {
                for(int j=items.length-1;j>i;j--){
                    items[j].score = items[j-1].score;
                    items[j].name = items[j-1].name;
                }
                items[i].score = score;
                items[i].name = name;
                return;
            }
        }
    }

    /**
     * Saves data from items array to HighScore.txt
     */
    public void saveScores(){
        try(FileWriter fw = new FileWriter("src/HighScore.txt", false)){
            for(Element i:items){
                fw.write(i.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return text representation of items array
     */
    @Override public String toString(){
        return Arrays.toString(items);
    }

    /**
     * Inner class.
     * Contains player's name and score
     */
    private static class Element{
        private int score;
        private String name;

        /**
         * Initializes score and name
         * @param score player score
         * @param name player name
         */
        public Element(int score, String name) {
            this.score = score;
            this.name = name;
        }

        /**
         *
         * @return text representation of the element
         */
        @Override
        public String toString(){
            return score + " " + name + "\n";
        }
    }
}
