package com.infolga.messengerinfolga;

import org.jdom2.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Conversation implements Myin {

    private int conversation_id;
    private String title;
    private int photo_id;
    private int creator_id;
    private String created_at;
    private String type;
    private String name_conversation;
    private String time_last_viev;

    private int countUnreadMes;
    private String time_last_Mes;
    private String text_last_Mes;

    public Conversation() {
    }

    public Conversation(Element el) {
        conversation_id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_CONVERSATION_ID).getText());

        title = el.getChild(MSG.XML_ELEMENT_TITLE).getText();
        photo_id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_PHOTO_ID).getText());
        creator_id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_CONVERSATION_CREATOR_ID).getText());

        type = el.getChild(MSG.XML_ELEMENT_TYPE).getText();
        name_conversation = el.getChild(MSG.XML_ELEMENT_NAME_CONVERSATION).getText();


        SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date d = null;
        Date d2 = null;
        try {
            d2 = dfm.parse(el.getChild(MSG.XML_ELEMENT_CONVERSATION_TIME_LAST_VIEV).getText());
            d = dfm.parse(el.getChild(MSG.XML_ELEMENT_CREATED_AT).getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar time = Calendar.getInstance();
        time.setTime(d);
        time.add(Calendar.MILLISECOND, time.getTimeZone().getOffset(time.getTimeInMillis()));
        created_at = dfm.format(time.getTime());


        time = Calendar.getInstance();
        time.setTime(d2);
        time.add(Calendar.MILLISECOND, time.getTimeZone().getOffset(time.getTimeInMillis()));

        time_last_viev = dfm.format(time.getTime());


    }

    public int getCountUnreadMes() {
        return countUnreadMes;
    }

    public void setCountUnreadMes(int countUnreadMes) {
        this.countUnreadMes = countUnreadMes;
    }

    public String getTime_last_Mes() {
        return time_last_Mes;
    }

    public void setTime_last_Mes(String time_last_Mes) {
        this.time_last_Mes = time_last_Mes;
    }

    public String getText_last_Mes() {
        return text_last_Mes;
    }

    public void setText_last_Mes(String text_last_Mes) {
        this.text_last_Mes = text_last_Mes;
    }

    public Element getXMLElement() {


        Element element = new Element(MSG.XML_ELEMENT_CONVERSATION);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_CONVERSATION_ID, Integer.toString(conversation_id));
        element = MyXML.addChild(element, MSG.XML_ELEMENT_TITLE, title);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_PHOTO_ID, Integer.toString(photo_id));
        element = MyXML.addChild(element, MSG.XML_ELEMENT_CONVERSATION_CREATOR_ID, Integer.toString(creator_id));

        element = MyXML.addChild(element, MSG.XML_ELEMENT_CREATED_AT, created_at);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_TYPE, type);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_NAME_CONVERSATION, name_conversation);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_CONVERSATION_TIME_LAST_VIEV, time_last_viev);


        return element;


    }


    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName_conversation() {
        return name_conversation;
    }

    public void setName_conversation(String name_conversation) {
        this.name_conversation = name_conversation;
    }

    public String getTime_last_viev() {
        return time_last_viev;
    }

    public void setTime_last_viev(String time_last_viev) {
        this.time_last_viev = time_last_viev;
    }


}
