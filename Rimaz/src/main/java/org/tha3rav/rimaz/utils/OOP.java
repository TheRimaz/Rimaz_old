package org.tha3rav.rimaz.utils;

public class OOP
{
    public static final class OOPConstants
    {
        public static class MethodPrefixes
        {
            public static final String GET = "get";
            public static final String SET = "set";
            public static final String IS = "is";
            public static final String HAS = "has";
        }

        public static class FieldPrefixes
        {
            public static final String M = "m";
        }

        public static class Numbers
        {
            public static final byte ZERO = 0;
            public static final byte ONE = 1;
            public static final byte TWO = 2;
            public static final byte THREE = 3;
            public static final byte FOUR = 4;

            public static final String AT_LEAST_FOUR_DIGIT_REGEX = "([\\d]){4,}+";
        }

        public static class Symbols
        {
            public static final String DOLAR = "\\$";

            public static final Character SLASH = '/';

            public static final Character DOT = '.';

            public static final String EMPTY_STRING = "";

            public static final String AT_LEAST_ONE_LETTER_REGEX = ".*[a-zA-Z]+.*";
        }
    }
}
