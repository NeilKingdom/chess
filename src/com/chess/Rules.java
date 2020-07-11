package com.chess;

/**
 * An interface used for the player rule-set
 * @author Neil Kingdom
 * @version 1.0
 * @since 2020-07-09
 */
public interface Rules {

    boolean bPawnRules(boolean isPlayer);
    boolean wPawnRules(boolean isPlayer);
    boolean bishopRules(boolean isPlayer);
    boolean knightRules(boolean isPlayer);
    boolean rookRules(boolean isPlayer);
    boolean kingRules(boolean isPlayer);
    boolean queenRules(boolean isPlayer);
}