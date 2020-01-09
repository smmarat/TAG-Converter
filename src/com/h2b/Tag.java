package com.h2b;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mar on 9/13/19.
 */
public class Tag {

    private String name, attrStr = "";
    private String content = "";
    private List<Tag> childs = new ArrayList<>();

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Tag> getChilds() {
        return childs;
    }

    public void setChilds(List<Tag> childs) {
        this.childs = childs;
    }

    public String getContent() {
        return content;
    }

    public void setAttrStr(String attrStr) {
        this.attrStr = attrStr;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addToContent(char c) {
        content += c;
    }

    public void addChild(Tag t) {
        childs.add(t);
    }

    /**
     * Parse html string
     * @param hh html string
     * @return Tag
     */
    public static Tag fromHTML(String hh) {
        hh = hh.replace(" = ", "=");
        Tag top = new Tag("span");
        fromHTML(hh, top);
        return (top.getChilds().size() == 1) ? top.getChilds().get(0) : top;
    }

    /**
     * Parse BBCode string
     * @param bb BBCode string
     * @return
     */
    public static Tag fromBBCode(String bb) {
        Tag top = new Tag("span");
        fromBBCode(bb, top);
        return (top.getChilds().size() == 1) ? top.getChilds().get(0) : top;
    }

    public static void fromHTML(String bb, Tag parent) {
        char[] chars = bb.toCharArray();
        Tag t = null;
        StringBuilder tagBuff = null, attrBuff = null;
        boolean inTag = false;
        for(int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (inTag) {
                String closeStr = "</" + t.getName() + ">";
                int end = bb.indexOf(closeStr, i);
                String body = bb.substring(i, end);
                if (body.contains("<" + t.getName())) {
                    end = bb.lastIndexOf(closeStr);
                    body = bb.substring(i, end);
                }
                fromHTML(body, t);
                i = end + closeStr.length() - 1;
                inTag = false;
                parent.addChild(t);
                continue;
            }
            if (attrBuff != null) {
                if ('>' == c) {
                    t.setAttrStr(attrBuff.toString());
                    if ("img".equalsIgnoreCase(t.getName()) && (bb.indexOf("</img>", i) < 0)) {
                        attrBuff = null;
                        parent.addChild(t);
                        continue;
                    }
                    attrBuff = null;
                    inTag = true;
                }
                else attrBuff.append(c);
                continue;
            }
            if (tagBuff != null) {
                if ('>' == c) {
                    t = new Tag(tagBuff.toString());
                    tagBuff = null;
                    inTag = true;
                }
                else if (' ' == c) {
                    t = new Tag(tagBuff.toString());
                    attrBuff = new StringBuilder();
                    attrBuff.append(c);
                    tagBuff = null;
                }
                else if ('/' == c) {
                    t = new Tag(tagBuff.toString());
                    tagBuff = null;
                    parent.addChild(t);
                    i++;
                }
                else tagBuff.append(c);
                continue;
            }
            if ('<' == c) {
                if (parent.getContent().length() > 0) {
                    Tag ct = new Tag("span");
                    ct.setContent(parent.content);
                    parent.addChild(ct);
                    parent.content = "";
                }
                tagBuff = new StringBuilder();
                continue;
            }
            parent.addToContent(c);
        }
    }

    public static void fromBBCode(String bb, Tag parent) {
        char[] chars = bb.toCharArray();
        Tag t = null;
        StringBuilder tagBuff = null, attrBuff = null;
        boolean inTag = false;
        for(int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (inTag) {
                String closeStr = "[/" + t.getName() + "]";
                int end = bb.indexOf(closeStr, i);
                if (end < 0) continue; // Ignore unclosed tags
                String body = bb.substring(i, end);
                if (body.contains("[" + t.getName())) {
                    end = bb.lastIndexOf(closeStr);
                    if (end < 0) continue;
                    body = bb.substring(i, end);
                }
                fromBBCode(body, t);
                i = end + closeStr.length() - 1;
                inTag = false;
                parent.addChild(t);
                continue;
            }
            if (attrBuff != null) {
                if (']' == c) {
                    t.setAttrStr(attrBuff.toString());
                    attrBuff = null;
                    inTag = true;
                }
                else attrBuff.append(c);
                continue;
            }
            if (tagBuff != null) {
                if (']' == c) {
                    t = new Tag(tagBuff.toString());
                    tagBuff = null;
                    inTag = true;
                }
                else if ('=' == c) {
                    t = new Tag(tagBuff.toString());
                    attrBuff = new StringBuilder();
                    attrBuff.append(c);
                    tagBuff = null;
                }
                else tagBuff.append(c);
                continue;
            }
            if ('[' == c) {
                tagBuff = new StringBuilder();
                continue;
            }
            parent.addToContent(c);
        }
    }

    /**
     * Serialize to html string
     * @return html string
     */
    public String toHTML() {
        if (name.equalsIgnoreCase("size")) {
            String val = getBBCodeAttrValue(attrStr);
            name = "span";
            attrStr = " style=\"font-size: " + val + ";\"";
        }
        if (name.equalsIgnoreCase("color")) {
            String val = getBBCodeAttrValue(attrStr);
            name = "span";
            attrStr = " style=\"color: " + val + ";\"";
        }
        if (name.equalsIgnoreCase("url")) {
            String val = getBBCodeAttrValue(attrStr);
            name = "a";
            attrStr = " href=\"" + val + "\"";
        }
        if (name.equalsIgnoreCase("img")) {
            attrStr = "src=\"" + content + "\"";
            content = "";
        }
        return toString("<", ">");
    }

    /**
     * Serialize to BBCode string
     * @return BBCode string
     */
    public String toBBCode() {
        if (name.equalsIgnoreCase("strong")) {
            name = "b";
        }
        if ((name + attrStr).equalsIgnoreCase("span style=\"font-weight:bold;\"")) {
            name = "b";
            attrStr = "";
        }
        if (name.equalsIgnoreCase("em")) name = "i";
        if ((name + attrStr).equalsIgnoreCase("span style=\"font-style:italic;\"")) {
            name = "i";
            attrStr = "";
        }
        if (name.equalsIgnoreCase("ins")) name = "u";
        if ((name + attrStr).equalsIgnoreCase("span style=\"text-decoration:underline;\"")) {
            name = "u";
            attrStr = "";
        }
        if (name.equalsIgnoreCase("del")) name = "s";
        if ((name + attrStr).equalsIgnoreCase("span style=\"text-decoration:line-through;\"")) {
            name = "s";
            attrStr = "";
        }
        if (name.equalsIgnoreCase("a")) {
            attrStr = attrStr.replace(" href=\"", "=");
            attrStr = attrStr.replace("\"", "");
            name = "url";
        }
        if (name.equalsIgnoreCase("img") && attrStr.contains("src")) {
            content = getHtmlAttrValue(attrStr, "src");
            attrStr = "";
        }
        if (name.equalsIgnoreCase("blockquote")) name = "quote";
        if (name.equalsIgnoreCase("pre")) name = "code";
        if ((name + attrStr).equalsIgnoreCase("code style=\"white-space:pre;\"")) {
            name = "code";
            attrStr = "";
        }
        if (name.equalsIgnoreCase("span")) {
            if (attrStr.startsWith(" style=\"font-size:")) {
                name = "size";
                attrStr = "=\"" + attrStr.substring(18).replace(";", "");
            }
            if (attrStr.startsWith(" style=\"color:")) {
                name = "color";
                attrStr = "=\"" + attrStr.substring(14).replace(";", "");
            }
        }
        if (name.equalsIgnoreCase("font")) {
            if (attrStr.contains(" size=") && attrStr.contains(" color=")) {
                String size = getHtmlAttrValue(attrStr, "size");
                String color = getHtmlAttrValue(attrStr, "color");
                name = "size";
                attrStr = "=\"" + size + "\"";
                Tag t = new Tag("color");
                t.setAttrStr("=\"" + color + "\"");
                t.setChilds(getChilds());
                t.setContent(getContent());
                childs = new ArrayList<>();
                content = "";
                addChild(t);
            }
            else if (attrStr.contains(" size=")) {
                name = "size";
                String val = getHtmlAttrValue(attrStr, "size");
                attrStr = "=\"" + val + "\"";
            }
            else if (attrStr.contains(" color=")) {
                name = "color";
                String val = getHtmlAttrValue(attrStr, "color");
                attrStr = "=\"" + val + "\"";
            }
            else {
                attrStr = "";
                name = "span";
            }
        }
        if (attrStr.indexOf(" ") > 1 || attrStr.contains(";")) attrStr = "";
        return toString("[", "]");
    }

    private String getHtmlAttrValue(String attr, String name) {
        String val = attr.substring(attr.indexOf(" " + name + "=") + name.length() + 3);
        return val.substring(0, val.indexOf("\""));
    }

    private String getBBCodeAttrValue(String attr) {
        String val = attr.substring(attr.indexOf("=") + 1);
        return val.replace("\"", "");
    }

    private String toString(String l, String r) {
        StringBuilder sb = new StringBuilder();
        if ("<".equals(l) && attrStr.trim().length() > 0) attrStr = " " + attrStr.trim();
        sb.append(l).append(name).append(attrStr).append(r);
        for(Tag t : childs) {
            sb.append("<".equals(l) ? t.toHTML() : t.toBBCode());
        }
        sb.append(content).append(l).append("/").append(name).append(r);
        return sb.toString();
    }
}
