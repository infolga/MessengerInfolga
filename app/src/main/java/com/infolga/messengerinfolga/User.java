package com.infolga.messengerinfolga;

import org.jdom2.Element;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class User   implements Myin{
    private int users_id;
    private String user_name;
    private String phone;
    private String email;
    private String first_name;
    private String last_name;
    private int is_active;
    private String last_online_at;


    public User() {
    }

    public User(Element el) {
        users_id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_USERS_ID).getText());
        user_name = el.getChild(MSG.XML_ELEMENT_USER_NAME).getText();
        phone = el.getChild(MSG.XML_ELEMENT_PHONE).getText();
        email = el.getChild(MSG.XML_ELEMENT_EMAIL).getText();
        first_name = el.getChild(MSG.XML_ELEMENT_FIRST_NAME).getText();
        last_name = el.getChild(MSG.XML_ELEMENT_LAST_NAME).getText();
        is_active = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_IS_ACTIVE).getText());


        SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date d = null;
        try {
            d = dfm.parse( el.getChild(MSG.XML_ELEMENT_LAST_ONLINE).getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar time = Calendar.getInstance();
        time.setTime(d);
        time.add(Calendar.MILLISECOND, time.getTimeZone().getOffset(time.getTimeInMillis()));


        last_online_at = dfm.format( time.getTime());
    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getLast_online_at() {
        return last_online_at;
    }

    public void setLast_online_at(String last_online_at) {
        this.last_online_at = last_online_at;
    }

    public Element getXMLElement() {

        Element element = new Element(MSG.XML_ELEMENT_USER);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_USERS_ID, Integer.toString(users_id));
        element = MyXML.addChild(element, MSG.XML_ELEMENT_USER_NAME, user_name);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_PHONE, phone);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_EMAIL, email);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_FIRST_NAME, first_name);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_LAST_NAME, last_name);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_IS_ACTIVE, Integer.toString(is_active));
        element = MyXML.addChild(element, MSG.XML_ELEMENT_LAST_ONLINE, last_online_at);
        return element;
    }
}
