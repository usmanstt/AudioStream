package com.example.audiostream.playlist;

public class CreateListModel {
    private String listName;
    private String listId;

    public CreateListModel(){}

    public CreateListModel(String listName, String listId) {
        this.listName = listName;
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }
}
