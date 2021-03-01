import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class OthelloGUI extends JFrame implements ActionListener {

    private OthelloModel model = new OthelloModel();
    private JButton[] spaces = new JButton[64];
    private JButton newGame;
    private JButton undo;
    private JButton computer;
    private JLabel playerWord;
    private JLabel currTurn;
    private JLabel score;
    private JPanel game;

    OthelloGUI() {


        ////////////////
        JFrame frame = new JFrame("Othello Game");
        frame.setLayout(new BorderLayout(0, 0));
        frame.setSize(new Dimension(415, 515));
        JPanel topBar = new JPanel();
        frame.add(topBar, BorderLayout.NORTH);
        newGame = (JButton) topBar.add(new JButton("New"));
        newGame.addActionListener(this);
        undo = (JButton) topBar.add(new JButton("Undo"));
        undo.addActionListener(this);
        playerWord = (JLabel) topBar.add(new JLabel("Player:"));
        currTurn = (JLabel) topBar.add(new JLabel(""));
        playerWord.setForeground(Color.white);
        JPanel bottomBar = new JPanel();
        frame.add(bottomBar, BorderLayout.SOUTH);
        computer = (JButton) bottomBar.add(new JButton("Player"));
        computer.addActionListener(this);
        score = (JLabel) bottomBar.add(new JLabel("Score: White - " + model.getScores()[0] + "   black - " + model.getScores()[1]));
        score.setForeground(Color.white);
        score.setFont(new Font("Arial", Font.PLAIN, 20));
        game = new JPanel();
        Color darkGreen = new Color(44, 115, 44);
        Color maroon = new Color(100, 0, 0);
        game.setBackground(darkGreen);
        topBar.setBackground(maroon);
        bottomBar.setBackground(darkGreen);
        frame.getContentPane().add(game, BorderLayout.CENTER);

        // Adding JButtons
        for (int i = 0; i < spaces.length; i++) {
            spaces[i] = new JButton("");
        }
        for (JButton space : spaces) {
            game.add(space);
            space.addActionListener(this);
        }
        //reset board
        refreshBoard();
        refreshInfo();
        //set stuff
        frame.setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        ////////////////


    }


    public void actionPerformed(ActionEvent e) {
        System.out.println("Action!!!");
        if (e.getSource() == newGame) {
            System.out.println("new");
            model.restart();
            refreshBoard();
            refreshInfo();
        } else if (e.getSource() == undo) {
            if (!model.isComputerPlayer()) {
                model.undo();
            } else {
                model.undo();
                model.undo();
            }
            refreshBoard();
            refreshInfo();
        } else if (e.getSource() == computer) {
            if (model.isComputerPlayer()) {
                model.setComputerPlayer(false);
                computer.setText("Player");
            } else {
                model.setComputerPlayer(true);
                computer.setText("Computer");
                if(model.getPlayer()==2){computerTurn(); refreshBoard();refreshInfo();}

            }

        } else {
            int x = 0;
            int y = 0;

            for (JButton button : spaces) {
                if (e.getSource() == button) {
                    button.setFocusPainted(false);
                    System.out.println(" GUISource:" + x + ", " + y);
                    model.playerTurn(new int[]{y, x});
                    model.boardToString();
                    refreshBoard();
                    refreshInfo();
                    if(model.isComputerPlayer()){computerTurn();}
                    break;
                }
                if (x == 7) {
                    y++;
                }
                x = x == 7 ? 0 : x + 1;
            }
        }
    }

    void refreshBoard() {
        int spacesCounter = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (model.getValue(new int[]{i, j}) == 1) {
                    spaces[spacesCounter].setText("\u2022");
                    spaces[spacesCounter].setFont(new Font("Arial", Font.PLAIN, 110));
                    spaces[spacesCounter].setForeground(Color.white);
                } else if (model.getValue(new int[]{i, j}) == 2) {
                    spaces[spacesCounter].setText("\u2022");
                    spaces[spacesCounter].setFont(new Font("Arial", Font.PLAIN, 110));
                    spaces[spacesCounter].setForeground(Color.black);
                } else {
                    //spaces[spacesCounter].setText(String.valueOf(model.getValue(new int[]{i, j})));
                    spaces[spacesCounter].setText("");
                }
                spaces[spacesCounter].setOpaque(false);
                spaces[spacesCounter].setContentAreaFilled(false);
                spaces[spacesCounter].setBorder(new LineBorder(Color.BLACK));
                spaces[spacesCounter].setPreferredSize(new Dimension(40, 40));

                spacesCounter++;

            }
        }
        for (JButton space : spaces) {
            game.add(space);
            space.addActionListener(this);
        }
    }

    void refreshInfo() {
        if (model.getPlayer() == 1) {
            currTurn.setForeground(Color.white);
        } else if (model.getPlayer() == 2) {
            currTurn.setForeground(Color.black);
        }
        currTurn.setText("\u25CF");
        currTurn.setFont(new Font("Arial", Font.PLAIN, 60));
        playerWord.setFont(new Font("Arial", Font.PLAIN, 30));
        score.setText("Score: White - " + model.getScores()[0] + "   Black - " + model.getScores()[1]);
    }
    void computerTurn() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        model.computerTurn();
        refreshBoard();
        refreshInfo();
    }


    public static void main(String[] args) {
        new OthelloGUI();
    }
}