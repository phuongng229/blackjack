/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package blackjack_project2_group40;

/**
 *
 * @author phuong & jonathan
 */
public enum Face { //Declare enum for card face
    ACE  ("Ace", 11, "A"),
    TWO  (2, "2"),
    THREE(3, "3"),
    FOUR (4, "4"),
    FIVE (5, "5"),
    SIX  (6, "6"),
    SEVEN(7, "7"),
    EIGHT(8, "8"),
    NINE (9, "9"),
    TEN  (10, "10"),
    JACK ("Jack", 10, "J"),
    QUEEN("Queen",10, "Q"),
    KING ("King", 10, "K");

    private final int value;
    private final String name;
    private final String code;

    // full constructor
    Face(String name, int value, String code) {
        this.name  = name;
        this.value = value;
        this.code  = code;
    }

    Face(int value, String code) {
        this(null, value, code);
    }

    public int getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }
    
    @Override
    public String toString() {
        return this.name != null ? name : rename(name()); //name() return Name of constant in Enum
    }
    
    private String rename(String text) {
        String reName = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase(); //convert from NAME to Name: example EIGHT -> Eight
        return reName;
    }
}
