package org.example.sysdesign.api.util;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Date;

/*
 * utily class that puts a given date and number into a JSON. 
 * The JSON will be used in 'StaffResource.java' and sent to a PUT endpoint in the ticketservice.
 * This JSON could potentially be created when something changes to the personnel (PUT endpoint personnel service),
 * which needs to be passed to the ticketservice
 */

@JsonPropertyOrder({"date", "amount"})
public class AnswerConstruct{

    private Date date;
    private int amount;

    public AnswerConstruct(Date date, int amount) {
        this.date = date;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
