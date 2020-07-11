package com.chess;
import javax.swing.*;

/**
 * This class serves the purpose of automating the creation of each playerType,
 * as well as both black and white counterparts
 * @author Neil Kingdom
 * @version 1.0
 * @since 2020-07-09
 */
public class Players extends JLabel {

    private int playerNum;

    public Players(int numOfPlayers){
        this.playerNum = numOfPlayers;
    }

    /**
     *  Creates JLabels for each unique player and returns an array containing each one
     * @since 2020-07-09
     * @param wIcon: The icon which should be used for the white player
     * @param bIcon: The icon which should be used for the black player
     * @return jlArr: The array which stores each player of the current playerType
     */
    public JLabel[] playerCreator(ImageIcon wIcon, ImageIcon bIcon) {

        JLabel[] jlArr = new JLabel[playerNum];
        for(int i = 0; i < playerNum/2; i++)
            jlArr[i] = new JLabel(wIcon);
        for(int i = playerNum/2; i < playerNum; i++)
            jlArr[i] = new JLabel(bIcon);
        return jlArr;
    }
}
