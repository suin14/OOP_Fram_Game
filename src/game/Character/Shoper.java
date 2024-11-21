package game.Character;

import game.Other.StaticValue;


public class Shoper extends Character {
    private static Shoper instance;

    private Shoper(int x, int y) {
        super(x, y);
        setShow(StaticValue.shoper);
    }

    public static Shoper getInstance(int x, int y) {
        if (instance == null) {
            instance = new Shoper(x, y);
        }
        return instance;
    }

    @Override
    public void interact() {

    }
}
