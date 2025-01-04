package game.Tool;

import game.Map.MapLoader;

public abstract class Tool {
    protected String name; // 道具名称
    protected int quantity; // 剩余数量

    public Tool(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void decreaseQuantity(int amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    // 使用道具时的行为
    public abstract void use(int x, int y, MapLoader map);
}
