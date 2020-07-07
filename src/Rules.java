public interface Rules {

    boolean bPawnRules(boolean isPlayer);

    boolean wPawnRules(boolean isPlayer);

    boolean bishopRules(boolean isPlayer);

    boolean knightRules(boolean isPlayer);

    boolean rookRules(boolean isPlayer);

    boolean kingRules(boolean isPlayer);

    boolean queenRules(boolean isPlayer);
}