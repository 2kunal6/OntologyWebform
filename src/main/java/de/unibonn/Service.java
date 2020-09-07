package de.unibonn;

import java.util.ArrayList;
import java.util.List;

public class Service {

    public String getValues(String webformInput){
        if(webformInput.equals("A"))return "A is selected";
        if(webformInput.equals("B"))return "B is selected";
        if(webformInput.equals("C"))return "C is selected";
        return null;
    }
}