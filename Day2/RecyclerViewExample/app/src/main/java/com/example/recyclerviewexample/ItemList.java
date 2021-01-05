package com.example.recyclerviewexample;

class ItemList {
    private String text;
    private int icon;

    public ItemList(String text, int icon) {
        this.text = text;
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public int getIcon() {
        return icon;
    }

}
