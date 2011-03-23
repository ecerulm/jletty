This a LDAP server develop in Java. 

I created this project not to compete with Apache Directory (which I think you should use if you are looking for a java LDAP server) but to teach myself Java. The parser is handwritten (instead of using an ASN.1 compiler to generate code) and it's non-blocking, it uses Java NIO. 

I decided to make the project available because it could be useful for people interested on writing parsers by hand and also mainly to avoid losing the code. 

The code has extensive unit test and coverage test (usign emma). The project was originally built with ant but I migrated to Maven2 before making it public. 

Again, I recommend usign Apache Directory instead.
