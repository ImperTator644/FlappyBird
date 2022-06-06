package flappyBird;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * obsluguje score board
 */
public class Score {
    private final Element[] items = new Element[5];

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

    public int getLast(){
        return items[4].score;
    }

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

    public void saveScores(){
        try(FileWriter fw = new FileWriter("src/HighScore.txt", false)){
            for(Element i:items){
                fw.write(i.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public String toString(){
        return Arrays.toString(items);
    }

    private static class Element{
        private int score;
        private String name;

        public Element(int score, String name) {
            this.score = score;
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString(){
            return score + " " + name + "\n";
        }
    }
}
