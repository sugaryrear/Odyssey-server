package io.Odyssey.content.mail;

public class Mail {
private int Id;
    private int itemId;
    private int amount;
    private String textmsg;

    public Mail(int id, int itemId, int amount, String textmsg) {
        this.Id = id;
        this.itemId = itemId;
        this.amount = amount;
        this.textmsg = textmsg;
    }
    public int getId() {
        return Id;
    }
    public int getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }
    public String getTextmsg(){
        return textmsg;
    }
}
