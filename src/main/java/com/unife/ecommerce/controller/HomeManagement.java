package com.unife.ecommerce.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HomeManagement {

    public static void view(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("viewUrl", "homeManagement/view");
    }
}
