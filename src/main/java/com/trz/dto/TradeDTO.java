package com.trz.dto;

import java.util.ArrayList;
import java.util.List;

public class TradeDTO {

    private String sender;
    private List<ItemDTO> postTradeSenderItems = new ArrayList<>();
    private String receiver;
    private List<ItemDTO> postTradeReceiverItems = new ArrayList<>();

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<ItemDTO> getPostTradeSenderItems() {
        return postTradeSenderItems;
    }

    public void setPostTradeSenderItems(List<ItemDTO> postTradeSenderItems) {
        this.postTradeSenderItems = postTradeSenderItems;
    }

    public void setPostTradeSenderItems(ItemDTO postTradeSenderItems) {
        this.postTradeSenderItems.add(postTradeSenderItems);
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public List<ItemDTO> getPostTradeReceiverItems() {
        return postTradeReceiverItems;
    }

    public void setPostTradeReceiverItems(List<ItemDTO> postTradeReceiverItems) {
        this.postTradeReceiverItems = postTradeReceiverItems;
    }

    public void setPostTradeReceiverItems(ItemDTO postTradeReceiverItems) {
        this.postTradeReceiverItems.add(postTradeReceiverItems);
    }
}
