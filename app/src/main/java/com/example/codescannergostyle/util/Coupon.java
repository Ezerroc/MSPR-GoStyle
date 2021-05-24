package com.example.codescannergostyle.util;

import androidx.appcompat.app.ActionBar;
import androidx.navigation.Navigator;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity
public class Coupon implements Serializable {

    @PrimaryKey
    private int ID;
    private String date_from;
    private String date_to;
    private String description;
    private String code;
    private float minimum_amount;
    private float minimum_amount_tax;
    private float minimum_amount_shipping;
    private float reduction_percent;
    private double reduction_amount;
    private int active;
    private String date_add;
    private String date_upd;

    @Ignore
    private JSONObject couponObject;

    public Coupon(JSONObject coupon) throws JSONException {
        this.couponObject = coupon;
        this.ID = Integer.parseInt(coupon.get("id_cart_rule").toString());
        this.date_from = coupon.get("date_from").toString();
        this.date_to = coupon.get("date_to").toString();
        this.description = coupon.get("description").toString();
        this.code = coupon.get("code").toString();
        this.minimum_amount = Float.parseFloat(coupon.get("minimum_amount").toString());
        this.minimum_amount_tax = Float.parseFloat(coupon.get("minimum_amount_tax").toString());
        this.minimum_amount_shipping = Float.parseFloat(coupon.get("minimum_amount_shipping").toString());
        this.reduction_percent = Float.parseFloat(coupon.get("reduction_percent").toString());
        this.reduction_amount = Double.parseDouble(coupon.get("reduction_amount").toString());
        this.active = Integer.parseInt(coupon.get("active").toString());
        this.date_add = coupon.get("date_add").toString();
        this.date_upd = coupon.get("date_upd").toString();
    }

    public Coupon(int ID, String date_from, String date_to, String description, String code, float minimum_amount, float minimum_amount_tax, float minimum_amount_shipping, float reduction_percent, double reduction_amount, int active, String date_add, String date_upd) {
        this.ID = ID;
        this.date_from = date_from;
        this.date_to = date_to;
        this.description = description;
        this.code = code;
        this.minimum_amount = minimum_amount;
        this.minimum_amount_tax = minimum_amount_tax;
        this.minimum_amount_shipping = minimum_amount_shipping;
        this.reduction_percent = reduction_percent;
        this.reduction_amount = reduction_amount;
        this.active = active;
        this.date_add = date_add;
        this.date_upd = date_upd;
    }

    public int getID() {
        return ID;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getMinimum_amount() {
        return minimum_amount;
    }

    public void setMinimum_amount(float minimum_amount) {
        this.minimum_amount = minimum_amount;
    }

    public float getMinimum_amount_tax() {
        return minimum_amount_tax;
    }

    public void setMinimum_amount_tax(float minimum_amount_tax) {
        this.minimum_amount_tax = minimum_amount_tax;
    }

    public float getMinimum_amount_shipping() {
        return minimum_amount_shipping;
    }

    public void setMinimum_amount_shipping(float minimum_amount_shipping) {
        this.minimum_amount_shipping = minimum_amount_shipping;
    }

    public float getReduction_percent() {
        return reduction_percent;
    }

    public void setReduction_percent(float reduction_percent) {
        this.reduction_percent = reduction_percent;
    }

    public double getReduction_amount() {
        return reduction_amount;
    }

    public void setReduction_amount(double reduction_amount) {
        this.reduction_amount = reduction_amount;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDate_add() {
        return date_add;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
    }

    public String getDate_upd() {
        return date_upd;
    }

    public void setDate_upd(String date_upd) {
        this.date_upd = date_upd;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "ID=" + ID +
                ", date_from='" + date_from + '\'' +
                ", date_to='" + date_to + '\'' +
                ", description='" + description + '\'' +
                ", code='" + code + '\'' +
                ", minimum_amount=" + minimum_amount +
                ", minimum_amount_tax=" + minimum_amount_tax +
                ", minimum_amount_shipping=" + minimum_amount_shipping +
                ", reduction_percent=" + reduction_percent +
                ", reduction_amount=" + reduction_amount +
                ", active=" + active +
                ", date_add='" + date_add + '\'' +
                ", date_upd='" + date_upd + '\'' +
                '}';
    }
}
