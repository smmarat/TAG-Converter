package com.h2b;

/**
 * Created by mar on 9/11/19.
 * Usage example for "html to BBCode to html" converter library
 */
public class Main {

    private static final String html = "<span style = \"color:#FF00FF;\"><b>ololo</b><br/><a href=\"http://google.com\">Google</a><strong>ololos</strong><i>aaa</i><span style=\"font-size: 15px;\">aaa</span></span>";
    private static final String html2 = "<font size=\"6\" face=\"Times New Roman\" color=\"#ff0000\"><b><u>qweQ -&nbsp;</u></b><strong style=\"letter-spacing: -0.07px; white-space: pre-wrap; background-color: rgb(235, 236, 240);\">WU ADMIN ROLE</strong></font>";
    private static final String html3 = "<b><u>111 Q -&nbsp;</u></b><strong style=\"color: rgb(23, 43, 77); font-family: -apple-system, system-ui, &quot;Segoe UI&quot;, Roboto, Oxygen, Ubuntu, &quot;Fira Sans&quot;, &quot;Droid Sans&quot;, &quot;Helvetica Neue&quot;, sans-serif; font-size: 14px; letter-spacing: -0.07px; white-space: pre-wrap; background-color: rgb(235, 236, 240);\">WU ADMIN ROLE</strong>";
    private static final String html4 = "<div><span class=\"sf_chat_msg_text_message\" style=\"box-sizing: border-box;\"><font color=\"#444444\" face=\"Roboto, sans-serif\">#{merchant_settings.bank_name}</font></span></div><div><span class=\"sf_chat_msg_text_message\" style=\"box-sizing: border-box;\"><font color=\"#444444\" face=\"Roboto, sans-serif\"><a href=\"http://www.google.lt\">link&nbsp;</a>&nbsp;bla bla</font></span></div>";
    private static final String bb = "[size=\"6\"][color=\"#ff0000\"][b][u]qweQ[/u][/b][b]IN ROLE[/b][/color][/size]";
    private static final String tt2 = "<ol><li>1</li><li>2</li><ol type=\"a\"><li>3</li><li>33</li></ol><li>4</li></ol>";
    private static final String bb2 = "[span]link[/span]";

    public static void main(String[] args) {
        System.out.println(tt2);
        String bbCode = Tag.fromHTML(tt2).toBBCode();
        System.out.println(bbCode);
        String html2 = Tag.fromBBCode(bbCode).toHTML();
        System.out.println(html2);
    }
}
