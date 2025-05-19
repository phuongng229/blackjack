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
    ACE("Ace", 11),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK("Jack", 10),
    QUEEN("Queen", 10),
    KING("King", 10);
    
    private final int value;
    private final String name;
    
    Face (String name, int value) {
        this.name = name;
        this.value = value;
    }
    
    Face (int value) {
        this.name = null;
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
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
