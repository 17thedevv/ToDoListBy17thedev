/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.todolistby17thedev;

import java.time.LocalDate;

/**
 *
 * @author Asus
 */
public class Controller {
    public static boolean isValidDate(String dateStr) {
    try {
        LocalDate.parse(dateStr);
        return true;
    } catch (Exception e) {
        return false;
    }
    }
}
