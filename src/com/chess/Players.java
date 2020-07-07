package com.chess;
import javax.swing.*;

public class Players extends JLabel {

    private int playerNum;

    public Players(int numOfPlayers){
        this.playerNum = numOfPlayers;
    }

    public JLabel[] playerCreator(ImageIcon wIcon, ImageIcon bIcon) {

        JLabel[] jlArr = new JLabel[playerNum];
        for(int i = 0; i < playerNum/2; i++)
            jlArr[i] = new JLabel(wIcon);

        for(int i = playerNum/2; i < playerNum; i++)
            jlArr[i] = new JLabel(bIcon);

        return jlArr;
    }
}
