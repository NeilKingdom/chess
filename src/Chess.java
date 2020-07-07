import java.lang.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//TODO
//Clean code: Namely, I hate the large switch statements, hard-to-read HashMaps

public class Chess implements MouseListener, KeyListener, Rules {

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

    public Image getImageAsStream(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ImageIcon wPawnICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WhitePawn.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bPawnICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BlackPawn.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon wBishopICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WhiteBishop.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bBishopICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BlackBishop.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon wRookICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WhiteRook.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bRookICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BlackRook.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon wKnightICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WhiteKnight.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bKnightICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BlackKnight.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon wQueenICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WhiteQueen.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bQueenICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BlackQueen.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon wKingICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WhiteKing.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bKingICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BlackKing.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon nullTileICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/Blank.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));

    private ImageIcon wPawnSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WPawnSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bPawnSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BPawnSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon wBishopSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WBishopSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bBishopSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BBishopSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon wRookSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WRookSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bRookSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BRookSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon wKnightSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WKnightSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bKnightSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BKnightSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon wQueenSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WQueenSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bQueenSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BQueenSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon wKingSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/WKingSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon bKingSelICO = new ImageIcon(getImageAsStream("/resource/images/chessPieces/BKingSelect.png").getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    private ImageIcon logoICO = new ImageIcon(getImageAsStream("/resource/images/ChessLogo.png").getScaledInstance(1250, 1000, Image.SCALE_SMOOTH));

    //Backgrounds
    protected Image background;
    protected Image[] allBackgrounds;

    //Player Objects
    private JLabel lastPlayerSelected;
    private JLabel[] pawn = new Players(16).playerCreator(wPawnICO, bPawnICO);
    private JLabel[] bishop = new Players(20).playerCreator(wBishopICO, bBishopICO);
    private JLabel[] rook = new Players(20).playerCreator(wRookICO, bRookICO);
    private JLabel[] knight = new Players(20).playerCreator(wKnightICO, bKnightICO);
    private JLabel[] king = new Players(2).playerCreator(wKingICO, bKingICO);
    private JLabel[] queen = new Players(18).playerCreator(wQueenICO, bQueenICO);
    private JLabel[] nullTile = new Players(64).playerCreator(nullTileICO, nullTileICO);
    private JLabel[][] allPlayers = new JLabel[][] {pawn, bishop, rook, knight, king, queen};

    public Chess() {

        nextNull = 0;
        promoting = false;
        whitesTurn = true; //Default starting player

        jf = new JFrame("Chess");
        canvas = new DrawCanvas();
        canvas.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        callMenu();
        JFrame.setDefaultLookAndFeelDecorated(true); // Make window appear as it would on the current OS
        jf.add(canvas);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //Start window in the middle of the screen
        jf.setLocation(screenSize.width/2 - jf.getWidth()/2, screenSize.height/2 - jf.getHeight()/2);
        jf.setResizable(false);
        jf.setVisible(true);

        for (JLabel[] playerType : allPlayers)
            onSelect(playerType.length, playerType);


        for (int i = 0; i < nullTile.length; i++) {
            final int j = i;
            nullTile[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if(promoting == false)
                        onMove(nullTile, nullTile[j], false);
                }
            });
        }

        //Adds the ability to deselect objects using the escape key
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                synchronized(Chess.class) {
                    switch (e.getID()) {
                        case KeyEvent.KEY_RELEASED:
                            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && playerState != 0) {

                                switch(playerState) {
                                    case 1:
                                        if (Arrays.asList(pawn).indexOf(lastPlayerSelected) >= pawn.length/2)
                                            lastPlayerSelected.setIcon(bPawnICO);
                                        else
                                            lastPlayerSelected.setIcon(wPawnICO);
                                        break;
                                    case 2:
                                        if (Arrays.asList(rook).indexOf(lastPlayerSelected) >= rook.length/2)
                                            lastPlayerSelected.setIcon(bRookICO);
                                        else
                                            lastPlayerSelected.setIcon(wRookICO);
                                        break;
                                    case 3:
                                        if (Arrays.asList(bishop).indexOf(lastPlayerSelected) >= bishop.length/2)
                                            lastPlayerSelected.setIcon(bBishopICO);
                                        else
                                            lastPlayerSelected.setIcon(wBishopICO);
                                        break;
                                    case 4:
                                        if (Arrays.asList(knight).indexOf(lastPlayerSelected) >= knight.length/2)
                                            lastPlayerSelected.setIcon(bKnightICO);
                                        else
                                            lastPlayerSelected.setIcon(wKnightICO);
                                        break;
                                    case 5:
                                        if (Arrays.asList(king).indexOf(lastPlayerSelected) >= king.length/2)
                                            lastPlayerSelected.setIcon(bKingICO);
                                        else
                                            lastPlayerSelected.setIcon(wKingICO);
                                        break;
                                    case 6:
                                        if (Arrays.asList(queen).indexOf(lastPlayerSelected) >= queen.length/2)
                                            lastPlayerSelected.setIcon(bQueenICO);
                                        else
                                            lastPlayerSelected.setIcon(wQueenICO);
                                        break;
                                }
                            }
                            jf.revalidate();
                            canvas.repaint();
                            playerState = 0;
                            break;
                    }
                }
                return true;
            }
        });

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
     * Setup the main menu before the game actually starts
     */
    public void callMenu() {

        String[] boards = new String[10];
        boards[0] = "Coral Reef";
        boards[1] = "Drab";
        boards[2] = "Neon City";
        boards[3] = "Pastel";
        boards[4] = "Plain Blue";
        boards[5] = "Plain Green";
        boards[6] = "Plain Red";
        boards[7] = "Trick or Treat";
        boards[8] = "Ultra Violet";
        boards[9] = "World Famous Fries";

        allBackgrounds = new Image[10];
        allBackgrounds[0] = getImageAsStream("/resource/images/CoralReef.jpg");
        allBackgrounds[1] = getImageAsStream("/resource/images/Drab.jpg");
        allBackgrounds[2] = getImageAsStream("/resource/images/NeonCity.jpg");
        allBackgrounds[3] = getImageAsStream("/resource/images/Pastel.jpg");
        allBackgrounds[4] = getImageAsStream("/resource/images/PlainBlue.jpg");
        allBackgrounds[5] = getImageAsStream("/resource/images/PlainGreen.jpg");
        allBackgrounds[6] = getImageAsStream("/resource/images/PlainRed.jpg");
        allBackgrounds[7] = getImageAsStream("/resource/images/TrickorTreat.jpg");
        allBackgrounds[8] = getImageAsStream("/resource/images/UltraViolet.jpg");
        allBackgrounds[9] = getImageAsStream("/resource/images/WorldFamousFries.jpg");
        background = allBackgrounds[0]; //Default background

        JComboBox<String> cBox = new JComboBox<>(boards);
        cBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selection = e.getItem().toString();
                    for(int j = 0; j < allBackgrounds.length; j++) {
                        if(boards[j] == selection) {
                            background = allBackgrounds[j];
                            jf.revalidate();
                            canvas.repaint();
                        }
                    }
                }
            }
        });

        JButton jb = new JButton("Start");
        jb.addMouseListener(new MouseAdapter() {
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
        canvas.add(jb, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.ipadx = 90;
        gbc.ipady = 10;
        gbc.insets = new Insets(300, 0, 0, 0);
        canvas.add(cBox, gbc);
    }

    //Refresh values which change due to scalability
    public void refresh() {
        //Content pane does not include border size
        tileWidth = jf.getContentPane().getWidth()/8;
        tileHeight = jf.getContentPane().getHeight()/8;
        coorX = coordinates.get(((tileWidth*8) - lastPlayerSelected.getX())/tileWidth);
        coorY = (((tileHeight*9) - lastPlayerSelected.getY())/tileHeight);
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

        for (int i = 0; i < pawn.length/2; i++) {
            gbc.gridx = i;
            gbc.gridy = 1;
            canvas.add(pawn[i], gbc);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                gbc.gridx = j;
                gbc.gridy = i + 2;
                canvas.add(nullTile[nextNull++], gbc);
            }
        }

        for (int i = pawn.length/2; i < pawn.length; i++) {
            gbc.gridx = i - (pawn.length/2);
            gbc.gridy = 6;
            canvas.add(pawn[i], gbc);
        }

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

        jf.revalidate();
        jf.repaint();
    }

    // Add Event Listeners to all players
    public void onSelect(int playerSize, JLabel[] selectedPlayer) {

        for (int i = 0; i < playerSize; i++) {

            final int j = i;
            selectedPlayer[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (playerState == 0 && promoting == false) {
                        playerState = setState.get(selectedPlayer);
                        lastPlayerSelected = selectedPlayer[j];
                        refresh();
                        String s = (whitesTurn) ? (s = "White") : (s = "Black");
                        String p = parentName.get(playerState);
                        System.out.println(s + " selected " + p + " at: " + coorX + coorY + "\n");

                        switch(playerState) {
                            case 1:
                                if (Arrays.asList(pawn).indexOf(lastPlayerSelected) >= pawn.length/2)
                                    lastPlayerSelected.setIcon(bPawnSelICO);
                                else
                                    lastPlayerSelected.setIcon(wPawnSelICO);
                                break;
                            case 2:
                                if (Arrays.asList(rook).indexOf(lastPlayerSelected) >= rook.length/2)
                                    lastPlayerSelected.setIcon(bRookSelICO);
                                else
                                    lastPlayerSelected.setIcon(wRookSelICO);
                                break;
                            case 3:
                                if (Arrays.asList(bishop).indexOf(lastPlayerSelected) >= bishop.length/2)
                                    lastPlayerSelected.setIcon(bBishopSelICO);
                                else
                                    lastPlayerSelected.setIcon(wBishopSelICO);
                                break;
                            case 4:
                                if (Arrays.asList(knight).indexOf(lastPlayerSelected) >= knight.length/2)
                                    lastPlayerSelected.setIcon(bKnightSelICO);
                                else
                                    lastPlayerSelected.setIcon(wKnightSelICO);
                                break;
                            case 5:
                                if (Arrays.asList(king).indexOf(lastPlayerSelected) >= king.length/2)
                                    lastPlayerSelected.setIcon(bKingSelICO);
                                else
                                    lastPlayerSelected.setIcon(wKingSelICO);
                                break;
                            case 6:
                                if (Arrays.asList(queen).indexOf(lastPlayerSelected) >= queen.length/2)
                                    lastPlayerSelected.setIcon(bQueenSelICO);
                                else
                                    lastPlayerSelected.setIcon(wQueenSelICO);
                                break;
                        }
                        jf.validate();
                        canvas.repaint();
                    }
                    else
                        onMove(selectedPlayer, selectedPlayer[j], true);
                }
            });
        }
    }

    //Calls ruleSet and decides new placement of players
    public void onMove(JLabel[] underAttackLabel, JLabel underAttack, boolean isPlayer) {

        if(isPlayer)
            refresh();
        clickX = underAttack.getX();
        clickY = underAttack.getY();

        switch (playerState) {
            case 1:
                if (Arrays.asList(pawn).indexOf(lastPlayerSelected) >= pawn.length/2)
                    lastPlayerSelected.setIcon(bPawnICO);
                else
                    lastPlayerSelected.setIcon(wPawnICO);
                break;
            case 2:
                if (Arrays.asList(rook).indexOf(lastPlayerSelected) >= rook.length/2)
                    lastPlayerSelected.setIcon(bRookICO);
                else
                    lastPlayerSelected.setIcon(wRookICO);
                break;
            case 3:
                if (Arrays.asList(bishop).indexOf(lastPlayerSelected) >= bishop.length/2)
                    lastPlayerSelected.setIcon(bBishopICO);
                else
                    lastPlayerSelected.setIcon(wBishopICO);
                break;
            case 4:
                if (Arrays.asList(knight).indexOf(lastPlayerSelected) >= knight.length/2)
                    lastPlayerSelected.setIcon(bKnightICO);
                else
                    lastPlayerSelected.setIcon(wKnightICO);
                break;
            case 5:
                if (Arrays.asList(king).indexOf(lastPlayerSelected) >= king.length/2)
                    lastPlayerSelected.setIcon(bKingICO);
                else
                    lastPlayerSelected.setIcon(wKingICO);
                break;
            case 6:
                if (Arrays.asList(queen).indexOf(lastPlayerSelected) >= queen.length/2)
                    lastPlayerSelected.setIcon(bQueenICO);
                else
                    lastPlayerSelected.setIcon(wQueenICO);
                break;
        }

        //Have to account for underAttack variable
        chooseRuleSet(isPlayer);
        //If player being attacked and last player selected are on the same team, return false
        try {
            if(Arrays.asList(parentLabel.get(playerState)).indexOf(lastPlayerSelected) < parentLabel.get(playerState).length/2 && Arrays.asList(underAttackLabel).indexOf(underAttack) < underAttackLabel.length/2 && underAttackLabel != nullTile
                    || Arrays.asList(parentLabel.get(playerState)).indexOf(lastPlayerSelected) >= parentLabel.get(playerState).length/2 && Arrays.asList(underAttackLabel).indexOf(underAttack) >= underAttackLabel.length/2 && underAttackLabel != nullTile) {
                if(clickX != lastPlayerSelected.getX() || clickY != lastPlayerSelected.getY())
                    System.out.println("I'm not your enemy!\n");
                isValid = false;
            }
        }
        catch(NullPointerException e){}

        if(isValid && underAttack != lastPlayerSelected) {

            refresh();
            if(whitesTurn && parentLabel.get(playerState) == pawn && underAttack.getY() > tileHeight*7
                    || !whitesTurn && underAttack.getY() < tileHeight && parentLabel.get(playerState) == pawn)
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
                String s = (whitesTurn) ? (s = "White") : (s = "Black");
                String p = parentName.get(setState.get(underAttackLabel));
                System.out.println(s + " conquered the opponent's " + p + "\n");

                if(underAttack == king[0] || underAttack == king[1]) {
                    System.out.println(s + " wins!\n");
                    canvas.removeAll();
                    JButton jb = new JButton("Play Again?");
                    jb.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            nextNull = 0;
                            canvas.removeAll();
                            callMenu();
                            jf.revalidate();
                            canvas.repaint();
                        }
                    });
                    gbc.ipadx = 160;
                    gbc.ipady = 50;
                    gbc.anchor = GridBagConstraints.CENTER;
                    canvas.add(jb, gbc);
                    jf.revalidate();
                    canvas.repaint();
                }
            }

            else {
                canvas.add(underAttack, gbc);
                coorX = coordinates.get(((tileWidth*8) - underAttack.getX())/tileWidth);
                coorY = (((tileHeight*9) - underAttack.getY())/tileHeight);
                String s = (whitesTurn) ? (s = "White's") : (s = "Black's");
                String p = parentName.get(playerState);
                System.out.println(s + " " + p +" moved to position: " + coorX + coorY + "\n");
            }

            if (whitesTurn)
                whitesTurn = false;
            else
                whitesTurn = true;
        }

        jf.validate();
        jf.repaint();
        playerState = 0;
        isValid = false;
    }

    class DrawCanvas extends JComponent {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g); //Override g in Graphics class to avoid bugs
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    //Rules
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

    public void promotePawn() {

        final JPanel glassPane = (JPanel) jf.getGlassPane();
        glassPane.setVisible(true);
        promoting = true;
        String[] allPlayerNames = new String[]{"", "queen", "bishop", "knight", "rook"};

        JComboBox<String> cBox = new JComboBox<String>(allPlayerNames);
        cBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    String selection = e.getItem().toString();
                    for(JLabel[] playerType : allPlayers) {
                        if(selection == parentName.get(setState.get(playerType))) {
                            //Turns are reversed when this function is called
                            if(whitesTurn) {
                                for(int i = playerType.length/2; i < playerType.length; i++) {
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
                            else {
                                for(int i = 0; i < playerType.length/2; i++) {
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
                    glassPane.remove(cBox);
                    glassPane.setVisible(false);
                    jf.revalidate();
                    canvas.repaint();
                }
            }
        });

        glassPane.setLayout(new GridBagLayout());
        GridBagConstraints menuConstraints = new GridBagConstraints();
        glassPane.add(cBox, menuConstraints);
        jf.revalidate();
        canvas.repaint();
    }

    //Checks for players between click and LPS
    public boolean checkSkip() {

        int lpsX = lastPlayerSelected.getX();
        int lpsY = lastPlayerSelected.getY();

        if(clickX == lpsX && clickY < lpsY) {
            for(int y = lpsY - tileHeight; y > clickY; y-=tileHeight){
                for(JLabel[] jl : allPlayers) {
                    for(JLabel p : jl) {
                        if(p.getParent() != null && p.getX() == lpsX && p.getY() == y) {
                            System.out.println("Cannot jump player\n");
                            return false;
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
                            return false;
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
                            return false;
                        }
                    }
                }
            }
        }
        else if(clickX > lpsX && clickY > lpsY) {
            for(int x = lpsX+tileWidth; x < clickX; x+=tileWidth){
                for(JLabel[] jl : allPlayers) {
                    for(JLabel p : jl) {
                        if(p.getParent() != null && p.getX() == x && p.getY() == lpsY + ((x-lpsX)/tileWidth)*tileHeight) {
                            System.out.println("Cannot jump player\n");
                            return false;
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
                            return false;
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
                            return false;
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
                            return false;
                        }
                    }
                }
            }
        }
        else if(clickX < lpsX && clickY < lpsY) {
            for(int x = lpsX-tileWidth; x > clickX; x-=tileWidth){
                for(JLabel[] jl : allPlayers) {
                    for(JLabel p : jl) {
                        if(p.getParent() != null && p.getX() == x && p.getY() == lpsY - ((lpsX-x)/tileWidth)*tileHeight) {
                            System.out.println("Cannot jump player\n");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
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
        else if(!checkSkip()) {
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
        else if(checkSkip() == false) {
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
        else if(!checkSkip()) {
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

    /**
     * Main method for entire project. Run JFrame in new thread do avoid deadlock
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                    new Chess();
            }
        });
    }

    /************UNUSED INTERFACE METHODS**************/

    //MouseListener
    @Override
    public void mouseClicked(MouseEvent arg0) {}
    @Override
    public void mouseEntered(MouseEvent arg0) {}
    @Override
    public void mouseExited(MouseEvent arg0) {}
    @Override
    public void mousePressed(MouseEvent arg0) {}
    @Override
    public void mouseReleased(MouseEvent arg0) {}

    //KeyListener
    @Override
    public void keyPressed(KeyEvent arg0) {}
    @Override
    public void keyReleased(KeyEvent arg0) {}
    @Override
    public void keyTyped(KeyEvent arg0) {}
}
