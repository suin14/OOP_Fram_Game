package game.Character;


import game.Dialog.DialogBubble;
import game.Hud.InventoryBar;
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
        super.interact();

        if (checkEvents("hello")) {
            DialogBubble.talk(getClass().getSimpleName(), "hello");
            addEvents("hello");
        } else {
            if (InventoryBar.getInstance().getItemCounts() != 0) {
                DialogBubble.talk(getClass().getSimpleName(), "sell");
                InventoryBar.getInstance().sellItem();
            }
            else {
                DialogBubble.talk(getClass().getSimpleName(), "talk");
            }
        }


    }
}
