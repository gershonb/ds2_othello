import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        OthelloModel test = new OthelloModel();
        //System.out.println(test.boardToString());
        //System.out.println("----------" +test.getPlayer() + "|"+ Arrays.toString(test.getScores()) +"--------");
        test.playerTurn(new int[] {3,5});
        //System.out.println(test.boardToString(new int[] {3,5}));
        //System.out.println("----------" +test.getPlayer() + "|"+ Arrays.toString(test.getScores()) +"--------");
        //test.undo();
        //System.out.println("----------" + "undo" +"--------");
        //System.out.println(test.boardToString());
        test.restart();
        for (int[][] i: test.turns) {
            for (int[] j:i) {
                System.out.println(Arrays.toString(j));
            }
            System.out.println("-----");


        }


    }
}
