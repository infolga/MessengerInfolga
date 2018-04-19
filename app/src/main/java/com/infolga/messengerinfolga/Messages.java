package com.infolga.messengerinfolga;

import org.jdom2.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Messages implements Myin {


    private int id;
    private int conversation_id;
    private int sender_id;
    private String message_type;
    private String message;
    private String attachment_thumb_url;
    private String attachment_url;
    private String created_at;

    private String us_FL_name;

    public Messages() {
    }

    public Messages(Element el) {

        id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_MESSAGE_ID).getText());
        conversation_id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_CONVERSATION_ID).getText());
        sender_id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_SENDER_ID).getText());
        message_type = el.getChild(MSG.XML_ELEMENT_MESSAGE_TYPE).getText();
        message = el.getChild(MSG.XML_ELEMENT_MESSAGE).getText();
        attachment_thumb_url = el.getChild(MSG.XML_ELEMENT_ATTACHMENT_THUMB_URL).getText();
        attachment_url = el.getChild(MSG.XML_ELEMENT_ATTACHMENT_URL).getText();
        created_at = el.getChild(MSG.XML_ELEMENT_CREATED_AT).getText();

        SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date d = null;
        try {
            d = dfm.parse(el.getChild(MSG.XML_ELEMENT_CREATED_AT).getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar time = Calendar.getInstance();
        time.setTime(d);
        time.add(Calendar.MILLISECOND, time.getTimeZone().getOffset(time.getTimeInMillis()));
        created_at = dfm.format(time.getTime());
    }

    public String getUs_FL_name() {
        return us_FL_name;
    }

    public void setUs_FL_name(String us_FL_name) {
        this.us_FL_name = us_FL_name;
    }

    @Override
    public Element getXMLElement() {
        Element element = new Element(MSG.XML_ELEMENT_MESSAGES);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_MESSAGE_ID, Integer.toString(id));
        element = MyXML.addChild(element, MSG.XML_ELEMENT_CONVERSATION_ID, Integer.toString(conversation_id));
        element = MyXML.addChild(element, MSG.XML_ELEMENT_SENDER_ID, Integer.toString(sender_id));
        element = MyXML.addChild(element, MSG.XML_ELEMENT_MESSAGE_TYPE, message_type);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_MESSAGE, message);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_ATTACHMENT_THUMB_URL, attachment_thumb_url);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_ATTACHMENT_URL, attachment_url);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_CREATED_AT, created_at);
        return element;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttachment_thumb_url() {
        return attachment_thumb_url;
    }

    public void setAttachment_thumb_url(String attachment_thumb_url) {
        this.attachment_thumb_url = attachment_thumb_url;
    }

    public String getAttachment_url() {
        return attachment_url;
    }

    public void setAttachment_url(String attachment_url) {
        this.attachment_url = attachment_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


}
