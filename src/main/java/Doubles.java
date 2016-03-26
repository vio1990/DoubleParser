/**
 * Class for parsing input string to double type value.
 */
public class Doubles {
    /**
     * Parsing input string to double. Checking every
     * char of input string and change a condition of state
     * machine, depending on the char value.
     *
     * @param s input string.
     * @return double result of state machine's parsing procedures.
     */
    public Double parse(String s) {
        StateMachine sm = new StateMachine();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            sm.next(c);
        }
        return sm.getResult();
    }

    /**
     * Class with conditions, which are beign changed,
     * depending on the input char.
     */
    static class StateMachine {

        /**
         * Change the condition of the State machine, depending on
         * the input char and executing appropriate operations with the char by Parser data.
         *
         * @param c input char.
         */
        public void next(char c) {
            currentState = currentState.next(c, data);
        }

        private State currentState = State.INIT;
        private ParseData data = new ParseData();

        /**
         * Returning the result of parsing.
         *
         * @return double result.
         */
        public Double getResult() {
            Double result = null;
            if (currentState == State.VALID_END || currentState == State.DECIMAL || currentState == State.NUMBER || currentState == State.EXP_NUMBER || currentState == State.POINT_AFTER) {
                result = data.getNumber();
            }
            return result;
        }

        /**
         * Appropriate State machine conditions.
         */
        public enum State {
            INIT {
                @Override
                public State next(char c, ParseData data) {
                    if (c - '0' <= 9 && c - '0' >= 0) {
                        data.addDigit(c - '0');
                        return NUMBER;
                    } else if (c == '-' || c == '+') {
                        data.setNegativeNumber(c == '-');
                        return SIGN;
                    } else if (c == '.') {
                        return POINT_BEFORE;
                    } else if (c == ' ') {
                        return INIT;
                    }
                    return INVALID_END;
                }
            }, NUMBER {
                @Override
                public State next(char c, ParseData data) {
                    if (c - '0' <= 9 && c - '0' >= 0) {
                        data.addDigit(c - '0');
                        return NUMBER;
                    } else if (c == '.') {
                        return POINT_AFTER;
                    } else if (c == ' ') {
                        return VALID_END;
                    }
                    return INVALID_END;
                }
            }, VALID_END {
                @Override
                public State next(char c, ParseData data) {
                    if (c == ' ') {
                        return VALID_END;
                    }
                    return INVALID_END;
                }
            }, INVALID_END {
                @Override
                public State next(char c, ParseData data) {
                    return INVALID_END;
                }
            }, SIGN {
                @Override
                public State next(char c, ParseData data) {
                    if (c - '0' <= 9 && c - '0' >= 0) {
                        data.addDigit(c - '0');
                        return NUMBER;
                    } else if (c == '.') {
                        return POINT_BEFORE;
                    }
                    return INVALID_END;
                }
            }, POINT_BEFORE {
                @Override
                public State next(char c, ParseData data) {
                    if (c - '0' <= 9 && c - '0' >= 0) {
                        data.addDecimalDigit(c - '0');
                        return DECIMAL;
                    }
                    return INVALID_END;
                }
            }, POINT_AFTER {
                @Override
                public State next(char c, ParseData data) {
                    if (c - '0' <= 9 && c - '0' >= 0) {
                        data.addDecimalDigit(c - '0');
                        return DECIMAL;
                    } else if (c == 'e') {
                        data.expExist(true);
                        return EXP;
                    } else if (c == ' ') {
                        return VALID_END;
                    }
                    return INVALID_END;
                }
            }, DECIMAL {
                @Override
                public State next(char c, ParseData data) {
                    if (c - '0' <= 9 && c - '0' >= 0) {
                        data.addDecimalDigit(c - '0');
                        return DECIMAL;
                    } else if (c == ' ') {
                        return VALID_END;
                    } else if (c == 'e') {
                        data.expExist(true);
                        return EXP;
                    }
                    return INVALID_END;
                }
            }, EXP {
                @Override
                public State next(char c, ParseData data) {
                    if (c == '-' || c == '+') {
                        data.expSign(c == '-');
                        return EXP_SIGN;
                    } else if (c - '0' <= 9 && c - '0' >= 0) {
                        data.addExpNumber(c - '0');
                        return EXP_NUMBER;
                    }
                    return INVALID_END;
                }
            }, EXP_SIGN {
                @Override
                public State next(char c, ParseData data) {
                    if (c - '0' <= 9 && c - '0' >= 0) {
                        data.addExpNumber(c - '0');
                        return EXP_NUMBER;
                    }
                    return INVALID_END;
                }
            }, EXP_NUMBER {
                @Override
                public State next(char c, ParseData data) {
                    if (c - '0' <= 9 && c - '0' >= 0) {
                        data.addExpNumber(c - '0');
                        return EXP_NUMBER;
                    } else if (c == ' ') {
                        return VALID_END;
                    }
                    return INVALID_END;
                }
            };

            public abstract State next(char c, ParseData data);
        }

        /**
         * Class, which generates the result and executes operations
         * with numbers.
         */
        private class ParseData {
            private boolean negativeNumber = false;
            private int number = 0;
            private int decimal = 0;
            private int expNumber = 0;
            private int decimalQuantity = 0;
            private boolean expExist = false;
            private boolean negativeExp = false;

            public void addDigit(int i) {
                number = number * 10 + i;
            }

            /**
             * Generating the final result.
             *
             * @return double result of the computing.
             */
            public Double getNumber() {
                double result = data.number;
                result += data.decimal / Math.pow(10, data.decimalQuantity);
                if (data.negativeNumber) {
                    result *= -1;
                }
                if (data.expExist) {
                    if (data.negativeExp) {
                        data.expNumber *= -1;
                    }
                    result *= Math.pow(10, data.expNumber);
                }
                return result;
            }

            public void setNegativeNumber(boolean b) {
                negativeNumber = b;
            }

            public void addDecimalDigit(int i) {
                decimal = decimal * 10 + i;
                decimalQuantity++;
            }

            public void expSign(boolean b) {
                negativeExp = b;
            }

            public void addExpNumber(int i) {
                expNumber = expNumber * 10 + i;
            }

            public void expExist(boolean b) {
                expExist = b;
            }
        }
    }
}

