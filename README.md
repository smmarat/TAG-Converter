TAG Converter
=============

Lightweight library for converting HTML to BBCode and back

Usage:
-----

- Just add 'Tag' class to your project 
- Use it:

`
String bbCode = Tag.fromHTML(html).toBBCode();
`

`
String html2 = Tag.fromBBCode(bbCode).toHTML();
`

Limitations
-----------

- Any Javascript inclusions in HTML will be ignored
- 'style' HTML attribute will be ignored except 'font-size' or 'color' values
- If HTML tag has several attributes (except 'font-size' and 'color') or several style values separated by ';' 
all attributes except first will be ignored.
- Other BBCode limitation according to <https://en.wikipedia.org/wiki/BBCode>

Dependencies
------------

No dependencies except java core ;)