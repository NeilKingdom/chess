package chess;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.util.Arrays;
import java.util.HashMap;

//TODO
//Clean code: Namely, I hate the hard-to-read HashMaps, constructor and method structure

/**
 * A simple game of chess created by Neil Kingdom using the java.awt and javax.swing libraries
 * @author Neil Kingdom
 * @version 1.0
 * @since 2020-07-09
 */
public class Chess implements Rules {

    private JFrame jf;
    private GridBagConstraints gbc;
    private DrawCanvas canvas;
    private HashMap<JLabel[], Integer> setState;
    private HashMap<Integer, JLabel[]> parentLabel;
    private HashMap<Integer, String> parentName;
    private HashMap<Integer, String> coordinates;
    private String coorX;
    private boolean isValid, whitesTurn, promoting;
    private int coorY, clickX, clickY, tileWidth, tileHeight, playerState, nextNull;
    private Image background;
    private Image[] allBackgrounds;
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    //Default Icons
    private ImageIcon wPawnICO    = new ImageIcon(getImageAsStream("/WhitePawn.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bPawnICO    = new ImageIcon(getImageAsStream("/BlackPawn.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon wBishopICO  = new ImageIcon(getImageAsStream("/WhiteBishop.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bBishopICO  = new ImageIcon(getImageAsStream("/BlackBishop.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon wRookICO    = new ImageIcon(getImageAsStream("/WhiteRook.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bRookICO    = new ImageIcon(getImageAsStream("/BlackRook.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon wKnightICO  = new ImageIcon(getImageAsStream("/WhiteKnight.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bKnightICO  = new ImageIcon(getImageAsStream("/BlackKnight.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon wQueenICO   = new ImageIcon(getImageAsStream("/WhiteQueen.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bQueenICO   = new ImageIcon(getImageAsStream("/BlackQueen.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon wKingICO    = new ImageIcon(getImageAsStream("/WhiteKing.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bKingICO    = new ImageIcon(getImageAsStream("/BlackKing.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon nullTileICO = new ImageIcon(getImageAsStream("/Blank.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));

    //Selected Icons
    private ImageIcon wPawnSelICO   = new ImageIcon(getImageAsStream("/WPawnSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bPawnSelICO   = new ImageIcon(getImageAsStream("/BPawnSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon wBishopSelICO = new ImageIcon(getImageAsStream("/WBishopSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bBishopSelICO = new ImageIcon(getImageAsStream("/BBishopSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon wRookSelICO   = new ImageIcon(getImageAsStream("/WRookSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bRookSelICO   = new ImageIcon(getImageAsStream("/BRookSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon wKnightSelICO = new ImageIcon(getImageAsStream("/WKnightSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bKnightSelICO = new ImageIcon(getImageAsStream("/BKnightSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon wQueenSelICO  = new ImageIcon(getImageAsStream("/WQueenSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bQueenSelICO  = new ImageIcon(getImageAsStream("/BQueenSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon wKingSelICO   = new ImageIcon(getImageAsStream("/WKingSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon bKingSelICO   = new ImageIcon(getImageAsStream("/BKingSelect.png").getScaledInstance(SCREEN_SIZE.width/32, SCREEN_SIZE.width/32, Image.SCALE_SMOOTH));
    private ImageIcon logoICO       = new ImageIcon(getImageAsStream("/ChessLogo.png").getScaledInstance(SCREEN_SIZE.width/3, SCREEN_SIZE.width/4, Image.SCALE_SMOOTH));

    //Player Objects
    private JLabel lastPlayerSelected;
    private JLabel[] pawn = new Players(16).playerCreator(wPawnICO, bPawnICO);
    private JLabel[] bishop = new Players(20).playerCreator(wBishopICO, bBishopICO);
    private JLabel[] rook = new Players(20).playerCreator(wRookICO, bRookICO);
    private JLabel[] knight = new Players(20).playerCreator(wKnightICO, bKnightICO);
    private JLabel[] king = new Players(2).playerCreator(wKingICO, bKingICO);
    private JLabel[] queen = new Players(18).playerCreator(wQueenICO, bQueenICO);
    private JLabel[] nullTile = new Players(64).playerCreator(nullTileICO, nullTileICO);
    private JLabel[][] allPlayers = new JLabel[][] {pawn, rook, bishop, knight, king, queen};

    public Chess() {

        nextNull = 0;
        promoting = false;
        whitesTurn = true; //Default starting player
        background = getImageAsStream("/PlainGreen.png"); //Default background

        jf = new JFrame("Java Chess");
        canvas = new DrawCanvas();
        canvas.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        initMenu();
        JFrame.setDefaultLookAndFeelDecorated(true); // Make window appear as it would on the current OS
        jf.add(canvas);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setMinimumSize(new Dimension(jf.getWidth(), jf.getHeight()));
        jf.setLocation(SCREEN_SIZE.width/2 - jf.getWidth()/2, SCREEN_SIZE.height/2 - jf.getHeight()/2);
        jf.setResizable(false);
        jf.setVisible(true);

        for(JLabel[] playerType : allPlayers)
            onSelect(playerType.length, playerType);

        for(int i = 0; i < nullTile.length; i++) {
            final int j = i;
            nullTile[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if(!promoting)
                        onMove(nullTile, nullTile[j], false);
                }
            });
        }

        //Not a fan of all these hashmaps
        setState = new HashMap<>();
        setState.put(pawn, 1);
        setState.put(rook, 2);
        setState.put(bishop, 3);
        setState.put(knight, 4);
        setState.put(king, 5);
        setState.put(queen, 6);

        parentLabel = new HashMap<>();
        parentLabel.put(1, pawn);
        parentLabel.put(2, rook);
        parentLabel.put(3, bishop);
        parentLabel.put(4, knight);
        parentLabel.put(5, king);
        parentLabel.put(6, queen);

        parentName = new HashMap<>();
        parentName.put(1, "pawn");
        parentName.put(2, "rook");
        parentName.put(3, "bishop");
        parentName.put(4, "knight");
        parentName.put(5, "king");
        parentName.put(6, "queen");

        coordinates = new HashMap<>();
        coordinates.put(0, "A");
        coordinates.put(1, "B");
        coordinates.put(2, "C");
        coordinates.put(3, "D");
        coordinates.put(4, "E");
        coordinates.put(5, "F");
        coordinates.put(6, "G");
        coordinates.put(7, "H");
    }

    /**
     * Setup the main menu before the game starts.
     * @since 2020-07-09
     */
    public void initMenu() {

        allBackgrounds = new Image[10];
        String[] boards = new String[10];
        String[] bkrFilePaths = new String[10];
        bkrFilePaths[0] = "/PlainGreen.png";
        bkrFilePaths[1] = "/PlainBlue.png";
        bkrFilePaths[2] = "/PlainRed.png";
        bkrFilePaths[3] = "/Drab.png";
        bkrFilePaths[4] = "/CoralReef.png";
        bkrFilePaths[5] = "/NeonCity.png";
        bkrFilePaths[6] = "/Pastel.png";
        bkrFilePaths[7] = "/TrickorTreat.png";
        bkrFilePaths[8] = "/UltraViolet.png";
        bkrFilePaths[9] = "/WorldFamousFries.png";

        for(int i = 0; i < allBackgrounds.length; i++) {
            allBackgrounds[i] = getImageAsStream(bkrFilePaths[i]);
            boards[i] = bkrFilePaths[i].substring(1).replace(".png", "");
        }

        JComboBox<String> bkrSelect = new JComboBox<>(boards);
        bkrSelect.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selection = e.getItem().toString();
                    for(int j = 0; j < allBackgrounds.length; j++) {
                        if(boards[j].equals(selection)) {
                            background = allBackgrounds[j];
                            jf.revalidate();
                            canvas.repaint();
                        }
                    }
                }
            }
        });

        JButton start = new JButton("Start");
        start.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Welcome to chess! The white player begins!\n");
                canvas.removeAll();
                jf.setResizable(true);
                initBoard();
            }
        });

        //Title screen component placements
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 0, 200, 0);
        JLabel logo = new JLabel(logoICO);
        canvas.add(logo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.ipadx = 150;
        gbc.ipady = 40;
        gbc.insets = new Insets(300, 0, 0, 0);
        canvas.add(start, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.ipadx = 90;
        gbc.ipady = 10;
        gbc.insets = new Insets(300, 0, 0, 0);
        canvas.add(bkrSelect, gbc);
    }

    /**
     * Refresh values which change due to scalability
     * @since 2020-07-09
     */
    public void refresh() {
        //Content pane does not include border size
        tileWidth = jf.getContentPane().getWidth()/8;
        tileHeight = jf.getContentPane().getHeight()/8;
        coorX = coordinates.get(((tileWidth*8) - lastPlayerSelected.getX())/tileWidth);
        coorY = ((tileHeight*9) - lastPlayerSelected.getY())/tileHeight;
    }

    //Adds players to board
    public void initBoard() {

        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.insets = new Insets(0, 0, 0, 0);

        canvas.add(rook[0], gbc);
        gbc.gridx = 1;
        canvas.add(knight[0], gbc);
        gbc.gridx = 2;
        canvas.add(bishop[0], gbc);
        gbc.gridx = 3;
        canvas.add(king[0], gbc);
        gbc.gridx = 4;
        canvas.add(queen[0], gbc);
        gbc.gridx = 5;
        canvas.add(bishop[1], gbc);
        gbc.gridx = 6;
        canvas.add(knight[1], gbc);
        gbc.gridx = 7;
        canvas.add(rook[1], gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;

        canvas.add(rook[10], gbc);
        gbc.gridx = 1;
        canvas.add(knight[10], gbc);
        gbc.gridx = 2;
        canvas.add(bishop[10], gbc);
        gbc.gridx = 3;
        canvas.add(king[1], gbc);
        gbc.gridx = 4;
        canvas.add(queen[9], gbc);
        gbc.gridx = 5;
        canvas.add(bishop[11], gbc);
        gbc.gridx = 6;
        canvas.add(knight[11], gbc);
        gbc.gridx = 7;
        canvas.add(rook[11], gbc);

        int n = 0;
        for(int j = 1; j <= 6; j+=5) {
            for (int i = 0; i < pawn.length/2; i++) {
                gbc.gridx = i;
                gbc.gridy = j;
                canvas.add(pawn[n++], gbc);
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                gbc.gridx = j;
                gbc.gridy = i + 2;
                canvas.add(nullTile[nextNull++], gbc);
            }
        }
        jf.revalidate();
        jf.repaint();
    }

    /**
     * Images must be obtained as an InputStream using a classLocator in order to build the .jar file
     * @since 2020-07-09
     * @param filePath: The path to obtain a given image file
     * @return Returns an Image object
     */
    public Image getImageAsStream(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Images must be obtained as an InputStream using a classLocator in order to build the .jar file
     * @since 2020-07-09
     */
    public void swapIcon() {
        switch(playerState) {
            case 1:
                if(lastPlayerSelected.getIcon() == bPawnICO)
                    lastPlayerSelected.setIcon(bPawnSelICO);
                else if(lastPlayerSelected.getIcon() == bPawnSelICO)
                    lastPlayerSelected.setIcon(bPawnICO);
                else if(lastPlayerSelected.getIcon() == wPawnICO)
                    lastPlayerSelected.setIcon(wPawnSelICO);
                else lastPlayerSelected.setIcon(wPawnICO);
                break;
            case 2:
                if(lastPlayerSelected.getIcon() == bRookICO)
                    lastPlayerSelected.setIcon(bRookSelICO);
                else if(lastPlayerSelected.getIcon() == bRookSelICO)
                    lastPlayerSelected.setIcon(bRookICO);
                else if(lastPlayerSelected.getIcon() == wRookICO)
                    lastPlayerSelected.setIcon(wRookSelICO);
                else lastPlayerSelected.setIcon(wRookICO);
                break;
            case 3:
                if(lastPlayerSelected.getIcon() == bBishopICO)
                    lastPlayerSelected.setIcon(bBishopSelICO);
                else if(lastPlayerSelected.getIcon() == bBishopSelICO)
                    lastPlayerSelected.setIcon(bBishopICO);
                else if(lastPlayerSelected.getIcon() == wBishopICO)
                    lastPlayerSelected.setIcon(wBishopSelICO);
                else lastPlayerSelected.setIcon(wBishopICO);
                break;
            case 4:
                if(lastPlayerSelected.getIcon() == bKnightICO)
                    lastPlayerSelected.setIcon(bKnightSelICO);
                else if(lastPlayerSelected.getIcon() == bKnightSelICO)
                    lastPlayerSelected.setIcon(bKnightICO);
                else if(lastPlayerSelected.getIcon() == wKnightICO)
                    lastPlayerSelected.setIcon(wKnightSelICO);
                else lastPlayerSelected.setIcon(wKnightICO);
                break;
            case 5:
                if(lastPlayerSelected.getIcon() == bKingICO)
                    lastPlayerSelected.setIcon(bKingSelICO);
                else if(lastPlayerSelected.getIcon() == bKingSelICO)
                    lastPlayerSelected.setIcon(bKingICO);
                else if(lastPlayerSelected.getIcon() == wKingICO)
                    lastPlayerSelected.setIcon(wKingSelICO);
                else lastPlayerSelected.setIcon(wKingICO);
                break;
            case 6:
                if(lastPlayerSelected.getIcon() == bQueenICO)
                    lastPlayerSelected.setIcon(bQueenSelICO);
                else if(lastPlayerSelected.getIcon() == bQueenSelICO)
                    lastPlayerSelected.setIcon(bQueenICO);
                else if(lastPlayerSelected.getIcon() == wQueenICO)
                    lastPlayerSelected.setIcon(wQueenSelICO);
                else lastPlayerSelected.setIcon(wQueenICO);
                break;
        }
        jf.validate();
        canvas.repaint();
    }

    /**
     * Add Event Listeners to all players
     * @since 2020-07-09
     * @param playerSize: The amount of players given a player type
     * @param selectedPlayer: The given player-type
     */
    public void onSelect(int playerSize, JLabel[] selectedPlayer) {

        for (int i = 0; i < playerSize; i++) {

            final int j = i;
            selectedPlayer[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (playerState == 0 && !promoting) {
                        playerState = setState.get(selectedPlayer);
                        lastPlayerSelected = selectedPlayer[j];
                        refresh();
                        String p = parentName.get(playerState);
                        System.out.println("Selected " + p + " at: " + coorX + coorY + "\n");
                        swapIcon();
                    }
                    else
                        onMove(selectedPlayer, selectedPlayer[j], true);
                }
            });
        }
    }

    /**
     * Calls ruleSet and decides new placement of players
     * @since 2020-07-09
     * @param underAttackLabel: The player-type/parent of the opponent under attack
     * @param underAttack: The specific opponent under attack
     * @param isPlayer: States whether or not the "opponent" is truly an opponent, or a blank tile
     */
    public void onMove(JLabel[] underAttackLabel, JLabel underAttack, boolean isPlayer) {

        if(isPlayer)
            refresh();
        clickX = underAttack.getX();
        clickY = underAttack.getY();
        swapIcon();

        //This is awful
        //If player being attacked and last player selected are on the same team, return false
        try {
            if(Arrays.asList(parentLabel.get(playerState)).indexOf(lastPlayerSelected) < parentLabel.get(playerState).length/2 && Arrays.asList(underAttackLabel).indexOf(underAttack) < underAttackLabel.length/2 && underAttackLabel != nullTile
                    || Arrays.asList(parentLabel.get(playerState)).indexOf(lastPlayerSelected) >= parentLabel.get(playerState).length/2 && Arrays.asList(underAttackLabel).indexOf(underAttack) >= underAttackLabel.length/2 && underAttackLabel != nullTile) {
                if(clickX != lastPlayerSelected.getX() || clickY != lastPlayerSelected.getY())
                    System.out.println("I'm not your enemy!\n");
                isValid = false;
            }
            else chooseRuleSet(isPlayer);
        } catch(NullPointerException ignored){}

        if(isValid && underAttack != lastPlayerSelected) {

            refresh();
            if(whitesTurn && parentLabel.get(playerState) == pawn && underAttack.getY() > tileHeight*7 && underAttackLabel != king
                    || !whitesTurn && underAttack.getY() < tileHeight && parentLabel.get(playerState) == pawn && underAttackLabel != king)
                promotePawn();

            clickX = lastPlayerSelected.getX()/tileWidth;
            clickY = lastPlayerSelected.getY()/tileHeight;
            gbc.gridx = underAttack.getX()/tileWidth;
            gbc.gridy = underAttack.getY()/tileHeight;
            canvas.add(lastPlayerSelected, gbc);
            gbc.gridx = clickX;
            gbc.gridy = clickY;

            if(isPlayer) {
                canvas.remove(underAttack);
                canvas.add(nullTile[nextNull++], gbc);
                String s = (whitesTurn) ? "White" : "Black";
                String p = parentName.get(setState.get(underAttackLabel));
                System.out.println(s + " conquered the opponent's " + p + "\n");

                if(underAttack == king[0] || underAttack == king[1]) {
                    canvas.removeAll();
                    JLabel winner = new JLabel(s + " wins!");
                    JButton playAgain = new JButton("Play Again?");
                    playAgain.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            whitesTurn = true;
                            nextNull = 0;
                            canvas.removeAll();
                            initMenu();
                            jf.revalidate();
                            canvas.repaint();
                        }
                    });
                    winner.setFont(new Font("SansSerif", Font.PLAIN, 32));
                    gbc.gridy = 0;
                    gbc.anchor = GridBagConstraints.PAGE_END;
                    canvas.add(winner, gbc);
                    gbc.gridy = 1;
                    gbc.anchor = GridBagConstraints.PAGE_START;
                    canvas.add(playAgain, gbc);
                    jf.revalidate();
                    canvas.repaint();
                }
            }

            else {
                canvas.add(underAttack, gbc);
                coorX = coordinates.get(((tileWidth*8) - underAttack.getX())/tileWidth);
                coorY = (((tileHeight*9) - underAttack.getY())/tileHeight);
                String s = (whitesTurn) ? "White's" : "Black's";
                String p = parentName.get(playerState);
                System.out.println(s + " " + p +" moved to position: " + coorX + coorY + "\n");
            }
            whitesTurn = !whitesTurn;
        }
        jf.validate();
        jf.repaint();
        playerState = 0;
        isValid = false;
    }

    /**
     * Anonymous inner class used for drawing the background
     */
    class DrawCanvas extends JComponent {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g); //Override g in Graphics class to avoid bugs
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Chooses the appropriate rule-set for validating moves based on the current playerState
     * @since 2020-07-09
     * @param isPlayer: States whether or not the "opponent" is truly an opponent, or a blank tile
     */
    public void chooseRuleSet(boolean isPlayer) {
        switch (playerState) {
            case 1:
                if(Arrays.asList(pawn).indexOf(lastPlayerSelected) >= pawn.length/2)
                    isValid = bPawnRules(isPlayer);
                else isValid = wPawnRules(isPlayer);
                break;
            case 2:
                isValid = rookRules(isPlayer);
                break;
            case 3:
                isValid = bishopRules(isPlayer);
                break;
            case 4:
                isValid = knightRules(isPlayer);
                break;
            case 5:
                isValid = kingRules(isPlayer);
                break;
            case 6:
                isValid = queenRules(isPlayer);
                break;
        }
    }

    /**
     * Used for promoting pawns to either queen, bishop, rook, or knight if they reach the opposite side of the board
     * @since 2020-07-09
     */
    public void promotePawn() {

        promoting = true;
        String s = (whitesTurn) ? "White's" : "Black's";
        JLabel promotionText = new JLabel(s +" pawn may be promoted");
        promotionText.setFont(new Font("SansSerif", Font.PLAIN, 25));
        String[] allPlayerNames = new String[]{"", "queen", "bishop", "knight", "rook"};
        JComboBox<String> promoBox = new JComboBox<>(allPlayerNames);
        JFrame promoFrame = new JFrame();
        GridBagConstraints pfConstraints = new GridBagConstraints();
        promoFrame.setLayout(new GridBagLayout());
        promoFrame.setVisible(true);
        promoFrame.setResizable(false);

        promoBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    String selection = e.getItem().toString();
                    for(JLabel[] playerType : allPlayers) {
                        if(selection.equals(parentName.get(setState.get(playerType)))) {
                            int i = 0;
                            if(whitesTurn) i = playerType.length/2;
                            for(; i < playerType.length; i++) {
                                if(playerType[i].getParent() == null) {
                                    System.out.println("Player's pawn has become a " + selection + "\n");
                                    gbc.gridx = lastPlayerSelected.getX()/tileWidth;
                                    gbc.gridy = lastPlayerSelected.getY()/tileHeight;
                                    canvas.remove(lastPlayerSelected);
                                    lastPlayerSelected = playerType[i];
                                    canvas.add(lastPlayerSelected, gbc);
                                    break;
                                }
                            }
                        }
                    }
                }
                promoting = false;
                promoFrame.dispose();
                jf.revalidate();
                canvas.repaint();
            }
        });
        pfConstraints.gridy = 0;
        pfConstraints.insets = new Insets(SCREEN_SIZE.height/10, 0, 0, 0);
        pfConstraints.anchor = GridBagConstraints.PAGE_END;
        promoFrame.add(promotionText, pfConstraints);
        pfConstraints.gridy = 1;
        pfConstraints.insets = new Insets(0, 0, SCREEN_SIZE.height/10, 0);
        pfConstraints.anchor = GridBagConstraints.PAGE_START;
        promoFrame.add(promoBox, pfConstraints);
        promoFrame.pack();
        promoFrame.setLocation(SCREEN_SIZE.width/2 - promoFrame.getWidth()/2, SCREEN_SIZE.height/2 - promoFrame.getHeight()/2);
    }

    /**
     * Checks for any players between the lastPlayerSelected and their new move location
     * ie. checks if the user is attempting to skip over another player
     * @since 2020-07-09
     */
    public boolean checkSkip() {

        int lpsX = lastPlayerSelected.getX();
        int lpsY = lastPlayerSelected.getY();

        if(clickX == lpsX && clickY < lpsY) {
            for(int y = lpsY - tileHeight; y > clickY; y-=tileHeight){
                for(JLabel[] jl : allPlayers) {
                    for(JLabel p : jl) {
                        if(p.getParent() != null && p.getX() == lpsX && p.getY() == y) {
                            System.out.println("Cannot jump player\n");
                            return true;
                        }
                    }
                }
            }
        }
        else if(clickX > lpsX && clickY < lpsY) {
            for(int x = lpsX+tileWidth; x < clickX; x+=tileWidth){
                for(JLabel[] jl : allPlayers) {
                    for(JLabel p : jl) {
                        if(p.getParent() != null && p.getX() == x && p.getY() == lpsY - ((x-lpsX)/tileWidth)*tileHeight) {
                            System.out.println("Cannot jump player\n");
                            return true;
                        }
                    }
                }
            }
        }
        else if(clickX > lpsX && clickY == lpsY) {
            for(int x = lpsX + tileWidth; x < clickX; x+=tileWidth){
                for(JLabel[] jl : allPlayers) {
                    for(JLabel p : jl) {
                        if(p.getParent() != null && p.getX() == x && p.getY() == lpsY) {
                            System.out.println("Cannot jump player\n");
                            return true;
                        }
                    }
                }
            }
        }
        else if(clickX > lpsX) {
            for(int x = lpsX+tileWidth; x < clickX; x+=tileWidth){
                for(JLabel[] jl : allPlayers) {
                    for(JLabel p : jl) {
                        if(p.getParent() != null && p.getX() == x && p.getY() == lpsY + ((x-lpsX)/tileWidth)*tileHeight) {
                            System.out.println("Cannot jump player\n");
                            return true;
                        }
                    }
                }
            }
        }
        else if(clickX == lpsX && clickY > lpsY) {
            for(int y = lpsY + tileHeight; y < clickY; y+=tileHeight){
                for(JLabel[] jl : allPlayers) {
                    for(JLabel p : jl) {
                        if(p.getParent() != null && p.getX() == lpsX && p.getY() == y) {
                            System.out.println("Cannot jump player\n");
                            return true;
                        }
                    }
                }
            }
        }
        else if(clickX < lpsX && clickY > lpsY) {
            for(int x = lpsX-tileWidth; x > clickX; x-=tileWidth){
                for(JLabel[] jl : allPlayers) {
                    for(JLabel p : jl) {
                        if(p.getParent() != null && p.getX() == x && p.getY() == lpsY + ((lpsX-x)/tileWidth)*tileHeight) {
                            System.out.println("Cannot jump player\n");
                            return true;
                        }
                    }
                }
            }
        }
        else if(clickX < lpsX && clickY == lpsY) {
            for(int x = lpsX - tileWidth; x > clickX; x-=tileWidth){
                for(JLabel[] jl : allPlayers) {
                    for(JLabel p : jl) {
                        if(p.getParent() != null && p.getX() == x && p.getY() == lpsY) {
                            System.out.println("Cannot jump player\n");
                            return true;
                        }
                    }
                }
            }
        }
        else if(clickX < lpsX) {
            for(int x = lpsX-tileWidth; x > clickX; x-=tileWidth) {
                for(JLabel[] jl : allPlayers) {
                    for(JLabel p : jl) {
                        if(p.getParent() != null && p.getX() == x && p.getY() == lpsY - ((lpsX-x)/tileWidth)*tileHeight) {
                            System.out.println("Cannot jump player\n");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean bPawnRules(boolean isPlayer) {

        int lpsX = lastPlayerSelected.getX();
        int lpsY = lastPlayerSelected.getY();

        if(whitesTurn) {
            System.out.println("It is white's turn to move\n");
            whitesTurn = true;
            return false;
        }
        else if(clickX == (lpsX + tileWidth) && clickY == (lpsY - tileHeight) && isPlayer
                || clickX == (lpsX - tileWidth) && clickY == (lpsY - tileHeight) && isPlayer) {
            return true;
        }
        else if(clickX > lpsX || clickX < lpsX || clickY > lpsY || clickY < (lpsY-tileHeight*2) || clickY < (lpsY - tileHeight) && lpsY <= tileHeight*6 || isPlayer) {
            System.out.println("Invalid move\n");
            return false;
        }
        else return true;
    }

    @Override
    public boolean wPawnRules(boolean isPlayer) {

        int lpsX = lastPlayerSelected.getX();
        int lpsY = lastPlayerSelected.getY();

        if(!whitesTurn) {
            System.out.println("It is black's turn to move\n");
            whitesTurn = false;
            return false;
        }
        else if(clickX == (lpsX + tileWidth) && clickY == (lpsY + tileHeight) && isPlayer
                || clickX == (lpsX - tileWidth) && clickY == (lpsY + tileHeight) && isPlayer) {
            return true;
        }
        else if(clickX > lpsX || clickX < lpsX || clickY < lpsY || clickY > (lpsY+tileHeight*2) || clickY > (lpsY + tileHeight) && lpsY >= tileHeight*2 || isPlayer) {
            System.out.println("Invalid move\n");
            return false;
        }
        else return true;
    }

    @Override
    public boolean bishopRules(boolean isPlayer) {

        int lpsX = lastPlayerSelected.getX();
        int lpsY = lastPlayerSelected.getY();

        if(Arrays.asList(bishop).indexOf(lastPlayerSelected) >= bishop.length/2 && whitesTurn) {
            System.out.println("It is white's turn to move\n");
            whitesTurn = true;
            return false;
        }
        else if (Arrays.asList(bishop).indexOf(lastPlayerSelected) < bishop.length/2 && !whitesTurn) {
            System.out.println("It is black's turn to move\n");
            whitesTurn = false;
            return false;
        }
        else if(checkSkip()) {
            return false;
        }
        else if(Math.abs(clickX - lpsX)/tileWidth == Math.abs(clickY - lpsY)/tileHeight) {
            return true;
        }
        else {
            System.out.println("Invalid move\n");
            return false;
        }
    }

    @Override
    public boolean knightRules(boolean isPlayer) {

        int lpsX = lastPlayerSelected.getX();
        int lpsY = lastPlayerSelected.getY();

        if(Arrays.asList(knight).indexOf(lastPlayerSelected) >= knight.length/2 && whitesTurn) {
            System.out.println("It is white's turn to move\n");
            whitesTurn = true;
            return false;
        }
        else if (Arrays.asList(knight).indexOf(lastPlayerSelected) < knight.length/2 && !whitesTurn) {
            System.out.println("It is black's turn to move\n");
            whitesTurn = false;
            return false;
        }
        else if(clickX == (lpsX - tileWidth) && clickY == (lpsY - tileHeight*2) || clickX == (lpsX + tileWidth) && clickY == (lpsY - tileHeight*2) ||
                clickX == (lpsX + tileWidth*2) && clickY == (lpsY - tileHeight) || clickX == (lpsX + tileWidth*2) && clickY == (lpsY + tileHeight) ||
                clickX == (lpsX - tileWidth) && clickY == (lpsY + tileHeight*2) || clickX == (lpsX + tileWidth) && clickY == (lpsY + tileHeight*2) ||
                clickX == (lpsX - tileWidth*2) && clickY == (lpsY + tileHeight) || clickX == (lpsX - tileWidth*2) && clickY == (lpsY - tileHeight)) {
            return true;
        }
        else {
            System.out.println("Invalid move\n");
            return false;
        }
    }

    @Override
    public boolean rookRules(boolean isPlayer) {

        int lpsX = lastPlayerSelected.getX();
        int lpsY = lastPlayerSelected.getY();

        if(Arrays.asList(rook).indexOf(lastPlayerSelected) >= rook.length/2 && whitesTurn) {
            System.out.println("It is white's turn to move\n");
            whitesTurn = true;
            return false;
        }
        else if (Arrays.asList(rook).indexOf(lastPlayerSelected) < rook.length/2 && !whitesTurn) {
            System.out.println("It is black's turn to move\n");
            whitesTurn = false;
            return false;
        }
        else if(checkSkip()) {
            return false;
        }
        else if(clickX != lpsX && clickY == lpsY || clickY != lpsY && clickX == lpsX) {
            return true;
        }
        else {
            System.out.println("Invalid move\n");
            return false;
        }
    }

    @Override
    public boolean queenRules(boolean isPlayer) {

        int lpsX = lastPlayerSelected.getX();
        int lpsY = lastPlayerSelected.getY();

        if(lastPlayerSelected == queen[1] && whitesTurn) {
            System.out.println("It is white's turn to move\n");
            whitesTurn = true;
            return false;
        }
        else if (lastPlayerSelected == queen[0] && !whitesTurn) {
            System.out.println("It is black's turn to move\n");
            whitesTurn = false;
            return false;
        }
        else if(checkSkip()) {
            return false;
        }
        else if(clickX != lpsX && clickY == lpsY || clickY != lpsY && clickX == lpsX || Math.abs(clickX - lpsX)/tileWidth == Math.abs(clickY - lpsY)/tileHeight) {
            return true;
        }
        else {
            System.out.println("Invalid move\n");
            return false;
        }
    }

    @Override
    public boolean kingRules(boolean isPlayer) {

        int lpsX = lastPlayerSelected.getX();
        int lpsY = lastPlayerSelected.getY();

        if(lastPlayerSelected == king[1] && whitesTurn) {
            System.out.println("It is white's turn to move\n");
            whitesTurn = true;
            return false;
        }
        else if (lastPlayerSelected == king[0] && !whitesTurn) {
            System.out.println("It is black's turn to move\n");
            whitesTurn = false;
            return false;
        }
        else if(clickX > (lpsX + tileWidth) || clickY > (lpsY + tileHeight) || clickX < (lpsX - tileWidth) || clickY < (lpsY - tileHeight)) {
            System.out.println("Invalid move\n");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Chess();
            }
        });
    }
}

