package com.infolga.messengerinfolga;

import org.jdom2.Element;

public class Participants implements Myin {

    private int id_participants;
    private int conversation_id;
    private int users_id;

    public Participants() {
    }

    public Participants(Element el) {
        users_id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_USERS_ID).getText());
        conversation_id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_CONVERSATION_ID).getText());
        id_participants = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_PARTICIPANTS_ID).getText());
    }

    public int getId_participants() {
        return id_participants;
    }

    public void setId_participants(int id_participants) {
        this.id_participants = id_participants;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    @Override
    public Element getXMLElement() {
        Element element = new Element(MSG.XML_ELEMENT_PARTICIPANTS);

        element = MyXML.addChild(element, MSG.XML_ELEMENT_PARTICIPANTS_ID, Integer.toString(id_participants));
        element = MyXML.addChild(element, MSG.XML_ELEMENT_CONVERSATION_ID, Integer.toString(conversation_id));
        element = MyXML.addChild(element, MSG.XML_ELEMENT_USERS_ID, Integer.toString(users_id));
        return element;
    }
}
