import java.util.*;

public class OthelloModel {
    ///NOTE: X AND Y COORDINATES AR FLIPPED
    private int player = 1;
    private boolean computerPlayer = false;
    private int[] scores = {0, 0};
    private int winner = 0;
    private int[] currentPiece = new int[2];
    //board / turns
    private int[][] board = new int[8][8];
    Stack<int[][]> turns = new Stack<>();
    Stack<int[]> info = new Stack<>();


    // top left and clockwise
    private int[][] directions = {new int[]{1, -1}, new int[]{1, 0}, new int[]{1, 1},
            new int[]{0, 1}, new int[]{-1, 1}, new int[]{-1, 0}, new int[]{-1, -1}, new int[]{0, -1}};

    public OthelloModel() {
        restart();
    }

    void playerTurn(int[] myMove) {
        //check for valid turn
        if (winner == 0 && validPlacement(myMove)) {
            List<Integer> moveValues = directionsCheck(myMove);
            if (noValueMove(moveValues)) {
                throw new RuntimeException("Illegal move.");
            } else {
                //flip pieces (based on values in list where index is direction and value is amount to flip); add to scores
                scores[player - 1]++;

                board[myMove[0]][myMove[1]] = player;
                for (int i = 0; i < moveValues.size(); i++) {
                    int temp0 = myMove[0];
                    int temp1 = myMove[1];
                    for (int j = 0; j < moveValues.get(i); j++) {
                        //*************fix to flip more than one piece
                        board[temp0 + directions[i][0]][temp1 + directions[i][1]] = player;
                        temp0 += directions[i][0];
                        temp1 += directions[i][1];
                        scores[player - 1]++;
                        scores[(3 - player) - 1]--;
                    }
                }
                //check for winner after flips
                if (scores[0] + scores[1] == 64) {
                    winner = Math.max(scores[0], scores[1]);
                }
                endTurn();
            }
        } else {
            throw new RuntimeException("Illegal move.");
        }

    }

    private boolean validPlacement(int[] piece) {
        try {
            int i = board[piece[0]][piece[1]];

        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return board[piece[0]][piece[1]] == 0;
    }

    private List<Integer> directionsCheck(int[] currentPiece) {
        ArrayList<Integer> directionsAndValues = new ArrayList<Integer>();
        // Loop and check all directions
        // ('3 - player' is the other player (1 or 2))
        int[] currPiece = {currentPiece[0], currentPiece[1]};
        for (int[] direction : directions) {
            currPiece[0] = currentPiece[0];
            currPiece[1] = currentPiece[1];
            int currPieceValue = board[currPiece[0]][currPiece[1]];
            int moveValue = 0;
            while (true) {
                try {
                    currPieceValue = board[currPiece[0] + direction[0]][currPiece[1] + direction[1]];
                } catch (IndexOutOfBoundsException e) {
                    directionsAndValues.add(0);
                    break;
                }
                if (currPieceValue == 3 - player) {
                    currPiece[0] += +direction[0];
                    currPiece[1] += +direction[1];
                    moveValue++;
                    if (moveValue == 10) {
                        System.out.println("inf Loop-" + currPieceValue);
                    }


                } else if (currPieceValue == player) {
                    directionsAndValues.add(moveValue);
                    break;
                } else if (currPieceValue == 0) {
                    directionsAndValues.add(0);
                    break;
                }

            }
        }

        return directionsAndValues;
    }

    void endTurn() {
        player = 3 - player;
        turns.push(deepCopy(board));
        info.push(new int[]{player, scores[0], scores[1]});
    }

    private int[][] deepCopy(int[][] matrix) {
        int[][] tempBoard = new int[8][8];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, tempBoard[i], 0, board[i].length);
        }
        return tempBoard;
    }

    void undo() {
        if (turns.size() > 1) {
            turns.pop();
            board = turns.peek();
            info.pop();
            player = info.peek()[0];
            scores[0] = info.peek()[1];
            scores[1] = info.peek()[2];
            System.out.println(player + "|" + scores[0] + "|" + scores[1]);

        }
    }

    void restart() {
        for (int[] ints : board) {
            Arrays.fill(ints, 0);
        }
        Arrays.fill(scores, 2);
        startingboardFlip();
        turns.clear();
        turns.push(deepCopy(board));
        player = 1;
        winner = 0;
        info.clear();
        info.push(new int[]{player, scores[0], scores[1]});
    }

    public int getPlayer() {
        return player;
    }

    public int[] getScores() {
        return scores;
    }

    public int getWinner() {
        return winner;
    }

    public int getValue(int[] space) {
        int temp = board[space[0]][space[1]];
        return temp;
    }

    public boolean isComputerPlayer() {
        return computerPlayer;
    }

    public void setComputerPlayer(boolean computerPlayer)
    {
        this.computerPlayer = computerPlayer;
    }

    private boolean noValueMove(List<Integer> values) {
        for (Integer i : values) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    private void startingboardFlip() {
        board[3][3] = 1;
        board[3][4] = 2;
        board[4][4] = 1;
        board[4][3] = 2;
    }

    void computerTurn(){
        int max = listTotal(directionsCheck(new int[] {0,0}));
        int[] space = {0,0};
        for (int i = 0; i < board.length;i++) {
            for (int j = 0; j < board[i].length;j++) {
               int temp = Math.max(max ,listTotal(directionsCheck(new int[]{i,j})));
                //System.out.println(Arrays.toString(space) + "--" + listTotal(directionsCheck(new int[]{i,j})));
                if(temp != max) {
                    space[0] = i;
                    space[1] = j;
                }
            }
        }
        //System.out.println("MAX: " + Arrays.toString(space) + " " + max);
        playerTurn(space);
    }
    private int listTotal(List<Integer> list){
        int total = 0;
        for(int i:list){
            total +=i;
        }
        return total;
    }

    String boardToString() {
        StringBuilder result = new StringBuilder();
        for (int[] ints : board) {
            for (int anInt : ints) {
                result.append(anInt).append("  ");
            }
            result.append("\n");
        }


        return result.toString();
    }

    String boardToString(int[] currentPiece) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                final String ANSI_RESET = "\u001B[0m";
                final String ANSI_RED = "\u001B[31m";
                if (currentPiece[0] == i && currentPiece[1] == j) {
                    result.append(ANSI_RED).append(board[i][j]).append(ANSI_RESET).append("  ");
                } else {
                    result.append(board[i][j]).append("  ");
                }

            }
            result.append("\n");

        }
        return result.toString();
    }

}
