package com.javatechie.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckPassWord {

    // kiểm tra mật khẩu có hợp lệ không
    public static boolean isStrongPassword(String password) {
        // Kiểm tra xem mật khẩu có ít nhất 1 ký tự đặc biệt
        Pattern specialCharPattern = Pattern.compile("[!@#\\$%\\^&\\*()]");
        Matcher specialCharMatcher = specialCharPattern.matcher(password);

        // Kiểm tra xem mật khẩu có ít nhất 1 ký tự viết hoa
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Matcher upperCaseMatcher = upperCasePattern.matcher(password);

        // Kiểm tra xem mật khẩu không có khoảng trắng
        if (password.contains(" ")) {
            return false;
        }
        // Độ dài mật khẩu tối thiểu 8 ký tự
        if(password.length() < 8) {
            return false;
        }
        return specialCharMatcher.find() && upperCaseMatcher.find();
    }
}
