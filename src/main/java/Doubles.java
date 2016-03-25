public class Doubles {

    public Double parse(String s) {
        StateMachine sm = new StateMachine();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            sm.next(c);

        }
        return sm.getResult();
    }

    private static class StateMachine {

        private Double result;

        public void next(char c) {
            currentState = currentState.next(c, data);

        }

        State currentState = State.INIT;
        ParseData data = new ParseData();

        public Double getResult() {
            if (currentState == State.NUMBER) {
                return new Double(data.getNumber());
            }
            return null;
        }

        public enum State {
            INIT {
                public State next(char c, ParseData data) {
                    if (c - '0' <= 9 && c - '0' >= 0) {
                        data.addDigit(c - '0');
                        return NUMBER;
                    }
                    return INVALID_END;
                }
            }, NUMBER {
                public State next(char c, ParseData data) {
                    if (c - '0' <= 9 && c - '0' >= 0) {
                        data.addDigit(c - '0');
                        return NUMBER;
                    }
                    return INVALID_END;
                }
            }, VALID_END {
                public State next(char c, ParseData data) {
                    if (c == ' ') {
                        return VALID_END;
                    }
                    return INVALID_END;
                }
            }, INVALID_END {
                public State next(char c, ParseData data) {
                    return INVALID_END;
                }
            };

            public abstract State next(char c, ParseData data);


        }

    }

    public static class ParseData {
        private int number = 0;

        public void addDigit(int i) {
            number = number * 10 + i;
        }

        public int getNumber() {
            return number;
        }
    }
}
